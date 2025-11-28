package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class ProductConfigActivity extends AppCompatActivity {

    private TextView tvTotalPrice, tvQuantity;
    private ImageView productImage;
    private Button btnDecrease, btnIncrease, btnConfirmPurchase;
    private MaterialButtonToggleGroup storageGroup;

    private int basePrice = 0;
    private int quantity = 1;
    private String productName = "";
    private int productImageRes = R.drawable.iphone_14_pro_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_config);

        initializeViews();
        loadProductData();
        setupEventListeners();
        updateTotalPrice();
    }

    private void initializeViews() {
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        productImage = findViewById(R.id.productImage);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnConfirmPurchase = findViewById(R.id.btnConfirmPurchase);
        storageGroup = findViewById(R.id.storageGroup);
    }

    private void loadProductData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_NAME")) {
            productName = intent.getStringExtra("PRODUCT_NAME");
            basePrice = intent.getIntExtra("PRODUCT_PRICE", 0);
            String description = intent.getStringExtra("PRODUCT_DESCRIPTION");
            productImageRes = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.iphone_14_pro_max);

            // Cập nhật tên sản phẩm
            TextView productNameView = findViewById(R.id.productName);
            if (productNameView != null) {
                productNameView.setText(productName);
            }

            // Cập nhật giá gốc
            TextView productPriceView = findViewById(R.id.productPrice);
            if (productPriceView != null) {
                productPriceView.setText(formatPrice(basePrice));
            }

            // Cập nhật hình ảnh sản phẩm
            if (productImage != null) {
                productImage.setImageResource(productImageRes);
            }

            // Cập nhật tổng tiền ban đầu
            updateTotalPrice();
        }
    }

    private void setupEventListeners() {
        // Nút giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Nút tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        // Xử lý chọn dung lượng
        if (storageGroup != null) {
            storageGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (isChecked) {
                    updateTotalPrice();
                }
            });
        }

        // Nút xác nhận mua hàng
        btnConfirmPurchase.setOnClickListener(v -> confirmPurchase());
    }

    private void updateTotalPrice() {
        int totalPrice = basePrice * quantity;

        // Tính thêm phí nếu chọn dung lượng cao hơn
        if (storageGroup != null) {
            int checkedButtonId = storageGroup.getCheckedButtonId();
            if (checkedButtonId == R.id.storage256) {
                totalPrice += 3000000; // +3 triệu cho 256GB
            } else if (checkedButtonId == R.id.storage512) {
                totalPrice += 6000000; // +6 triệu cho 512GB
            }
        }

        if (tvTotalPrice != null) {
            tvTotalPrice.setText(formatPrice(totalPrice));
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    private void confirmPurchase() {
        int finalPrice = basePrice * quantity;

        // Tính thêm phí dung lượng cuối cùng
        if (storageGroup != null) {
            int checkedButtonId = storageGroup.getCheckedButtonId();
            if (checkedButtonId == R.id.storage256) {
                finalPrice += 3000000;
            } else if (checkedButtonId == R.id.storage512) {
                finalPrice += 6000000;
            }
        }

        Toast.makeText(this,
                "Đã xác nhận mua " + productName +
                        "\nSố lượng: " + quantity +
                        "\nTổng tiền: " + formatPrice(finalPrice),
                Toast.LENGTH_LONG).show();
    }
}