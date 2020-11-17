package Service;

import DataAccess.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Requests.EventIdRequest;
import Response.EventIdResponse;
import Response.EventResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Event Service
 */
public class EventService {
  Database db=new Database();
  Connection connection;
  /**
   * Event Service
   * Constructor
   */
  public EventService(){

  }

  /**
   * event
   * Returns ALL events for ALL family members of the current user. The current
   * user is determined from the provided auth token.
   * @param req
   * @return EventResponse
   */
  public EventIdResponse eventId(EventIdRequest req, String userToken) throws AccessException {

    try {
      if(db.getConnection() == null) {
        connection=db.openConnection();
      }else{
        connection = db.getConnection();
      }

      AuthTokenDAO authTokenDao=new AuthTokenDAO(connection);
      AuthToken token;
      Person person;
      PersonDAO personDao=new PersonDAO(connection);
      EventDAO eventDao=new EventDAO(connection);
      ArrayList<Event> allEvents;
      Event foundEvent=new Event();

      if(req == null){
        db.closeConnection(false);
        return new EventIdResponse("Error in Event ID service given Event ID");
      }
      token=authTokenDao.find(userToken);
      if(token == null){
        db.closeConnection(false);
        return new EventIdResponse("Error in Event ID service given Event ID");
      }
      person=personDao.find(token.getUserId());
      if(person == null){
        db.closeConnection(false);
        return new EventIdResponse("Error in Event ID service given Event ID");
      }
      allEvents=eventDao.getAllPersonRelatedEvents(person.getAssociatedUsername());
      if(allEvents == null){
        db.closeConnection(false);
        return new EventIdResponse("Error in Event ID service given Event ID");
      }


      for (Event event : allEvents) {
        if (event.getEventID().equals(req.getEventId())) {
          foundEvent=event;
        }
      }

      if(foundEvent.getEventID() == null){
        db.closeConnection(false);
        return new EventIdResponse("Error in Event ID service given Event ID"); }

      db.closeConnection(true);

      return new EventIdResponse(foundEvent);
    }
    catch (AccessException e){
      db.closeConnection(false);
      return new EventIdResponse("Error in Event ID service given Event ID");
    }

  }

  public EventResponse event(String userToken) throws AccessException {

    try {
      if(db.getConnection() == null) {
        connection = db.openConnection();
      }
      else {
        connection = db.getConnection();
      }

      AuthTokenDAO authTokenDao=new AuthTokenDAO(connection);
      AuthToken token;
      Person person;
      PersonDAO personDao=new PersonDAO(connection);
      EventDAO eventDao=new EventDAO(connection);
      ArrayList<Event> allEvents;


      token=authTokenDao.find(userToken);
      if(token == null){
        db.closeConnection(false);
        return new EventResponse("Error in Event Service given auth token");
      }
      person=personDao.find(token.getUserId());
      if(person == null){
        db.closeConnection(false);
        return new EventResponse("Error in Event Service given auth token");
      }
      allEvents=eventDao.getAllPersonRelatedEvents(person.getAssociatedUsername());
      if(allEvents == null){
        db.closeConnection(false);
        return new EventResponse("Error in Event Service given auth token");
      }

      db.closeConnection(true);

      return new EventResponse(allEvents);
    }
    catch (AccessException e){
      db.closeConnection(false);
      return new EventResponse("Error in Event Service given auth token");
    }
  }
}
