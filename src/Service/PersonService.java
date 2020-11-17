package Service;

import DataAccess.AccessException;
import DataAccess.AuthTokenDAO;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Models.AuthToken;
import Models.Person;
import Requests.PersonIdRequest;
import Response.PersonIdResponse;
import Response.PersonResponse;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Person Service
 */
public class PersonService {

  Database db=new Database();
  Connection connection;

  /**
   * Person Service
   * Constructor
   */
  public PersonService() {
  }

  /**
   * person
   * Returns ALL family members of the current user. The current user is
   * determined from the provided auth token.
   *
   * @param authToken
   * @return PersonResponse
   */
  public PersonResponse person(String authToken) throws AccessException {

    try {
      if(db.getConnection() == null) {
        connection=db.openConnection();
      } else{
        connection = db.getConnection();
      }

      AuthToken token;
      Person person;
      AuthTokenDAO tokenDao = new AuthTokenDAO(connection);
      PersonDAO personDao = new PersonDAO(connection);
      ArrayList<Person> family;

      token=tokenDao.find(authToken);
      if(token == null){
        db.closeConnection(false);
        return new PersonResponse("Error in person service given authToken");
      }
      person=personDao.find(token.getUserId());
      if(person == null){
        db.closeConnection(false);
        return new PersonResponse("Error in person service given authToken");
      }
      family=personDao.findFamily(person.getAssociatedUsername());
      if(family == null){
        db.closeConnection(false);
        return new PersonResponse("Error in person service given authToken");
      }

      db.closeConnection(true);

      return new PersonResponse(family, true); //Turning this into JSON should be done in the handler
    }
    catch (AccessException e){
      db.closeConnection(false);
      return new PersonResponse("Error in person service given authToken");
    }
  }

  public PersonIdResponse personId(PersonIdRequest req, String authToken) throws AccessException {  //should I add String authToken as parameter

    try {
      if(db.getConnection() == null) {
        connection=db.openConnection();
      }else{
        connection = db.getConnection();
      }

      AuthToken token;
      AuthTokenDAO tokenDao = new AuthTokenDAO(connection);
      Person person;
      PersonDAO personDao = new PersonDAO(connection);
      ArrayList<Person> family = new ArrayList<>();
      Person foundPerson = new Person();

      if(req == null){
        db.closeConnection(false);
        return new PersonIdResponse("No person found matching given person id");
      }
      token=tokenDao.find(authToken);
      if(token == null){
        db.closeConnection(false);
        return new PersonIdResponse("No person found matching given person id");
      }
      person=personDao.find(token.getUserId());
      if(person == null){
        db.closeConnection(false);
        return new PersonIdResponse("No person found matching given person id");
      }
      family=personDao.findFamily(person.getAssociatedUsername());
      if(family == null){
        db.closeConnection(false);
        return new PersonIdResponse("No person found matching given person id");
      }

      for(Person searchPerson: family){
        if(searchPerson.getPersonID().equals( req.getPersonID())){
          foundPerson = searchPerson;
        }
      }

      if(foundPerson.getPersonID() == null){
        db.closeConnection(false);
        return new PersonIdResponse("No person found matching given person id");
      }

      db.closeConnection(true);

      return new PersonIdResponse(foundPerson);
    }
    catch (AccessException e){
      db.closeConnection(false);
      return new PersonIdResponse("Error in person service given personID");
    }
  }
}


