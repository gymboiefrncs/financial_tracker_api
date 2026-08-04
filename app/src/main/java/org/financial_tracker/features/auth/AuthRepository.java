package org.financial_tracker.features.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.financial_tracker.features.auth.DTO.UserAuthData;
import org.financial_tracker.features.user.Role;

public class AuthRepository {

  private final DataSource dataSource;

  public AuthRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Optional<UserAuthData> findByUsername(String username) {
    String sql = "SELECT id, username, full_name, password_hash, role FROM users WHERE username = ?";
    try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new UserAuthData(
              rs.getObject("id", UUID.class),
              rs.getString("username"),
              rs.getString("full_name"),
              rs.getString("password_hash"),
              Role.valueOf(rs.getString("role"))));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error while fetching credentials", e);
    }
  }

  // sessions
  public Session createSession(UUID userId) {
    String sql = "INSERT INTO sessions (user_id, expires_at) VALUES (?, ?) RETURNING *";

    try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
      LocalDateTime expiry = LocalDateTime.now().plusDays(7);
      stmt.setObject(1, userId);
      stmt.setTimestamp(2, Timestamp.valueOf(expiry));

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Creating session failed, no rows returned.");
        }
        return new Session(
            rs.getObject("id", UUID.class),
            rs.getObject("user_id", UUID.class),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("expires_at").toLocalDateTime());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Optional<Session> findSessionById(UUID id) {

    String sql = """
            SELECT *
            FROM sessions
            WHERE id = ?
        """;

    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setObject(1, id);

      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        return Optional.empty();
      }

      return Optional.of(new Session(
          rs.getObject("id", UUID.class),
          rs.getObject("user_id", UUID.class),
          rs.getTimestamp("created_at")
              .toLocalDateTime(),
          rs.getTimestamp("expires_at")
              .toLocalDateTime()));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
