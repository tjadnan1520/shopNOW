package com.example.shopnow.DPApply;

import com.example.shopnow.controller.Product;
import java.util.ArrayList;
import java.util.List;

public class ReminderSubject implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }


    @Override
    public void notifyObservers(Product product, String message) {
        for (Observer observer : observers) {
            observer.update(product, message);
        }
    }
}
