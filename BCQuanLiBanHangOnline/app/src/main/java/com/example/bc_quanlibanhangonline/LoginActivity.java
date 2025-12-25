package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.UserDatabase;
import com.example.bc_quanlibanhangonline.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ImageView btnBack;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDatabase = new UserDatabase(this);
        initViews();
        setupClickListeners();
        setupBackPressedHandler(); // Thêm xử lý nút back mới

        // Nếu có email từ đăng ký
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("REGISTER_EMAIL")) {
            etEmail.setText(intent.getStringExtra("REGISTER_EMAIL"));
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        // Nút back - quay về UnProfileActivity
        btnBack.setOnClickListener(v -> {
            navigateBackToUnProfile();
        });

        // Nút đăng nhập
        btnLogin.setOnClickListener(v -> handleLogin());

        // Nút đăng ký
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        // Quên mật khẩu
        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupBackPressedHandler() {
        // Sử dụng OnBackPressedDispatcher mới
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBackToUnProfile();
            }
        });
    }

    private void navigateBackToUnProfile() {
        // Quay về UnProfileActivity
        Intent intent = new Intent(LoginActivity.this, UnProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userDatabase.checkLogin(email, password);

        if (user == null) {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("locked".equals(user.getStatus())) {
            Toast.makeText(this, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu session vào SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit()
                .putInt("user_id", user.getUserId())
                .putString("user_role", user.getRole() != null ? user.getRole() : "customer")
                .apply();

        // Chuyển về HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USER_ID", user.getUserId());
        intent.putExtra("USER_ROLE", user.getRole() != null ? user.getRole() : "customer");

        // Xóa tất cả Activity trước đó
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDatabase != null) userDatabase.close();
    }
}