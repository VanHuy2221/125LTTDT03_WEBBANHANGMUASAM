package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvFinalTotal;
    private Button btnProcessPayment;
    private RadioGroup paymentMethodGroup;
    private RadioButton radioQR, radioCreditCard, radioCOD;

    // Sửa thành LinearLayout
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

        // Khởi tạo các layout - ĐÃ CÓ ID TRONG XML
        layoutQR = findViewById(R.id.layoutQR);
        layoutCreditCard = findViewById(R.id.layoutCreditCard);
        layoutCOD = findViewById(R.id.layoutCOD);

        // Set mặc định chọn QR
        radioQR.setChecked(true);
    }

    private void setupEventListeners() {
        // Xử lý click trên toàn bộ layout
        layoutQR.setOnClickListener(v -> radioQR.setChecked(true));
        layoutCreditCard.setOnClickListener(v -> radioCreditCard.setChecked(true));
        layoutCOD.setOnClickListener(v -> radioCOD.setChecked(true));

        btnProcessPayment.setOnClickListener(v -> {
            processPayment();
        });

        // Không cần xử lý RadioGroup change listener nữa
        // vì đã có selectableItemBackground tự động
    }

    private void calculateFinalTotal() {
        int finalTotal = totalPrice + shippingFee - discount;
        tvFinalTotal.setText(formatPrice(finalTotal));
    }

    private void processPayment() {
        int selectedMethodId = paymentMethodGroup.getCheckedRadioButtonId();

        if (selectedMethodId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedMethodId == R.id.radioQR) {
            navigateToQRPayment();
        } else if (selectedMethodId == R.id.radioCreditCard) {
            navigateToCreditCardPayment();
        } else if (selectedMethodId == R.id.radioCOD) {
            confirmCODPayment();
        }
    }

    private void navigateToQRPayment() {
        Intent intent = new Intent(this, QRPaymentActivity.class);
        intent.putExtra("FINAL_TOTAL", getFinalTotalAmount());
        startActivity(intent);
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