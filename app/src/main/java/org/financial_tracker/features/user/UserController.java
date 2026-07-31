package org.financial_tracker.features.user;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
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

}
