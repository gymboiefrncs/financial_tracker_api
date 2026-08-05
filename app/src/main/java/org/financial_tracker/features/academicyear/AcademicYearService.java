package org.financial_tracker.features.academicyear;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.financial_tracker.features.academicyear.DTO.AcademicYearResponse;
import org.financial_tracker.features.academicyear.DTO.CreateAcademicYearRequest;

public class AcademicYearService {

  private final AcademicYearRepository repository;

  public AcademicYearService(AcademicYearRepository repository) {
    this.repository = repository;
  }

  public AcademicYearResponse createAcademicYear(CreateAcademicYearRequest request, UUID createdBy) {
    if (request.schoolYear() == null || request.schoolYear().isBlank()) {
      throw new IllegalArgumentException("School year format is required (e.g., '2026-2027')");
    }
    if (request.totalBudget() == null || request.totalBudget().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Total budget must be greater than or equal to 0");
    }

    return repository.create(request.schoolYear(), request.totalBudget(), createdBy);
  }

  public List<AcademicYearResponse> getAllAcademicYears() {
    return repository.findAll();
  }

  public AcademicYearResponse getAcademicYearById(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Academic year not found with ID: " + id));
  }
}