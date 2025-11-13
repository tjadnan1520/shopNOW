package com.example.shopnow.Utill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/data.db";

    private DatabaseConnection() throws SQLException {
        openNewConnection();
    }

    private void openNewConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to database (Singleton).");
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Reopening closed database connection...");
            openNewConnection();
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }
}
