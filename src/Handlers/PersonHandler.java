package Handlers;

import DataAccess.AccessException;
import Requests.PersonIdRequest;
import Requests.PersonRequest;
import Resources.Services;
import Response.PersonIdResponse;
import Response.PersonResponse;
import Service.PersonService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class PersonHandler implements HttpHandler {
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
        buf=service.getBodyString(exchange);
      } catch (Exception ex) {
        exchange.sendResponseHeaders(HTTP_OK, 0);
        PersonResponse message=new PersonResponse(ex.getMessage());
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
        PersonIdResponse message=new PersonIdResponse(ex.getMessage());
        PrintWriter out=new PrintWriter(exchange.getResponseBody());
        gson.toJson(message, out);
        out.close();
        exchange.close();
        return;
      }
    }

    PersonService personService = new PersonService();

    if(args.length == 2){
      PersonResponse response = null;

      try{
        response = personService.person(token);
      }
      catch (AccessException e){
        e.printStackTrace();
      }

      if(response.getSuccess() == false){
        PersonResponse message = new PersonResponse("error, getting people associated with user");

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
      PersonIdRequest idInfo = new PersonIdRequest(lookUpId);
      PersonIdResponse idResponse = null;

      try{
        idResponse = personService.personId(idInfo, token);
      }
      catch (AccessException e){
        e.printStackTrace();
      }

      if(idResponse.getSuccess() == false){
        PersonIdResponse message = new PersonIdResponse("error, getting people associated with user");

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
        exchange.sendResponseHeaders(HTTP_OK, 0);
        PrintWriter out = new PrintWriter(exchange.getResponseBody());
        gson.toJson(idResponse, out);
        out.close();
        exchange.close();
      }
    }
  }
}
