package org.financial_tracker.features.auth;

import java.util.Map;
import java.util.Optional;

import org.financial_tracker.features.auth.DTO.LoginRequest;
import org.financial_tracker.features.auth.DTO.LoginResponse;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;

public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @OpenApi(path = "/users/login", methods = HttpMethod.POST, summary = "Login user", requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = LoginRequest.class)), responses = {
      @OpenApiResponse(status = "200", content = @OpenApiContent(from = LoginResponse.class)),
      @OpenApiResponse(status = "401", description = "Invalid credentials")
  })

  public void loginUser(Context ctx) {
    LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
    Optional<LoginResponse> res = authService.login(req);

    if (res.isEmpty()) {
      ctx.status(HttpStatus.UNAUTHORIZED)
          .json(Map.of("message", "Invalid Credentials"));
      return;
    }

    ctx.cookie(
        "session_id",
        res.get().sessionId().toString(),
        60 * 60 * 24 * 7);

    ctx.status(HttpStatus.OK)
        .json(res.get());
  }
}
