package org.financial_tracker.features.user;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

public class UserRoutes implements EndpointGroup {
  private final UserController userController;

  public UserRoutes(UserController userController) {
    this.userController = userController;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/users", () -> {
      ApiBuilder.post("/signup", userController::createUser);
    });
  }
}