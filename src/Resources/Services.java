package Resources;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Services {

  public String getBodyString(HttpExchange exchange) throws Exception {
    try {
      InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
      BufferedReader br = new BufferedReader(isr);

      int b;
      StringBuilder buf = new StringBuilder();
      while ((b = br.read()) != -1) {
        buf.append((char) b);
      }

      br.close();
      isr.close();
      return buf.toString();
    }
    catch (Exception exc) {
      throw exc;
    }
  }
}