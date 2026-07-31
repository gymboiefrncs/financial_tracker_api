package org.financial_tracker.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

public class Database {
  public static void runDatabaseMigrations(DataSource dataSource) {
    Flyway flyway = Flyway.configure().dataSource(dataSource).load();
    flyway.migrate();
  }
}
