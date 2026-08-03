package org.financial_tracker.features.profile;

import java.util.UUID;

import org.financial_tracker.features.user.Role;

public record Profile(UUID id, String full_name, String username, String position, Role role) {
}
