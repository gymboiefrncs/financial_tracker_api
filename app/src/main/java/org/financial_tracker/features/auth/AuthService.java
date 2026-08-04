package org.financial_tracker.features.auth;

import java.util.Optional;

import org.financial_tracker.features.auth.DTO.LoginRequest;
import org.financial_tracker.features.auth.DTO.LoginResponse;
import org.financial_tracker.features.auth.DTO.UserAuthData;
import org.financial_tracker.features.user.Role;

public class AuthService {
  private final AuthRepository authRepository;

  public AuthService(AuthRepository authRepository) {
    this.authRepository = authRepository;
  }

  public Optional<LoginResponse> login(LoginRequest req) {

    Optional<UserAuthData> user = authRepository.findByUsername(req.username());

    if (user.isEmpty()) {
      return Optional.empty();
    }

    UserAuthData credentials = user.get();

    if (!credentials.passwordHash().equals(req.rawPassword())) {
      return Optional.empty();
    }

    Session session = authRepository.createSession(credentials.id());

    return Optional.of(new LoginResponse(
        credentials.username(),
        credentials.fullName(),
        credentials.role(),
        session.id()));
  }

}
