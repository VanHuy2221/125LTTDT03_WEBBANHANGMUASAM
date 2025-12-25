package com.example.bc_quanlibanhangonline.models;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role; // "customer", "seller", "admin"
    private String status; // "active", "locked"
    private String createdAt;

    // Constructor 1: Đầy đủ tham số
    public User(int userId, String fullName, String email, String password,
                String phone, String address, String role, String status, String createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor 2: Cho đăng ký (không có ID, status, createdAt)
    public User(String fullName, String email, String password,
                String phone, String address, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = "active"; // Mặc định
        this.createdAt = ""; // Sẽ được set khi đăng ký
    }

    // Constructor 3: Rỗng
    public User() {}

    // ============ GETTERS ============
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }

    // ============ SETTERS ============
    public void setUserId(int userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(String role) { this.role = role; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // ============ HELPER METHODS ============
    public boolean isAdmin() { return "admin".equals(role); }
    public boolean isSeller() { return "seller".equals(role); }
    public boolean isCustomer() { return "customer".equals(role); }
    public boolean isActive() { return "active".equals(status); }
    public boolean isLocked() { return "locked".equals(status); }

    @Override
    public String toString() {
        return fullName + " (" + email + ")";
    }
}