package com.example.shopnow.DPApply.Report;

import java.io.FileWriter;
import java.io.IOException;

public class PDFFormatter implements ReportFormatter {
    @Override
    public void generateFile(String reportContent, String fileName) {
        try (FileWriter writer = new FileWriter(fileName + ".pdf")) {
            writer.write(reportContent);
            System.out.println("PDF report generated: " + fileName + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
