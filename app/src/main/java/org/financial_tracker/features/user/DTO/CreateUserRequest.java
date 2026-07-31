package org.financial_tracker.features.user.DTO;

import org.financial_tracker.features.user.Role;

/**
 * CreateUserRquest
 */
public record CreateUserRequest(String full_name, String username, String password, String position, Role role) {
}