package org.financial_tracker.features.auth;

import java.time.LocalDateTime;
import java.util.UUID;

public record Session(UUID id, UUID userId, LocalDateTime createdAt, LocalDateTime expiresAt) {
}
