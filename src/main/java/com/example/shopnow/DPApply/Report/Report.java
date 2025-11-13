package com.example.shopnow.DPApply.Report;

public abstract class Report {
    protected ReportFormatter formatter;

    public Report(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    public abstract void generate(String fileName);
}