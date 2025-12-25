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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExchangeSuccessActivity extends AppCompatActivity {

    private ImageView btnBack, btnMessage;
    private TextView tvProductName, tvYourProduct, tvEstimatedValue,
            tvExchangeId, tvStatus, tvDate, tvSuccessMessage;
    private Button btnGoHome, btnViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_success);

        // Nhận dữ liệu từ ExchangeActivity
        Intent intent = getIntent();
        String productName = intent.getStringExtra("PRODUCT_NAME");
        String yourProduct = intent.getStringExtra("YOUR_PRODUCT");
        String estimatedPrice = intent.getStringExtra("ESTIMATED_PRICE");
        String description = intent.getStringExtra("DESCRIPTION");
        String exchangeId = intent.getStringExtra("EXCHANGE_ID");

        // Ánh xạ view
        initViews();

        // Thiết lập dữ liệu
        setupData(productName, yourProduct, estimatedPrice, exchangeId);

        // Thiết lập sự kiện
        setupEvents();

        // Xử lý nút back (SỬA LẠI)
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
    }

    private void setupEvents() {
        // Nút back - về ExchangeActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Nút tin nhắn - mở ChatListActivity
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatListActivity();
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

    private void openChatListActivity() {
        try {
            Intent intent = new Intent(this, ChatListActivity.class);

            // Truyền thông tin trao đổi để bắt đầu chat
            String exchangeId = tvExchangeId.getText().toString();
            String productName = tvProductName.getText().toString();
            String yourProduct = tvYourProduct.getText().toString();

            intent.putExtra("EXCHANGE_ID", exchangeId);
            intent.putExtra("PRODUCT_NAME", productName);
            intent.putExtra("YOUR_PRODUCT", yourProduct);
            intent.putExtra("CHAT_TYPE", "exchange"); // Loại chat: trao đổi

            startActivity(intent);

            Toast.makeText(this, "Mở hội thoại trao đổi", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở tin nhắn", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void viewExchangeDetails() {
        Toast.makeText(this,
                "Đang theo dõi đề nghị trao đổi: " + tvExchangeId.getText(),
                Toast.LENGTH_SHORT).show();

        // TODO: Có thể mở màn hình chi tiết trao đổi
        // Intent intent = new Intent(this, ExchangeDetailActivity.class);
        // intent.putExtra("EXCHANGE_ID", tvExchangeId.getText().toString());
        // startActivity(intent);
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    // XÓA hoặc SỬA phương thức onBackPressed cũ
    // @Override
    // public void onBackPressed() {
    //     // Khi nhấn nút back vật lý, quay về ExchangeActivity
    //     super.onBackPressed();
    // }
}