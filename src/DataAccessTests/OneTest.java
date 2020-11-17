package DataAccessTests;

import DataAccess.AccessException;
import DataAccess.AuthTokenDAO;
import DataAccess.Database;
import Models.AuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class OneTest {

  private Database db;
  private AuthToken fakeToken;

  @BeforeEach
  public void setup() throws AccessException {
    db = new Database();
    fakeToken= new AuthToken("abcdefg","p2020");
  }

  @Test
  public void insert() throws AccessException, SQLException {

    AuthToken token = null;
    Connection connection = db.getConnection();
    AuthTokenDAO tokenAccess = new AuthTokenDAO(connection);

    tokenAccess.insert(fakeToken.getUserId());

    db.closeConnection(true);

  }
}
