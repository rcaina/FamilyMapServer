package DataAccess;

public class AccessException extends Exception {

  public AccessException(String s) {
    super(s);
  }

  public AccessException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
