package Handlers;

import DataAccess.AccessException;
import Requests.EventIdRequest;
import Requests.EventRequest;
import Resources.Services;
import Response.*;
import Service.EventService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class EventHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Gson gson = new Gson();
    Services service = new Services();
    String buf = "";

    String path = exchange.getRequestURI().getPath();
    String [] args = path.split("/");

    Headers headers = exchange.getRequestHeaders();
    List<String> tokenList = headers.get("Authorization");
    if (tokenList.isEmpty()) try {
      throw new Exception("No Authorization token included");
    } catch (Exception e) {
      e.printStackTrace();
    }
    String token = tokenList.get(0);

    if(args.length == 2) {
      try {
        buf = service.getBodyString(exchange);
      } catch (Exception ex) {
        exchange.sendResponseHeaders(HTTP_OK, 0);
        EventResponse message=new EventResponse(ex.getMessage());
        PrintWriter out=new PrintWriter(exchange.getResponseBody());
        gson.toJson(message, out);
        out.close();
        exchange.close();
        return;
      }
    }
    else{
      try {
        buf=service.getBodyString(exchange);
      } catch (Exception ex) {
        exchange.sendResponseHeaders(HTTP_OK, 0);
        EventIdResponse message=new EventIdResponse(ex.getMessage());
        PrintWriter out=new PrintWriter(exchange.getResponseBody());
        gson.toJson(message, out);
        out.close();
        exchange.close();
        return;
      }
    }

    EventService eventService = new EventService();

    if(args.length == 2){

      EventResponse response = null;

      try{
        response = eventService.event(token);
      }
      catch (AccessException e){
        e.printStackTrace();
      }

      if(response.getSuccess() == false){
        EventResponse message = new EventResponse("error, getting events associated with user");

        try{
          exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
          PrintWriter out = new PrintWriter(exchange.getResponseBody());
          gson.toJson(message, out);
          out.close();
          exchange.close();
        }
        catch (Exception e) {
          System.out.println(e);
        }
      }
      else{
        exchange.sendResponseHeaders(HTTP_OK, 0);
        PrintWriter out = new PrintWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();
        exchange.close();
      }
    }
    else{
      String lookUpId = args[2];
      EventIdRequest eventIdInfo = new EventIdRequest(lookUpId);
      EventIdResponse idResponse = null;

      try{
        idResponse = eventService.eventId(eventIdInfo, token);
      }
      catch (AccessException e){
        e.printStackTrace();
      }

      if(idResponse.getSuccess() == false){
        EventIdResponse message = new EventIdResponse("error, getting people associated with user");

        try{
          exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
          PrintWriter out = new PrintWriter(exchange.getResponseBody());
          gson.toJson(message, out);
          out.close();
          exchange.close();
        }
        catch (Exception e) {
          System.out.println(e);
        }
      }
      else {
        exchange.sendResponseHeaders(HTTP_OK, 0); //before writing to the response body
        PrintWriter out = new PrintWriter(exchange.getResponseBody());
        gson.toJson(idResponse, out);
        out.close();
        exchange.close();
      }
    }
  }
}
