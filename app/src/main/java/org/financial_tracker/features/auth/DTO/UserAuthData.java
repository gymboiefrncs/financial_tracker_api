package org.financial_tracker.features.auth.DTO;

import java.util.UUID;

public record UserAuthData(UUID id, String username, String fullName, String passwordHash) {

}
