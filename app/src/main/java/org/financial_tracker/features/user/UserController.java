package org.financial_tracker.features.user;

import java.util.Map;
import java.util.Optional;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
import org.financial_tracker.features.user.DTO.LoginRequest;
import org.financial_tracker.features.user.DTO.LoginResponse;

import org.financial_tracker.features.user.DTO.CreateUserResponse;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @OpenApi(path = "/users/signup", methods = HttpMethod.POST, summary = "Create user", requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = CreateUserRequest.class)), responses = {
      @OpenApiResponse(status = "201", content = @OpenApiContent(from = CreateUserResponse.class))
  })
  public void createUser(Context ctx) {
    CreateUserRequest req = ctx.bodyAsClass(CreateUserRequest.class);
    CreateUserResponse user = userService.registerUser(req);
    ctx.status(HttpStatus.CREATED).json(user);
  }

  @OpenApi(path = "/users/login", methods = HttpMethod.POST, summary = "Login user", requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = LoginRequest.class)), responses = {
      @OpenApiResponse(status = "200", content = @OpenApiContent(from = LoginResponse.class)),
      @OpenApiResponse(status = "401", description = "Invalid credentials")
  })

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
