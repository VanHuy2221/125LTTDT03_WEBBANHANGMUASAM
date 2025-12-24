package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvFinalTotal;
    private Button btnProcessPayment;
    private ImageView btnBack;

    // Main method
    private RadioGroup mainMethodGroup;
    private RadioButton radioBuyMoney, radioExchange;

    // Payment methods
    private RadioGroup paymentMethodGroup;
    private RadioButton radioQR, radioCreditCard, radioCOD;

    private int quantity;
    private int totalPrice;
    private final int shippingFee = 30000;
    private final int discount = 500000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        quantity = intent.getIntExtra("QUANTITY", 1);
        totalPrice = intent.getIntExtra("TOTAL_PRICE", 25990000);

        initViews();
        setupEvents();
        calculateFinalTotal();
    }

    private void initViews() {
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        btnProcessPayment = findViewById(R.id.btnProcessPayment);
        btnBack = findViewById(R.id.btnBack);

        mainMethodGroup = findViewById(R.id.mainMethodGroup);
        radioBuyMoney = findViewById(R.id.radioBuyMoney);
        radioExchange = findViewById(R.id.radioExchange);

        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        radioQR = findViewById(R.id.radioQR);
        radioCreditCard = findViewById(R.id.radioCreditCard);
        radioCOD = findViewById(R.id.radioCOD);

        // mặc định
        radioBuyMoney.setChecked(true);
        radioQR.setChecked(true);
        paymentMethodGroup.setVisibility(View.VISIBLE);
    }

    private void setupEvents() {

        btnBack.setOnClickListener(v -> finish());

        // 🔹 Chọn Mua bằng tiền / Trao đổi
        mainMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBuyMoney) {
                paymentMethodGroup.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radioExchange) {
                paymentMethodGroup.setVisibility(View.GONE);
                Toast.makeText(
                        this,
                        "Chức năng trao đổi sản phẩm đang phát triển",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // 🔹 TỰ QUẢN LÝ chọn 1 trong 3 thanh toán
        radioQR.setOnClickListener(v -> selectPayment(radioQR));
        radioCreditCard.setOnClickListener(v -> selectPayment(radioCreditCard));
        radioCOD.setOnClickListener(v -> selectPayment(radioCOD));

        btnProcessPayment.setOnClickListener(v -> processPayment());
    }

    // 🔥 ĐẢM BẢO CHỈ 1 RADIO ĐƯỢC CHỌN
    private void selectPayment(RadioButton selected) {
        radioQR.setChecked(false);
        radioCreditCard.setChecked(false);
        radioCOD.setChecked(false);
        selected.setChecked(true);
    }

    private void processPayment() {

        if (radioExchange.isChecked()) {
            Toast.makeText(
                    this,
                    "Chuyển sang luồng trao đổi sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (radioQR.isChecked()) {
            navigateToQRPayment();
        } else if (radioCreditCard.isChecked()) {
            Toast.makeText(
                    this,
                    "Thanh toán thẻ đang phát triển",
                    Toast.LENGTH_SHORT
            ).show();
        } else if (radioCOD.isChecked()) {
            confirmCODPayment();
        } else {
            Toast.makeText(
                    this,
                    "Vui lòng chọn phương thức thanh toán",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void calculateFinalTotal() {
        int finalTotal = totalPrice + shippingFee - discount;
        tvFinalTotal.setText(formatPrice(finalTotal));
    }

    private void navigateToQRPayment() {
        Intent intent = new Intent(this, QRPaymentActivity.class);
        intent.putExtra("FINAL_TOTAL", getFinalTotalAmount());
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("TOTAL_PRICE", totalPrice);
        startActivity(intent);
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
