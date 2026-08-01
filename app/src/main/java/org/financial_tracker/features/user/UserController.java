package org.financial_tracker.features.user;

import java.util.Map;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
import org.financial_tracker.features.user.DTO.LoginUserRequest;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void createUser(Context ctx) {
    CreateUserRequest req = ctx.bodyAsClass(CreateUserRequest.class);
    User user = userService.registerUser(req);
    ctx.status(HttpStatus.CREATED).json(user);
  }

  public void loginUser(Context ctx) {
    LoginUserRequest req = ctx.bodyAsClass(LoginUserRequest.class);
    boolean res = userService.login(req);

    ctx.status(res ? HttpStatus.ACCEPTED : HttpStatus.UNAUTHORIZED)
        .json(Map.of("message", res ? "Login successful" : "Invalid Credentials"));
  }
}
