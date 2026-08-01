package org.financial_tracker.features.user;

import org.financial_tracker.features.user.DTO.CreateUserRequest;
import org.financial_tracker.features.user.DTO.LoginUserRequest;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User registerUser(CreateUserRequest req) {
    validate(req);
    return userRepository.save(req.full_name(), req.username(), req.password(), req.position(), req.role());
  }

  public boolean login(LoginUserRequest req) {
    var credentials = userRepository.findByUsername(req.username());

    if (credentials.isEmpty()) {
      return false;
    }

    return credentials.get().password_hash().equals(req.rawPassword());
  }

  private void validate(CreateUserRequest req) {
    if (req.username() == null || req.username().isBlank()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    if (req.password() == null || req.password().isBlank()) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    if (req.full_name() == null || req.full_name().isBlank()) {
      throw new IllegalArgumentException("Full name cannot be empty");
    }
    if (req.position() == null || req.position().isBlank()) {
      throw new IllegalArgumentException("Position cannot be empty");
    }
  }
}
