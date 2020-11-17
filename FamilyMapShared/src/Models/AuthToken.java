package Models;

public class AuthToken {

  private String Token;
  private String UserId;

  public AuthToken(){}

  public AuthToken(String Token, String UserId){
    this.Token = Token;
    this.UserId = UserId;
  }

  public String getToken() {
    return Token;
  }

  public String getUserId() {
    return UserId;
  }

  public void setToken(String token) { Token=token; }

}
