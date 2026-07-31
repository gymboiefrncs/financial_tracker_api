package org.financial_tracker.features.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserRepository {
  private final DataSource dataSource;

  public UserRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void save(String fullName, String username, String password, String position, Role role) {
    String sql = "INSERT INTO users (full_name, username, password_hash, position, role) VALUES (?,?, ?, ?, ?::user_role)";

    try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, fullName);
      stmt.setString(2, username);
      stmt.setString(3, password);
      stmt.setString(4, position);
      stmt.setString(5, role.name());

      stmt.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException("Database error while creating user: " + e.getMessage(), e);
    }
  }
}
