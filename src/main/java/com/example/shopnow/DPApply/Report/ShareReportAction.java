package com.example.shopnow.DPApply.Report;

public class ShareReportAction implements ReportActionStrategy {
    @Override
    public void execute(String fileName) {
        System.out.println("Sharing report: " + fileName);
    }
}