package org.financial_tracker.features.profile;

import org.financial_tracker.features.user.User;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;

public class ProfileController {
  private final ProfileService profileService;

  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @OpenApi(path = "/profile/me", methods = HttpMethod.GET, summary = "Get profile", responses = {
      @OpenApiResponse(status = "200", content = @OpenApiContent(from = Profile.class))
  })
  public void getMe(Context ctx) {
    User user = ctx.attribute("user");

    Profile profile = profileService.getProfile(user.id());
    ctx.json(profile);
  }
}
