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
    userService.registerUser(req.full_name(), req.username(), req.password(), req.position(), req.role());
    ctx.status(HttpStatus.CREATED);
  }

}
