package com.example.shopnow.DPApply;

import com.example.shopnow.controller.Product;

public interface Observer {
    void update(Product product, String message);
}
