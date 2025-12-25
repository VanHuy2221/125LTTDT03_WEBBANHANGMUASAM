package com.example.bc_quanlibanhangonline.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.adapters.UserAdapter;
import com.example.bc_quanlibanhangonline.database.UserDatabase;
import com.example.bc_quanlibanhangonline.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    private ImageView btnBack;
    private TextView tvTotalUsers, tvTotalSellers;
    private EditText etSearch;
    private RecyclerView rvUsers;

    private UserDatabase userDatabase;
    private UserAdapter userAdapter;
    private List<User> allUsers = new ArrayList<>();
    private List<User> filteredUsers = new ArrayList<>();

    private int userId = -1;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Lấy thông tin admin từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");
        }

        // Khởi tạo database
        userDatabase = new UserDatabase(this);

        // Ánh xạ view
        initViews();

        // Thiết lập sự kiện
        setupClickListeners();

        // Thiết lập RecyclerView
        setupRecyclerView();

        // Xử lý nút back
        setupBackPressedHandler();

        // Load dữ liệu
        loadData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalSellers = findViewById(R.id.tvTotalSellers);
        etSearch = findViewById(R.id.etSearch);
        rvUsers = findViewById(R.id.rvUsers);
    }

    private void setupClickListeners() {
        // Nút back
        btnBack.setOnClickListener(v -> navigateBackToAdminDashboard());

        // Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        // Sử dụng UserAdapter từ package adapters
        userAdapter = new UserAdapter(filteredUsers, this);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(userAdapter);
    }

    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBackToAdminDashboard();
            }
        });
    }

    private void loadData() {
        // Load tất cả users từ database
        allUsers = userDatabase.getAllUsers();
        filteredUsers = new ArrayList<>(allUsers);

        // Cập nhật số liệu thống kê
        updateStatistics();

        // Cập nhật RecyclerView
        userAdapter.updateUsers(filteredUsers);
    }

    private void updateStatistics() {
        if (allUsers.isEmpty()) {
            tvTotalUsers.setText("0");
            tvTotalSellers.setText("0");
            return;
        }

        // Đếm tổng users
        int totalUsers = allUsers.size();
        tvTotalUsers.setText(String.valueOf(totalUsers));

        // Đếm số sellers
        int totalSellers = 0;
        for (User user : allUsers) {
            if ("seller".equalsIgnoreCase(user.getRole())) {
                totalSellers++;
            }
        }
        tvTotalSellers.setText(String.valueOf(totalSellers));
    }

    private void filterUsers(String query) {
        filteredUsers.clear();

        if (query.isEmpty()) {
            filteredUsers.addAll(allUsers);
        } else {
            String lowerQuery = query.toLowerCase();
            for (User user : allUsers) {
                if (user.getFullName().toLowerCase().contains(lowerQuery) ||
                        user.getEmail().toLowerCase().contains(lowerQuery) ||
                        (user.getPhone() != null && user.getPhone().toLowerCase().contains(lowerQuery))) {
                    filteredUsers.add(user);
                }
            }
        }

        userAdapter.updateUsers(filteredUsers);
    }

    private void navigateBackToAdminDashboard() {
        Intent intent = new Intent(UserManagementActivity.this, AdminDashboardActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_ROLE", userRole);
        startActivity(intent);
        finish();
    }

    // ============ UserAdapter Callbacks ============

    @Override
    public void onItemClick(User user) {
        // Hiển thị chi tiết thông tin user khi click
        showUserDetailsDialog(user);
    }

    @Override
    public void onViewDetails(User user) {
        // Tương tự onItemClick, có thể gọi chung
        showUserDetailsDialog(user);
    }

    @Override
    public void onToggleStatus(User user) {
        // Đổi trạng thái user
        toggleUserStatus(user);
    }

    @Override
    public void onEditUser(User user) {
        // Mở màn hình chỉnh sửa user
        editUserInfo(user);
    }

    // ============ Các phương thức xử lý ============

    private void showUserDetailsDialog(User user) {
        // Hiển thị chi tiết thông tin user
        String details = String.format(
                "Thông tin chi tiết:\n" +
                        "ID: %d\n" +
                        "Tên: %s\n" +
                        "Email: %s\n" +
                        "SĐT: %s\n" +
                        "Địa chỉ: %s\n" +
                        "Vai trò: %s\n" +
                        "Trạng thái: %s\n" +
                        "Ngày đăng ký: %s",
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone() != null ? user.getPhone() : "N/A",
                user.getAddress() != null ? user.getAddress() : "N/A",
                getRoleDisplay(user.getRole()),
                isUserActive(user) ? "Hoạt động" : "Bị khóa",
                user.getCreatedAt() != null ? user.getCreatedAt() : "N/A"
        );

        // Sử dụng AlertDialog thay vì Toast để hiển thị nhiều thông tin hơn
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Thông tin người dùng")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .setNeutralButton("Chỉnh sửa", (dialog, which) -> editUserInfo(user))
                .show();
    }

    private void toggleUserStatus(User user) {
        // Kiểm tra không cho phép khóa chính mình
        if (user.getUserId() == userId) {
            Toast.makeText(this, "Không thể khóa tài khoản của chính mình", Toast.LENGTH_SHORT).show();
            return;
        }

        String action = isUserActive(user) ? "khóa" : "mở khóa";
        String message = String.format("Bạn có chắc muốn %s tài khoản %s?", action, user.getFullName());

        // Hiển thị dialog xác nhận
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Xác nhận")
                .setMessage(message)
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    performToggleStatus(user);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private boolean isUserActive(User user) {
        return user != null && "active".equalsIgnoreCase(user.getStatus());
    }

    private void performToggleStatus(User user) {
        String newStatus = isUserActive(user) ? "locked" : "active";
        String statusMessage = isUserActive(user) ? "khóa" : "mở khóa";

        boolean success = userDatabase.updateUserStatus(user.getUserId(), newStatus);

        if (success) {
            Toast.makeText(this, "Đã " + statusMessage + " tài khoản " + user.getFullName(), Toast.LENGTH_SHORT).show();
            // Cập nhật lại dữ liệu
            refreshUserData(user, newStatus);
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshUserData(User updatedUser, String newStatus) {
        // Cập nhật trạng thái trong danh sách
        for (User user : allUsers) {
            if (user.getUserId() == updatedUser.getUserId()) {
                user.setStatus(newStatus);
                break;
            }
        }

        for (User user : filteredUsers) {
            if (user.getUserId() == updatedUser.getUserId()) {
                user.setStatus(newStatus);
                break;
            }
        }

        // Cập nhật RecyclerView
        userAdapter.updateUsers(filteredUsers);

        // Cập nhật số liệu thống kê (nếu cần)
        updateStatistics();
    }

    private void editUserInfo(User user) {
        // Mở activity chỉnh sửa user (cần tạo activity này)
        // Intent intent = new Intent(UserManagementActivity.this, EditUserActivity.class);
        // intent.putExtra("USER_ID", user.getUserId());
        // startActivity(intent);

        // Tạm thời hiển thị Toast
        Toast.makeText(this, "Chức năng chỉnh sửa đang phát triển: " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }

    private String getRoleDisplay(String role) {
        if (role == null) return "Người mua";
        switch (role.toLowerCase()) {
            case "admin": return "Quản trị viên";
            case "seller": return "Người bán";
            case "customer": return "Người mua";
            default: return "Người mua";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data khi quay lại activity
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDatabase != null) {
            userDatabase.close();
        }
    }
}