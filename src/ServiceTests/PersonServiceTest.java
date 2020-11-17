package ServiceTests;

import DataAccess.*;
import Models.AuthToken;
import Models.Person;
import Models.User;
import Requests.PersonIdRequest;
import Response.PersonIdResponse;
import Response.PersonResponse;
import Service.ClearService;
import Service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

  Database db = new Database();
  private AuthToken fakeToken;
  private User fakeUser;
  private Person fakePerson;
  private Person fakePerson2;

  @BeforeEach
  void setUp() throws SQLException, AccessException {

    Connection connection = db.openConnection();
    AuthTokenDAO tokenDao = new AuthTokenDAO(connection);
    UserDAO userDao  = new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);

    fakeToken= new AuthToken("abcdefg","p2020");
    fakeUser = new User("coolPerson", "dugdug1", "jdug@gmail.com", "John", "Bond", "Male", "p2020");
    fakePerson = new Person("p2020","coolPerson","John","Bond","Male","Bob","Jane","baby");
    fakePerson2 = new Person("p2050","coolPerson","James","Bond","Male","Richard","robin","boo");

    tokenDao.insert(fakeToken.getUserId());//insertTest
    userDao.insert(fakeUser);
    personDao.insert(fakePerson);
    personDao.insert(fakePerson2);

    db.closeConnection(true);
  }

  @AfterEach
  void tearDown() {
    ClearService clear = new ClearService();

    clear.ClearService();
  }

  @Test
  void person() throws AccessException {

    PersonService personService = new PersonService();
    PersonResponse response = new PersonResponse();


    response = personService.person(fakeToken.getToken());

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertNotNull(response.getData());
  }

  @Test
  void personFail() throws AccessException {

    PersonService personService = new PersonService();
    PersonResponse response = new PersonResponse();

    response = personService.person("2551");

    assertNotNull(response);
    assertFalse(response.getSuccess());
    assertNull(response.getData());
    assertEquals("Error in person service given authToken", response.getMessage());
  }

  @Test
  void personId() throws AccessException {

    PersonService personService = new PersonService();
    PersonIdResponse response;
    PersonIdRequest newRequest = new PersonIdRequest(fakePerson2.getPersonID());

    response = personService.personId(newRequest, fakeToken.getToken());

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertNotNull(response.getPersonID());
    assertEquals(response.getPersonID(), fakePerson2.getPersonID());
    assertEquals(response.getAssociatedUsername(), fakePerson2.getAssociatedUsername());
    assertEquals(response.getFirstName(), fakePerson2.getFirstName());
    assertEquals(response.getLastName(), fakePerson2.getLastName());
    assertEquals(response.getGender(), fakePerson2.getGender());
    assertEquals(response.getFatherID(), fakePerson2.getFatherID());
    assertEquals(response.getMotherID(), fakePerson2.getMotherID());
    assertEquals(response.getSpouseID(), fakePerson2.getSpouseID());
    assertEquals(response.getMessage(), null);
  }

  @Test
  void personIdFail() throws AccessException {

    PersonService personService = new PersonService();
    PersonIdResponse response;

    response = personService.personId(null, fakeToken.getToken());

    assertNotNull(response);
    assertFalse(response.getSuccess());
    assertNull(response.getPersonID());
    assertEquals(response.getMessage(), "No person found matching given person id");
    assertEquals(null, response.getPersonID());
    assertEquals(null, response.getAssociatedUsername());
    assertEquals(null, response.getFirstName());
    assertEquals(null, response.getLastName());
    assertEquals(null, response.getGender());
    assertEquals(null, response.getFatherID());
    assertEquals(null, response.getMotherID());
    assertEquals(null, response.getSpouseID());
  }
}