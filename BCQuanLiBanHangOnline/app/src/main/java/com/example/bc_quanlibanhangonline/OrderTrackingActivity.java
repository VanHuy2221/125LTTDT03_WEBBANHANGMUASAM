package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.OrderAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrderTrackingActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private DatabaseHelper db;

    private int userId = -1;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_order);
        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");
        }

        List<Order> allOrders = db.getOrdersByUser(userId);
        List<Order> activeOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (!"cancelled".equalsIgnoreCase(order.getStatus())) {
                activeOrders.add(order);
            }
        }

        adapter = new OrderAdapter(this, activeOrders, db);
        recyclerView.setAdapter(adapter);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d("OrderTrackingActivity", "Bottom nav clicked: " + itemId);

            if (itemId == R.id.nav_home) {
                navigateWithLoginCheck(HomeActivity.class);
                return true;
            } else if (itemId == R.id.nav_order) {
                navigateWithLoginCheck(OrderTrackingActivity.class);
                return true;
            } else if (itemId == R.id.nav_cart) {
                navigateWithLoginCheck(CartActivity.class);
                return true;
            } else if (itemId == R.id.nav_account) {
                navigateToAccount();
                return true;
            }

            return false;
        });
    }

    private void navigateWithLoginCheck(Class<?> targetActivity) {
        if (userId == -1) {
            startActivity(new Intent(this, UnProfileActivity.class));
        } else {
            Intent intent = new Intent(this, targetActivity);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_ROLE", userRole);
            startActivity(intent);
        }
    }

    private void navigateToAccount() {
        try {
            Intent intent;
            if (userId == -1) {
                intent = new Intent(this, UnProfileActivity.class);
            } else if ("admin".equalsIgnoreCase(userRole)) {
                intent = new Intent(this, com.example.bc_quanlibanhangonline.admin.AdminDashboardActivity.class);
            } else if ("seller".equalsIgnoreCase(userRole)) {
                intent = new Intent(this, SellerProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            } else {
                intent = new Intent(this, ProfileActivity.class);
            }
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_ROLE", userRole);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("OrderTrackingActivity", "ERROR in navigateToAccount: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi chuyển trang", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToReview(){
        Intent intent = new Intent(OrderTrackingActivity.this, ReviewActivity.class);
        startActivity(intent);
    }
}