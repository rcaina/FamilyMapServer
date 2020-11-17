package DataAccess;

import Models.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Events Data Access
 * adds or removes events from database as well as finds a specific event
 */
public class EventDAO {

  private Connection connection;

  public EventDAO(Connection connection) {
    this.connection=connection;
  }

  /**
   * insert
   * adds an event to the database
   *
   * @param addEvent - the event to be added to the database
   * @return true if the event was added, false otherwise
   */
  public void insert(Event addEvent) throws AccessException {

    String sqlString="INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longtitude, Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
    PreparedStatement statement;

    try {
      statement=connection.prepareStatement(sqlString);
      statement.setString(1, addEvent.getEventID());
      statement.setString(2, addEvent.getAssociatedUsername());
      statement.setString(3, addEvent.getPersonID());
      statement.setFloat(4, addEvent.getLatitude());
      statement.setFloat(5, addEvent.getLongitude());
      statement.setString(6, addEvent.getCountry());
      statement.setString(7, addEvent.getCity());
      statement.setString(8, addEvent.getEventType());
      statement.setInt(9, addEvent.getYear());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new AccessException("Error while inserting Event");
    }
  }

  /**
   * find
   * find a specific event that matches the ID
   *
   * @param eventId - the Id of the event to be found
   * @return the event if found in the database, otherwise it returns null
   */
  public Event find(String eventId) throws AccessException {

    String sqlString="SELECT * FROM Events WHERE EventID = ?;";
    ResultSet rs=null;
    Event ru=null;
    PreparedStatement statement;

    try {
      statement=connection.prepareStatement(sqlString);
      statement.setString(1, eventId);
      rs=statement.executeQuery();
      if (rs.next()) {
        ru=new Event(
                rs.getString("EventID"),
                rs.getString("AssociatedUsername"),
                rs.getString("PersonID"),
                rs.getFloat("Latitude"),
                rs.getFloat("Longtitude"),
                rs.getString("Country"),
                rs.getString("City"),
                rs.getString("EventType"),
                rs.getInt("Year"));
        return ru;
      }
    } catch (SQLException e) {
      throw new AccessException("Error while finding Event");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  /**
   * remove
   * removes an event from the database
   *
   * @param rmEvent - event to be removed from the database
   * @return - true if the event was removed, otherwise returns false
   */
  public void remove(String rmEvent) throws AccessException {

    String sqlString="DELETE FROM Events WHERE EventID = ?;";
    PreparedStatement statement;

    try {
      statement=connection.prepareStatement(sqlString);
      statement.setString(1, rmEvent);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new AccessException("Error while deleting specific event");
    }
  }

  public void removeUser(String username) throws AccessException {

    String sqlString="DELETE FROM Events WHERE AssociatedUsername = ?;";
    PreparedStatement statement;

    try {
      statement=connection.prepareStatement(sqlString);
      statement.setString(1, username);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new AccessException("Error while deleting specific event");
    }
  }

  /**
   * clear
   * deletes all events from the database
   */
  public void clear() throws AccessException {

    String sqlString="DELETE FROM Events";
    PreparedStatement statement;

    try {
      statement=connection.prepareStatement(sqlString);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new AccessException("Error while clearing Events");
    }
  }

  public ArrayList<Event> getAllPersonRelatedEvents(String username) throws AccessException {

    if(username == null){
      return null;
    }
    String sqlString="SELECT * from Events where AssociatedUsername=?";
    Event event;
    ResultSet rs;
    PreparedStatement statement;
    ArrayList<Event> allEvents = new ArrayList<>();

    try {
      statement=connection.prepareStatement(sqlString);
      statement.setString(1, username);
      rs=statement.executeQuery();

      while (rs.next()) {

        event=new Event(rs.getString(1),
                rs.getString(2), rs.getString(3),
                rs.getFloat(4), rs.getFloat(5),
                rs.getString(6), rs.getString(7),
                rs.getString(8), rs.getInt(9));

        allEvents.add(event);
      }

    } catch (SQLException e) {
      throw new AccessException("Error in find all family related events");
    }

    return allEvents;
  }
}
