package org.financial_tracker.features.user;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void registerUser(String fullName, String username, String password, String position, Role role) {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    userRepository.save(fullName, username, password, position, role);
  }
}
