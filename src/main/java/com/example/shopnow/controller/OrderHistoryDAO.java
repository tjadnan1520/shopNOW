package com.example.shopnow.controller;

import com.example.shopnow.Utill.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class OrderHistoryDAO {

    public static ObservableList<ProductOrderSummary> getOrderHistory() throws SQLException {
        ObservableList<ProductOrderSummary> list = FXCollections.observableArrayList();
        String sql = """
            SELECT 
                c.name AS customer_name,
                c.phone AS phone_number,
                o.order_date AS order_date,
                p.id AS product_id,
                p.name AS product_name,
                oi.quantity AS quantity
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            JOIN customers c ON o.customer_id = c.id
            JOIN products p ON oi.product_id = p.id
            ORDER BY o.order_date DESC;
        """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ProductOrderSummary(
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        rs.getString("order_date"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity")
                ));
            }
        }
        return list;
    }

    public static ObservableList<ProductOrderSummary> getOrderHistoryByDate(LocalDate date) throws SQLException {
        ObservableList<ProductOrderSummary> list = FXCollections.observableArrayList();
        String sql = """
            SELECT 
                c.name AS customer_name,
                c.phone AS phone_number,
                o.order_date AS order_date,
                p.id AS product_id,
                p.name AS product_name,
                oi.quantity AS quantity
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            JOIN customers c ON o.customer_id = c.id
            JOIN products p ON oi.product_id = p.id
            WHERE DATE(o.order_date) = ?
            ORDER BY o.order_date DESC;
        """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, date.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new ProductOrderSummary(
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        rs.getString("order_date"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity")
                ));
            }
        }
        return list;
    }
}
