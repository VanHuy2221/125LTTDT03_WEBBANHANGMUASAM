package com.example.bc_quanlibanhangonline.models;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;
    private String avatar;
    private String createdAt;

    public User(int userId, String fullName, String email, String password,
                String phone, String address, String role,
                String avatar, String createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.avatar = avatar;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
}
