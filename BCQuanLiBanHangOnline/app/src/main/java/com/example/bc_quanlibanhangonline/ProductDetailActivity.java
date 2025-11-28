package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private Button btnAddToCart, btnBuyNow;
    private TextView productName, productPrice, productDescription;
    private ImageView productImage, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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
        if (intent != null && intent.hasExtra("PRODUCT_NAME")) {
            String name = intent.getStringExtra("PRODUCT_NAME");
            int price = intent.getIntExtra("PRODUCT_PRICE", 0);
            String description = intent.getStringExtra("PRODUCT_DESCRIPTION");
            int imageRes = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.iphone_14_pro_max);

            productName.setText(name);
            productPrice.setText(formatPrice(price));
            productDescription.setText(description);
            productImage.setImageResource(imageRes);
        }
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
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại
                }
            });
        }

        btnAddToCart.setOnClickListener(v -> addToCart());
        btnBuyNow.setOnClickListener(v -> navigateToProductConfig());
    }

    private void addToCart() {
        String productNameText = productName.getText().toString();
        Toast.makeText(this, "Đã thêm " + productNameText + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void navigateToProductConfig() {
        // Lấy dữ liệu từ Intent gốc (sản phẩm thực tế người dùng bấm vào)
        Intent originalIntent = getIntent();

        String productNameText = originalIntent.getStringExtra("PRODUCT_NAME");
        int productPriceValue = originalIntent.getIntExtra("PRODUCT_PRICE", 0);
        String productDescriptionText = originalIntent.getStringExtra("PRODUCT_DESCRIPTION");
        int productImageRes = originalIntent.getIntExtra("PRODUCT_IMAGE", R.drawable.iphone_14_pro_max);

        // Chuyển sang ProductConfigActivity với đúng sản phẩm
        Intent configIntent = new Intent(this, ProductConfigActivity.class);
        configIntent.putExtra("PRODUCT_NAME", productNameText);
        configIntent.putExtra("PRODUCT_PRICE", productPriceValue);
        configIntent.putExtra("PRODUCT_DESCRIPTION", productDescriptionText);
        configIntent.putExtra("PRODUCT_IMAGE", productImageRes);
        startActivity(configIntent);
    }
}