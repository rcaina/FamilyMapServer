package Handlers;

import DataAccess.AccessException;
import Requests.RegisterRequest;
import Resources.Services;
import Response.RegisterResponse;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class RegisterHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Gson gson = new Gson();
    Services service = new Services();
    String buf = "";
    try{
      buf = service.getBodyString(exchange);
    }catch (Exception ex) {
      exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
      RegisterResponse message=new RegisterResponse(ex.getMessage());
      PrintWriter out=new PrintWriter(exchange.getResponseBody());
      gson.toJson(message, out);
      out.close();
      exchange.close();
      return;
    }

    RegisterService register = new RegisterService();
    RegisterRequest userInfo = gson.fromJson(buf, RegisterRequest.class);
    RegisterResponse response = null;
    try{
      response = register.register(userInfo);
    } catch (AccessException e) {
      e.printStackTrace();
    }

    if(response.getSuccess() == false) {
      RegisterResponse message=new RegisterResponse("error, user not registered");

      try {
        exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
        PrintWriter out = new PrintWriter(exchange.getResponseBody());
        gson.toJson(message, out);
        out.close();
        exchange.close();
      }catch (Exception e){
        System.out.println(e);
      }
    }
    else {
      try {
        exchange.sendResponseHeaders(HTTP_OK, 0);
        PrintWriter out=new PrintWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();
        exchange.close();
      }
      catch (Exception e){
        System.out.println(e);
      }
    }
  }
}
