package org.financial_tracker.routes;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

import org.financial_tracker.features.academicyear.AcademicYearRoutes;
import org.financial_tracker.features.auth.AuthRoutes;
import org.financial_tracker.features.profile.ProfileRoutes;
import org.financial_tracker.features.user.UserRoutes;

public class AppRoutes implements EndpointGroup {
  private final UserRoutes userRoutes;
  private final AuthRoutes authRoutes;
  private final ProfileRoutes profileRoutes;
  private final AcademicYearRoutes academicYearRoutes;

  public AppRoutes(UserRoutes userRoutes, AuthRoutes authRoutes, ProfileRoutes profileRoutes,
      AcademicYearRoutes academicYearRoutes) {
    this.userRoutes = userRoutes;
    this.authRoutes = authRoutes;
    this.profileRoutes = profileRoutes;
    this.academicYearRoutes = academicYearRoutes;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/api", () -> {
      userRoutes.addEndpoints();
      authRoutes.addEndpoints();
      profileRoutes.addEndpoints();
      academicYearRoutes.addEndpoints();
    });
  }
}