package ServiceTests;

import DataAccess.AccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.RegisterRequest;
import Response.FillResponse;
import Service.ClearService;
import Service.FillService;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {
  Event event1 = new Event("123", "asionjason", "4321", 20.17777f, (float) 20.44345, "europe", "france", "birth", 1901);
  Person person1 = new Person("2345", "asianJason", null, null, null, null, null, null);

  @Test
  void fill() throws AccessException {

    Database db = new Database();
    int numGenerations=4;
    boolean passed = false;

    Connection connection = db.openConnection();
    ClearService clear = new ClearService();

    User newUser=new User();
    Person newPerson = new Person();
    UserDAO userDao=new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);
    RegisterRequest req = new RegisterRequest("asianJason", "pass", "jason@gmail.com", "jason", "curtin", "m");
    FillResponse response = new FillResponse();

    newUser.setUserName(req.getUserName());
    newUser.setPassword(req.getPassword());
    newUser.setEmail(req.getEmail());
    newUser.setFirstName(req.getFirstName());
    newUser.setLastName(req.getLastName());
    newUser.setGender(req.getGender());
    newUser.setPersonID(person1.getPersonID());

    newPerson.setPersonID(person1.getPersonID());
    newPerson.setAssociatedUsername(req.getUserName());
    newPerson.setFirstName(req.getFirstName());
    newPerson.setLastName(req.getLastName());
    newPerson.setGender(req.getGender());


    try {
      userDao.insert(newUser);
      personDao.insert(newPerson);
      db.closeConnection(true);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (AccessException e) {
      e.printStackTrace();
    }
    Connection connection1 = db.openConnection();
    FillService reFill = new FillService(connection1);
    response = reFill.fill(req.getUserName(), numGenerations);
    db.closeConnection(true);

    //Because the total events vary (due to different ages in husband wife and range of lives lived, this will be a range of number of events
    if(response.getNumEvents() < 154 && response.getNumEvents() > 146){
      passed = true;
    }
    assertEquals(31, response.getNumPersons());
    assertTrue(passed); // This test the correct range number of events)
    assertTrue(response.getSuccess());
    clear.ClearService();
  }

  @Test
  void fillFail() throws AccessException {

    Database db = new Database();
    int numGenerations=-2;
    boolean passed = false;

    Connection connection = db.openConnection();
    ClearService clear = new ClearService();

    User newUser=new User();
    Person newPerson = new Person();
    UserDAO userDao=new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);
    RegisterRequest req = new RegisterRequest("asianJason", "pass", "jason@gmail.com", "jason", "curtin", "m");
    FillResponse response = new FillResponse();

    newUser.setUserName(req.getUserName());
    newUser.setPassword(req.getPassword());
    newUser.setEmail(req.getEmail());
    newUser.setFirstName(req.getFirstName());
    newUser.setLastName(req.getLastName());
    newUser.setGender(req.getGender());
    newUser.setPersonID(person1.getPersonID());

    newPerson.setPersonID(person1.getPersonID());
    newPerson.setAssociatedUsername(req.getUserName());
    newPerson.setFirstName(req.getFirstName());
    newPerson.setLastName(req.getLastName());
    newPerson.setGender(req.getGender());


    try {
      userDao.insert(newUser);
      personDao.insert(newPerson);
      db.closeConnection(true);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (AccessException e) {
      e.printStackTrace();
    }
    Connection connection1 = db.openConnection();
    FillService reFill = new FillService(connection1);
    response = reFill.fill(req.getUserName(), numGenerations);
    db.closeConnection(true);

    if(response.getNumEvents() < 152 && response.getNumEvents() > 146){
      passed = true;
    }
    assertEquals(response.getNumPersons(), 0);
    assertFalse(passed);
    assertFalse(response.getSuccess());
    assertEquals(response.getMessage(), "Error in fill service");
    clear.ClearService();
  }
}