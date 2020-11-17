package DataAccessTests;

import DataAccess.AccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Models.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventsAccessTest {

  private Database db;
  private Event fakeEvent;
  private Event fakeEvent2;
  private Event fakeEvent3;
  private Event fakeEvent4;

  @BeforeEach
  public void setup() throws AccessException {
    db = new Database();
    fakeEvent = new Event("p2020","jdmartin","someID",107.5f, 10.75f,"USA","Provo","party",1993);
    fakeEvent2 = new Event("yvgg","jdmartin","secondID",107.31f, 105.75f,"Peru","Lima","wedding",1995);
    fakeEvent3 = new Event("ngtd","jdmartin","thirdID",10.5f,1.65f,"Canada","Toronto","festival",1994);
    fakeEvent4 = new Event("p2020","tgsmith","fourthID",137.5f, 15.65f,"Italy","Rome","party",1990);
  }

  @AfterEach
  public void tearDown() throws AccessException {
    db.openConnection();
    db.clearTables();;
    db.closeConnection(true);
  }

  @Test
  public void testInsert() throws AccessException {
    Event compare = null;

      Connection connection = db.openConnection();
      EventDAO accessDao = new EventDAO(connection);
      accessDao.insert(fakeEvent);
      compare = accessDao.find(fakeEvent.getEventID());
      System.out.println(fakeEvent.getEventID());
      db.closeConnection(true);

    assertNotNull(compare);
    assertEquals(fakeEvent.getEventID(), compare.getEventID());
  }

  @Test
  public void insertFail() throws Exception{
    boolean itWorked = true;
    Connection connection = db.openConnection();
    EventDAO EventsDao = new EventDAO(connection);

    try{
      EventsDao.insert(fakeEvent);

      EventsDao.insert(fakeEvent4);

    } catch (AccessException e){
      itWorked = false;
    }

    assertFalse(itWorked);

    Event compare = null;

      compare = EventsDao.find(fakeEvent.getEventID());
      db.closeConnection(true);

    assertNotEquals(compare, fakeEvent4);
  }

  @Test
  void testFind() throws AccessException {
    Event compare = null;

      Connection connection = db.openConnection();
      EventDAO accessDao = new EventDAO(connection);
      accessDao.insert(fakeEvent);
      accessDao.insert(fakeEvent2);
      accessDao.insert(fakeEvent3);
      compare = accessDao.find(fakeEvent3.getEventID());
      db.closeConnection(true);

    assertNotNull(compare);
    assertEquals(fakeEvent3.getEventID(), compare.getEventID());
  }

  @Test
  public void findFail() throws Exception{
      Connection connection = db.openConnection();
      EventDAO EventsDao = new EventDAO(connection);

      EventsDao.insert(fakeEvent);

    Event compare = null;

      compare = EventsDao.find(fakeEvent3.getEventID());
      db.closeConnection(true);

    assertNull(compare);
  }
  @Test
  void testRemove() throws AccessException {
    Event compare = null;

      Connection connection = db.openConnection();
      EventDAO accessDao = new EventDAO(connection);
      accessDao.insert(fakeEvent);
      compare = accessDao.find(fakeEvent.getEventID());
      accessDao.remove(fakeEvent.getEventID());
      compare = accessDao.find(fakeEvent.getEventID());
      System.out.println(compare);
      db.closeConnection(true);

    assertNull(compare);
    assertEquals(null, compare);
  }

  @Test
  void testClear() throws AccessException {

    Event compare = null;

      Connection connection = db.openConnection();
      EventDAO EventsDao = new EventDAO(connection);

      EventsDao.insert(fakeEvent);
      EventsDao.clear();
      compare = EventsDao.find(fakeEvent.getEventID());
      db.closeConnection(true);

    assertNull(compare);

  }

  @Test
  void testRemoveUser() throws AccessException {

    Event compare = null;

    Connection connection = db.openConnection();
    EventDAO accessDao = new EventDAO(connection);
    accessDao.insert(fakeEvent);

    db.closeConnection(true);

    Connection connection1 = db.openConnection();
    EventDAO accessDao1 = new EventDAO(connection1);
    compare = accessDao1.find(fakeEvent.getEventID());

    assertEquals(fakeEvent.getEventID(), compare.getEventID());

    accessDao1.removeUser(fakeEvent.getAssociatedUsername());

    compare = accessDao1.find(fakeEvent.getAssociatedUsername());

    db.closeConnection(true);

    assertNull(compare);
    assertEquals(null, compare);
  }

  @Test
  void testGetPersonRelatedEvents() throws AccessException {

    ArrayList<Event> compare = new ArrayList<>();

    Connection connection = db.openConnection();
    EventDAO accessDao = new EventDAO(connection);
    accessDao.insert(fakeEvent);
    accessDao.insert(fakeEvent2);
    accessDao.insert(fakeEvent3);

    db.closeConnection(true);

    Connection connection1 = db.openConnection();
    EventDAO accessDao1 = new EventDAO(connection1);
    compare = accessDao1.getAllPersonRelatedEvents(fakeEvent3.getAssociatedUsername());
    db.closeConnection(true);

    assertNotNull(compare);

    int arraySize = compare.size();

    assertEquals(3, arraySize);
  }

  @Test
  void testGetPersonRelatedEventsFail() throws AccessException {

    ArrayList<Event> compare = new ArrayList<>();

    Connection connection = db.openConnection();
    EventDAO accessDao = new EventDAO(connection);
    accessDao.insert(fakeEvent);
    accessDao.insert(fakeEvent2);
    accessDao.insert(fakeEvent3);

    db.closeConnection(true);

    Connection connection1 = db.openConnection();
    EventDAO accessDao1 = new EventDAO(connection1);
    compare = accessDao1.getAllPersonRelatedEvents(null);
    db.closeConnection(true);

    assertNull(compare);

    int arraySize = 15;
    if(compare == null){
      arraySize = 0;
    }

    assertEquals(0, arraySize);
  }
}