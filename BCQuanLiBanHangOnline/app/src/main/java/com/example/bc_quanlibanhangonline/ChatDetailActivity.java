package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChatDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        setupChatItemClickListeners();
    }

    private void setupChatItemClickListeners() {
        // Xử lý click vào Nguyễn Hà
        View nguyenHaItem = findViewById(R.id.btnBack);
        nguyenHaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChatList();
            }
        });

        // Bạn cũng có thể thêm click listeners cho các chat item khác ở đây
        // Ví dụ: Văn Huy, Hòa Bình, Minh Chính...
    }

    private void navigateToChatList(){
        Intent intent = new Intent(ChatDetailActivity.this, ChatListActivity.class);
        startActivity(intent);
    }
}