package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class QRPaymentActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnCancelPayment, btnPaymentDone;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 15 * 60 * 1000; // 15 phút

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_payment);

        initializeViews();
        setupEventListeners();
        startTimer();
    }

    private void initializeViews() {
        tvTimer = findViewById(R.id.tvTimer);
        btnCancelPayment = findViewById(R.id.btnCancelPayment);
        btnPaymentDone = findViewById(R.id.btnPaymentDone);
    }

    private void setupEventListeners() {
        btnCancelPayment.setOnClickListener(v -> {
            cancelPayment();
        });

        btnPaymentDone.setOnClickListener(v -> {
            confirmPayment();
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(QRPaymentActivity.this, "Thời gian thanh toán đã hết", Toast.LENGTH_LONG).show();
                finish();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);
    }

    private void cancelPayment() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Toast.makeText(this, "Đã hủy thanh toán", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmPayment() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Giả lập xử lý thanh toán thành công
        simulatePaymentProcessing();
    }

    private void simulatePaymentProcessing() {
        // Hiển thị loading hoặc processing
        Toast.makeText(this, "Đang xử lý thanh toán...", Toast.LENGTH_SHORT).show();

        // Giả lập delay xử lý
        new android.os.Handler().postDelayed(
                () -> {
                    navigateToPaymentSuccess();
                }, 2000
        );
    }

    private void navigateToPaymentSuccess() {
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra("ORDER_TOTAL", getIntent().getIntExtra("FINAL_TOTAL", 25520000));
        intent.putExtra("PAYMENT_METHOD", "QR Code");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
