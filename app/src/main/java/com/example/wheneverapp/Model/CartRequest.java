package com.example.wheneverapp.Model;

public class CartRequest {
    private String customerId;
    private String productId;
    private int quantity;

    public CartRequest(String customerId, String productId, int quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
