package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentSuccessActivity extends AppCompatActivity {

    private TextView tvOrderNumber, tvOrderDate, tvOrderTotal, tvPaymentMethod, tvOrderStatus;
    private Button btnGoHome, btnViewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        initializeViews();
        setupEventListeners();
        displayOrderDetails();
    }

    private void initializeViews() {
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);

        btnGoHome = findViewById(R.id.btnGoHome);
        btnViewOrder = findViewById(R.id.btnViewOrder);
    }

    private void setupEventListeners() {
        btnGoHome.setOnClickListener(v -> {
            navigateToHome();
        });

        btnViewOrder.setOnClickListener(v -> {
            navigateToOrderDetails();
        });
    }

    private void displayOrderDetails() {
        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        int orderTotal = intent.getIntExtra("ORDER_TOTAL", 25520000);
        String paymentMethod = intent.getStringExtra("PAYMENT_METHOD");

        // Generate order number
        String orderNumber = generateOrderNumber();
        tvOrderNumber.setText(orderNumber);

        // Set current date
        String currentDate = getCurrentDate();
        tvOrderDate.setText(currentDate);

        // Set order total
        tvOrderTotal.setText(formatPrice(orderTotal));

        // Set payment method
        if (paymentMethod != null) {
            tvPaymentMethod.setText(paymentMethod);
        }

        // Status is always success in this activity
        tvOrderStatus.setText("Thành công");
        tvOrderStatus.setTextColor(getColor(android.R.color.holo_green_dark));
    }

    private String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        return "#DH" + timestamp.substring(2); // Lấy 12 số cuối
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    private void navigateToHome() {
        // Clear back stack và quay về MainActivity
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToOrderDetails() {
        // Chuyển đến màn hình chi tiết đơn hàng
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("ORDER_NUMBER", tvOrderNumber.getText().toString());
        startActivity(intent);
        finish();
    }

    //@Override
    //public void onBackPressed() {
        // Khi nhấn back, quay về trang chủ
        //navigateToHome();
    //}
}
