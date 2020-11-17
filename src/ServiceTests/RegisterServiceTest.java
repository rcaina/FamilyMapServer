package ServiceTests;

import DataAccess.AccessException;
import Requests.RegisterRequest;
import Response.RegisterResponse;
import Service.ClearService;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

  RegisterRequest request1 = new RegisterRequest("asianJason", "pass", "jason@gmail.com", "jason", "curtin", "m");
  RegisterRequest requestFail = new RegisterRequest();
  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
    ClearService clear = new ClearService();

    clear.ClearService();
  }

  @Test
  void register() throws AccessException {
    RegisterService createAccount = new RegisterService();
    RegisterResponse response = new RegisterResponse();
    ClearService clear = new ClearService();

    response = createAccount.register(request1);

    assertNotNull(response);
    assertTrue(response.getSuccess());
    assertEquals(response.getUserName(), request1.getUserName());
  }

  @Test
  void registerFail() throws AccessException {
    RegisterService createAccount = new RegisterService();
    RegisterResponse response = new RegisterResponse();
    ClearService clear = new ClearService();

    response = createAccount.register(requestFail);

    assertNotNull(response);
    assertFalse(response.getSuccess());
    assertNull(response.getUserName());
    assertEquals(response.getMessage(), "Error in register response");
  }
}