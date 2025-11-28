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

public class ProfileActivity extends AppCompatActivity {

    LinearLayout menuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuContainer = findViewById(R.id.menuContainer);

        addMenuItems();
    }

    private void addMenuItems() {
        addItem("Thông tin cá nhân", R.drawable.ic_person, v -> {
            // TODO: Mở PersonalInfoActivity
        });

        addItem("Tin nhắn", R.drawable.ic_message, v -> {
            // Mở ChatListActivity khi nhấn vào Tin nhắn
            Intent intent = new Intent(ProfileActivity.this, ChatListActivity.class);
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

        addItem("Đăng xuất", R.drawable.ic_logout, v -> {
            // Xử lý đăng xuất
            performLogout();
        });
    }

    private void addItem(String title, int iconRes, View.OnClickListener clickListener) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_menu, menuContainer, false);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(iconRes);
        txtTitle.setText(title);

        // Click handler
        view.setOnClickListener(clickListener);

        menuContainer.addView(view);

        // Add divider
        View divider = getLayoutInflater().inflate(R.layout.divider_item, menuContainer, false);
        menuContainer.addView(divider);
    }

    private void performLogout() {
        // TODO: Xử lý logic đăng xuất
        // Ví dụ: xóa token, clear shared preferences, v.v.

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}