package com.example.shopnow.controller;

public class ProductOrderSummary {
    private String customerName;
    private String phoneNumber;
    private String orderDate;
    private int productId;
    private String productName;
    private int quantity;

    public ProductOrderSummary(String customerName, String phoneNumber, String orderDate,
                               int productId, String productName, int quantity) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.orderDate = orderDate;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getOrderDate() { return orderDate; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
}
