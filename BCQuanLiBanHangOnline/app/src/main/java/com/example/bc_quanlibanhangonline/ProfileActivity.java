package com.example.bc_quanlibanhangonline;

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
}

