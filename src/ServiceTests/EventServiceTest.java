package ServiceTests;

import DataAccess.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.EventIdRequest;
import Response.EventIdResponse;
import Response.EventResponse;
import Service.ClearService;
import Service.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

  Database db = new Database();
  private AuthToken fakeToken;
  private User fakeUser;
  private Person fakePerson;
  private Event fakeEvent;
  private Event fakeEvent2;
  private String eventId1;
  private String eventId2;

  @BeforeEach
  void setUp() throws AccessException, SQLException {

    Connection connection = db.openConnection();
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
    fakeEvent2 = new Event(eventId2, "coolPerson", "p2020", -10.50f, 70.76f, "Japan", "Tokyo", "baptism", 2002);

    tokenDao.insert(fakeToken.getUserId()); //insertTest
    userDao.insert(fakeUser);
    personDao.insert(fakePerson);
    eventDao.insert(fakeEvent);
    eventDao.insert(fakeEvent2);

    db.closeConnection(true);
  }

  @AfterEach
  void tearDown() {
    ClearService clear = new ClearService();

    clear.ClearService();
  }

  @Test
  void eventId() throws AccessException {

    EventService eventService = new EventService();
    EventIdResponse response;
    EventIdRequest newRequest = new EventIdRequest(fakeEvent2.getEventID());

    response = eventService.eventId(newRequest, fakeToken.getToken());

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertNotNull(response.getEventID());
    assertEquals(fakeEvent2.getEventID(), response.getEventID());
    assertEquals(fakeEvent2.getAssociatedUsername(), response.getAssociatedUser());
    assertEquals(fakeEvent2.getPersonID(), response.getPersonID1());
    assertEquals(fakeEvent2.getEventType(), response.getEventType());
    assertEquals(fakeEvent2.getCountry(), response.getCountry());
    assertEquals(fakeEvent2.getCity(), response.getCity());
    assertEquals(fakeEvent2.getYear(), response.getYear());
    assertEquals(fakeEvent2.getLatitude(), response.getLatitude());
    assertEquals(fakeEvent2.getLongitude(), response.getLongitude());
    assertEquals(null, response.getMessage());
  }

  @Test
  void eventIdFail() throws AccessException {
    EventService eventService = new EventService();
    EventIdResponse response;

    response = eventService.eventId(null, fakeToken.getToken());

    assertNotNull(response);
    assertFalse(response.getSuccess());
    assertNull(response.getEventID());
    assertEquals("Error in Event ID service given Event ID", response.getMessage());
    assertFalse(response.getSuccess());
    assertNull(response.getEventID());

    assertEquals(null, response.getEventID());
    assertEquals(null, response.getAssociatedUser());
    assertEquals(null, response.getPersonID1());
    assertEquals(null, response.getEventType());
    assertEquals(null, response.getCountry());
    assertEquals(null, response.getCity());
    assertEquals(null, response.getYear());
  }

  @Test
  void event() throws AccessException {

    EventService eventService = new EventService();
    EventResponse response = new EventResponse();


    response = eventService.event(fakeToken.getToken());

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertNotNull(response.getData());
  }

  @Test
  void eventFail() throws AccessException {

    EventService eventService = new EventService();
    EventResponse response = new EventResponse();

    response = eventService.event("2551");

    assertNotNull(response);
    assertFalse(response.getSuccess());
    assertNull(response.getData());
    assertEquals("Error in Event Service given auth token", response.getMessage());
  }
}