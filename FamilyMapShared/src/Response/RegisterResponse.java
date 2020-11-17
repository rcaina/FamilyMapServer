package Response;

/**
 * Register Response
 * Response to the register request
 */
public class RegisterResponse extends CommonResponse {

  private String authToken;
  private String userName;
  private String personID;

  public RegisterResponse(){}

  /**
   * Register Response
   * Response is succesful, sets the new user information
   * @param authToken - new authorization token for the new user
   * @param userName- new user name for the new account to be created
   * @param personID - new person ID for the new account user
   */
  public RegisterResponse(String authToken, String userName, String personID) {
    this.authToken=authToken;
    this.userName=userName;
    this.personID=personID;
    this.success=true;
  }

  /**
   * Register Response
   * Response failed, returns error message
   * @param message - the type of error message
   */
  public RegisterResponse(String message) {
    this.message=message;
    this.success = false;
  }

  public String getUserName(){ return userName; }
  public boolean getSuccess(){ return this.success; }
  public String getMessage(){ return this.message; }
  public String getAuthToken(){ return this.authToken; }
}
