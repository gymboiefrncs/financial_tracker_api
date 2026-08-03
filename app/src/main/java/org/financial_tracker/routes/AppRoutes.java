package org.financial_tracker.routes;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

import org.financial_tracker.features.auth.AuthRoutes;
import org.financial_tracker.features.profile.ProfileRoutes;
import org.financial_tracker.features.user.UserRoutes;

public class AppRoutes implements EndpointGroup {
  private final UserRoutes userRoutes;
  private final AuthRoutes authRoutes;
  private final ProfileRoutes profileRoutes;

  public AppRoutes(UserRoutes userRoutes, AuthRoutes authRoutes, ProfileRoutes profileRoutes) {
    this.userRoutes = userRoutes;
    this.authRoutes = authRoutes;
    this.profileRoutes = profileRoutes;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/api", () -> {
      userRoutes.addEndpoints();
      authRoutes.addEndpoints();
      profileRoutes.addEndpoints();
    });
  }
}