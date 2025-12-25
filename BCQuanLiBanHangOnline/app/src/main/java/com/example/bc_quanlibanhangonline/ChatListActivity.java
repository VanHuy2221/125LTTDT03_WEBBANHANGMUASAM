package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Nhận exchangeId nếu có từ các màn hình khác
        Intent intent = getIntent();
        String exchangeId = intent.getStringExtra("EXCHANGE_ID");

        // Nếu có exchangeId, mở thẳng ChatDetailActivity
        if (exchangeId != null) {
            openChatDetailWithExchange(exchangeId);
            return; // Không cần hiển thị chat list
        }

        setupChatItemClickListeners();
    }

    private void openChatDetailWithExchange(String exchangeId) {
        Intent intent = new Intent(this, ChatDetailActivity.class);
        intent.putExtra("EXCHANGE_ID", exchangeId);
        intent.putExtra("SENDER_ID", getIntent().getIntExtra("SENDER_ID", -1));
        intent.putExtra("RECEIVER_ID", getIntent().getIntExtra("RECEIVER_ID", -1));
        intent.putExtra("CHAT_TYPE", "exchange");
        startActivity(intent);
        finish(); // Đóng ChatListActivity
    }

    private void setupChatItemClickListeners() {
        // Xử lý click vào Nguyễn Hà
        View nguyenHaItem = findViewById(R.id.nguyenHaChatItem);
        View Back = findViewById(R.id.btnBack);
        nguyenHaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChatDetail(
                        "Nguyễn Hà",
                        "NH",
                        "#2196F3",
                        "Tất cả"
                );
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });
    }

    private void navigateToChatDetail(String userName, String userInitials,
                                      String userColor, String userStatus) {
        Intent intent = new Intent(ChatListActivity.this, ChatDetailActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("USER_INITIALS", userInitials);
        intent.putExtra("USER_COLOR", userColor);
        intent.putExtra("USER_STATUS", userStatus);
        startActivity(intent);
    }

    private void navigateToProfile(){
        Intent intent = new Intent(ChatListActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}