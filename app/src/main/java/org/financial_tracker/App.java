package org.financial_tracker;

import org.financial_tracker.config.Database;
import org.financial_tracker.config.Env;
import org.financial_tracker.features.user.UserController;
import org.financial_tracker.features.user.UserRepository;
import org.financial_tracker.features.user.UserRoutes;
import org.financial_tracker.features.user.UserService;
import org.financial_tracker.routes.AppRoutes;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Env envConfig = Env.load();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(envConfig.dbUrl());
        config.setUsername(envConfig.dbUser());
        config.setPassword(envConfig.dbPassword());
        config.setMaximumPoolSize(5);

        HikariDataSource dataSource = new HikariDataSource(config);

        Database.runDatabaseMigrations(dataSource);

        UserRepository userRepository = new UserRepository(dataSource);
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        UserRoutes userRoutes = new UserRoutes(userController);

        AppRoutes appRoutes = new AppRoutes(userRoutes);

        Javalin.create(javalinConfig -> {
            javalinConfig.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });

            javalinConfig.routes.apiBuilder(appRoutes);
        }).start(envConfig.port());

        System.out.println("Javalin Application successfully spawned on port " + envConfig.port());
    }

}
