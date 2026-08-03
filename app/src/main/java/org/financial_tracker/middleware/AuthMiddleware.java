package org.financial_tracker.middleware;

import java.util.Optional;
import java.util.UUID;

import org.financial_tracker.features.auth.AuthRepository;
import org.financial_tracker.features.auth.Session;
import org.financial_tracker.features.user.User;
import org.financial_tracker.features.user.UserRepository;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class AuthMiddleware {
  private final AuthRepository authRepository;
  private final UserRepository userRepository;

  public AuthMiddleware(AuthRepository authRepository, UserRepository userRepository) {
    this.authRepository = authRepository;
    this.userRepository = userRepository;
  }

  public void requireAuth(Context ctx) {

    String cookie = ctx.cookie("session_id");

    if (cookie == null) {
      throw new UnauthorizedResponse("Invalid session");
    }

    UUID sessionId;

    try {
      sessionId = UUID.fromString(cookie);
    } catch (IllegalArgumentException e) {
      throw new UnauthorizedResponse("Invalid session");
    }

    Optional<Session> session = authRepository.findSessionById(sessionId);

    if (session.isEmpty()) {
      throw new UnauthorizedResponse("Session expired");
    }

    Optional<User> user = userRepository.findUserById(session.get().userId());

    if (user.isEmpty()) {
      throw new UnauthorizedResponse("Session expired");
    }

    ctx.attribute(
        "user",
        user.get());
  }
}