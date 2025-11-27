package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private Button btnAddToCart, btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews();
        setupEventListeners();
    }

    private void initializeViews() {
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    private void setupEventListeners() {
        btnAddToCart.setOnClickListener(v -> addToCart());

        btnBuyNow.setOnClickListener(v -> navigateToProductConfig());
    }

    private void addToCart() {
        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        // Implement add to cart logic here
    }

    private void navigateToProductConfig() {
        Intent intent = new Intent(this, ProductConfigActivity.class);
        intent.putExtra("PRODUCT_NAME", "iPhone 14 Pro Max 128GB");
        intent.putExtra("PRODUCT_PRICE", 25990000);
        startActivity(intent);
    }
}