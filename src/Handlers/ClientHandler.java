package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import static java.net.HttpURLConnection.*;

public class ClientHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {

    String line;
    String path = exchange.getRequestURI().getPath();
    if(path.equals("/")){
      path = "index.html";
    }
    else {
      path = path.substring(1);
    }

    try{
      BufferedReader reader = new BufferedReader(new FileReader(path));
      exchange.sendResponseHeaders(HTTP_OK, 0);
      PrintWriter out = new PrintWriter(exchange.getResponseBody());

      while ((line = reader.readLine()) != null)
      {
        out.write(line);
      }
      reader.close();

      out.close();
      exchange.close();

    }
    catch (Exception e){
      path = "HTML/404.html";
      BufferedReader reader = new BufferedReader(new FileReader(path));
      exchange.sendResponseHeaders(HTTP_NOT_FOUND, 0);
      PrintWriter out = new PrintWriter(exchange.getResponseBody());

      while ((line = reader.readLine()) != null)
      {
        out.write(line);
      }
      reader.close();

      out.close();
      exchange.close();
    }
  }
}
