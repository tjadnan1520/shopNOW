package com.example.shopnow.DPApply;

import com.example.shopnow.controller.Product;
import javafx.scene.control.Alert;

public class ReminderObserver implements Observer {
    @Override
    public void update(Product product, String message) {
        showAlert(Alert.AlertType.WARNING, "Product Reminder", message);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
