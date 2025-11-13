package com.example.shopnow.controller;

import javafx.beans.property.*;

public class StockEntry {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final IntegerProperty quantity;
    private final StringProperty date;

    public StockEntry(int id, int productId, String productName, int quantity, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.date = new SimpleStringProperty(date);
    }

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }

    public int getProductId() {
        return productId.get();
    }
    public IntegerProperty productIdProperty() {
        return productId;
    }

    public String getProductName() {
        return productName.get();
    }
    public StringProperty productNameProperty() {
        return productName;
    }

    public int getQuantity() {
        return quantity.get();
    }
    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public String getDate() {
        return date.get();
    }
    public StringProperty dateProperty() {
        return date;
    }
}

