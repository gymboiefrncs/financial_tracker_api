package org.financial_tracker.routes;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import org.financial_tracker.features.user.UserRoutes;

public class AppRoutes implements EndpointGroup {
  private final UserRoutes userRoutes;

  public AppRoutes(UserRoutes userRoutes) {
    this.userRoutes = userRoutes;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/api", () -> {
      userRoutes.addEndpoints();
    });
  }
}