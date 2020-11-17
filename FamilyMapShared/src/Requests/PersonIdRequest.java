package Requests;

public class PersonIdRequest {

  String personID;
  public PersonIdRequest(String personID){
    this.personID = personID;
  }

  public String getPersonID(){ return personID; }
}
