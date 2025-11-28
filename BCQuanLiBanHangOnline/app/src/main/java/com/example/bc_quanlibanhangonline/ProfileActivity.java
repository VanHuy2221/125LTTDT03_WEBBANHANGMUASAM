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

public class ProfileActivity extends AppCompatActivity {

    LinearLayout menuContainer;
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        bottomNav = findViewById(R.id.bottom_nav);

        // Xử lý sự kiện bottom navigation
        setupBottomNavigation();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuContainer = findViewById(R.id.menuContainer);

        addMenuItems();
    }

    private void addMenuItems() {
        addItem("Thông tin cá nhân", R.drawable.ic_person);
        addItem("Tin nhắn", R.drawable.ic_message);
        addItem("Địa chỉ giao hàng", R.drawable.ic_location);
        addItem("Đơn hàng của tôi", R.drawable.ic_cart);
        addItem("Sản phẩm yêu thích", R.drawable.ic_heart);
        addItem("Phương thức thanh toán", R.drawable.ic_payment);
        addItem("Cài đặt thông báo", R.drawable.ic_bell);
        addItem("Cài đặt", R.drawable.ic_settings);
        addItem("Đăng xuất", R.drawable.ic_logout);
    }

    private void addItem(String title, int iconRes) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_menu, menuContainer, false);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(iconRes);
        txtTitle.setText(title);

        // Click handler
        view.setOnClickListener(v -> {
            // TODO: mở activity tương ứng
        });

        menuContainer.addView(view);

        // Add divider
        View divider = getLayoutInflater().inflate(R.layout.divider_item, menuContainer, false);
        menuContainer.addView(divider);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_account) {
                // Đã ở Home rồi nên không cần làm gì
                return true;
            } else if (itemId == R.id.nav_home) {
                // Chuyển đến OrderTrackingActivity
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(ProfileActivity.this, OrderTrackingActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Chuyển đến AccountActivity
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}

