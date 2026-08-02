package org.financial_tracker.features.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.financial_tracker.features.user.DTO.CreateUserResponse;
import org.financial_tracker.features.user.DTO.LoginResponse;
import org.financial_tracker.features.user.DTO.UserAuthData;

public class UserRepository {
  private final DataSource dataSource;

  public UserRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public CreateUserResponse save(String fullName, String username, String password, String position, Role role) {
    String sql = "INSERT INTO users (full_name, username, password_hash, position, role) VALUES (?,?, ?, ?, ?::user_role) RETURNING id, full_name, username, position";

    try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, fullName);
      stmt.setString(2, username);
      stmt.setString(3, password);
      stmt.setString(4, position);
      stmt.setString(5, role.name());

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Creating user failed, no rows returned.");
        }
        return new CreateUserResponse(
            rs.getObject("id", UUID.class),
            rs.getString("full_name"),
            rs.getString("username"),
            rs.getString("position"));
      }

    } catch (SQLException e) {
      throw new RuntimeException("Database error while creating user: " + e.getMessage(), e);
    }
  }

  public Optional<UserAuthData> findByUsername(String username) {
    String sql = "SELECT username, full_name, password_hash FROM users WHERE username = ?";
    try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new UserAuthData(
              rs.getString("username"),
              rs.getString("full_name"),
              rs.getString("password_hash")));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error while fetching credentials", e);
    }
  }
}
