package org.financial_tracker.config;

import org.flywaydb.core.Flyway;

public class Database {
  public static void runDatabaseMigrations(String url, String user, String password) {
    System.out.println("Initialising database check: processing migration versions...");
    try {
      Flyway flyway = Flyway.configure()
          .dataSource(url, user, password)
          .load();

      flyway.migrate();
      System.out.println("Database migration successfully finished!");
    } catch (Exception e) {
      System.err.println("CRITICAL: Schema migration aborted due to error: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
