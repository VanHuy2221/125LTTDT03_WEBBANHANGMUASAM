package com.example.bc_quanlibanhangonline.models;

public class Product {
    private int productId;
    private int sellerId;
    private int categoryId;
    private String productName;
    private String brand;
    private double price;
    private String description;
    private int imageResource; // Resource ID cho hình ảnh
    private int quantity;
    private String status;
    private float rating;

    public Product() {}

    public Product(int productId, int sellerId, int categoryId, String productName, 
                   String brand, double price, String description, int imageResource, int quantity, 
                   String status, float rating) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.imageResource = imageResource;
        this.quantity = quantity;
        this.status = status;
        this.rating = rating;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // Phương thức format giá tiền
    public String getFormattedPrice() {
        return String.format("%,.0fđ", price);
    }
}