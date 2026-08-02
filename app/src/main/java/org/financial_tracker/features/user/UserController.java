package org.financial_tracker.features.user;

import java.util.Map;
import java.util.Optional;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
import org.financial_tracker.features.user.DTO.LoginRequest;
import org.financial_tracker.features.user.DTO.LoginResponse;

import org.financial_tracker.features.user.DTO.CreateUserResponse;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void createUser(Context ctx) {
    CreateUserRequest req = ctx.bodyAsClass(CreateUserRequest.class);
    CreateUserResponse user = userService.registerUser(req);
    ctx.status(HttpStatus.CREATED).json(user);
  }

  public void loginUser(Context ctx) {
    LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
    Optional<LoginResponse> res = userService.login(req);

    if (res.isEmpty()) {
      ctx.status(HttpStatus.UNAUTHORIZED)
          .json(Map.of("message", "Invalid Credentials"));
      return;
    }

    ctx.status(HttpStatus.OK)
        .json(res.get());
  }
}
