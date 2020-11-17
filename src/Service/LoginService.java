package Service;

import DataAccess.AccessException;
import DataAccess.AuthTokenDAO;
import DataAccess.Database;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Response.LoginResponse;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Login Service
 */
public class LoginService {

  Database db = new Database();
  /**
   * Login Service
   * Constructor
   */
  public LoginService(){

  }

  /**
   * Login
   * Logs in the user and returns an auth token
   * @param req
   * @return LoginResponse
   */
  public LoginResponse login(LoginRequest req) throws AccessException {

    Connection connection = db.openConnection();
    AuthTokenDAO createToken= new AuthTokenDAO(connection);
    UserDAO user = new UserDAO(connection);

    try{
      User newUser = user.find(req.getUserName());
      if(newUser == null){
        db.closeConnection(false);
        return new LoginResponse("Error in Login Service");
      }
      if(req.getUserName().matches(newUser.getUserName())) {

        if (!req.getPassword().matches(newUser.getPassword())) {
          db.closeConnection(false);
          return new LoginResponse("Error in Login Service, invalid password");
        }
      }
      else {
        db.closeConnection(false);
        return new LoginResponse("Error in Login Service, invalid username");
      }
      AuthToken uniqueToken = createToken.insert(newUser.getUserName());
      if(uniqueToken == null){
        db.closeConnection(false);
        return new LoginResponse("Error in Login Service");
      }
      db.closeConnection(true);
      return new LoginResponse(uniqueToken.getToken(), req.getUserName(), newUser.getPersonID());
    }catch (AccessException e){
      db.closeConnection(false);
      return new LoginResponse("Error in Login Service");
    } catch (SQLException e) {
      db.closeConnection(false);
      return new LoginResponse("Error in Login Service");
    }
  }
}
