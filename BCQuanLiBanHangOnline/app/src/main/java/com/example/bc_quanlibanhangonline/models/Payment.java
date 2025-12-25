package com.example.bc_quanlibanhangonline.models;

public class Payment {
    private int paymentId;
    private int orderId;
    private String paymentMethod; // cash | banking | cad | exchange
    private String paymentStatus; // pending | paid | failed
    private String paymentDate;

    public Payment(int paymentId, int orderId, String paymentMethod,
                   String paymentStatus, String paymentDate) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    /* ===== Getter ===== */

    public int getPaymentId() { return paymentId; }
    public int getOrderId() { return orderId; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getPaymentDate() { return paymentDate; }
}
