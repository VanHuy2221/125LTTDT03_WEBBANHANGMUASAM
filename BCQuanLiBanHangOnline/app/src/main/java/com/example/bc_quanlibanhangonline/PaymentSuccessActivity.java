package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
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

        // THÊM CODE XỬ LÝ BACK GESTURE MỚI
        setupBackPressedHandler();
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
            navigateToOrderTracking();
        });
    }

    private void displayOrderDetails() {
        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();

        String orderId = intent.getStringExtra("ORDER_ID");
        String orderDate = intent.getStringExtra("ORDER_DATE");
        int orderTotal = intent.getIntExtra("ORDER_TOTAL", 0);
        String paymentMethod = intent.getStringExtra("PAYMENT_METHOD");

        tvOrderNumber.setText(orderId);
        tvOrderDate.setText(orderDate);
        tvOrderTotal.setText(formatPrice(orderTotal));
        tvPaymentMethod.setText(paymentMethod);
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
        // Chuyển về HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToOrderTracking() {
        // Chuyển sang OrderTrackingActivity và truyền thông tin đơn hàng
        Intent intent = new Intent(this, OrderTrackingActivity.class);

        // Truyền dữ liệu đơn hàng sang OrderTrackingActivity
        intent.putExtra("ORDER_NUMBER", tvOrderNumber.getText().toString());
        intent.putExtra("ORDER_DATE", tvOrderDate.getText().toString());
        intent.putExtra("ORDER_TOTAL", tvOrderTotal.getText().toString());
        intent.putExtra("PAYMENT_METHOD", tvPaymentMethod.getText().toString());
        intent.putExtra("ORDER_STATUS", tvOrderStatus.getText().toString());

        // Clear back stack và start activity mới
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // PHƯƠNG THỨC MỚI: Xử lý back gesture với OnBackPressedDispatcher
    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Khi nhấn back (cả nút vật lý lẫn gesture), quay về trang chủ
                navigateToHome();
            }
        });
    }
}