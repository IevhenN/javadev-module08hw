package org.hw.db;

import org.flywaydb.core.Flyway;
import org.hw.settings.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static final DataBase INSTANCE = new DataBase();
    private final Connection connection;

    private DataBase() {
        String dbUrl = Settings.getInstance().getString(Settings.DB_CONNECTION_URL);
        String dbUser = Settings.getInstance().getString(Settings.DB_USERNAME);
        String dbPassword = Settings.getInstance().getString(Settings.DB_PASSWORD);
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Flyway flyway = Flyway.configure()
                    .locations("db/migrations")
                    .dataSource(dbUrl, dbUser, dbPassword)
                    .load();
            flyway.migrate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataBase getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
}
