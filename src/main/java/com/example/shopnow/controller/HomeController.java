package com.example.shopnow.controller;

import com.example.shopnow.DPApply.Singleton;
import com.example.shopnow.Utill.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HomeController {

    @FXML
    private Button addProductButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage != null) {
            this.stage.setTitle("ShopNow: Home");
            this.stage.setOnCloseRequest(event -> {
                try {
                    DatabaseConnection.getInstance().closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    public void initialize() {
        Singleton session = Singleton.getInstance();
        String role = session.getUserRole();

        if ("employee".equalsIgnoreCase(role)) {
            addProductButton.setDisable(true);
        } else {
            addProductButton.setDisable(false);
        }
    }

    @FXML
    private void onAddProductClick() {
        if ("employee".equalsIgnoreCase(Singleton.getInstance().getUserRole())) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to add products.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/AddProduct.fxml"));
            Parent root = loader.load();

            AddProductController addProductController = loader.getController();
            addProductController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Add Product");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Add Product page: " + e.getMessage());
        }
    }

    @FXML
    private void onProductListClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/ProductList.fxml"));
            Parent root = loader.load();
            ProductListController productListController = loader.getController();
            productListController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Product List");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Product List page: " + e.getMessage());
        }
    }

    @FXML
    private void onCustomerListClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/CustomerList.fxml"));
            Parent root = loader.load();
            CustomerListController customerListController = loader.getController();
            customerListController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Customer List");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Customer List page: " + e.getMessage());
        }
    }

    @FXML
    private void onOrderHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/OrderHistory.fxml"));
            Parent root = loader.load();
            OrderHistoryController orderHistoryController = loader.getController();
            orderHistoryController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Order History");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Order History page: " + e.getMessage());
        }
    }

    @FXML
    private void onCreateSalesClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/CreateSale.fxml"));
            Parent root = loader.load();
            CreateSalesController createSalesController = loader.getController();
            createSalesController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Create Sale");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Create Sales page: " + e.getMessage());
        }
    }

    @FXML
    private void onViewStockClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/ViewStock.fxml"));
            Parent root = loader.load();
            StockController stockController = loader.getController();
            stockController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: View Stock");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load View Stock page: " + e.getMessage());
        }
    }

    @FXML
    private void onShowReportsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/ReportOptions.fxml"));
            Parent root = loader.load();
            ReportOptionController reportController = loader.getController();
            reportController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Report Option");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load View Stock page: " + e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/EntryPage.fxml"));
            Parent root = loader.load();

            EntryController entryController = loader.getController();
            entryController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Entry");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Entry page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
