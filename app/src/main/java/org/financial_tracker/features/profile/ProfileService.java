package org.financial_tracker.features.profile;

import java.util.UUID;

import io.javalin.http.NotFoundResponse;

public class ProfileService {
  private final ProfileRepository profileRepository;

  public ProfileService(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  public Profile getProfile(UUID id) {
    return profileRepository.findMe(id).orElseThrow(() -> new NotFoundResponse("Profile not found"));
  }
}
