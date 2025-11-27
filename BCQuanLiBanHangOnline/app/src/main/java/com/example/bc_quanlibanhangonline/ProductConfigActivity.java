package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductConfigActivity extends AppCompatActivity {

    private TextView tvQuantity, tvTotalPrice;
    private Button btnDecrease, btnIncrease, btnConfirmPurchase;
    private RadioGroup colorGroup;
    private com.google.android.material.button.MaterialButtonToggleGroup storageGroup;
    private int quantity = 1;
    private int basePrice = 25990000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_config);

        initializeViews();
        setupEventListeners();
        updateTotalPrice();
    }

    private void initializeViews() {
        tvQuantity = findViewById(R.id.tvQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnConfirmPurchase = findViewById(R.id.btnConfirmPurchase);

        // Khởi tạo color group và storage group
        colorGroup = findViewById(R.id.colorGroup);
        storageGroup = findViewById(R.id.storageGroup);
    }

    private void setupEventListeners() {
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantity();
                updateTotalPrice();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            updateQuantity();
            updateTotalPrice();
        });

        btnConfirmPurchase.setOnClickListener(v -> {
            navigateToPayment();
        });

        // Xử lý khi chọn dung lượng (MaterialButtonToggleGroup)
        storageGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                // Cái được chọn
                String storage = "";
                if (checkedId == R.id.storage128) {
                    storage = "128GB";
                } else if (checkedId == R.id.storage256) {
                    storage = "256GB";
                } else if (checkedId == R.id.storage512) {
                    storage = "512GB";
                }
                // Xử lý logic với storage được chọn
            } else {
                // Cái bị bỏ chọn (không cần xử lý vì đã có cái mới được chọn)
            }
        });
    }

    private void updateQuantity() {
        tvQuantity.setText(String.valueOf(quantity));
    }

    private void updateTotalPrice() {
        int total = basePrice * quantity;
        tvTotalPrice.setText(formatPrice(total));
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    private void navigateToPayment() {
        // Lấy màu sắc được chọn
        int selectedColorId = colorGroup.getCheckedRadioButtonId();
        String selectedColor = "Đen"; // mặc định
        if (selectedColorId == R.id.colorWhite) {
            selectedColor = "Trắng";
        } else if (selectedColorId == R.id.colorGold) {
            selectedColor = "Vàng";
        }

        // Lấy dung lượng được chọn
        int selectedStorageId = storageGroup.getCheckedButtonId();
        String selectedStorage = "128GB"; // mặc định
        if (selectedStorageId == R.id.storage256) {
            selectedStorage = "256GB";
        } else if (selectedStorageId == R.id.storage512) {
            selectedStorage = "512GB";
        }

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("TOTAL_PRICE", basePrice * quantity);
        intent.putExtra("SELECTED_COLOR", selectedColor);
        intent.putExtra("SELECTED_STORAGE", selectedStorage);
        startActivity(intent);
    }
}