package Response;

import Models.Person;
import java.util.ArrayList;

/**
 * Person Response
 * Response to the person request
 */
public class PersonResponse extends CommonResponse {

  private ArrayList<Person> data;

  public PersonResponse(){}
  /**
   * Person Response
   * Successful response to request
   * @param data - array list of persons
   */
  public PersonResponse(ArrayList<Person> data, boolean success) {
    this.data=data;
    this.success=success;
  }

  /**
   * Person Respeonse
   * Failed response to request
   * @param message - type of error message
   */
  public PersonResponse(String message){
    this.message=message;
    this.success=false;
  }

  public boolean getSuccess(){ return this.success; }
  public String getMessage(){ return this.message; }
  public ArrayList<Person> getData(){ return data; }
}
