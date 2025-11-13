package com.example.shopnow.DPApply.Report;

public class ReportContext {
    private ReportActionStrategy strategy;

    public void setStrategy(ReportActionStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy(String fileName) {
        if (strategy != null) {
            strategy.execute(fileName);
        }
    }
}

