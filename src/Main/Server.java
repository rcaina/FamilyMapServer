package Main;

import Handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Server {

  public void startServer(int port) throws Exception{

    System.out.println("Server listening on port: " + port + "\n");

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 3);
    server.createContext("/", new ClientHandler());
    server.createContext("/user/login", new LoginHandler());
    server.createContext("/user/register", new RegisterHandler());
    server.createContext("/clear", new ClearHandler());
    server.createContext("/load", new LoadHandler());
    server.createContext("/person", new PersonHandler());
    server.createContext("/event", new EventHandler());
    server.createContext("/fill", new FillHandler());

    server.start();
  }
}
