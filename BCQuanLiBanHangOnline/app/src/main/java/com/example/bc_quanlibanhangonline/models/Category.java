package com.example.bc_quanlibanhangonline.models;

public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private int iconResource; // Resource ID cho icon

    public Category() {}

    public Category(int categoryId, String categoryName, String description, int iconResource) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.iconResource = iconResource;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }
}