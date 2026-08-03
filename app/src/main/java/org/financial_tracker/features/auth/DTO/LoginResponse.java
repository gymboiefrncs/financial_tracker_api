package org.financial_tracker.features.auth.DTO;

import java.util.UUID;

public record LoginResponse(String username, String fullName, UUID sessionId) {
}