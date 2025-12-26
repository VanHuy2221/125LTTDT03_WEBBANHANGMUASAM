package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;

public class ProductDetailActivity extends AppCompatActivity {

    private Button btnAddToCart, btnBuyNow;
    private TextView productName, productPrice, productDescription;
    private ImageView productImage, btnBack;

    private String currentProductName;
    private int currentProductPrice;
    private String currentProductDescription;
    private int currentProductImage;

    DatabaseHelper db;
    private int productId;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        int productId = intent.getIntExtra("PRODUCT_ID", -1);

        Log.d("PRODUCT_DETAIL", "productId = " + productId);
        db = new DatabaseHelper(this);
        initializeViews();
        loadProductData();
        setupEventListeners();
    }

    private void initializeViews() {
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productImage = findViewById(R.id.productImage);

        // THÊM NÚT BACK
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadProductData() {
        Intent intent = getIntent();
        if (intent == null) return;

        currentProductName = intent.getStringExtra("PRODUCT_NAME");
        currentProductPrice = intent.getIntExtra("PRODUCT_PRICE", 0);
        currentProductDescription = intent.getStringExtra("PRODUCT_DESCRIPTION");
        currentProductImage = intent.getIntExtra(
                "PRODUCT_IMAGE",
                R.drawable.iphone_14_pro_max
        );
        productId = intent.getIntExtra("PRODUCT_ID", -1);
        userId = intent.getIntExtra("USER_ID", -1);

        productName.setText(currentProductName);
        productPrice.setText(formatPrice(currentProductPrice));
        productDescription.setText(currentProductDescription);
        productImage.setImageResource(currentProductImage);
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    private void setupEventListeners() {
        // NÚT BACK - QUAY VỀ HOME ACTIVITY
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Tạo Intent quay về HomeActivity
                    Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại
                }
            });
        }

        btnAddToCart.setOnClickListener(v -> {
            Log.d("PRODUCT_DETAIL", "Add to cart productId = " + productId);

            if (productId == -1) {
                Toast.makeText(this, "Lỗi sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            db.addToCart(userId, productId);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
        btnBuyNow.setOnClickListener(v -> navigateToProductConfig());
    }

    private void addToCart() {
        if (userId == -1) {
            Toast.makeText(this,
                    "Vui lòng đăng nhập",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        db.addToCart(userId, productId); // ✅ GỌI DATABASEHELPER

        Toast.makeText(this,
                "Đã thêm vào giỏ hàng",
                Toast.LENGTH_SHORT).show();
    }

    private void navigateToProductConfig() {
        Intent configIntent = new Intent(this, ProductConfigActivity.class);

        configIntent.putExtra("PRODUCT_NAME", currentProductName);
        configIntent.putExtra("PRODUCT_PRICE", currentProductPrice);
        configIntent.putExtra("PRODUCT_DESCRIPTION", currentProductDescription);
        configIntent.putExtra("PRODUCT_IMAGE", currentProductImage);
        configIntent.putExtra("USER_ID", userId);
        configIntent.putExtra("PRODUCT_ID", productId);
        startActivity(configIntent);
    }
}