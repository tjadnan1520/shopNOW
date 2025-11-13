package com.example.shopnow.DPApply;

import com.example.shopnow.controller.CreateSalesController;

public class ConfirmSaleCommand implements Command {
    private final CreateSalesController controller;

    public ConfirmSaleCommand(CreateSalesController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.onConfirmSale();
    }
}

