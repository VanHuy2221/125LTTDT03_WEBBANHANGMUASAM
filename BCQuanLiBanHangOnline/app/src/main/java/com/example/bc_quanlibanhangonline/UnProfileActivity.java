package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UnProfileActivity extends AppCompatActivity {
    LinearLayout menuContainer;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_unlogin);

        bottomNav = findViewById(R.id.bottom_nav);
        menuContainer = findViewById(R.id.menuContainer);

        // Thiết lập bottom navigation
        setupBottomNavigation();

        setupLoginClickListener();

        // Đặt item "Account" là selected (vì đang ở ProfileActivity)
        bottomNav.setSelectedItemId(R.id.nav_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addMenuItems();
    }

    private void addMenuItems() {
        addItem("Thông tin cá nhân", R.drawable.ic_person, v -> {
            // TODO: Mở PersonalInfoActivity
        });

        addItem("Tin nhắn", R.drawable.ic_message, v -> {
            // Mở ChatListActivity khi nhấn vào Tin nhắn
            Intent intent = new Intent(UnProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        addItem("Địa chỉ giao hàng", R.drawable.ic_location, v -> {
            // TODO: Mở AddressActivity
        });

        addItem("Đơn hàng của tôi", R.drawable.ic_cart, v -> {
            // TODO: Mở OrderHistoryActivity
        });

        addItem("Sản phẩm yêu thích", R.drawable.ic_heart, v -> {
            // TODO: Mở FavoriteProductsActivity
        });

        addItem("Phương thức thanh toán", R.drawable.ic_payment, v -> {
            // TODO: Mở PaymentMethodsActivity
        });

        addItem("Cài đặt thông báo", R.drawable.ic_bell, v -> {
            // TODO: Mở NotificationSettingsActivity
        });

        addItem("Cài đặt", R.drawable.ic_settings, v -> {
            // TODO: Mở SettingsActivity
        });
    }

    private void addItem(String title, int iconRes, View.OnClickListener clickListener) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_menu, menuContainer, false);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(iconRes);
        txtTitle.setText(title);

        // Click handler - sử dụng listener được truyền vào
        view.setOnClickListener(clickListener);

        menuContainer.addView(view);

        // Add divider
        View divider = getLayoutInflater().inflate(R.layout.divider_item, menuContainer, false);
        menuContainer.addView(divider);
    }

    private void setupLoginClickListener() {
        View nguyenHaItem = findViewById(R.id.btnLogin);
        nguyenHaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAction();
            }
        });
    }


    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Chuyển về Home và kết thúc Activity hiện tại
                Intent intent = new Intent(UnProfileActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến OrderTrackingActivity
                Intent intent = new Intent(UnProfileActivity.this, OrderTrackingActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(UnProfileActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_account) {
                // Đã ở Profile rồi, không làm gì cả
                return true;
            }
            return false;
        });
    }

    private void performLogout() {
        // TODO: Xử lý logic đăng xuất
        // Ví dụ: xóa token, clear shared preferences, v.v.

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(UnProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void LoginAction() {
        Intent intent = new Intent(UnProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
