package Service;

import DataAccess.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Resources.NameData;
import Resources.OneLocation;
import Response.FillResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.sql.Connection;
import java.util.Random;
import java.sql.SQLException;
import java.util.*;

/**
 * Fill Service
 */
public class FillService {

  private Connection connection;

  /**
   * Fill Service
   * Constructor
   */

  public FillService(Connection connection){
    this.connection = connection;
  }

  /**
   * Fill
   * Populates the server's database with generated data for the specified user name.
   * The required "username" parameter must be a user already registered with the server. If there is
   * any data in the database already associated with the given user name, it is deleted. The
   * optional generations parameter lets the caller specify the number of generations of ancestors
   * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
   * persons each with associated events)
   * @param username
   * @param numGenerations - number of generations
   * @return Fill Response
   */

  public FillResponse fill(String username, int numGenerations) throws AccessException {

    int gens = numGenerations;

    if(gens == -1){gens = 4;}

    if(gens < 0 && gens != -1){

      return  new FillResponse("Error in fill service");
    }

    UserDAO userDao = new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);
    EventDAO eventDao = new EventDAO(connection);
    Person self;

    HashMap<String, Person> newSoul;

    try{
      User user = userDao.find(username);
      if(user == null){
        return new FillResponse("Error in fill service");
      }

      personDao.removeUser(username);
      eventDao.removeUser(username);

      self = createNewPerson(username);

      int year = 1993;

      newSoul =new HashMap<>();
      newSoul = createGenerations(self, 0, numGenerations, newSoul);

      Set<String> ids = newSoul.keySet();

      HashSet<Event> newEvent =new HashSet<>();


      ArrayList<Person> PersonArray = new ArrayList<>();

      newEvent = createEvents(newSoul, year, self.getPersonID(), newEvent);

      for(String id: ids){
        Person temp = newSoul.get(id);
        PersonArray.add(temp);
      }

      matchCoupleMarriage(ids, newSoul, newEvent);

      HashSet<Event> currentFamEvents = new HashSet<>();

      for(Person person: newSoul.values()){
        if(person.getAssociatedUsername() == self.getAssociatedUsername()) {
          for(Event event: newEvent)
            if(event.getAssociatedUsername() == self.getAssociatedUsername()) {
              currentFamEvents.add(event);
            }
        }
        personDao.remove(person.getPersonID());
      }

      for(Event event: currentFamEvents){
        eventDao.remove(event.getEventID());
      }

      Person[] personArray = PersonArray.toArray(new Person[PersonArray.size()]);
      Event[] eventArray = newEvent.toArray(new Event[newEvent.size()]);
      User [] userArray = new User[0];

      LoadService loadIt = new LoadService(connection);
      LoadRequest loadReq = new LoadRequest(personArray, userArray, eventArray);
      loadIt.load(loadReq, false);

      return new FillResponse(PersonArray.size(), newEvent.size());

    } catch (DataAccess.AccessException | SQLException e) {
      return new FillResponse("Error in fill service");
    }
  }

  private Person createNewPerson(String username) throws AccessException {
    User user;
    Person person = new Person();
    UserDAO userDao = new UserDAO(connection);
    PersonDAO personDao = new PersonDAO(connection);

    user = userDao.find(username);

    person.setAssociatedUsername(username);
    person.setFirstName(user.getFirstName());
    person.setLastName(user.getLastName());
    person.setGender(user.getGender());
    person.setPersonID(user.getPersonID());

    personDao.insert(person);

    return person;
  }

  private void matchCoupleMarriage(Set<String> ids, HashMap<String, Person> newSoul, HashSet<Event> newEvent) {

    Event husbandMarriage = new Event();
    HashSet<Event> husbandEvents = new HashSet<>();
    HashSet<Event> wifeEvents = new HashSet<>();

    for(String id: ids){
      Person temp = newSoul.get(id);
      if(temp.getGender().equals("m")){
        for(Event event: newEvent){
          if(event.getPersonID() == temp.getPersonID()){
            husbandEvents.add(event);
          }
        }
        for(Event event: husbandEvents){
          if(event.getEventType().equals("marriage")){
            husbandMarriage = event;
          }
        }
        for(Event event: newEvent){
          if(event.getPersonID() == temp.getSpouseID()){
            wifeEvents.add(event);
          }
        }
        for(Event searchEvent: newEvent) {
          for (Event event : wifeEvents) {
            if(searchEvent == event) {
              if (event.getEventType().equals("marriage")) {
                searchEvent.setYear(husbandMarriage.getYear());
                searchEvent.setCountry(husbandMarriage.getCountry());
                searchEvent.setCity(husbandMarriage.getCity());
                searchEvent.setLongitude(husbandMarriage.getLongitude());
                searchEvent.setLatitude(husbandMarriage.getLatitude());
              }
            }
          }
        }
      }
      husbandEvents.clear();
      wifeEvents.clear();
    }
  }

  private HashSet<Event> createEvents(HashMap<String, Person> newSouls, int year, String personID, HashSet<Event> newEvent) {

    Person currentPerson = newSouls.get(personID);

    try {
      Event birth = createBirthday(year, currentPerson);
      Event death = createDeath(birth, currentPerson);
      Event baptism = createBaptism(year, currentPerson);
      Event graduation = createGraduation(year, currentPerson);
      Event marriage = createMarriage(year, currentPerson);

      newEvent.add(birth);
      newEvent.add(baptism);
      newEvent.add(graduation);
      if(currentPerson.getSpouseID() != null) {
        newEvent.add(marriage);
      }
      if(death.getYear() != null && death.getYear() < 2020){
        newEvent.add(death);
      }

      if(currentPerson.getMotherID() != null){
        Person mom = newSouls.get(currentPerson.getMotherID());
        int nextYear = year - 25;
        newEvent = createEvents(newSouls, nextYear, mom.getPersonID(), newEvent);
      }

      if(currentPerson.getFatherID() != null){
        Person dad = newSouls.get(currentPerson.getFatherID());
        int nextYear = year - 25;
        newEvent = createEvents(newSouls, nextYear, dad.getPersonID(), newEvent);
      }


    } catch (Exception e) {
      e.printStackTrace();
    }
    return newEvent;
  }

  private HashMap<String, Person> createGenerations(Person soul, int gen, int max, HashMap<String, Person> current) throws SQLException, AccessException {

    if(gen < max){
      Person dad = RandomPerson(soul.getAssociatedUsername(),  soul.getLastName(),  "m");
      Person mom = RandomPerson(soul.getAssociatedUsername(),  dad.getLastName(), "f");

      dad.setSpouseID(mom.getPersonID());
      mom.setSpouseID(dad.getPersonID());
      soul.setFatherID(dad.getPersonID());
      soul.setMotherID(mom.getPersonID());

      current.put(soul.getPersonID(), soul);

      int nextGeneration = gen + 1;

      current = createGenerations(dad, nextGeneration, max, current);
      current = createGenerations(mom, nextGeneration, max, current);

      return current;
    }

    current.put(soul.getPersonID(), soul);
    return current;
  }

  private Person RandomPerson(String username,  String lastName, String gender) throws SQLException, AccessException {

    Person newPerson = new Person();
    String newPersonId = "";
    String name = "";

    newPersonId = UUID.randomUUID().toString();

    if(gender.equals("m")){
      name = getRandomName("dad");
    }
    else {
      name = getRandomName("mom");
    }

    newPerson.setPersonID(newPersonId);
    newPerson.setAssociatedUsername(username);
    newPerson.setFirstName(name);
    newPerson.setLastName(lastName);
    newPerson.setGender(gender);

    return newPerson;
  }

  private Event createBaptism(int year, Person person) {

    Event newEvent = new Event();
    OneLocation location;
    int r = year + 8;

    newEvent.setEventID(UUID.randomUUID().toString());
    newEvent.setAssociatedUsername(person.getAssociatedUsername());
    newEvent.setPersonID(person.getPersonID());
    newEvent.setEventType("baptism");
    newEvent.setYear(r);

    location = getRandomLocation();

    newEvent.setCountry(location.getCountry());
    newEvent.setCity(location.getCity());
    newEvent.setLongitude(location.getLong());
    newEvent.setLatitude(location.getLat());

    return newEvent;
  }

  private Event createGraduation(int year, Person person) {

    Event newEvent = new Event();
    OneLocation location;
    int r = year + 18;

    newEvent.setEventID(UUID.randomUUID().toString());
    newEvent.setAssociatedUsername(person.getAssociatedUsername());
    newEvent.setPersonID(person.getPersonID());
    newEvent.setEventType("graduation");
    newEvent.setYear(r);

    location = getRandomLocation();

    newEvent.setCountry(location.getCountry());
    newEvent.setCity(location.getCity());
    newEvent.setLongitude(location.getLong());
    newEvent.setLatitude(location.getLat());

    return newEvent;
  }
  private Event createMarriage(int year, Person person) {

    Event newEvent = new Event();
    OneLocation location;
    int r = year + 27;


    newEvent.setEventID(UUID.randomUUID().toString());
    newEvent.setAssociatedUsername(person.getAssociatedUsername());
    newEvent.setPersonID(person.getPersonID());
    newEvent.setEventType("marriage");
    newEvent.setYear(r);

    location = getRandomLocation();

    newEvent.setCountry(location.getCountry());
    newEvent.setCity(location.getCity());
    newEvent.setLongitude(location.getLong());
    newEvent.setLatitude(location.getLat());

    return newEvent;

  }

  private Event createBirthday(int year, Person person) throws FileNotFoundException {

    Event newEvent = new Event();
    OneLocation location;

    newEvent.setEventID(UUID.randomUUID().toString());
    newEvent.setAssociatedUsername(person.getAssociatedUsername());
    newEvent.setPersonID(person.getPersonID());
    newEvent.setEventType("birth");
    newEvent.setYear(year);

    location = getRandomLocation();

    newEvent.setCountry(location.getCountry());
    newEvent.setCity(location.getCity());
    newEvent.setLongitude(location.getLong());
    newEvent.setLatitude(location.getLat());

    return newEvent;
  }

  private Event createDeath(Event birth, Person person) throws FileNotFoundException {
    Event newEvent = new Event();
    OneLocation location;
    int r = 35;
    int death = birth.getYear() + r;

    newEvent.setEventID(UUID.randomUUID().toString());
    newEvent.setAssociatedUsername(person.getAssociatedUsername());
    newEvent.setPersonID(person.getPersonID());
    newEvent.setEventType("death");
    if(death < 2020) {
      newEvent.setYear(death);
    }

    location = getRandomLocation();

    newEvent.setCountry(location.getCountry());
    newEvent.setCity(location.getCity());
    newEvent.setLongitude(location.getLong());
    newEvent.setLatitude(location.getLat());

    return newEvent;
  }

  private OneLocation getRandomLocation() {
    Reader allLocations;
    Random generate = new Random();
    int r = generate.nextInt(143);
    OneLocation location[];
    OneLocation singleLocation = null;

    try {
      allLocations = new FileReader("TextFiles/locations.json");
      Gson gson = new Gson();
      JsonObject jsonObject = gson.fromJson(allLocations, JsonObject.class);
      location = gson.fromJson(jsonObject.get("data"), OneLocation[].class);
      singleLocation = location[r];

    }catch (FileNotFoundException e){
      System.out.println("Error while opening locations file");
    }
    return singleLocation;
  }

  private String getRandomName(String parent) {

    Scanner scan = null;
    StringBuilder allNames = new StringBuilder();
    Random generate = new Random();
    int r = generate.nextInt(143) ;
    NameData names;
    String firstName;

    try {
      if (parent.equals("dad")) {
        scan=new Scanner(new File("TextFiles/mnames.json"));
      }
      else {
        scan=new Scanner(new File("TextFiles/fnames.json"));
      }
    }catch (FileNotFoundException e){
      System.out.println("Error while opening names file");
    }

    while(scan.hasNext()){

      String current = scan.next();
      allNames.append(current);
    }
    names = (new Gson()).fromJson(allNames.toString(), NameData.class);

    firstName = names.getNames().get(r);

    return firstName;
  }
}

