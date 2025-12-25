package com.example.bc_quanlibanhangonline.models;

public class Order {

    private int orderId;          // orders.order_id
    private int userId;           // orders.user_id
    private Integer exchangeId;   // orders.exchange_id (nullable)

    private int totalPrice;       // orders.total_price
    private String orderType;     // normal | exchange
    private String paymentMethod; // cash | banking | cad | exchange
    private String status;        // processing | shipping | completed | cancelled
    private String orderDate;     // display

    // Constructor chuẩn để tạo Order
    public Order(int orderId,
                 int userId,
                 Integer exchangeId,
                 int totalPrice,
                 String orderType,
                 String paymentMethod,
                 String status,
                 String orderDate) {

        this.orderId = orderId;
        this.userId = userId;
        this.exchangeId = exchangeId;
        this.totalPrice = totalPrice;
        this.orderType = orderType;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.orderDate = orderDate;
    }

    /* ===== Getter ===== */

    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public Integer getExchangeId() { return exchangeId; }
    public int getTotalPrice() { return totalPrice; }
    public String getOrderType() { return orderType; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getOrderDate() { return orderDate; }
    public void setStatus(String status) {
        this.status = status;
    }
}
