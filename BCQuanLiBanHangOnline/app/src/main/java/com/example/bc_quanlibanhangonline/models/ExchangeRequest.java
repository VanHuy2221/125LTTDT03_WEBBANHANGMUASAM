package com.example.bc_quanlibanhangonline.models;

public class ExchangeRequest {
    private String exchangeId;
    private String productName;
    private String exchangeItemName;
    private String message;
    private String status;

    public ExchangeRequest(String exchangeId,
                           String productName,
                           String exchangeItemName,
                           String message,
                           String status) {
        this.exchangeId = exchangeId;
        this.productName = productName;
        this.exchangeItemName = exchangeItemName;
        this.message = message;
        this.status = status;
    }

    public String getExchangeId() { return exchangeId; }
    public String getProductName() { return productName; }
    public String getExchangeItemName() { return exchangeItemName; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
}
