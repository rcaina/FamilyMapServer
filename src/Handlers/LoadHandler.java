package Handlers;

import DataAccess.AccessException;
import DataAccess.Database;
import Requests.LoadRequest;
import Resources.Services;
import Response.LoadResponse;
import Service.LoadService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoadHandler implements HttpHandler {

  Database db = new Database();
  Connection connection;
  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Gson gson = new Gson();
    Services service = new Services();
    String buf = "";

    try {
      connection = db.openConnection();
    } catch (AccessException e) {
      e.printStackTrace();
    }

    try {
      buf = service.getBodyString(exchange);
    }
    catch (Exception ex) {
      exchange.sendResponseHeaders(HTTP_OK, 0);
      LoadResponse message = new LoadResponse(ex.getMessage());
      PrintWriter out = new PrintWriter(exchange.getResponseBody());
      gson.toJson(message, out);
      out.close();
      exchange.close();
      return;
    }

    LoadService load = new LoadService(connection);
    LoadRequest loadIt = gson.fromJson(buf, LoadRequest.class);
    LoadResponse response =null;
    try {
      response = load.load(loadIt,true);
    } catch (AccessException e) {
      e.printStackTrace();
    }

    if(connection != null){
      try {
        db.closeConnection(true);
      } catch (AccessException ex) {
        ex.printStackTrace();
      }
    }

    if(response == null){
      LoadResponse message = new LoadResponse("Error could not load database");

      try {
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
      exchange.sendResponseHeaders(HTTP_OK, 0);
      PrintWriter out = new PrintWriter(exchange.getResponseBody());
      gson.toJson(response, out);
      out.close();
      exchange.close();
    }
  }
}
