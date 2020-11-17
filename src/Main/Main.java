package Main;

public class Main {

  public static void main(String[] args) throws Exception {
    Server server = new Server();
    server.startServer(8080);
    //server.startServer(Integer.parseInt(args[0]));
  }
}

