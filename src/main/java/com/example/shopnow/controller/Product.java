package com.example.shopnow.controller;

public class Product {
    private int productId;
    private String productName;
    private String category;
    private double buyPrice;
    private double sellPrice;
    private int quantity;
    private String date;
    private String expiryDate;

    public Product(int productId, String productName, String category,
                   double buyPrice, double sellPrice, int quantity,
                   String date, String expiryDate) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.date = date;
        this.expiryDate = expiryDate;
    }

    // Getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getBuyPrice() { return buyPrice; }
    public double getSellPrice() { return sellPrice; }
    public int getQuantity() { return quantity; }
    public String getDate() { return date; }
    public String getExpiryDate() { return expiryDate; }

    // Setters
    public void setProductId(int productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setCategory(String category) { this.category = category; }
    public void setBuyPrice(double buyPrice) { this.buyPrice = buyPrice; }
    public void setSellPrice(double sellPrice) { this.sellPrice = sellPrice; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDate(String date) { this.date = date; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}

