package Handlers;

import DataAccess.AccessException;
import Requests.LoginRequest;
import Resources.Services;
import Response.LoginResponse;
import Service.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoginHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Gson gson = new Gson();
    Services service = new Services();
    String buf = "";
    try {
      buf = service.getBodyString(exchange);
    }
    catch (Exception ex) {
      exchange.sendResponseHeaders(HTTP_OK, 0);
      LoginResponse message = new LoginResponse(ex.getMessage());
      PrintWriter out = new PrintWriter(exchange.getResponseBody());
      gson.toJson(message, out);
      out.close();
      exchange.close();
      return;
    }

    LoginService login = new LoginService();
    LoginRequest userToLogin = gson.fromJson(buf, LoginRequest.class);
    LoginResponse response =null;
    try {
      response = login.login(userToLogin);
    } catch (AccessException e) {
      e.printStackTrace();
    }

    if(response.getSuccess1() == false) {
      LoginResponse message = new LoginResponse("error, no user found");

      try {
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
      gson.toJson(response, out);
      out.flush();
      out.close();
      exchange.close();
    }
  }
}
