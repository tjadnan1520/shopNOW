package com.example.shopnow.Utill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatabaseSetup {

    public static void main(String[] args) {
        String sqlFilePath = "data.sql";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            StringBuilder sqlBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }
            } catch (IOException e) {
                System.out.println("Error reading SQL file: " + e.getMessage());
                return;
            }

            String[] sqlStatements = sqlBuilder.toString().split(";");

            try (Statement stmt = conn.createStatement()) {
                for (String sql : sqlStatements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Database setup completed successfully!");
            } catch (SQLException e) {
                System.out.println("SQL error: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}