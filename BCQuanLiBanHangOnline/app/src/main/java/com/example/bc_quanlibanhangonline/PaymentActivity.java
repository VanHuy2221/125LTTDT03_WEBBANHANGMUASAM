package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvFinalTotal;
    private Button btnProcessPayment;
    private RadioGroup paymentMethodGroup;
    private RadioButton radioQR, radioCreditCard, radioCOD;
    private ImageView btnBack;

    private LinearLayout layoutQR, layoutCreditCard, layoutCOD;

    private int quantity;
    private int totalPrice;
    private int shippingFee = 30000;
    private int discount = 500000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        quantity = intent.getIntExtra("QUANTITY", 1);
        totalPrice = intent.getIntExtra("TOTAL_PRICE", 25990000);

        initializeViews();
        setupEventListeners();
        calculateFinalTotal();
    }

    private void initializeViews() {
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        btnProcessPayment = findViewById(R.id.btnProcessPayment);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        radioQR = findViewById(R.id.radioQR);
        radioCreditCard = findViewById(R.id.radioCreditCard);
        radioCOD = findViewById(R.id.radioCOD);

        // Nút back
        btnBack = findViewById(R.id.btnBack);

        // Các layout
        layoutQR = findViewById(R.id.layoutQR);
        layoutCreditCard = findViewById(R.id.layoutCreditCard);
        layoutCOD = findViewById(R.id.layoutCOD);

        // Set mặc định chọn QR
        radioQR.setChecked(true);
    }

    private void setupEventListeners() {
        // NÚT BACK - QUAY VỀ PRODUCT CONFIG
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity hiện tại, quay về ProductConfig
                }
            });
        }

        // Xử lý click trên toàn bộ layout
        layoutQR.setOnClickListener(v -> radioQR.setChecked(true));
        layoutCreditCard.setOnClickListener(v -> radioCreditCard.setChecked(true));
        layoutCOD.setOnClickListener(v -> radioCOD.setChecked(true));

        btnProcessPayment.setOnClickListener(v -> {
            processPayment();
        });
    }

    private void calculateFinalTotal() {
        int finalTotal = totalPrice + shippingFee - discount;
        tvFinalTotal.setText(formatPrice(finalTotal));
    }

    private void processPayment() {
        // SỬA Ở ĐÂY: LUÔN CHUYỂN SANG QR PAYMENT KHÔNG CẦN KIỂM TRA
        navigateToQRPayment();
    }

    private void navigateToQRPayment() {
        try {
            // THÊM CODE NÀY: Chuyển sang QRPaymentActivity
            Intent intent = new Intent(PaymentActivity.this, QRPaymentActivity.class);
            intent.putExtra("FINAL_TOTAL", getFinalTotalAmount());

            // Thêm các thông tin cần thiết khác nếu có
            intent.putExtra("QUANTITY", quantity);
            intent.putExtra("TOTAL_PRICE", totalPrice);

            startActivity(intent);

            // THÊM TOAST ĐỂ XÁC NHẬN
            Toast.makeText(this, "Đang chuyển đến thanh toán QR...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void navigateToCreditCardPayment() {
        Toast.makeText(this, "Chức năng thanh toán thẻ đang phát triển", Toast.LENGTH_SHORT).show();
    }

    private void confirmCODPayment() {
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra("ORDER_TOTAL", getFinalTotalAmount());
        intent.putExtra("PAYMENT_METHOD", "COD");
        startActivity(intent);
        finish();
    }

    private int getFinalTotalAmount() {
        return totalPrice + shippingFee - discount;
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }
}