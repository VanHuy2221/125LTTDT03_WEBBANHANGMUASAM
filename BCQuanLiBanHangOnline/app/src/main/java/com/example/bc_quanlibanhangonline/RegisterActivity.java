package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bc_quanlibanhangonline.database.UserDatabase;
import com.example.bc_quanlibanhangonline.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPhone, etAddress, etPassword, etConfirmPassword;
    private AutoCompleteTextView spinnerAccountType;
    private CheckBox cbAgreeTerms;
    private MaterialButton btnRegister;
    private TextView tvLogin;
    private ImageView btnBack;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo UserDatabase
        userDatabase = new UserDatabase(this);

        // Ánh xạ view
        initViews();

        // Thiết lập Spinner cho loại tài khoản
        setupAccountTypeSpinner();

        // Thiết lập sự kiện
        setupClickListeners();

        // Xử lý nút back với OnBackPressedDispatcher
        setupBackPressedHandler();
    }

    private void initViews() {
        // Ánh xạ các TextInputEditText
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Ánh xạ Spinner
        spinnerAccountType = findViewById(R.id.spinnerAccountType);

        // Ánh xạ các view khác
        cbAgreeTerms = findViewById(R.id.cbAgreeTerms);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupAccountTypeSpinner() {
        // Tạo danh sách loại tài khoản
        String[] accountTypes = {
                "Người mua (Customer)",
                "Người bán (Seller)"
        };

        // Tạo adapter cho AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                accountTypes
        );

        // Thiết lập adapter cho Spinner
        spinnerAccountType.setAdapter(adapter);

        // Đặt giá trị mặc định
        spinnerAccountType.setText(accountTypes[0], false);

        // Ngăn không cho nhập từ bàn phím
        spinnerAccountType.setKeyListener(null);
    }

    private void setupClickListeners() {
        // Xử lý click nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        // Xử lý click "Đăng nhập ngay"
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        // Xử lý click nút quay lại (mũi tên)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToLogin();
            }
        });
    }

    private void handleRegister() {
        // Lấy dữ liệu từ form
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String selectedAccountType = spinnerAccountType.getText().toString().trim();

        // Kiểm tra dữ liệu nhập
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return;
        }

        if (selectedAccountType.isEmpty()) {
            spinnerAccountType.setError("Vui lòng chọn loại tài khoản");
            spinnerAccountType.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        // Kiểm tra điều khoản
        if (!cbAgreeTerms.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Vui lòng đồng ý với điều khoản dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email đã tồn tại
        if (userDatabase.isEmailExists(email)) {
            etEmail.setError("Email này đã được sử dụng");
            etEmail.requestFocus();
            return;
        }

        // Xác định role từ loại tài khoản đã chọn
        String role;
        if (selectedAccountType.contains("Người bán") || selectedAccountType.contains("Seller")) {
            role = "seller";
        } else {
            role = "customer"; // Mặc định là người mua
        }

        // Tạo user mới
        User newUser = new User(fullName, email, password, phone, address, role);

        // Đăng ký user
        boolean success = userDatabase.registerUser(newUser);

        if (success) {
            String roleName = role.equals("seller") ? "người bán" : "người mua";
            Toast.makeText(RegisterActivity.this, "Đăng ký thành công với vai trò " + roleName + "!", Toast.LENGTH_SHORT).show();

            // Quay lại màn hình đăng nhập với email vừa đăng ký
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("REGISTER_EMAIL", email);
            startActivity(intent);
            finish(); // Đóng RegisterActivity
        } else {
            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Đóng RegisterActivity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng database khi Activity bị hủy
        if (userDatabase != null) {
            userDatabase.close();
        }
    }
}