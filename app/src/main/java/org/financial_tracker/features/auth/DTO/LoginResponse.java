package org.financial_tracker.features.auth.DTO;

import java.util.UUID;

import org.financial_tracker.features.user.Role;

public record LoginResponse(String username, String fullName, Role role, UUID sessionId) {
}