package ServiceTests;

import DataAccess.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Service.ClearService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

  Database db = new Database();
  Connection connection;
  private AuthToken fakeToken;
  private User fakeUser;
  private Person fakePerson;
  private Event fakeEvent;
  private Event fakeEvent2;
  private String eventId1;
  private String eventId2;

  @BeforeEach
  void setUp() throws AccessException, SQLException {

    if(connection == null) {
      connection=db.openConnection();
    }
    else{
      connection = db.getConnection();
    }
    AuthTokenDAO tokenDao = new AuthTokenDAO(connection);
    UserDAO userDao  = new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);
    EventDAO eventDao = new EventDAO(connection);
    eventId1 =UUID.randomUUID().toString();
    eventId2 = UUID.randomUUID().toString();

    fakeToken= new AuthToken("abcdefg","p2020");
    fakeUser = new User("coolPerson", "dugdug1", "jdug@gmail.com", "John", "Bond", "Male", "p2020");
    fakePerson = new Person("p2020","coolPerson","John","Bond","Male","Bob","Jane","baby");
    fakeEvent = new Event(eventId1, "coolPerson", "p2020", 20.50f, 30.76f, "Europe", "France", "graduation", 2012);


    tokenDao.insert(fakeToken.getUserId()); //insertTest
    userDao.insert(fakeUser);
    personDao.insert(fakePerson);
    eventDao.insert(fakeEvent);

    db.closeConnection(true);
  }

  @Test
  void clearService() throws AccessException {

    Connection connection = db.openConnection();

    Person person = new Person();
    PersonDAO personDao = new PersonDAO(connection);
    User user = new User();
    UserDAO userDao = new UserDAO(connection);
    Event event = new Event();
    EventDAO eventDao = new EventDAO(connection);
    AuthToken token = new AuthToken();
    AuthTokenDAO tokenDao = new AuthTokenDAO(connection);

    person = personDao.find(fakePerson.getPersonID());
    user = userDao.find(fakePerson.getAssociatedUsername());
    event = eventDao.find(fakeEvent.getEventID());
    token = tokenDao.find(fakeToken.getToken());

    assertEquals(fakePerson.getPersonID(), person.getPersonID());
    assertEquals(fakeToken.getToken(), token.getToken());
    assertEquals(fakePerson.getAssociatedUsername(), user.getUserName());
    assertEquals(fakeEvent.getEventID(), event.getEventID());

    db.closeConnection(true);

    ClearService clearNow = new ClearService();

    clearNow.ClearService();

    Connection connection1 = db.openConnection();

    Person person1 = new Person();
    PersonDAO personDao1 = new PersonDAO(connection1);
    User user1 = new User();
    UserDAO userDao1 = new UserDAO(connection1);
    Event event1 = new Event();
    EventDAO eventDao1 = new EventDAO(connection1);
    AuthToken token1 = new AuthToken();
    AuthTokenDAO tokenDao1 = new AuthTokenDAO(connection1);

    person1 = personDao1.find(fakePerson.getPersonID());
    user1 = userDao1.find(fakePerson.getAssociatedUsername());
    event1 = eventDao1.find(fakeEvent.getEventID());
    token1 = tokenDao1.find(fakeToken.getToken());

    db.closeConnection(true);

    assertEquals(null, person1.getPersonID());
    assertEquals(null, user1.getUserName());
    assertEquals(null, token1.getToken());
    assertEquals(null, event1.getEventID());

  }
}