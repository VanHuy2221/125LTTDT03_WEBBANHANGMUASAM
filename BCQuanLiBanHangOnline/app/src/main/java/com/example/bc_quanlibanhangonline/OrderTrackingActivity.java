package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderTrackingActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        bottomNav = findViewById(R.id.bottom_nav);
        setupBottomNavigation();

        setupReviewClickListener();
    }

    private void setupReviewClickListener(){
        View reviewButton = findViewById(R.id.btn_evaluate);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToReview();
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(OrderTrackingActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến OrderTrackingActivity
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(OrderTrackingActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_account) {
                // Chuyển đến AccountActivity
                Intent intent = new Intent(OrderTrackingActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void navigateToReview(){
        Intent intent = new Intent(OrderTrackingActivity.this, ReviewActivity.class);
        startActivity(intent);
    }
}