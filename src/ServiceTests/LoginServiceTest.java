package ServiceTests;

import DataAccess.AccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Models.Person;
import Models.User;
import Requests.LoginRequest;
import Response.LoginResponse;
import Service.ClearService;
import Service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

  Database db = new Database();
  private User fakeUser;
  private Person fakePerson;
  private LoginRequest info;
  private LoginRequest info2;

  @BeforeEach
  void setUp() throws AccessException, SQLException {

    Connection connection = db.openConnection();
    UserDAO userDao = new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);

    info = new LoginRequest("coolPerson", "youShallPass");
    info2 = new LoginRequest("weakBones", "youShallNotPass");
    fakeUser = new User("coolPerson", "dugdug1", "jdug@gmail.com", "John", "Bond", "Male", "p2020");
    fakePerson = new Person("p2020","coolPerson","John","Bond","Male","Bob","Jane","baby");

    userDao.insert(fakeUser);
    personDao.insert(fakePerson);

    db.closeConnection(true);
  }

  @AfterEach
  void tearDown() {
    ClearService clear = new ClearService();

    clear.ClearService();
  }

  @Test
  void login() throws AccessException {

    LoginService hack = new LoginService();
    LoginResponse response = new LoginResponse();

    response = hack.login(info);

    assertNotNull(response);
    assertNull(response.getMessage1());
    assertTrue(response.getSuccess1());

    assertNotNull(response.getAuthToken()); //Since it is created, i dnt have a way to compare it to anything but I can make sure it is there adn it was created
    assertEquals(info.getUserName(), response.getUserName1());
    assertEquals(fakePerson.getPersonID(), response.getPersonID1());
  }

  @Test
  void loginFail() throws AccessException {

    LoginService hack = new LoginService();
    LoginResponse response = new LoginResponse();

    response = hack.login(info2);

    assertNotNull(response);
    assertNotNull(response.getMessage1());
    assertFalse(response.getSuccess1());

    assertNull(response.getAuthToken());
    assertEquals(null, response.getUserName1());
    assertEquals(null, response.getPersonID1());
  }
}