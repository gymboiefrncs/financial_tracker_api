package org.financial_tracker;

import org.financial_tracker.config.Database;
import org.financial_tracker.config.Env;
import org.financial_tracker.features.auth.AuthController;
import org.financial_tracker.features.auth.AuthRepository;
import org.financial_tracker.features.auth.AuthRoutes;
import org.financial_tracker.features.auth.AuthService;
import org.financial_tracker.features.user.UserController;
import org.financial_tracker.features.user.UserRepository;
import org.financial_tracker.features.user.UserRoutes;
import org.financial_tracker.features.user.UserService;
import org.financial_tracker.middleware.AuthMiddleware;
import org.financial_tracker.middleware.RoleMiddleware;
import org.financial_tracker.routes.AppRoutes;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

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

        AuthRepository authRepository = new AuthRepository(dataSource);
        AuthService authService = new AuthService(authRepository);
        AuthController authController = new AuthController(authService);
        AuthRoutes authRoutes = new AuthRoutes(authController);

        AuthMiddleware authMiddleware = new AuthMiddleware(authRepository, userRepository);
        RoleMiddleware roleMiddleware = new RoleMiddleware();

        AppRoutes appRoutes = new AppRoutes(userRoutes, authRoutes);

        Javalin.create(javalinConfig -> {
            javalinConfig.routes.before("/api/admin/*", authMiddleware::requireAuth);
            javalinConfig.routes.before("/api/admin/*", roleMiddleware::requireAdmin);

            javalinConfig.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });

            javalinConfig.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.info(info -> {
                        info.title("Financial Tracker API");
                        info.version("1.0.0");
                    });
                });
            }));

            javalinConfig.registerPlugin(new SwaggerPlugin());

            javalinConfig.routes.apiBuilder(appRoutes);

        }).start(envConfig.port());

        System.out.println("Javalin Application successfully spawned on port " + envConfig.port());
    }

}
