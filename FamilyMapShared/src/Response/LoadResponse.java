package Response;

/**
 * Load Response
 * Response to the load request
 */
public class LoadResponse extends CommonResponse {

  private int persons;
  private int users;
  private int events;

  /**
   * Load Response
   * Successful response to request
   */
  public LoadResponse(int persons, int users, int events) {

    this.persons = persons;
    this.users = users;
    this.events = events;
    this.message="Successfully added " + users +" users, " + persons + " persons, and " + events + " events to the database";
    this.success=true;
  }

  /**
   * Load Response
   * Failed response to request
   * @param message - type of error message
   */
  public LoadResponse(String message){
    this.message=message;
    this.success=false;
  }

  public LoadResponse() {

  }

  public boolean getSuccess(){ return this.success; }
  public String getMessage(){ return this.message; }
  public Integer getNumPersons(){return persons; }
  public Integer getNumUsers(){return users; }
  public Integer getNumEvents(){return events; }
}
