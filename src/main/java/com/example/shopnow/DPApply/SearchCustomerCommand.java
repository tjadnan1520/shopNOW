package com.example.shopnow.DPApply;

import com.example.shopnow.controller.CreateSalesController;
import com.example.shopnow.controller.Customer;

public class SearchCustomerCommand implements Command {
    private final String phone;
    private final DraftManager draftManager;
    private final CreateSalesController controller;

    public SearchCustomerCommand(String phone, DraftManager draftManager, CreateSalesController controller) {
        this.phone = phone;
        this.draftManager = draftManager;
        this.controller = controller;
    }

    @Override
    public void execute() {
        Customer customer = controller.findCustomerByPhone(phone);
        if (customer != null) {
            controller.setCurrentCustomer(customer);

            if (draftManager.hasDraft(phone)) {
                controller.restoreCart(draftManager.getDraft(phone));
                controller.showAlert("Draft Restored", "Previous draft loaded for this customer.");
            } else {
                controller.clearCart();
                controller.showAlert("New Cart", "No draft found. Starting a new sale.");
            }
        } else {
            controller.showAlert("Not Found", "No customer found with this phone.");
        }
    }
}
