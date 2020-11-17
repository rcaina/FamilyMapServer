package Handlers;

import Resources.Services;
import Response.ClearResponse;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class ClearHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Gson gson = new Gson();
    Services service = new Services();
    String buf = "";
    try {
      buf = service.getBodyString(exchange);
    }
    catch (Exception ex) {

      ClearResponse message = new ClearResponse(ex.getMessage());
      PrintWriter out = new PrintWriter(exchange.getResponseBody());
      gson.toJson(message, out);
      out.close();
      exchange.close();
      return;
    }

    ClearService clear = new ClearService();
    ClearResponse response = null;

    response = clear.ClearService();

    if(response.getSuccess() == false){
      ClearResponse message = new ClearResponse("Error, could not clear database");

      try{
        exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
        PrintWriter out = new PrintWriter(exchange.getResponseBody());
        gson.toJson(message, out);
        out.close();
        exchange.close();
      }
      catch (Exception e){
        System.out.println(e);
      }
    }
    else {
      exchange.sendResponseHeaders(HTTP_OK, 0); //before writing to the response body
      PrintWriter out = new PrintWriter(exchange.getResponseBody());
      gson.toJson(response, out);
      out.close();
      exchange.close();
    }
  }
}
