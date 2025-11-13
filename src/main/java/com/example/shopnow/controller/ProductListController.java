package com.example.shopnow.controller;

import com.example.shopnow.DPApply.ReminderObserver;
import com.example.shopnow.DPApply.ReminderSubject;
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
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ProductListController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Double> buyPriceColumn;
    @FXML private TableColumn<Product, Double> sellPriceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, String> dateColumn;
    @FXML private TableColumn<Product, String> expireDateColumn;

    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ReminderSubject reminderSubject = new ReminderSubject();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        reminderSubject.attach(new ReminderObserver());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        buyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        expireDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        loadProducts();
        productTable.setItems(products);
    }

    private void loadProducts() {
        products.clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("buy_price"),
                        rs.getDouble("sell_price"),
                        rs.getInt("quantity"),
                        rs.getString("date"),
                        rs.getString("expiry_date")
                );

                // expiry check
                LocalDate expiry = safeParseDate(product.getExpiryDate());
                if (expiry != null && expiry.isBefore(LocalDate.now())) {
                    reminderSubject.notifyObservers(product,
                            "The product '" + product.getProductName() +
                                    "' has expired on " + product.getExpiryDate() + ". Consider deleting it.");
                }

                // stock check
                if (product.getQuantity() <= 1) {
                    reminderSubject.notifyObservers(product,
                            "The product '" + product.getProductName() +
                                    "' is low on stock (quantity: " + product.getQuantity() + "). Consider restocking.");
                }

                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load products: " + e.getMessage());
        }
    }

    private LocalDate safeParseDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) return null;

        String[] patterns = {"d-M-uuuu", "dd-MM-uuuu", "d/M/uuuu", "dd/MM/uuuu", "uuuu-MM-dd"};
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(rawDate.trim(), DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignored) {}
        }

        System.err.println("Unrecognized date format: " + rawDate);
        return null;
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
