package ServiceTests;

import DataAccess.AccessException;
import DataAccess.Database;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Response.LoadResponse;
import Service.ClearService;
import Service.LoadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

  private Person person1;
  private Person person2;
  private User user1;
  private User user2;
  private Event event1;
  private Event event2;
  ArrayList<Person> personList = new ArrayList<>();
  ArrayList<User> userList = new ArrayList<>();
  ArrayList<Event> eventList = new ArrayList<>();

  @BeforeEach
  void setUp() {

    person1=new Person("p2020", "coolPerson", "John", "Bond", "Male", "Bob", "Jane", "baby");
    person2=new Person("p2050", "coolPerson", "James", "Bond", "Male", "Richard", "robin", "boo");
    user1=new User("coolPerson", "dugdug1", "jdug@gmail.com", "John", "Bond", "Male", "p2020");
    //user2 = new User();
    event1=new Event("eventId1", "coolPerson", "p2020", 20.50f, 30.76f, "Europe", "France", "graduation", 2012);
    event2=new Event("eventId2", "coolPerson", "p2020", -10.50f, 70.76f, "Japan", "Tokyo", "baptism", 2002);
  }
  @AfterEach
  void tearDown() {
    ClearService clear = new ClearService();

    clear.ClearService();
  }

  @Test
  void load() throws AccessException {
    Database db = new Database();
    Connection connection = db.openConnection();

    LoadResponse response = new LoadResponse();
    LoadService loadIt = new LoadService(connection);

    personList.add(person1);
    personList.add(person2);
    userList.add(user1);
    eventList.add(event1);
    eventList.add(event2);

    Person[] personArray = personList.toArray(new Person[personList.size()]);
    Event[] eventArray = eventList.toArray(new Event[eventList.size()]);
    User[] userArray = new User[0];

    LoadRequest req = new LoadRequest(personArray, userArray, eventArray);

    response = loadIt.load(req, false);

    assertNotNull(response);
    assertEquals(true, response.getSuccess());
    assertEquals(2, response.getNumPersons());
    assertEquals(0, response.getNumUsers());
    assertEquals(2, response.getNumEvents());
    assertEquals("Successfully added 0 users, 2 persons, and 2 events to the database", response.getMessage());


    db.closeConnection(true);

  }

  @Test
  void loadFail() throws AccessException {
    Database db = new Database();
    Connection connection = db.openConnection();

    LoadResponse response = new LoadResponse();
    LoadService loadIt = new LoadService(connection);

    Person[] personArray = personList.toArray(new Person[personList.size()]);
    Event[] eventArray = eventList.toArray(new Event[eventList.size()]);
    User[] userArray = new User[0];

    LoadRequest req = new LoadRequest(personArray, userArray, eventArray);

    response = loadIt.load(req, false);

    assertNotNull(response);
    assertEquals(false, response.getSuccess());
    assertEquals(0, response.getNumPersons());
    assertEquals(0, response.getNumUsers());
    assertEquals(0, response.getNumEvents());
    assertEquals("Error in load response", response.getMessage());


    db.closeConnection(true);

  }

}