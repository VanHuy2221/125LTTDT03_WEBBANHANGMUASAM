package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bc_quanlibanhangonline.database.UserDatabase;
import com.example.bc_quanlibanhangonline.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout menuContainer;
    private BottomNavigationView bottomNav;
    private TextView txtUserName, txtUserType;
    private ImageView imgAvatar;

    private UserDatabase userDatabase;
    private int userId = -1;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d("ProfileActivity", "=== START ProfileActivity ===");

        // Lấy userId từ Intent hoặc SharedPreferences
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");
            Log.d("ProfileActivity", "From Intent - UserID: " + userId + ", Role: " + userRole);
        }

        if (userId == -1) {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            userId = prefs.getInt("user_id", -1);
            userRole = prefs.getString("user_role", "");
            Log.d("ProfileActivity", "From Prefs - UserID: " + userId + ", Role: " + userRole);
        }

        // Nếu chưa đăng nhập thì chuyển về UnProfile
        if (userId == -1) {
            Log.d("ProfileActivity", "User not logged in, redirecting to UnProfile");
            startActivity(new Intent(this, UnProfileActivity.class));
            finish();
            return;
        }

        userDatabase = new UserDatabase(this);

        initViews();
        setupBottomNavigation();
        loadUserData();
        addMenuItems();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("ProfileActivity", "ProfileActivity setup completed");
    }

    private void initViews() {
        bottomNav = findViewById(R.id.bottom_nav);
        menuContainer = findViewById(R.id.menuContainer);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserType = findViewById(R.id.txtUserType);
        imgAvatar = findViewById(R.id.imgAvatar);

        bottomNav.setSelectedItemId(R.id.nav_account);
    }

    private void loadUserData() {
        if (userId == -1) {
            txtUserName.setText("Đang tải...");
            return;
        }

        User user = userDatabase.getUserById(userId);
        if (user != null) {
            txtUserName.setText(user.getFullName() != null ? user.getFullName() : user.getEmail());
            String role = user.getRole() != null ? user.getRole() : "customer";

            txtUserType.setVisibility(View.VISIBLE);
            String roleDisplay = role.equalsIgnoreCase("admin") ? "Quản trị viên"
                    : role.equalsIgnoreCase("seller") ? "Người bán" : "Khách hàng";
            txtUserType.setText(roleDisplay);

            int badgeColor = role.equalsIgnoreCase("admin") ? getColor(android.R.color.holo_red_dark)
                    : role.equalsIgnoreCase("seller") ? getColor(android.R.color.holo_orange_dark)
                    : getColor(android.R.color.holo_green_dark);
            txtUserType.setTextColor(badgeColor);

            Log.d("ProfileActivity", "User data loaded: " + user.getFullName() + " (" + role + ")");
        } else {
            txtUserName.setText("Người dùng không tồn tại");
            txtUserType.setVisibility(View.GONE);
            Log.e("ProfileActivity", "User not found in database for ID: " + userId);
        }
    }

    private void addMenuItems() {
        addItem("Thông tin cá nhân", R.drawable.ic_person, v -> Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
        addItem("Tin nhắn", R.drawable.ic_message, v -> Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
        addItem("Địa chỉ giao hàng", R.drawable.ic_location, v -> Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
        addItem("Đơn hàng của tôi", R.drawable.ic_cart, v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderTrackingActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_ROLE", userRole);
            startActivity(intent);
        });
        addItem("Sản phẩm yêu thích", R.drawable.ic_heart, v -> Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
        addItem("Cài đặt", R.drawable.ic_settings, v -> Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show());
        addItem("Đăng xuất", R.drawable.ic_logout, v -> performLogout());
    }

    private void addItem(String title, int iconRes, View.OnClickListener clickListener) {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.item_menu, menuContainer, false);
            view.findViewById(R.id.imgIcon).setBackgroundResource(iconRes);
            ((TextView)view.findViewById(R.id.txtTitle)).setText(title);
            view.setOnClickListener(clickListener);
            menuContainer.addView(view);
        } catch (Exception e) {
            Log.e("ProfileActivity", "Error adding menu item: " + e.getMessage());
        }
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("ProfileActivity", "Bottom nav clicked: " + id);

            if (id == R.id.nav_home) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_order) {
                Intent intent = new Intent(ProfileActivity.this, OrderTrackingActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_cart) {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_account) return true;
            return false;
        });
    }

    private void performLogout() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    Log.d("ProfileActivity", "Logging out user: " + userId);
                    getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDatabase != null) userDatabase.close();
    }
}