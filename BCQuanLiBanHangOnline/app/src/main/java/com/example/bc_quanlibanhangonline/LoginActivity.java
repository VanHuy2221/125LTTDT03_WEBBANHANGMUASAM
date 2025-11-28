package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo views
        initViews();

        // Thiết lập sự kiện
        setupLoginClickListeners();
    }

    private void initViews() {
        // Tìm view theo ID - đảm bảo ID này tồn tại trong activity_login.xml
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupLoginClickListeners() {
        // Xử lý click vào nút Đăng nhập
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToHome();
                }
            });
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Kết thúc LoginActivity để không quay lại được
    }
}