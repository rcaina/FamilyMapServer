package DataAccessTests;

import DataAccess.AccessException;
import DataAccess.AuthTokenDAO;
import DataAccess.Database;
import Models.AuthToken;
import Resources.Services;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class authTokenAccessTest {

  private Database db;
  private AuthToken fakeToken;
  private AuthToken fakeToken2;
  private AuthToken fakeToken3;
  private AuthToken fakeToken4;

  @BeforeEach
  public void setup() throws AccessException {
    db = new Database();
    fakeToken= new AuthToken("abcdefg","p2020");
    fakeToken2 = new AuthToken("hijklmnop","yvgg");
    fakeToken3 = new AuthToken("qrstuvwxyz","0ngtd");
    fakeToken4 = new AuthToken("abcdggg","p2020");
  }

  @AfterEach
  public void tearDown() throws AccessException {
    db.clearTables();;
    db.closeConnection(true);
    db = null;
  }

  @Test
  public void testInsert() throws AccessException, SQLException {
    AuthToken compare = null;
    AuthToken newToken = null;

    Connection connection = db.getConnection();
    AuthTokenDAO accessDao = new AuthTokenDAO(connection);
    newToken = accessDao.insert(fakeToken.getUserId());

    compare = accessDao.find(newToken.getToken());
    System.out.println(newToken.getToken());

    assertNotNull(compare);
    assertEquals(fakeToken.getUserId(), compare.getUserId());
  }

  @Test
  public void insertFail() throws Exception{
    boolean itWorked = true;
    Connection connection = db.getConnection();
    AuthTokenDAO AuthorizationTokensDao = new AuthTokenDAO(connection);

    try{

      AuthorizationTokensDao.insert(fakeToken.getUserId());

      AuthorizationTokensDao.insert(null);

    } catch (AccessException e){
      itWorked = false;
    }

    assertFalse(itWorked);
  }

  @Test
  void testFind() throws AccessException, SQLException {
    AuthToken compare = null;
    AuthToken user3 = null;

    Connection connection = db.getConnection();
    AuthTokenDAO accessDao = new AuthTokenDAO(connection);
    user3 = accessDao.insert(fakeToken3.getUserId());
    compare = accessDao.find(user3.getToken());

    assertNotNull(compare);
    assertEquals(fakeToken3.getUserId(), compare.getUserId());
  }

  @Test
  public void findFail() throws Exception{
    boolean itWorked = true;

    try{
      Connection connection = db.getConnection();
      AuthTokenDAO AuthorizationTokensDao = new AuthTokenDAO(connection);

      AuthorizationTokensDao.insert(fakeToken.getUserId());
      AuthorizationTokensDao.insert(fakeToken2.getUserId());

    } catch (AccessException e){
      itWorked = false;
    }

    assertTrue(itWorked);

    AuthToken compare;

    Connection connection = db.getConnection();
    AuthTokenDAO AuthorizationTokensDao = new AuthTokenDAO(connection);

    compare = AuthorizationTokensDao.find(fakeToken3.getUserId());

    assertNull(compare);
  }
  @Test
  void testRemove() throws AccessException, SQLException {
    AuthToken compare = null;
    AuthToken user1 = null;

    Connection connection = db.openConnection();
    AuthTokenDAO accessDao = new AuthTokenDAO(connection);
    user1 = accessDao.insert(fakeToken.getUserId());
    compare = accessDao.find(user1.getToken());

    assertEquals(user1.getUserId(), compare.getUserId());

    accessDao.remove(user1.getToken());

    compare = accessDao.find(fakeToken.getToken());


    assertNull(compare);
    assertEquals(null, compare);
  }
  @Test
  void testRemoveFail() throws AccessException, SQLException {
    AuthToken compare = null;
    AuthToken user1 = null;

    Connection connection = db.openConnection();
    AuthTokenDAO accessDao = new AuthTokenDAO(connection);
    user1 = accessDao.insert(fakeToken.getUserId());
    compare = accessDao.find(user1.getToken());
    accessDao.remove("1234abdc");

    assertNotNull(compare);
    assertEquals(user1.getUserId(), compare.getUserId());
  }

  @Test
  void testClear() throws AccessException, SQLException {

    AuthToken compare = null;

      Connection connection = db.openConnection();
      AuthTokenDAO AuthorizationTokensDao = new AuthTokenDAO(connection);

      AuthorizationTokensDao.insert(fakeToken.getUserId());
      AuthorizationTokensDao.clear();
      compare = AuthorizationTokensDao.find(fakeToken.getUserId());

    assertNull(compare);

  }
}