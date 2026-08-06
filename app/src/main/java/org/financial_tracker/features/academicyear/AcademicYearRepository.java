package org.financial_tracker.features.academicyear;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.financial_tracker.features.academicyear.DTO.AcademicYearResponse;

public class AcademicYearRepository {

  private final DataSource dataSource;

  public AcademicYearRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public AcademicYearResponse create(String schoolYear, java.math.BigDecimal totalBudget, UUID createdBy) {
    String sql = """
            INSERT INTO academic_years (school_year, total_budget, remaining_budget, created_by)
            VALUES (?, ?, ?, ?)
            RETURNING id, school_year, total_budget, remaining_budget, created_by, created_at
        """;

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, schoolYear);
      ps.setBigDecimal(2, totalBudget);
      ps.setBigDecimal(3, totalBudget); // initial remaining budget = total budget
      ps.setObject(4, createdBy);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return mapRow(rs);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error creating academic year", e);
    }
    throw new IllegalStateException("Failed to insert academic year");
  }

  public List<AcademicYearResponse> findAll() {
    String sql = """
            SELECT id, school_year, total_budget, remaining_budget, created_by, created_at
            FROM academic_years
            ORDER BY created_at DESC
        """;

    List<AcademicYearResponse> list = new ArrayList<>();
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        list.add(mapRow(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error fetching academic years", e);
    }
    return list;
  }

  public Optional<AcademicYearResponse> findById(UUID id) {
    String sql = """
            SELECT id, school_year, total_budget, remaining_budget, created_by, created_at
            FROM academic_years
            WHERE id = ?
        """;

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRow(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error finding academic year", e);
    }
    return Optional.empty();
  }

  private AcademicYearResponse mapRow(ResultSet rs) throws SQLException {
    return new AcademicYearResponse(
        rs.getObject("id", UUID.class),
        rs.getString("school_year"),
        rs.getBigDecimal("total_budget"),
        rs.getBigDecimal("remaining_budget"),
        rs.getObject("created_by", UUID.class),
        rs.getObject("created_at", OffsetDateTime.class));
  }
}