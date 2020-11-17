package Response;

import Models.Event;

/**
 * Event ID Response
 *response to the event ID request
 */
public class EventIdResponse extends CommonResponse {

  private String associatedUsername;
  private String eventID;
  private String personID;
  private float latitude;
  private float longitude;
  private String country;
  private String city;
  private String eventType;
  private Integer year;

  /**
   * Event Id Response
   * Successful response to the event Id request
   * @param event - event being returned

   */
  public EventIdResponse(Event event) {
    this.associatedUsername=event.getAssociatedUsername();
    this.eventID=event.getEventID();
    this.personID=event.getPersonID();
    this.latitude=event.getLatitude();
    this.longitude=event.getLongitude();
    this.country=event.getCountry();
    this.city=event.getCity();
    this.eventType=event.getEventType();
    this.year=event.getYear();
    this.success=true;
  }

  /**
   * Event Id Response
   * failed response to the ID request
   * @param message - Type of error
   */
  public EventIdResponse(String message){
    this.message=message;
    this.success=false;
  }

  public String getAssociatedUser(){return associatedUsername;}
  public String getEventID(){ return eventID; }
  public String getPersonID1(){ return personID; }
  public float getLatitude(){ return latitude; }
  public float getLongitude(){ return longitude; }
  public String getCountry(){ return country; }
  public String getCity(){ return city;}
  public String getEventType(){ return eventType; }
  public Integer getYear(){ return year; }
  public boolean getSuccess(){ return this.success; }
  public String getMessage(){ return this.message; }

}
