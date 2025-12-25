package com.example.bc_quanlibanhangonline.models;

public class OrderDetail {

    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private int price;

    public OrderDetail(int orderDetailId,
                       int orderId,
                       int productId,
                       int quantity,
                       int price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
}
