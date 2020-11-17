package Requests;

import Models.Person;
import Models.User;
import Models.Event;

import java.util.Arrays;
import java.util.List;

/**
 * load Request
 * sends load request to the server
 */
public class LoadRequest {

  private List<User> users;
  private List<Person> persons;
  private List<Event> events;

  /**
   * Empty Load Request Constructor
   * created new family tree request with events for new user
   * @param personArray
   * @param usersArray
   * @param eventArray
   */
  public LoadRequest(Person[] personArray, User[] usersArray, Event[] eventArray) {
    persons = Arrays.asList(personArray);
    users = Arrays.asList(usersArray);
    events = Arrays.asList(eventArray);
  }

  public List<User> getUsers() { return users; }

  public List<Person> getPersons() {
    return persons;
  }

  public List<Event> getEvents() {
    return events;
  }
}
