package com.example.bc_quanlibanhangonline.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.HomeActivity;
import com.example.bc_quanlibanhangonline.LoginActivity;
import com.example.bc_quanlibanhangonline.database.UserDatabase;
import com.example.bc_quanlibanhangonline.models.User;

public class AdminDashboardActivity extends AppCompatActivity {

    private int userId = -1;
    private String userRole = "";
    private UserDatabase userDatabase;

    // UI Components
    private TextView tvUserCount, tvOrderCount, tvRevenue, tvPendingCount;
    private ImageView btnLogout;
    private CardView cardDashboard, cardUserManagement, cardOrderManagement,
            cardCategoryManagement, cardProductManagement, cardReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // LẤY userId từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");

            if (userRole == null) {
                userRole = "";
            }

            Log.d("AdminDashboardActivity", "User ID: " + userId + ", Role: " + userRole);
        }

        // Khởi tạo database
        userDatabase = new UserDatabase(this);

        // Khởi tạo views
        initViews();

        // Load dữ liệu admin
        loadAdminData();

        // Load thống kê
        loadStatistics();

        // Thiết lập sự kiện click
        setupClickListeners();

        // Xử lý nút back
        setupBackPressedHandler();
    }

    private void initViews() {
        // Header views
        btnLogout = findViewById(R.id.btnLogout);

        // Stats views
        tvUserCount = findViewById(R.id.tvUserCount);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvRevenue = findViewById(R.id.tvRevenue);
        tvPendingCount = findViewById(R.id.tvPendingCount);

        // Menu CardViews
        cardDashboard = findViewById(R.id.cardDashboard);
        cardUserManagement = findViewById(R.id.cardUserManagement);
        cardOrderManagement = findViewById(R.id.cardOrderManagement);
        cardCategoryManagement = findViewById(R.id.cardCategoryManagement);
        cardProductManagement = findViewById(R.id.cardProductManagement);
        cardReports = findViewById(R.id.cardReports);
    }

    private void loadAdminData() {
        if (userId != -1) {
            User user = userDatabase.getUserById(userId);
            if (user != null) {
                String welcomeMsg = "Chào mừng " + user.getFullName() + "!";
                Toast.makeText(this, welcomeMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadStatistics() {
        try {
            // Load số lượng người dùng
            int totalUsers = userDatabase.getTotalUsers();
            tvUserCount.setText(String.valueOf(totalUsers));

            // TODO: Load số đơn hàng từ database
            // int totalOrders = orderDatabase.getTotalOrders();
            // tvOrderCount.setText(String.valueOf(totalOrders));
            tvOrderCount.setText("0"); // Tạm thời

            // TODO: Load doanh thu từ database
            // double totalRevenue = orderDatabase.getTotalRevenue();
            // tvRevenue.setText(formatCurrency(totalRevenue));
            tvRevenue.setText("0");

            // TODO: Load số đơn hàng đang chờ
            // int pendingOrders = orderDatabase.getPendingOrdersCount();
            // tvPendingCount.setText(String.valueOf(pendingOrders));
            tvPendingCount.setText("0");

        } catch (Exception e) {
            Log.e("AdminDashboardActivity", "Error loading statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatCurrency(double amount) {
        // Format tiền tệ
        return String.format("%,.0f đ", amount);
    }

    private void setupClickListeners() {
        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogout();
                }
            });
        }

        // Menu items click listeners
        setupMenuClickListeners();
    }

    private void setupMenuClickListeners() {
        // Dashboard
        if (cardDashboard != null) {
            cardDashboard.setOnClickListener(v -> {
                Toast.makeText(AdminDashboardActivity.this, "Bạn đang ở Dashboard", Toast.LENGTH_SHORT).show();
            });
        }

        // Quản lý người dùng - SỬA LẠI PHẦN NÀY
        if (cardUserManagement != null) {
            cardUserManagement.setOnClickListener(v -> {
                // Mở UserManagementActivity
                openUserManagementActivity();
            });
        }

        // Quản lý đơn hàng
        if (cardOrderManagement != null) {
            cardOrderManagement.setOnClickListener(v -> {
                Toast.makeText(AdminDashboardActivity.this, "Mở quản lý đơn hàng", Toast.LENGTH_SHORT).show();
                // TODO: Mở activity quản lý đơn hàng
                // Intent intent = new Intent(AdminDashboardActivity.this, OrderManagementActivity.class);
                // intent.putExtra("USER_ID", userId);
                // startActivity(intent);
            });
        }

        // Quản lý danh mục
        if (cardCategoryManagement != null) {
            cardCategoryManagement.setOnClickListener(v -> {
                Toast.makeText(AdminDashboardActivity.this, "Mở quản lý danh mục", Toast.LENGTH_SHORT).show();
                // TODO: Mở activity quản lý danh mục
                // Intent intent = new Intent(AdminDashboardActivity.this, CategoryManagementActivity.class);
                // intent.putExtra("USER_ID", userId);
                // startActivity(intent);
            });
        }

        // Quản lý sản phẩm
        if (cardProductManagement != null) {
            cardProductManagement.setOnClickListener(v -> {
                Toast.makeText(AdminDashboardActivity.this, "Mở quản lý sản phẩm", Toast.LENGTH_SHORT).show();
                // TODO: Mở activity quản lý sản phẩm
                // Intent intent = new Intent(AdminDashboardActivity.this, ProductManagementActivity.class);
                // intent.putExtra("USER_ID", userId);
                // startActivity(intent);
            });
        }

        // Báo cáo
        if (cardReports != null) {
            cardReports.setOnClickListener(v -> {
                Toast.makeText(AdminDashboardActivity.this, "Mở báo cáo", Toast.LENGTH_SHORT).show();
                // TODO: Mở activity báo cáo
                // Intent intent = new Intent(AdminDashboardActivity.this, ReportsActivity.class);
                // intent.putExtra("USER_ID", userId);
                // startActivity(intent);
            });
        }
    }

    // THÊM PHƯƠNG THỨC MỚI: Mở UserManagementActivity
    private void openUserManagementActivity() {
        try {
            Intent intent = new Intent(AdminDashboardActivity.this, UserManagementActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_ROLE", userRole);
            startActivity(intent);
            // KHÔNG gọi finish() để có thể quay lại dashboard
        } catch (Exception e) {
            Log.e("AdminDashboardActivity", "Error opening UserManagementActivity: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi mở quản lý người dùng", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void performLogout() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản admin?");
        builder.setPositiveButton("Đăng xuất", (dialog, which) -> {
            // Xóa session
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Chuyển về LoginActivity
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Toast.makeText(AdminDashboardActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBackToHome();
            }
        });
    }

    private void navigateBackToHome() {
        Intent intent = new Intent(AdminDashboardActivity.this, HomeActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_ROLE", userRole);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        navigateBackToHome();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDatabase != null) {
            userDatabase.close();
        }
    }
}