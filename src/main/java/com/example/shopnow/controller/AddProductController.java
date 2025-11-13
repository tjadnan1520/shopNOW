package com.example.shopnow.controller;

import com.example.shopnow.Utill.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AddProductController {

    @FXML private TextField productId;
    @FXML private TextField productName;
    @FXML private TextField category;
    @FXML private TextField buyPrice;
    @FXML private TextField sellPrice;
    @FXML private TextField quantity;
    @FXML private TextField date;
    @FXML private TextField expireDate;

    private Stage stage;

    public String getTitle() {
        return "ShopNow: Add Product";
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage != null) {
            this.stage.setTitle(getTitle());
            this.stage.setOnCloseRequest(event -> {
                try {
                    DatabaseConnection.getInstance().closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Database connection is closed or null");
        }
        return conn;
    }

    @FXML
    private void onClickAdd() {
        String id = productId.getText().trim();
        String name = productName.getText().trim();
        String categoryValue = category.getText().trim();
        String buyPriceValue = buyPrice.getText().trim();
        String sellPriceValue = sellPrice.getText().trim();
        String quantityValue = quantity.getText().trim();
        String dateValue = date.getText().trim();
        String expiryValue = expireDate.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Product ID and Name are required.");
            return;
        }

        int idInt;
        double buyPriceDouble, sellPriceDouble;
        int quantityInt;

        try {
            idInt = Integer.parseInt(id);
            buyPriceDouble = buyPriceValue.isEmpty() ? 0.0 : Double.parseDouble(buyPriceValue);
            sellPriceDouble = sellPriceValue.isEmpty() ? 0.0 : Double.parseDouble(sellPriceValue);
            quantityInt = quantityValue.isEmpty() ? 0 : Integer.parseInt(quantityValue);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Product ID, Buy Price, Sell Price, and Quantity must be valid numbers.");
            return;
        }

        try (Connection conn = getConnection()) {
            // Check product exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT quantity FROM products WHERE id = ?");
            checkStmt.setInt(1, idInt);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing product
                int existingQty = rs.getInt("quantity");
                int newQty = existingQty + quantityInt;

                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE products SET quantity = ?, expiry_date = ?, buy_price = ?, sell_price = ? WHERE id = ?");
                updateStmt.setInt(1, newQty);
                updateStmt.setString(2, expiryValue.isEmpty() ? null : expiryValue);
                updateStmt.setDouble(3, buyPriceDouble);
                updateStmt.setDouble(4, sellPriceDouble);
                updateStmt.setInt(5, idInt);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Stock Updated", "Existing product has been updated.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update product.");
                }
                updateStmt.close();

            } else {
                // Insert new product
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO products (id, name, category, buy_price, sell_price, quantity, date, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                insertStmt.setInt(1, idInt);
                insertStmt.setString(2, name);
                insertStmt.setString(3, categoryValue.isEmpty() ? null : categoryValue);
                insertStmt.setDouble(4, buyPriceDouble);
                insertStmt.setDouble(5, sellPriceDouble);
                insertStmt.setInt(6, quantityInt);
                insertStmt.setString(7, dateValue.isEmpty() ? null : dateValue);
                insertStmt.setString(8, expiryValue.isEmpty() ? null : expiryValue);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Product Added", "New product has been added.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Insert Error", "Failed to add new product.");
                }
                insertStmt.close();
            }

            // Insert into product_entries (log entry)
            PreparedStatement entryStmt = conn.prepareStatement(
                    "INSERT INTO product_entries (product_id, quantity, date) VALUES (?, ?, ?)");
            entryStmt.setInt(1, idInt);
            entryStmt.setInt(2, quantityInt);
            entryStmt.setString(3, dateValue.isEmpty() ? null : dateValue);

            int entryRows = entryStmt.executeUpdate();
            if (entryRows == 0) {
                showAlert(Alert.AlertType.ERROR, "Insert Error", "Failed to add product entry.");
            }
            entryStmt.close();
            rs.close();
            checkStmt.close();

            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to access or update the database: " + e.getMessage());
        }
    }

    @FXML
    private void onClickBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/Homepage.fxml"));
            Parent root = loader.load();

            HomeController home = loader.getController();
            home.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Home");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Home page: " + e.getMessage());
        }
    }

    private void clearFields() {
        productId.clear();
        productName.clear();
        category.clear();
        buyPrice.clear();
        sellPrice.clear();
        quantity.clear();
        date.clear();
        expireDate.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
