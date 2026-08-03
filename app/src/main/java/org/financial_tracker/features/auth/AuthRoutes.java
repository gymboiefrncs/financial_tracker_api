package org.financial_tracker.features.auth;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

public class AuthRoutes implements EndpointGroup {
  private final AuthController authController;

  public AuthRoutes(AuthController authController) {
    this.authController = authController;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/auth", () -> {
      ApiBuilder.post("/login", authController::loginUser);
    });
  }
}