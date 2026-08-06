package org.financial_tracker.features.academicyear.DTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AcademicYearResponse(UUID id, String schoolYear, BigDecimal totalBudget, BigDecimal remainingBudget,
        UUID createdBy, OffsetDateTime createdAt) {
}
