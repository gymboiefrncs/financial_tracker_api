package org.financial_tracker.routes;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

import org.financial_tracker.features.auth.AuthRoutes;
import org.financial_tracker.features.user.UserRoutes;

public class AppRoutes implements EndpointGroup {
  private final UserRoutes userRoutes;
  private final AuthRoutes authRoutes;

  public AppRoutes(UserRoutes userRoutes, AuthRoutes authRoutes) {
    this.userRoutes = userRoutes;
    this.authRoutes = authRoutes;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/api", () -> {
      userRoutes.addEndpoints();
      authRoutes.addEndpoints();
    });
  }
}