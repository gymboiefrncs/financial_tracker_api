package org.financial_tracker.features.user;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
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

  @OpenApi(path = "/admin/users/signup", methods = HttpMethod.POST, summary = "Create user", requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = CreateUserRequest.class)), responses = {
      @OpenApiResponse(status = "201", content = @OpenApiContent(from = CreateUserResponse.class))
  })
  public void createUser(Context ctx) {
    CreateUserRequest req = ctx.bodyAsClass(CreateUserRequest.class);
    CreateUserResponse user = userService.registerUser(req);
    ctx.status(HttpStatus.CREATED).json(user);
  }
}
