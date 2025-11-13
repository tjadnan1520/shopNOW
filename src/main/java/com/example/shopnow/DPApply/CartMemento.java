package com.example.shopnow.DPApply;

import java.util.List;
import com.example.shopnow.controller.CartItem;

public class CartMemento {
    private final List<CartItem> cartState;

    public CartMemento(List<CartItem> cartState) {
        this.cartState = cartState.stream()
                .map(item -> new CartItem(item.getProductName(), item.getQuantity(), item.getPrice()))
                .toList();
    }

    public List<CartItem> getSavedState() {
        return cartState;
    }
}