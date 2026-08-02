package org.financial_tracker.features.user.DTO;

import java.util.UUID;

public record CreateUserResponse(UUID id, String fullName, String username, String position) {
}