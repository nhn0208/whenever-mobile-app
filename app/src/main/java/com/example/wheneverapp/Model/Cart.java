package com.example.wheneverapp.Model;

public class Cart {
    private String _id;
    private String customerId;
    private ProductResponse productId;
    private int quantity;

    public Cart(String _id, String customerId, ProductResponse productId, int quantity) {
        this._id = _id;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ProductResponse getProductId() {
        return productId;
    }

    public void setProductId(ProductResponse productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "_id='" + _id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", productId='" + productId.get_id() + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
