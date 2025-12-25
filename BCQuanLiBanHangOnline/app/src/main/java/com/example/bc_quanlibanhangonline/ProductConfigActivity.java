package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class ProductConfigActivity extends AppCompatActivity {

    private TextView tvTotalPrice, tvQuantity;
    private ImageView productImage, btnBack;
    private Button btnDecrease, btnIncrease, btnConfirmPurchase;
    private MaterialButtonToggleGroup storageGroup;

    private String productName;
    private int basePrice;
    private int quantity = 1;
    private int productImageRes;

    private int finalTotalPrice;

    private int userId = -1; // Thêm biến này

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

        // THÊM NÚT BACK
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadProductData() {
        Intent intent = getIntent();
        if (intent == null) return;

        productName = intent.getStringExtra("PRODUCT_NAME");
        basePrice = intent.getIntExtra("PRODUCT_PRICE", 0);
        productImageRes = intent.getIntExtra(
                "PRODUCT_IMAGE",
                R.drawable.iphone_14_pro_max
        );

        userId = intent.getIntExtra("USER_ID", -1);

        TextView productNameView = findViewById(R.id.productName);
        TextView productPriceView = findViewById(R.id.productPrice);

        productNameView.setText(productName);
        productPriceView.setText(formatPrice(basePrice));
        productImage.setImageResource(productImageRes);

        tvQuantity.setText(String.valueOf(quantity));
        updateTotalPrice();
    }


    private void setupEventListeners() {
        // NÚT BACK - QUAY VỀ PRODUCT DETAIL
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity hiện tại, quay về ProductDetail
                }
            });
        }

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

        // Nút xác nhận mua hàng - CHUYỂN SANG PAYMENT ACTIVITY
        btnConfirmPurchase.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Vui lòng đăng nhập để mua sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            navigateToPayment();
        });
    }

    private void updateTotalPrice() {
        finalTotalPrice = basePrice * quantity;

        if (storageGroup != null) {
            int checkedButtonId = storageGroup.getCheckedButtonId();
            if (checkedButtonId == R.id.storage256) {
                finalTotalPrice += 3000000;
            } else if (checkedButtonId == R.id.storage512) {
                finalTotalPrice += 6000000;
            }
        }

        tvTotalPrice.setText(formatPrice(finalTotalPrice));
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    private void navigateToPayment() {

        Intent paymentIntent = new Intent(this, PaymentActivity.class);

        paymentIntent.putExtra("PRODUCT_NAME", productName);
        paymentIntent.putExtra("QUANTITY", quantity);
        paymentIntent.putExtra("TOTAL_PRICE", finalTotalPrice);
        paymentIntent.putExtra("USER_ID", userId);
        startActivity(paymentIntent);
    }
}