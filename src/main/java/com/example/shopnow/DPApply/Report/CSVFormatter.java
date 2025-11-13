package com.example.shopnow.DPApply.Report;

import java.io.FileWriter;
import java.io.IOException;

// CSV Formatter
public class CSVFormatter implements ReportFormatter {
    @Override
    public void generateFile(String reportContent, String fileName) {
        try (FileWriter writer = new FileWriter(fileName + ".csv")) {
            writer.write(reportContent);
            System.out.println("CSV report generated: " + fileName + ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

