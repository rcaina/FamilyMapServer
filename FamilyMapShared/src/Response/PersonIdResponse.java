package Response;

import Models.Person;

/**
 * Person ID Response
 * Response to the person ID request
 */
public class PersonIdResponse extends CommonResponse {

  private String associatedUsername;
  private String personID;
  private String firstName;
  private String lastName;
  private String gender;
  private String fatherID=null;
  private String motherID=null;
  private String spouseID=null;

  /**
   * Person ID Response
   * Successful response to request
   *
   * @param person - Person object with its attached information
   */

  public PersonIdResponse(Person person){

    this.associatedUsername=person.getAssociatedUsername();
    this.personID=person.getPersonID();
    this.firstName=person.getFirstName();
    this.lastName=person.getLastName();
    this.gender=person.getGender();
    this.fatherID=person.getFatherID();
    this.motherID=person.getMotherID();
    this.spouseID=person.getSpouseID();
    this.success=true;
  }

  /**
   * Person ID Response
   * Failed response to request
   *
   * @param message - Type of error message
   */
  public PersonIdResponse(String message) {
    this.message=message;
    this.success=false;
  }

  public String getMessage(){ return this.message; }
  public boolean getSuccess(){ return this.success; }
  public String getPersonID(){ return personID; }
  public String getAssociatedUsername(){return associatedUsername;}
  public String getFirstName(){ return firstName;}
  public String getLastName(){ return lastName; }
  public String getGender(){ return gender; }
  public String getFatherID() {return fatherID; }
  public String getMotherID(){ return motherID; }
  public String getSpouseID() {return spouseID; }
}
