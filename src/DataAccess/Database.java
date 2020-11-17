package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

  private Connection connect;


  public Connection openConnection() throws AccessException {
    try {
      final String CONNECTION_URL = "jdbc:sqlite:familydb.sqlite";

      connect = DriverManager.getConnection(CONNECTION_URL);

      connect.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new AccessException("Unable to open connection to database");
    }

    return connect;
  }

  public Connection getConnection() throws AccessException {
    if(connect == null) {
      return openConnection();
    } else {
      return connect;
    }
  }

  public void closeConnection(boolean commit) throws AccessException {
    try {
      if(connect == null) {
        System.out.println("the connection is null");
      }
      if (commit) {
        connect.commit();
      } else {
        connect.rollback();
      }

      connect.close();
      connect = null;
    } catch (SQLException e) {
      throw new AccessException("close Connection failed", e);
    }
  }

  public void clearTables() throws AccessException {
    String sql = "DELETE FROM Persons; DELETE FROM Users; DELETE FROM Events; DELETE FROM AuthorizationTokens";

    try {
      Statement stmt = connect.createStatement();
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new AccessException("SQL Error encountered while clearing tables");
    }
  }
}
