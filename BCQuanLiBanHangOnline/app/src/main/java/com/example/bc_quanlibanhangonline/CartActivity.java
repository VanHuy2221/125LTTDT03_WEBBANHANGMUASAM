package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        bottomNav = findViewById(R.id.bottom_nav);

        // Xử lý sự kiện bottom navigation
        setupBottomNavigation();

    }
    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_cart) {
                // Đã ở Home rồi nên không cần làm gì
                return true;
            } else if (itemId == R.id.nav_home) {
                // Chuyển đến OrderTrackingActivity
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(CartActivity.this, OrderTrackingActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_account) {
                // Chuyển đến AccountActivity
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}