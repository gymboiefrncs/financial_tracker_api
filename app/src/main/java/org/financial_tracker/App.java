package org.financial_tracker;

import org.financial_tracker.config.Database;
import org.financial_tracker.config.Env;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Env envConfig = Env.load();
        Database.runDatabaseMigrations(envConfig.dbUrl(), envConfig.dbUser(), envConfig.dbPassword());

        Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
        }).start(envConfig.port());

        System.out.println("Javalin Application successfully spawned on port " + envConfig.port());
    }

}
