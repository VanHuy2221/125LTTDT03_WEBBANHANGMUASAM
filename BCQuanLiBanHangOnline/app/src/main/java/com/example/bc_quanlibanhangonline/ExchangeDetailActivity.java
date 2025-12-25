package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;

public class ExchangeDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProductName, tvExchangeItem, tvMessage, tvStatus, tvExchangeId;
    private Button btnAccept, btnReject, btnMessage;

    private DatabaseHelper databaseHelper;
    private ExchangeRequest exchangeRequest;

    // THÊM: Lưu thông tin user
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detail);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String exchangeId = intent.getStringExtra("EXCHANGE_ID");

        // THÊM: Lấy userId từ Intent
        currentUserId = intent.getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            // Nếu không có, dùng mặc định là người bán (userId = 1)
            currentUserId = 1;
        }

        initializeViews();

        if (exchangeId != null) {
            loadExchangeDetail(exchangeId);
        } else {
            Toast.makeText(this, "Không tìm thấy yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupEventListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvProductName = findViewById(R.id.tvProductName);
        tvExchangeItem = findViewById(R.id.tvExchangeItem);
        tvMessage = findViewById(R.id.tvMessage);
        tvStatus = findViewById(R.id.tvStatus);
        tvExchangeId = findViewById(R.id.tvExchangeId);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnMessage = findViewById(R.id.btnMessage);
    }

    private void loadExchangeDetail(String exchangeId) {
        exchangeRequest = databaseHelper.getExchangeRequestById(exchangeId);

        if (exchangeRequest != null) {
            tvProductName.setText("Sản phẩm muốn: " + exchangeRequest.getProductName());
            tvExchangeItem.setText("Sản phẩm đổi: " + exchangeRequest.getExchangeItemName());
            tvMessage.setText("Nội dung: " + exchangeRequest.getMessage());
            tvStatus.setText("Trạng thái: " + exchangeRequest.getStatus());
            tvExchangeId.setText("Mã trao đổi: " + exchangeRequest.getExchangeId());

            updateButtonVisibility();
        }
    }

    private void updateButtonVisibility() {
        if (exchangeRequest != null) {
            String status = exchangeRequest.getStatus();

            switch (status) {
                case "Đang chờ phản hồi":
                    btnAccept.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
                    btnMessage.setVisibility(View.VISIBLE);
                    break;
                case "Đã chấp nhận":
                case "Đã từ chối":
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnMessage.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAccept.setOnClickListener(v -> {
            databaseHelper.updateExchangeStatus(exchangeRequest.getExchangeId(), "Đã chấp nhận");
            loadExchangeDetail(exchangeRequest.getExchangeId());
            Toast.makeText(this, "Đã chấp nhận yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
        });

        btnReject.setOnClickListener(v -> {
            databaseHelper.updateExchangeStatus(exchangeRequest.getExchangeId(), "Đã từ chối");
            loadExchangeDetail(exchangeRequest.getExchangeId());
            Toast.makeText(this, "Đã từ chối yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
        });

        btnMessage.setOnClickListener(v -> {
            // SỬA: Truyền đúng thông tin user
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra("EXCHANGE_ID", exchangeRequest.getExchangeId());

            // QUAN TRỌNG: Người bán là sender, người mua là receiver
            intent.putExtra("SENDER_ID", currentUserId); // Người bán (1)
            intent.putExtra("RECEIVER_ID", exchangeRequest.getUserId()); // Người mua từ exchange
            intent.putExtra("CHAT_TYPE", "exchange");

            startActivity(intent);
        });
    }
}