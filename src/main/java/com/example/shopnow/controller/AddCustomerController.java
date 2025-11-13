package com.example.shopnow.controller;

import com.example.shopnow.Utill.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCustomerController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private Stage stage;
    private Runnable onCustomerAdded;

    public String getTitle() {
        return "ShopNow : Add Customer";
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setTitle(getTitle());
    }

    public void setOnCustomerAdded(Runnable onCustomerAdded) {
        this.onCustomerAdded = onCustomerAdded;
    }

    public void initialize() {

    }

    @FXML
    private void onSaveCustomer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            String query = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.executeUpdate();

            showAlert("Success", "Customer added successfully!");

            if (onCustomerAdded != null) {
                onCustomerAdded.run();
            }

            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }

    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
