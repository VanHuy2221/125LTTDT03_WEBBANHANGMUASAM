package com.example.bc_quanlibanhangonline.models;

public class ExchangeRequest {
    private String exchangeId;
    private String productName;
    private String exchangeItemName;
    private String message;
    private String status;
    private int userId; // THÊM: userId để biết ai gửi

    // Constructor mới
    public ExchangeRequest(String exchangeId,
                           String productName,
                           String exchangeItemName,
                           String message,
                           String status,
                           int userId) {
        this.exchangeId = exchangeId;
        this.productName = productName;
        this.exchangeItemName = exchangeItemName;
        this.message = message;
        this.status = status;
        this.userId = userId;
    }

    // Constructor cũ (giữ để không break code)
    public ExchangeRequest(String exchangeId,
                           String productName,
                           String exchangeItemName,
                           String message,
                           String status) {
        this(exchangeId, productName, exchangeItemName, message, status, -1);
    }

    public String getExchangeId() { return exchangeId; }
    public String getProductName() { return productName; }
    public String getExchangeItemName() { return exchangeItemName; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public int getUserId() { return userId; } // THÊM getter
}