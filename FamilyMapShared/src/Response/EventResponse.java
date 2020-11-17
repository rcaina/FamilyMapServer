package Response;

import Models.Event;

import java.util.ArrayList;

/**
 * Event Response
 * reponse to the event request
 */
public class EventResponse extends CommonResponse {

  private ArrayList<Event> data;

  /**
   * Event Response
   * Successful response to event request
   * @param data - array of event object
   */
  public EventResponse(ArrayList<Event> data) {
    this.data=data;
    this.success=true;
  }

  /**
   * Event Response
   * Failed response to event request
   * @param message - type of error message
   */
  public EventResponse(String message){
    this.message=message;
    this.success=false;
  }

  public EventResponse() { }

  public boolean getSuccess(){ return this.success; }
  public String getMessage(){ return this.message; }
  public ArrayList<Event> getData(){ return data; }
}
