package org.financial_tracker.features.user;

import java.util.UUID;

public record User(UUID id, String fullName, String username, String position) {
}