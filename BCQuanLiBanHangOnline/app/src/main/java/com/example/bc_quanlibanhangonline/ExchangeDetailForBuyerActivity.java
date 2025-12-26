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

public class ExchangeDetailForBuyerActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProductName, tvExchangeItem, tvMessage, tvStatus, tvExchangeId;
    private Button btnCancel, btnMessage;

    private DatabaseHelper databaseHelper;
    private ExchangeRequest exchangeRequest;

    // THÊM: Lưu thông tin user
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detail_for_buyer);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String exchangeId = intent.getStringExtra("EXCHANGE_ID");

        // THÊM: Lấy userId từ Intent
        currentUserId = intent.getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            // Nếu không có, dùng mặc định là người mua (userId = 3)
            currentUserId = 3;
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
        btnCancel = findViewById(R.id.btnCancel);
        btnMessage = findViewById(R.id.btnMessage);
    }

    private void loadExchangeDetail(String exchangeId) {
        exchangeRequest = databaseHelper.getExchangeRequestById(exchangeId);

        if (exchangeRequest != null) {
            tvProductName.setText("Muốn: " + exchangeRequest.getProductName());
            tvExchangeItem.setText("Đổi: " + exchangeRequest.getExchangeItemName());
            tvMessage.setText("Nội dung: " + exchangeRequest.getMessage());
            tvStatus.setText(getStatusText(exchangeRequest.getStatus()));
            tvExchangeId.setText("Mã: " + exchangeRequest.getExchangeId());

            updateButtonVisibility();
        }
    }

    private String getStatusText(String status) {
        if (status == null) return "Đang xử lý";

        switch (status) {
            case "Đang chờ phản hồi":
                return "⏳ " + status;
            case "Đã chấp nhận":
                return "✅ " + status;
            case "Đã từ chối":
                return "❌ " + status;
            default:
                return status;
        }
    }

    private void updateButtonVisibility() {
        if (exchangeRequest != null) {
            String status = exchangeRequest.getStatus();

            // Người mua chỉ có thể hủy khi đang chờ phản hồi
            if ("Đang chờ phản hồi".equals(status)) {
                btnCancel.setVisibility(View.VISIBLE);
            } else {
                btnCancel.setVisibility(View.GONE);
            }

            // Luôn có nút nhắn tin
            btnMessage.setVisibility(View.VISIBLE);
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Người mua chỉ có thể hủy (không có chấp nhận/từ chối)
        btnCancel.setOnClickListener(v -> {
            if (exchangeRequest != null && "Đang chờ phản hồi".equals(exchangeRequest.getStatus())) {
                databaseHelper.updateExchangeStatus(exchangeRequest.getExchangeId(), "Đã từ chối");
                loadExchangeDetail(exchangeRequest.getExchangeId());
                Toast.makeText(this, "✅ Đã hủy yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không thể hủy yêu cầu này", Toast.LENGTH_SHORT).show();
            }
        });

        btnMessage.setOnClickListener(v -> {
            // SỬA: Truyền đúng thông tin user
            if (exchangeRequest != null) {
                Intent intent = new Intent(this, ChatDetailActivity.class);
                intent.putExtra("EXCHANGE_ID", exchangeRequest.getExchangeId());

                // QUAN TRỌNG: Người mua là sender, người bán là receiver
                intent.putExtra("SENDER_ID", currentUserId); // Người mua (3)
                intent.putExtra("RECEIVER_ID", 1); // Người bán (mặc định)
                intent.putExtra("CHAT_TYPE", "exchange");

                startActivity(intent);
            } else {
                Toast.makeText(this, "Không thể mở chat", Toast.LENGTH_SHORT).show();
            }
        });
    }
}