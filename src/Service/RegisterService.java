package Service;

import DataAccess.*;
import Models.AuthToken;
import Models.User;
import Requests.RegisterRequest;
import Response.FillResponse;
import Response.RegisterResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
  * Register Service
  */

public class RegisterService {

  Database db=new Database();

  /**
   * Register Service
   * Constructor
   */
  public RegisterService() {}
  /**
   * register
   * Creates a new user account, generates 4 generations of ancestor data for the new
   * user, logs the user in, and returns an auth token.
   *
   * @param req
   * @return RegisterResponse
   */
  public RegisterResponse register(RegisterRequest req) throws AccessException {

    try {
      String createPersonID="";
      AuthToken uniqueToken;
      int numGenerations=4;

      Connection connection=db.openConnection();
      User newUser=new User();
      User userCheck;
      UserDAO userDao=new UserDAO(connection);
      AuthTokenDAO tokenDao = new AuthTokenDAO(connection);
      FillService autoFill=new FillService(connection);
      FillResponse fillResponse=new FillResponse();

      userCheck = userDao.find(req.getUserName());
      if(userCheck != null){
        db.closeConnection(false);
        return new RegisterResponse("Error in register service, username already exists");
      }

      createPersonID=UUID.randomUUID().toString();
      uniqueToken = tokenDao.insert(req.getUserName()); //before was createPersonID instead of req.getUsername()

      newUser.setUserName(req.getUserName());
      newUser.setPassword(req.getPassword());
      newUser.setEmail(req.getEmail());
      newUser.setFirstName(req.getFirstName());
      newUser.setLastName(req.getLastName());
      newUser.setGender(req.getGender());
      newUser.setPersonID(createPersonID);

      try {
        userDao.insert(newUser);
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (AccessException e) {
        return new RegisterResponse("error inserting user from register service");
      }

      fillResponse = autoFill.fill(req.getUserName(), numGenerations);

      db.closeConnection(true);

      return new RegisterResponse(uniqueToken.getToken(), req.getUserName(), newUser.getPersonID());
    }
    catch (AccessException | SQLException e){
      db.closeConnection(false);
      return new RegisterResponse("Error in register response");
    }
  }
}
