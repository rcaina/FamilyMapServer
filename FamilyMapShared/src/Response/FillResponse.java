package Response;

/**
 * Fill Response
 * Response to the fill request
 */
public class FillResponse extends CommonResponse {

  private int persons;
  private int events;

  public FillResponse(){}

  /**
   * Fill Response
   * Successful response to request
   */
  public FillResponse(int numPersons, int numEvents) {
    this.message="Successfully added "+ numPersons + " persons and " + numEvents + " events to the database";
    this.persons = numPersons;
    this.events = numEvents;
    this.success=true;
  }

  /**
   * Fill Response
   * Failed response to request
   * @param message - type of error
   */
  public FillResponse(String message) {
    this.message=message;
    this.success=false;
  }

  public int getNumPersons(){ return persons;}
  public int getNumEvents(){return events;}
  public boolean getSuccess(){return this.success; }
  public String getMessage(){ return this.message; }
}
