package com.example.shopnow.DPApply.Report;

import com.example.shopnow.controller.Product;
import com.example.shopnow.controller.ProductOrderSummary;
import javafx.collections.ObservableList;

public class DailyReport extends Report {

    private ObservableList<ProductOrderSummary> orderList;
    private ObservableList<Product> productList;

    public DailyReport(ObservableList<ProductOrderSummary> orderList,
                       ObservableList<Product> productList,
                       ReportFormatter formatter) {
        super(formatter);
        this.orderList = orderList;
        this.productList = productList;
    }

    @Override
    public void generate(String fileName) {
        double totalAmount = 0;
        double totalProfit = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Customer,Phone,Date,Product,Quantity,Price,Total,Profit\n");

        for (ProductOrderSummary order : orderList) {
            // Find product by ID
            Product matchedProduct = null;
            for (Product p : productList) {
                if (p.getProductId() == order.getProductId()) {
                    matchedProduct = p;
                    break;
                }
            }

            if (matchedProduct == null) continue; // যদি product না মেলে, skip করবে

            double price = matchedProduct.getSellPrice();   // sell price from Product
            double buyPrice = matchedProduct.getBuyPrice(); // buy price from Product
            double total = price * order.getQuantity();
            double profit = (price - buyPrice) * order.getQuantity();

            totalAmount += total;
            totalProfit += profit;

            sb.append(order.getCustomerName()).append(",")
                    .append(order.getPhoneNumber()).append(",")
                    .append(order.getOrderDate()).append(",")
                    .append(order.getProductName()).append(",")
                    .append(order.getQuantity()).append(",")
                    .append(price).append(",")
                    .append(total).append(",")
                    .append(profit).append("\n");
        }

        sb.append("\nTotal Amount: ").append(totalAmount)
                .append(", Total Profit: ").append(totalProfit);

        formatter.generateFile(sb.toString(), fileName);
    }
}

