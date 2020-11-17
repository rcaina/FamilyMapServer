package Models;

public class Event {

  private String eventID;
  private String associatedUsername;
  private String personID;
  private float latitude;
  private float longitude;
  private String country;
  private String city;
  private String eventType;
  private Integer year;

  public Event(){}

  public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, Integer year) {

    this.eventID=eventID;
    this.associatedUsername=associatedUsername;
    this.personID=personID;
    this.latitude=latitude;
    this.longitude=longitude;
    this.country=country;
    this.city=city;
    this.eventType=eventType;
    this.year=year;
  }


  public void setEventID(String eventID) {
    this.eventID=eventID;
  }

  public void setAssociatedUsername(String associatedUsername) {
    this.associatedUsername=associatedUsername;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }

  public void setLatitude(float latitude) {
    this.latitude=latitude;
  }

  public void setLongitude(float longitude) {
    this.longitude=longitude;
  }

  public void setCountry(String country) {
    this.country=country;
  }

  public void setCity(String city) {
    this.city=city;
  }

  public void setEventType(String eventType) {
    this.eventType=eventType;
  }

  public void setYear(Integer year) {
    this.year=year;
  }

  public String getEventID() {
    return eventID;
  }

  public String getAssociatedUsername() {
    return associatedUsername;
  }

  public String getPersonID() {
    return personID;
  }

  public float getLatitude() {
    return latitude;
  }

  public float getLongitude() {
    return longitude;
  }

  public String getCountry() {
    return country;
  }

  public String getCity() {
    return city;
  }

  public String getEventType() {
    return eventType;
  }

  public Integer getYear() {
    return year;
  }
}
