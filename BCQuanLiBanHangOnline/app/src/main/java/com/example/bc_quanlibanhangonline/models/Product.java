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
    private String imagePath;

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

    public Product(int sellerId, int categoryId, String productName, String brand,
                   double price, String description, int imageResource,
                   int quantity, String status, float rating) {
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

    // --- Getters và Setters ---
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    // --- Phương thức tiện ích ---
    public String getFormattedPrice() {
        return String.format("%,.0fđ", price); // định dạng giá tiền: 1,000,000đ
    }

    @Override
    public String toString() {
        return productName + " - " + getFormattedPrice();
    }

    // Thêm vào Product.java
    // Thêm vào Product.java
    public String getCategory() {
        return getCategoryNameById(this.categoryId);
    }

    private String getCategoryNameById(int id) {
        switch (id) {
            case 1: return "Điện thoại";
            case 2: return "Laptop";
            case 3: return "Tablet";
            case 4: return "Phụ kiện điện tử";
            case 5: return "Thời trang nam";
            case 6: return "Thời trang nữ";
            case 7: return "Giày dép";
            case 8: return "Túi xách";
            case 9: return "Đồng hồ";
            case 10: return "Mỹ phẩm";
            case 11: return "Nội thất";
            case 12: return "Đồ gia dụng";
            case 13: return "Thiết bị nhà bếp";
            case 14: return "Sách";
            case 15: return "Thể thao";
            case 16: return "Đồ chơi";
            case 17: return "Mẹ và bé";
            case 18: return "Ô tô - Xe máy";
            case 19: return "Khác";
            default: return "Không xác định";
        }
    }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}