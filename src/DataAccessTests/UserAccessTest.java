package DataAccessTests;

import DataAccess.AccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserAccessTest {

  private Database db;
  private User fakeUser;
  private User fakeUser2;
  private User fakeUser3;
  private User fakeUser4;

  @BeforeEach
  public void setup() throws AccessException {
    db = new Database();
    fakeUser = new User("jDug", "dugdug1", "jdug@gmail.com", "John", "Duglas", "Male", "jdug123");
    fakeUser2 = new User("iRie", "lllll", "irie@gmail.com", "Iris", "english", "Female", "jrie456");
    fakeUser3 = new User("skywalker", "lsky10", "lskyg@gmail.com", "Luke", "sky", "Male", "luke789");
    fakeUser4 = new User("jDug", "doggie1", "jdugggie5@gmail.com", "Jeff", "lasm", "Male", "jdoggie45");
  }
  @AfterEach
  public void tearDown() throws AccessException {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void testInsert() throws AccessException, SQLException {
    User compare = null;

    Connection connection = db.getConnection();
    UserDAO accessDao = new UserDAO(connection);
    accessDao.insert(fakeUser);
    compare = accessDao.find(fakeUser.getUserName());
    db.closeConnection(true);

    assertNotNull(compare);
    assertEquals(fakeUser.getUserName(), compare.getUserName());
  }

  @Test
  public void insertFail() throws Exception{
    boolean itWorked = true;

    Connection connection = db.openConnection();
    UserDAO UserDao = new UserDAO(connection);
    try{
      UserDao.insert(fakeUser);

      UserDao.insert(fakeUser4);

    } catch (AccessException e){
      itWorked = false;
    }

    assertFalse(itWorked);

    User compare = null;

    compare = UserDao.find(fakeUser.getUserName());
    db.closeConnection(true);

    assertNotEquals(compare, fakeUser4);
  }

  @Test
  void testFind() throws AccessException, SQLException {
    User compare = null;

      Connection connection = db.openConnection();
      UserDAO accessDao = new UserDAO(connection);
      accessDao.insert(fakeUser);
      accessDao.insert(fakeUser2);
      accessDao.insert(fakeUser3);
      compare = accessDao.find(fakeUser3.getUserName());
      db.closeConnection(true);

    assertNotNull(compare);
    assertEquals(fakeUser3.getUserName(), compare.getUserName());
  }

  @Test
  public void findFail() throws Exception{

    Connection connection = db.openConnection();
    UserDAO UserDao = new UserDAO(connection);

    UserDao.insert(fakeUser);

    User compare = null;

    compare = UserDao.find(fakeUser3.getUserName());
    db.closeConnection(true);

    assertNull(compare);
  }
  @Test
  void testRemove() throws AccessException, SQLException {
    User compare = null;

      Connection connection = db.openConnection();
      UserDAO accessDao = new UserDAO(connection);
      accessDao.insert(fakeUser);
      compare = accessDao.find(fakeUser.getUserName());
      System.out.println(compare);
      accessDao.remove(fakeUser.getUserName()); //getPersonID
      compare = accessDao.find(fakeUser.getUserName());
      System.out.println(compare);
      db.closeConnection(true);

    assertNull(compare);
    assertEquals(null, compare);
  }

  @Test
  void testClear() throws AccessException, SQLException {

    User compare = null;

      Connection connection = db.openConnection();
      UserDAO UserDao = new UserDAO(connection);

      UserDao.insert(fakeUser);
      UserDao.clear();
      compare = UserDao.find(fakeUser.getUserName());
      db.closeConnection(true);

    assertNull(compare);

  }
}