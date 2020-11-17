package Response;

/**
 * Clear Response
 * Response to the clear request
 */
public class ClearResponse extends CommonResponse{

  /**
   * Clear Response
   * Successful clear response
   */
  public ClearResponse() {
    this.message="Clear succeeded";
    this.success=true;
  }

  /**
   * Clear Response
   * Failed clear response
   * @param message - the type of error
   */
  public ClearResponse(String message) {
    this.message=message;
    this.success=false;
  }

  public boolean getSuccess(){ return this.success;}
}
