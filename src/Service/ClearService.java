package Service;

import DataAccess.AccessException;
import DataAccess.Database;
import Response.ClearResponse;

/**
 * Clear Service
 */
public class ClearService {

  /**
   * Clear Service
   * Constructor
   */
  public ClearResponse ClearService(){
    Database db = new Database();
    try{
      db.openConnection();
      db.clearTables();
      db.closeConnection(true);
    }catch (AccessException e){
      return new ClearResponse("Error when clearing tables from clear service");
    }
    return new ClearResponse();
  }
}
