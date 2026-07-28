package org.example;

import org.flywaydb.core.Flyway;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        int port = Integer.parseInt(dotenv.get("PORT", "7070"));
        if (dbUrl == null || dbUser == null) {
            System.err.println("CRITICAL ERROR: DB_URL or DB_USER is not set in the .env file!");
            System.exit(1);
        }
        runDatabaseMigrations(dbUrl, dbUser, dbPassword);

        Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
        }).start(port);

        System.out.println("Javalin Application successfully spawned on port " + port);
    }

    private static void runDatabaseMigrations(String url, String user, String password) {
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
