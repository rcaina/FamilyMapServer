package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.String;

import Models.User;

/**
 * Users Data Access
 * adds or removes Users from database as well as finds a specific User
 */
public class UserDAO {

  private Connection connection;

  public UserDAO(){}

  public  UserDAO(Connection connection){
    this.connection = connection;
  }
  /**
   * insert
   * adds a user to the database
   * @param addUser - the user to be added to the database
   * @return true if the user was added, false otherwise
   */
  public void insert(User addUser) throws AccessException, SQLException {
    String sqlString = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
    PreparedStatement statement = null;

    try{
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, addUser.getUserName());
      statement.setString(2, addUser.getPassword());
      statement.setString(3, addUser.getEmail());
      statement.setString(4, addUser.getFirstName());
      statement.setString(5, addUser.getLastName());
      statement.setString(6, addUser.getGender());
      statement.setString(7, addUser.getPersonID());
      statement.executeUpdate();
    }catch (SQLException e){
      throw new AccessException("Error while inserting Users");
    }
  }

  /**
   * find
   * find a specific user that matches the ID
   * @param username - the Id of the user to be found
   * @return the user if found in the database, otherwise it returns null
   */
  public User find(String username) throws AccessException {

    String sqlString = "SELECT * FROM Users WHERE Username = ?;";
    ResultSet rs = null;
    User ru = null;
    PreparedStatement statement = null;

    try{
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, username);
      rs = statement.executeQuery();
      if (rs.next()){
        ru = new User(
                rs.getString("Username"),
                rs.getString("Password"),
                rs.getString("Email"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Gender"),
                rs.getString("PersonID"));
        return ru;
      }
    }catch (SQLException e){
      throw new AccessException("Error while finding User");
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
   * removes a user from the database
   * @param rmUser - user to be removed from the database
   * @return - true if the user was removed, otherwise returns false
   */
  public void remove(String rmUser) throws AccessException {

    String sqlString = "DELETE FROM Users WHERE Username = ?;";
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, rmUser);
      statement.executeUpdate();
    }catch (SQLException e){
      throw new AccessException("Error while deleting specific token");
    }
  }

  /**
   * clear
   * deletes all users from the database
   */
  public void clear() throws AccessException {
    String sqlString = "DELETE FROM Users";
    PreparedStatement statement = null;

    try{
      statement = connection.prepareStatement(sqlString);
      statement.executeUpdate();
    }
    catch (SQLException e){
      throw new AccessException("Error while clearing Users");
    }


  }
}
