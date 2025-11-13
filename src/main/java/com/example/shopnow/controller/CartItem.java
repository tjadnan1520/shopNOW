package com.example.shopnow.controller;

public class CartItem {
    private String productName;
    private int quantity;
    private double price;
    private double subtotal;

    public CartItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = quantity * price;
    }

    public String getProductName() {
        return productName;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPrice() {
        return price;
    }
    public double getSubtotal() {
        return price * quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = quantity * price;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
