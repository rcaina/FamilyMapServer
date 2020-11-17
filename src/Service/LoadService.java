package Service;

import DataAccess.*;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Response.LoadResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Load Service
 */
public class LoadService {
  Database db = new Database();
  private Connection connection;

  /**
   * Load Service
   * Constructor
   */
  public LoadService(Connection connection){
    this.connection = connection;
  }

  /**
   * Load
   * Clears all data from the database (just like the /clear API), and then loads the
   * posted user, person, and event data into the database.
   * @param req
   * @return LoadResponse
   */
  public LoadResponse load(LoadRequest req, boolean clear) throws AccessException {

    ClearService wipe = new ClearService();
    PersonDAO personDao = new PersonDAO(connection);
    UserDAO userDao = new UserDAO(connection);
    EventDAO eventDao = new EventDAO(connection);
    int numPersons = 0;
    int numUsers = 0;
    int numEvents = 0;

    if(clear) wipe.ClearService();

    try{
      List<Person> souls = req.getPersons();
      List<User> users = req.getUsers();
      List<Event> events = req.getEvents();

      for(Person soul: souls){
        personDao.insert(soul);
        numPersons++;
      }
      for(User user: users){
        userDao.insert(user);
        numUsers++;
      }
      for(Event event: events){
        eventDao.insert(event);
        numEvents++;
      }
      if(souls.size() == 0 && events.size() == 0){
        return new LoadResponse("Error in load response");
      }
    } catch (AccessException | SQLException e) {
      db.closeConnection(false);
      return new LoadResponse("Error in load response");
    }

    return new LoadResponse(numPersons, numUsers, numEvents);
  }
}
