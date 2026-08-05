package org.financial_tracker.features.academicyear.DTO;

import java.math.BigDecimal;

public record CreateAcademicYearRequest(String schoolYear, BigDecimal totalBudget) {
}
