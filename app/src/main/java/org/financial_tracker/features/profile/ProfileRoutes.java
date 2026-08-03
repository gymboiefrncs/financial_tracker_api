package org.financial_tracker.features.profile;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

public class ProfileRoutes implements EndpointGroup {
  private final ProfileController profileController;

  public ProfileRoutes(ProfileController profileController) {
    this.profileController = profileController;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/profile", () -> {
      ApiBuilder.get("/me", profileController::getMe);
    });
  }
}
