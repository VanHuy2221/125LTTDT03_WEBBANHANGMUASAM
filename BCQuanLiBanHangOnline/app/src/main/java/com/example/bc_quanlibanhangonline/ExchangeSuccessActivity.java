package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExchangeSuccessActivity extends AppCompatActivity {

    private ImageView btnBack, btnMessage;
    private TextView tvProductName, tvYourProduct, tvEstimatedValue,
            tvExchangeId, tvStatus, tvDate, tvSuccessMessage;
    private Button btnGoHome, btnViewDetails;

    private DatabaseHelper databaseHelper;
    private String currentExchangeId; // THÊM: Lưu exchangeId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_success);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Nhận dữ liệu từ ExchangeActivity
        Intent intent = getIntent();
        String productName = intent.getStringExtra("PRODUCT_NAME");
        String yourProduct = intent.getStringExtra("YOUR_PRODUCT");
        String estimatedPrice = intent.getStringExtra("ESTIMATED_PRICE");
        String description = intent.getStringExtra("DESCRIPTION");
        String exchangeId = intent.getStringExtra("EXCHANGE_ID");
        int userId = intent.getIntExtra("USER_ID", -1); // THÊM: Lấy userId

        currentExchangeId = exchangeId; // Lưu exchangeId

        // Ánh xạ view
        initViews();

        // Thiết lập dữ liệu
        setupData(productName, yourProduct, estimatedPrice, exchangeId);

        // Thiết lập sự kiện
        setupEvents();

        // Xử lý nút back
        setupBackPressedHandler();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnMessage = findViewById(R.id.btnMessage);

        tvProductName = findViewById(R.id.tvProductName);
        tvYourProduct = findViewById(R.id.tvYourProduct);
        tvEstimatedValue = findViewById(R.id.tvEstimatedValue);
        tvExchangeId = findViewById(R.id.tvExchangeId);
        tvStatus = findViewById(R.id.tvStatus);
        tvDate = findViewById(R.id.tvDate);
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage);

        btnGoHome = findViewById(R.id.btnGoHome);
        btnViewDetails = findViewById(R.id.btnViewDetails);
    }

    private void setupData(String productName, String yourProduct,
                           String estimatedPrice, String exchangeId) {
        // Thiết lập dữ liệu nhận được
        if (productName != null) {
            tvProductName.setText(productName);
        }

        if (yourProduct != null) {
            tvYourProduct.setText(yourProduct);
        } else {
            tvYourProduct.setText("Chưa đặt tên");
        }

        if (estimatedPrice != null && !estimatedPrice.isEmpty()) {
            try {
                int price = Integer.parseInt(estimatedPrice);
                tvEstimatedValue.setText(formatPrice(price));
            } catch (NumberFormatException e) {
                tvEstimatedValue.setText(estimatedPrice + "đ");
            }
        } else {
            tvEstimatedValue.setText("Đang ước tính");
        }

        if (exchangeId != null) {
            tvExchangeId.setText(exchangeId);
        } else {
            // Tạo mã đề nghị tự động nếu không có
            String autoExchangeId = "EX" + System.currentTimeMillis();
            tvExchangeId.setText(autoExchangeId);
            currentExchangeId = autoExchangeId;
        }

        // Ngày hiện tại
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());
        tvDate.setText(currentDate);

        // Tùy chỉnh thông báo thành công
        if (productName != null) {
            String successMessage = "Chúng tôi đã gửi đề nghị trao đổi của bạn đến người bán " +
                    productName + ". Bạn có thể nhắn tin để thương lượng thêm.";
            tvSuccessMessage.setText(successMessage);
        }

        // THÊM: Lấy thông tin ExchangeRequest từ database
        if (exchangeId != null) {
            ExchangeRequest exchange = databaseHelper.getExchangeRequestById(exchangeId);
            if (exchange != null) {
                // Cập nhật status từ database
                tvStatus.setText(getStatusText(exchange.getStatus()));

                // Lấy tin nhắn đầu tiên (nếu có)
                List<com.example.bc_quanlibanhangonline.models.Message> messages =
                        databaseHelper.getMessagesByExchangeId(exchangeId);

                if (!messages.isEmpty()) {
                    // Có thể hiển thị số tin nhắn
                    tvSuccessMessage.setText("Đã có " + messages.size() + " tin nhắn trong hội thoại");
                }
            }
        }
    }

    // THÊM phương thức mới để lấy status text
    private String getStatusText(String status) {
        if (status == null) return "Đang chờ phản hồi";

        switch (status.toLowerCase()) {
            case "pending": return "⏳ Đang chờ phản hồi";
            case "negotiating": return "💬 Đang thương lượng";
            case "accepted": return "✅ Đã chấp nhận";
            case "rejected": return "❌ Đã từ chối";
            case "completed": return "🎉 Hoàn thành";
            default: return "Đang chờ phản hồi";
        }
    }

    private void setupEvents() {
        // Nút back - về ExchangeActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Nút tin nhắn - mở ChatListActivity HOẶC ChatDetailActivity
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity();
            }
        });

        // Nút về trang chủ
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

        // Nút xem chi tiết/theo dõi
        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewExchangeDetails();
            }
        });
    }

    // SỬA: Thêm phương thức mở chat activity
    private void openChatActivity() {
        try {
            if (currentExchangeId == null || currentExchangeId.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy mã trao đổi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mở ChatDetailActivity trực tiếp với exchangeId
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra("EXCHANGE_ID", currentExchangeId);

            // Giả sử user hiện tại là người mua (userId = 3), người bán là (userId = 1)
            intent.putExtra("SENDER_ID", 3); // Người mua
            intent.putExtra("RECEIVER_ID", 1); // Người bán
            intent.putExtra("CHAT_TYPE", "exchange");

            startActivity(intent);
            Toast.makeText(this, "Mở chat trao đổi #" + currentExchangeId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // SỬA LẠI: Thêm phương thức xử lý back pressed mới
    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Khi nhấn nút back vật lý, quay về ExchangeActivity
                finish();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void viewExchangeDetails() {
        if (currentExchangeId != null) {
            Intent intent = new Intent(this, ExchangeDetailForBuyerActivity.class);
            intent.putExtra("EXCHANGE_ID", currentExchangeId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không tìm thấy chi tiết trao đổi", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }
}