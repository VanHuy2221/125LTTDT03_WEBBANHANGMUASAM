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

        setupChatItemClickListeners();
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
        // Bạn cũng có thể thêm click listeners cho các chat item khác ở đây
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