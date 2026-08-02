package org.financial_tracker.features.user;

import java.util.Optional;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
import org.financial_tracker.features.user.DTO.LoginRequest;
import org.financial_tracker.features.user.DTO.LoginResponse;
import org.financial_tracker.features.user.DTO.UserAuthData;
import org.financial_tracker.features.user.DTO.CreateUserResponse;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public CreateUserResponse registerUser(CreateUserRequest req) {
    validate(req);
    return userRepository.save(req.fullName(), req.username(), req.password(), req.position(), req.role());
  }

  public Optional<LoginResponse> login(LoginRequest req) {
    Optional<UserAuthData> user = userRepository.findByUsername(req.username());

    if (user.isEmpty()) {
      return Optional.empty();
    }

    UserAuthData credentials = user.get();

    if (!credentials.passwordHash().equals(req.rawPassword())) {
      return Optional.empty();
    }

    return Optional.of(new LoginResponse(
        credentials.username(),
        credentials.fullName()));
  }

  private void validate(CreateUserRequest req) {
    if (req.username() == null || req.username().isBlank()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    if (req.password() == null || req.password().isBlank()) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    if (req.fullName() == null || req.fullName().isBlank()) {
      throw new IllegalArgumentException("Full name cannot be empty");
    }
    if (req.position() == null || req.position().isBlank()) {
      throw new IllegalArgumentException("Position cannot be empty");
    }
  }
}
