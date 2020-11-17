package Handlers;

import DataAccess.AccessException;
import DataAccess.Database;
import Requests.FillRequest;
import Resources.Services;
import Response.FillResponse;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class FillHandler implements HttpHandler {
  Database db = new Database();
  Connection connection;
  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Gson gson = new Gson();
    Services service = new Services();
    String buf = "";
    int gens = 4;

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
      FillResponse message = new FillResponse(ex.getMessage());
      PrintWriter out = new PrintWriter(exchange.getResponseBody());
      gson.toJson(message, out);
      out.close();
      exchange.close();
      return;
    }
    String path= exchange.getRequestURI().getPath();
    String [] args = path.split("/");
    String username = args[2];
    if(args.length == 4){
      gens = Integer.parseInt(args[3]);
    }

    FillService fill = new FillService(connection);
    FillRequest fillInfo = gson.fromJson(buf, FillRequest.class);
    FillResponse response = null;

    try{
      response = fill.fill(username, gens);
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


    if(response.getSuccess() == false) {
      FillResponse message=new FillResponse("error, failed to register user");


      try {
        exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
        PrintWriter out=new PrintWriter(exchange.getResponseBody());
        gson.toJson(message, out);
        out.close();
        exchange.close();
      }
      catch (Exception ex){
        System.out.println(ex);
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
