package org.financial_tracker.middleware;

import org.financial_tracker.features.user.Role;
import org.financial_tracker.features.user.User;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;

public class RoleMiddleware {

  public void requireAdmin(Context ctx) {

    User user = ctx.attribute("user");

    if (user == null) {
      ctx.status(401);
      return;
    }

    if (user.role() != Role.admin) {
      throw new ForbiddenResponse("You are not allowed to do this");
    }
  }
}