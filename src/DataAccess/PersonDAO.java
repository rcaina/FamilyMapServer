package DataAccess;

import Models.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Persons Data Access
 * adds or removes Persons from database as well as finds a specific Person
 */
public class PersonDAO extends Database{

  Database db = new Database();
  private Connection connection;

  public PersonDAO(){}

  public PersonDAO(Connection connection) { this.connection = connection;}

  /**
   * insert
   * adds a Person to the database
   * @param addPerson - the Person to be added to the database
   * @return true if the Person was added, false otherwise
   */
  public void insert(Person addPerson) throws AccessException {

    String sqlString = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) VALUES (?,?,?,?,?,?,?,?)";

    try{
      PreparedStatement statement = connection.prepareStatement(sqlString);
      statement.setString(1, addPerson.getPersonID());
      statement.setString(2, addPerson.getAssociatedUsername());
      statement.setString(3, addPerson.getFirstName());
      statement.setString(4, addPerson.getLastName());
      statement.setString(5, addPerson.getGender());
      statement.setString(6, addPerson.getFatherID());
      statement.setString(7, addPerson.getMotherID());
      statement.setString(8, addPerson.getSpouseID());
      statement.executeUpdate();
    }
    catch (SQLException e){
      throw new AccessException("Error while inserting person");
    }
  }

  /**
   * find
   * find a specific Person that matches the ID
   * @param username - the Id of the Person to be found
   * @return returns the Person if found in the database, otherwise it returns null
   */
  public Person find(String username) throws AccessException {

    String sqlString =  "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
    Person person = null;
    ResultSet rs = null;
    PreparedStatement statement = null;

    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, username);
      rs = statement.executeQuery();

      if(rs.next()){ //while loop instead
        person = new Person(
                rs.getString("PersonID"),
                rs.getString("AssociatedUsername"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Gender"),
                rs.getString("FatherID"),
                rs.getString("MotherID"),
                rs.getString("SpouseID"));
        return person;
      }
    }catch (SQLException e){
      db.closeConnection(false);
      throw new AccessException("Error when finding person");
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
   * removes a Person from the database
   * @param rmPerson - Person to be removed from the database
   * @return - true if the Person was removed, otherwise returns false
   */
  public void remove(String rmPerson) throws AccessException {

    String sqlString = "DELETE FROM Persons WHERE PersonID = ?;";
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, rmPerson);
      statement.executeUpdate();
    }catch (SQLException e){
      throw new AccessException("Error while deleting specific token");
    }
  }

  public void removeUser(String username) throws AccessException {

    String sqlString = "DELETE FROM Persons WHERE AssociatedUsername = ?;";
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, username);
      statement.executeUpdate();
    }catch (SQLException e){
      throw new AccessException("Error while deleting specific token");
    }
  }

  /**
   * clear
   * deletes all Persons from the database
   */
  public void clear(){

    String sqlString = "DELETE FROM Persons";
    PreparedStatement statement = null;

    try{
      statement = connection.prepareStatement(sqlString);
      statement.executeUpdate();
    }
    catch (SQLException e){
      //
      System.out.println("Data Access Exception");
    }
  }

  public ArrayList<Person> findFamily(String username) throws AccessException {

    String sqlString =  "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
    Person person;
    ResultSet rs = null;
    PreparedStatement statement;
    ArrayList<Person> family = new ArrayList<>();

    try {
      statement = connection.prepareStatement(sqlString);
      statement.setString(1, username);
      rs = statement.executeQuery();

      while(rs.next()){
        person = new Person(
                rs.getString("PersonID"),
                rs.getString("AssociatedUsername"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Gender"),
                rs.getString("FatherID"),
                rs.getString("MotherID"),
                rs.getString("SpouseID"));

        family.add(person);
      }
    }catch (SQLException e){
      throw new AccessException("Error when finding person");
    }
    return family;
  }
}
