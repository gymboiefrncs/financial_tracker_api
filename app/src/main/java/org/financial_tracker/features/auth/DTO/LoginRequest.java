package org.financial_tracker.features.auth.DTO;

/**
 * LoginUserRequest
 */
public record LoginRequest(String username, String rawPassword) {
}
