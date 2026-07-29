package org.financial_tracker.config;

import io.github.cdimascio.dotenv.Dotenv;

public record Env(String dbUrl, String dbUser, String dbPassword, int port) {
  public static Env load() {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    String dbUrl = getRequired(dotenv, "DB_URL");
    String dbUser = getRequired(dotenv, "DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD", "");
    int port = Integer.parseInt(dotenv.get("PORT", "7070"));

    return new Env(dbUrl, dbUser, dbPassword, port);
  }

  private static String getRequired(Dotenv dotenv, String key) {
    String val = dotenv.get(key);
    if (val == null || val.isBlank()) {
      throw new IllegalStateException("Missing required environment variable: " + key);
    }
    return val;
  }
}