package Response;

/**
 * Login Response
 * Response to the login request
 */
public class LoginResponse extends CommonResponse {

  private String authToken;
  private String userName;
  private String personID;

  /**
   * Login Response
   * Successful response to request
   * @param authToken
   * @param userName
   * @param personID
   */
  public LoginResponse(String authToken, String userName, String personID) {
    this.authToken=authToken;
    this.userName=userName;
    this.personID=personID;
    this.success=true;
  }

  /**
   * Login Response
   * Failed response to request
   * @param message - Type of error
   */
  public LoginResponse(String message) {
    this.message=message;
    this.success=false;
  }
  public LoginResponse() {}

  public boolean getSuccess1(){ return this.success;}
  public String getMessage1(){ return this.message;}
  public String getAuthToken(){return authToken; }
  public String getUserName1(){return userName;}
  public String getPersonID1(){return personID;}
}
