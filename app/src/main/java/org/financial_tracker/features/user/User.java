
package org.financial_tracker.features.user;

import java.util.UUID;

public record User(
    UUID id,
    String username,
    String fullName,
    Role role) {
}