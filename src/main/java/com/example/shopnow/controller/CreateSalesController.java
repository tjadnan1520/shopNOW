package com.example.shopnow.controller;

import com.example.shopnow.Utill.DatabaseConnection;
import com.example.shopnow.DPApply.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class CreateSalesController {

    @FXML private ComboBox<String> productComboBox;
    @FXML private TextField quantityField;
    @FXML private TextField phoneField;

    @FXML private Label customerName, customerEmail, totalLabel;
    @FXML private Button searchButton, addCustomerButton, sellButton;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> productNameCol;
    @FXML private TableColumn<CartItem, Integer> quantityCol;
    @FXML private TableColumn<CartItem, Double> priceCol;
    @FXML private TableColumn<CartItem, Double> subtotalCol;

    private ObservableList<String> productNames = FXCollections.observableArrayList();
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private Stage stage;
    private int currentCustomerId = -1;
    private Customer currentCustomer;
    private final DraftManager draftManager = DraftManager.getInstance();

    public String getTitle() {
        return "ShopNow : Sale Manage";
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

    public void initialize() {
        try {
            loadProducts();
            setupAutoComplete();
            setupCartTable();
        } catch (SQLException e) {
            showAlert("Error", "Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn.isClosed()) {
            throw new SQLException("Database connection is closed");
        }
        return conn;
    }

    private void loadProducts() throws SQLException {
        productNames.clear();
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM products")) {
            while (rs.next()) {
                productNames.add(rs.getString("name"));
            }
            productComboBox.setItems(productNames);
        }
    }

    private int getProductIdByName(String name) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM products WHERE name = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        return -1;
    }

    private int getAvailableStock(String productName) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT quantity FROM products WHERE name = ?")) {
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("quantity");
            }
        }
        return 0;
    }

    private double getPriceOfProduct(String productName) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT sell_price FROM products WHERE name = ?")) {
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("sell_price");
            }
        }
        return 0.0;
    }

    private void setupAutoComplete() {
        productComboBox.setEditable(true);
        final Object lock = new Object();
        final boolean[] userInput = {false};

        productComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            synchronized (lock) {
                if (!userInput[0]) {
                    userInput[0] = true;

                    List<String> filtered = productNames.stream()
                            .filter(item -> item.toLowerCase().contains(newText.toLowerCase()))
                            .collect(Collectors.toList());

                    productComboBox.setItems(FXCollections.observableArrayList(filtered));
                    if (!productComboBox.isShowing()) productComboBox.show();

                    productComboBox.getEditor().setText(newText);
                    productComboBox.getEditor().positionCaret(newText.length());

                    userInput[0] = false;
                }
            }
        });

        productComboBox.setOnAction(event -> {
            String selected = productComboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Platform.runLater(() -> {
                    productComboBox.getEditor().setText(selected);
                    productComboBox.getEditor().positionCaret(selected.length());
                });
            }
        });
    }

    private void setupCartTable() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        subtotalCol.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        cartTable.setItems(cartItems);
    }

    @FXML
    private void onSearchCustomer() {
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            showAlert("Error", "Phone number is required.");
            return;
        }

        try {
            Connection conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, name, email FROM customers WHERE phone = ?")) {
                stmt.setString(1, phone);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        currentCustomerId = rs.getInt("id");
                        customerName.setText(rs.getString("name"));
                        customerEmail.setText(rs.getString("email"));
                        addCustomerButton.setVisible(false);

                        if (draftManager.hasDraft(phone)) {
                            restoreCart(draftManager.getDraft(phone));
                            showAlert("Draft Restored", "Draft for this customer has been restored.");
                        }
                    } else {
                        currentCustomerId = -1;
                        customerName.setText("-");
                        customerEmail.setText("-");
                        addCustomerButton.setVisible(true);
                        showAlert("Not Found", "Customer not found. Please add.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Command cmd = new SearchCustomerCommand(phone, draftManager, this);
        cmd.execute();
    }

    @FXML
    private void onAddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/AddCustomer.fxml"));
            Parent root = loader.load();

            AddCustomerController controller = loader.getController();
            Stage customerStage = new Stage();
            controller.setStage(customerStage);

            controller.setOnCustomerAdded(() -> {
                phoneField.setText(phoneField.getText().trim());
                onSearchCustomer();
            });

            customerStage.setScene(new Scene(root));
            customerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Add Customer window.");
        }
    }

    @FXML
    private void onAddToCart() {
        String product = productComboBox.getValue();
        String qtyStr = quantityField.getText();

        if (product == null || product.isEmpty() || qtyStr.isEmpty()) {
            showAlert("Input Error", "Please select product and quantity.");
            return;
        }

        try {
            int requestedQty = Integer.parseInt(qtyStr);
            int availableStock = getAvailableStock(product);
            if (requestedQty > availableStock) {
                showAlert("Stock Error", "Only " + availableStock + " item(s) available in stock.");
                return;
            }

            double price = getPriceOfProduct(product);
            CartItem newItem = new CartItem(product, requestedQty, price);

            Command cmd = new AddProductCommand(newItem, this);
            cmd.execute();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Quantity must be a number.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        currentCustomerId = customer.getId();
        customerName.setText(customer.getName());
        customerEmail.setText(customer.getEmail());
        addCustomerButton.setVisible(false);
    }

    public void clearCart() {
        cartItems.clear();
        updateTotal();
    }

    public void addProductToCart(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductName().equals(item.getProductName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                cartItem.setSubtotal(cartItem.getQuantity() * cartItem.getPrice());
                cartTable.refresh();
                updateTotal();
                return;
            }
        }
        cartItems.add(item);
        updateTotal();
    }

    public void restoreCart(com.example.shopnow.DPApply.CartMemento memento) {
        cartItems.clear();
        cartItems.addAll(memento.getSavedState());
        updateTotal();
    }

    @FXML
    public void onConfirmSale() {
        System.out.println("=== Confirm Sale Clicked ===");
        if (cartItems.isEmpty()) {
            showAlert("Error", "Cart is empty!");
            return;
        }
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            showAlert("Error", "Customer phone is empty!");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Connection conn = DatabaseConnection.getInstance().getConnection();
                try {
                    conn.setAutoCommit(false);

                    // Customer check
                    int customerId;
                    try (PreparedStatement custStmt = conn.prepareStatement(
                            "SELECT id FROM customers WHERE phone = ?")) {
                        custStmt.setString(1, phone);
                        try (ResultSet rs = custStmt.executeQuery()) {
                            if (rs.next()) {
                                customerId = rs.getInt("id");
                            } else {
                                throw new SQLException("Customer not found");
                            }
                        }
                    }

                    // Insert order
                    int orderId;
                    try (PreparedStatement orderStmt = conn.prepareStatement(
                            "INSERT INTO orders (customer_id, order_date, status) VALUES (?, datetime('now'), ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        orderStmt.setInt(1, customerId);
                        orderStmt.setString(2, "Completed");
                        orderStmt.executeUpdate();
                        try (ResultSet keys = orderStmt.getGeneratedKeys()) {
                            if (keys.next()) {
                                orderId = keys.getInt(1);
                            } else {
                                throw new SQLException("Failed to create order");
                            }
                        }
                    }

                    // Insert order items
                    try (PreparedStatement itemStmt = conn.prepareStatement(
                            "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)")) {
                        for (CartItem item : cartItems) {
                            int productId = getProductIdByName(item.getProductName());
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, productId);
                            itemStmt.setInt(3, item.getQuantity());
                            itemStmt.setDouble(4, item.getPrice());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    // Update stock
                    try (PreparedStatement stockStmt = conn.prepareStatement(
                            "UPDATE products SET quantity = quantity - ? WHERE id = ?")) {
                        for (CartItem item : cartItems) {
                            int productId = getProductIdByName(item.getProductName());
                            stockStmt.setInt(1, item.getQuantity());
                            stockStmt.setInt(2, productId);
                            stockStmt.addBatch();
                        }
                        stockStmt.executeBatch();
                    }
                    try (PreparedStatement stockStmt = conn.prepareStatement(
                            "UPDATE product_entries SET quantity = quantity - ? WHERE product_id = ?")) {
                        for (CartItem item : cartItems) {
                            int productId = getProductIdByName(item.getProductName());
                            stockStmt.setInt(1, item.getQuantity());
                            stockStmt.setInt(2, productId);
                            stockStmt.addBatch();
                        }
                        stockStmt.executeBatch();
                    }

                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            draftManager.clearDraft(phone);
            cartItems.clear();
            updateTotal();
            showAlert("Success", "Sale completed and saved successfully.");
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            showAlert("Error", "Failed to complete sale: " + (ex != null ? ex.getMessage() : "Unknown error"));
        });

        new Thread(task).start();
    }


    @FXML
    private void onSaveDraft() {
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            showAlert("Error", "Customer phone is required to save draft.");
            return;
        }

        if (cartItems.isEmpty()) {
            showAlert("Error", "Cart is empty, nothing to save.");
            return;
        }

        Customer customer = findCustomerByPhone(phone);
        if (customer == null) {
            showAlert("Error", "Customer not found. Please add customer first.");
            return;
        }

        draftManager.saveDraft(phone, new com.example.shopnow.DPApply.CartMemento(List.copyOf(cartItems)));
        this.currentCustomer = customer;
        showAlert("Success", "Draft saved successfully for customer: " + customer.getName());
    }


    private void updateTotal() {
        double total = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        totalLabel.setText(String.valueOf(total));
    }

    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.isEmpty()) return null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, name, email, phone FROM customers WHERE phone = ?")) {

            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @FXML
    public void onClickBack() {
        if (stage == null) stage = (Stage) productComboBox.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/Homepage.fxml"));
            Parent root = loader.load();

            HomeController home = loader.getController();
            home.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}