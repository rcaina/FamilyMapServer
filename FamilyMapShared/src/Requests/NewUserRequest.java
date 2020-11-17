package Requests;

public class NewUserRequest {

  private String userName;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String gender;
  private String personID;

  public String getUserName() {
    return userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender=gender;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }

}
