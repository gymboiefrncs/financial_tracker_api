package org.financial_tracker.features.user.DTO;

/**
 * LoginUserRequest
 */
public record LoginRequest(String username, String rawPassword) {
}
