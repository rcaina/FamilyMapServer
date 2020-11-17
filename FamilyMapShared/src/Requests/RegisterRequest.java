package Requests;

/**
 * Register Request
 * sends register request to the server
 */
public class RegisterRequest {

  private String userName;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String gender;

  public RegisterRequest(){}

  /**
   * Register Request Constructor
   * creates new account Request for new user
   * @param userName - new user name for account
   * @param password - new password for account
   * @param email - new email to associate with account
   * @param firstName - first name of the user
   * @param lastName - last name of the user
   * @param gender - gender of the user
   */
  public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
    this.userName=userName;
    this.password=password;
    this.email=email;
    this.firstName=firstName;
    this.lastName=lastName;
    this.gender=gender;
  }

  public String getUserName() { return userName; }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getGender() {
    return gender;
  }
}
