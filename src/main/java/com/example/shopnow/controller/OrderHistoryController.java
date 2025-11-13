package com.example.shopnow.controller;

import com.example.shopnow.Utill.DatabaseConnection;
import com.example.shopnow.controller.OrderHistoryDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class OrderHistoryController {

    private Stage stage;

    public String getTitle() {
        return "ShopNow: Order History";
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

    @FXML
    private TableView<ProductOrderSummary> productOrderTable;

    @FXML
    private TableColumn<ProductOrderSummary, String> customerNameColumn;

    @FXML
    private TableColumn<ProductOrderSummary, String> phoneNumberColumn;

    @FXML
    private TableColumn<ProductOrderSummary, String> orderDateColumn;

    @FXML
    private TableColumn<ProductOrderSummary, Integer> productIdColumn;

    @FXML
    private TableColumn<ProductOrderSummary, String> productNameColumn;

    @FXML
    private TableColumn<ProductOrderSummary, Integer> quantityColumn;

    @FXML
    private void initialize() {
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        try {
            productOrderTable.setItems(OrderHistoryDAO.getOrderHistory());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load order history: " + e.getMessage());
        }
    }

    @FXML
    public void onClickBack() {
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}