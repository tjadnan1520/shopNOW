package com.example.shopnow.DPApply.Report;

public class CSVReportAction implements ReportActionStrategy {
    @Override
    public void execute(String fileName) {
        System.out.println("Download CSV report: " + fileName);
    }
}
