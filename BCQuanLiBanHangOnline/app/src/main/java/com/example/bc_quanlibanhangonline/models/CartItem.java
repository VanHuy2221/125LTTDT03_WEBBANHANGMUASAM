package com.example.bc_quanlibanhangonline.models;

public class CartItem {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;

    public CartItem(int cartId, int userId, int productId, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }
}
