package com.example.shopnow.DPApply;

import com.example.shopnow.controller.Product;

public interface Subject {
    void attach(Observer observer);
    void notifyObservers(Product product, String message);
}
