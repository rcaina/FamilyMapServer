package DataAccess;

import Models.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Authorization Tokens Data Access
 * adds or removes authorization tokens from database as well as finds specific Token
 */

public class AuthTokenDAO extends Database{

  private Connection connection;

  public AuthTokenDAO() {}

  public AuthTokenDAO(Connection connection) {
    this.connection = connection;
  }

  /**
   * insert
   * adds a token to the database
   * @param username - the token to be added to the database
   * @return true if the token was added and false if the token was not added
   */
  public AuthToken insert(String username) throws AccessException, SQLException {

    String token = UUID.randomUUID().toString();
    String sqlString = "INSERT INTO AuthorizationTokens(Token, UserID) VALUES(?,?)";
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1,token);
      statement.setString(2, username);
      AuthToken newToken = new AuthToken(token, username);
      statement.executeUpdate();

      return newToken;
    }catch (SQLException e){
      throw new AccessException("Error while inserting Authorization Token");
    }
    finally {
      if(statement != null){
        statement.close();
      }
    }
  }

  /**
   * find
   * finds a token from the database by the ID that is passed in
   * @param token - the token that is unique to a specific user to find that user
   * @return the authorization token that is found, null if not found
   */
  public AuthToken find(String token) throws AccessException{

    String sqlString = "SELECT * FROM AuthorizationTokens WHERE Token = ?;";
    ResultSet rs = null;
    AuthToken ru = null;
    PreparedStatement statement = null;

    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, token);
      rs = statement.executeQuery();
      if(rs.next()){
        ru = new AuthToken(
                rs.getString("Token"),
                rs.getString("UserID"));

        return ru;
      }
    }catch (SQLException e){
      throw new AccessException("Error while finding Authorization Token");
    }
      finally {
        if(rs != null){
          try {
            rs.close();
          }catch (SQLException e){
            e.printStackTrace();
          }
        }
      }
    return null;
  }

  /**
   * remove
   * removes a token from the database
   * @param rmToken - the token to be removed from the database
   * @return true if it was successfully removed, false if it was not removed
   */
  public void remove(String rmToken) throws AccessException {

    String sqlString = "DELETE FROM AuthorizationTokens WHERE Token = ?;";
    PreparedStatement statement;

    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, rmToken);
      statement.executeUpdate();

    }catch (SQLException e){
      throw new AccessException("Error while deleting specific token");
    }
  }

  /**
   * clear
   * deletes all tokens from the database
   */
  public void clear() throws AccessException{

    String sqlString = "DELETE FROM AuthorizationTokens";
    PreparedStatement statement = null;

    try{
      statement = connection.prepareStatement(sqlString);
      statement.executeUpdate();

    }catch (SQLException e){
      throw new AccessException("Error while clearing Token");
    }
  }
}
