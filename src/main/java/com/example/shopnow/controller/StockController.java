package com.example.shopnow.controller;

import com.example.shopnow.Utill.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockController {

    @FXML
    private TableView<StockEntry> viewStock;

    @FXML
    private TableColumn<StockEntry, Integer> idColumn;

    @FXML
    private TableColumn<StockEntry, Integer> productIdColumn;

    @FXML
    private TableColumn<StockEntry, String> nameColumn;

    @FXML
    private TableColumn<StockEntry, Integer> quantityColumn;

    @FXML
    private TableColumn<StockEntry, String> dateColumn;

    private final ObservableList<StockEntry> stockEntries = FXCollections.observableArrayList();

    private Stage stage;

    public String getTitle() {
        return "ShopNow: View Stock";
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
        if (conn.isClosed()) {
            throw new SQLException("Database connection is closed");
        }
        return conn;
    }

    private void loadStockEntriesFromDB() throws SQLException {
        stockEntries.clear();
        String sql = """
            SELECT 
                pe.id, 
                pe.product_id, 
                p.name AS product_name, 
                pe.quantity, 
                pe.date
            FROM product_entries pe
            JOIN products p ON pe.product_id = p.id
            ORDER BY pe.id ASC;
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StockEntry entry = new StockEntry(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getString("date")
                );
                stockEntries.add(entry);
            }
            viewStock.setItems(stockEntries);
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        productIdColumn.setCellValueFactory(data -> data.getValue().productIdProperty().asObject());
        nameColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());

        try {
            loadStockEntriesFromDB();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load stock entries: " + e.getMessage());
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