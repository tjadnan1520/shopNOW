package com.example.shopnow.controller;

import com.example.shopnow.DPApply.Report.*;
import com.example.shopnow.Utill.DatabaseConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class DailyReportController {

    @FXML
    private DatePicker datePicker;

    private Stage stage;
    public String getTitle() {
        return "ShopNow: Report Option";
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
    public void onGenerateReport(ActionEvent event) throws IOException {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            System.out.println("Please select a date!");
            return;
        }

        System.out.println("Generating report for date: " + date);

        try {
            ObservableList<Product> products = ProductDAO.getAllProducts();
            ObservableList<ProductOrderSummary> orders = OrderHistoryDAO.getOrderHistoryByDate(date);

            if (orders.isEmpty()) {
                System.out.println("No orders found for " + date);
            }

            String fileName = "DailyReport_" + date.format(java.time.format.DateTimeFormatter.ofPattern("d-M-yyyy"));

            Report dailyCSV = new DailyReport(orders, products, new CSVFormatter());
            dailyCSV.generate(fileName);

            Report dailyPDF = new DailyReport(orders, products, new PDFFormatter());
            dailyPDF.generate(fileName);

            System.out.println("Reports generated successfully for " + date);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to generate reports: " + e.getMessage());
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/ReportActionPage.fxml"));
            Parent root = loader.load();

            ReportActionController controller = loader.getController();
            controller.setReportDate(date);

            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Report Action");
            stage.show();
        }catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Report Action page: " + e.getMessage());
        }

    }


    @FXML
    public void onBackClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/ReportOptions.fxml"));
            Parent root = loader.load();

            ReportOptionController report = loader.getController();
            report.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Report Option");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Home page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
