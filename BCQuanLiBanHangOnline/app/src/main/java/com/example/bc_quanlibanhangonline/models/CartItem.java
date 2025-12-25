package com.example.bc_quanlibanhangonline.models;

public class CartItem {
    private int cartId;
    private int userId;
    private Product product;
    private int quantity;

    public CartItem(int cartId, int userId, Product product, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
}
