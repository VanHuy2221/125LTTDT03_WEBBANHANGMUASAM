package com.example.bc_quanlibanhangonline.models;

public class ExchangeItem {
    private int itemId;
    private int exchangeId;
    private String itemName;
    private String description;
    private String image;
    private double estimatedValue;

    public ExchangeItem(int itemId, int exchangeId,
                        String itemName, String description,
                        String image, double estimatedValue) {
        this.itemId = itemId;
        this.exchangeId = exchangeId;
        this.itemName = itemName;
        this.description = description;
        this.image = image;
        this.estimatedValue = estimatedValue;
    }
}
