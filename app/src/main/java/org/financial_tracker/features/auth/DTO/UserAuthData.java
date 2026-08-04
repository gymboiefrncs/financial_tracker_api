package org.financial_tracker.features.auth.DTO;

import java.util.UUID;

import org.financial_tracker.features.user.Role;

public record UserAuthData(UUID id, String username, String fullName, String passwordHash, Role role) {
}
