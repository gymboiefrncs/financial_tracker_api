package org.financial_tracker.features.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.financial_tracker.features.user.Role;

public class ProfileRepository {
  private final DataSource dataSource;

  public ProfileRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Optional<Profile> findMe(UUID id) {
    String sql = "SELECT id, full_name, username, role, position FROM users WHERE id = ?";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setObject(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          return Optional.empty();
        }

        return Optional.of(
            new Profile(
                rs.getObject("id", UUID.class),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("position"),
                Role.valueOf(rs.getString("role"))));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error while fetching profile for ID: " + id, e);
    }
  }
}
