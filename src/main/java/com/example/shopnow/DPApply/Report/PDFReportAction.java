package com.example.shopnow.DPApply.Report;

public class PDFReportAction implements ReportActionStrategy {
    @Override
    public void execute(String fileName) {
        System.out.println("Download PDF report: " + fileName);
    }
}