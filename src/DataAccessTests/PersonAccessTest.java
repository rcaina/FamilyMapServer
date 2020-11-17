package DataAccessTests;

import DataAccess.AccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import Models.Event;
import Models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonAccessTest {

  private Database db;
  private Person fakePerson;
  private Person fakePerson2;
  private Person fakePerson3;
  private Person fakePerson4;

  @BeforeEach
  public void setup() throws AccessException {
    db = new Database();
    fakePerson = new Person("someID","coolPerson","James","Bond","Male","Bob","Jane","baby");
    fakePerson2 = new Person("secondID","coolPerson","Blake","Lively","Female","Anakin","Leia","");
    fakePerson3 = new Person("thirdID","coolPerson","Legolas","Elf","Male","Aragon","Gimli","Zelda");
    fakePerson4 = new Person("someID","coolPerson","James","Bond","Male","Bob","Jane","baby");
  }
  @AfterEach
  public void tearDown() throws AccessException {
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  public void testInsert() throws AccessException {
    Person compare = null;

      Connection connection = db.openConnection();
      PersonDAO accessDao = new PersonDAO(connection);
      accessDao.insert(fakePerson);

    db.closeConnection(true);

    connection = db.openConnection();
    PersonDAO personDao = new PersonDAO(connection);

    compare = personDao.find(fakePerson.getAssociatedUsername());

    assertNotNull(compare);
    assertEquals(fakePerson.getPersonID(), compare.getPersonID());
  }

  @Test
  public void insertFail() throws Exception{
    boolean itWorked = true;
    Connection connection = db.openConnection();
    PersonDAO personDao = new PersonDAO(connection);

    try{
      personDao.insert(fakePerson);

      personDao.insert(fakePerson4);

    } catch (AccessException e){
      itWorked = false;
    }

    assertFalse(itWorked);

    Person compare = null;

    compare = personDao.find(fakePerson.getPersonID());

    assertNotEquals(compare, fakePerson4);
  }

  @Test
  void testFind() throws AccessException {
    Person compare = null;

      Connection connection = db.openConnection();
      PersonDAO accessDao = new PersonDAO(connection);
      accessDao.insert(fakePerson);
      accessDao.insert(fakePerson2);
      accessDao.insert(fakePerson3);

      db.closeConnection(true);

      connection = db.openConnection();

      PersonDAO personDao = new PersonDAO(connection);
      compare = personDao.find(fakePerson3.getAssociatedUsername());

    assertNotNull(compare);
    assertEquals(fakePerson.getPersonID(), compare.getPersonID());
  }

  @Test
  public void findFail() throws Exception{

    Connection connection = db.openConnection();
    PersonDAO personDao = new PersonDAO(connection);

    personDao.insert(fakePerson);

    Person compare = null;

    compare = personDao.find(fakePerson3.getPersonID());

    assertNull(compare);
  }
  @Test
  void testRemove() throws AccessException {
    Person compare = null;

      Connection connection = db.openConnection();
      PersonDAO accessDao = new PersonDAO(connection);
      accessDao.insert(fakePerson);
      compare = accessDao.find(fakePerson.getPersonID());
      System.out.println(compare);
      accessDao.remove(fakePerson.getPersonID());
      compare = accessDao.find(fakePerson.getPersonID());
      System.out.println(compare);

    assertNull(compare);
    assertEquals(null, compare);
  }

  @Test
  void testClear() throws AccessException {

    Person compare = null;

      Connection connection = db.openConnection();
      PersonDAO personDao = new PersonDAO(connection);

      personDao.insert(fakePerson);
      personDao.clear();
      compare = personDao.find(fakePerson.getPersonID());

    assertNull(compare);

  }

  @Test
  public void testRemoveUser() throws Exception{

    Person compare = null;

    Connection connection = db.openConnection();
    PersonDAO accessDao = new PersonDAO(connection);
    accessDao.insert(fakePerson);

    db.closeConnection(true);

    Connection connection1 = db.openConnection();
    PersonDAO accessDao1 = new PersonDAO(connection1);
    compare = accessDao1.find(fakePerson.getAssociatedUsername());

    assertEquals(fakePerson.getAssociatedUsername(), compare.getAssociatedUsername());

    accessDao1.removeUser(fakePerson.getAssociatedUsername());

    compare = accessDao1.find(fakePerson.getAssociatedUsername());

    assertEquals(null, compare);
  }

  @Test
  public void testFindFamily() throws AccessException {

    ArrayList<Person> compare = new ArrayList<>();

    Connection connection = db.openConnection();
    PersonDAO accessDao = new PersonDAO(connection);
    accessDao.insert(fakePerson);
    accessDao.insert(fakePerson2);
    accessDao.insert(fakePerson3);

    db.closeConnection(true);

    Connection connection1 = db.openConnection();
    PersonDAO accessDao1 = new PersonDAO(connection1);
    compare = accessDao1.findFamily(fakePerson.getAssociatedUsername());

    assertNotNull(compare);

    int arraySize = compare.size();

    assertEquals(3, arraySize);
  }

  @Test
  public void testFindFamilyFail() throws AccessException {

    ArrayList<Person> compare = new ArrayList<>();

    Connection connection = db.openConnection();
    PersonDAO accessDao = new PersonDAO(connection);
    accessDao.insert(fakePerson);
    accessDao.insert(fakePerson2);
    accessDao.insert(fakePerson3);

    db.closeConnection(true);

    Connection connection1 = db.openConnection();
    PersonDAO accessDao1 = new PersonDAO(connection1);
    compare = accessDao1.findFamily(null);
    //db.closeConnection(true);

    int arraySize = 15;
    if(compare.isEmpty()){
      compare = null;
      arraySize = 0;
    }

    assertNull(compare);

    assertEquals(0, arraySize);
  }
}