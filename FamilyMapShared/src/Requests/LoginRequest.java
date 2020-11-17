package Requests;

/**
 * login Request
 * sends login request to the server
 */
public class LoginRequest {

  private String userName;
  private String password;

  /**
   * Login Request Constructor
   * contructs the data members with the strings being passed int
   * @param userName - name of current user that is login in
   * @param password - password of the current user that is logging in
   */
  public LoginRequest(String userName, String password) {
    this.userName=userName;
    this.password=password;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }
}
