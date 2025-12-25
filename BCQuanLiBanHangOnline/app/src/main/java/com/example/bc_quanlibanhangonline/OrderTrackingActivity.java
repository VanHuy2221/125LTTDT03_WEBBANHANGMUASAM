package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.OrderAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class OrderTrackingActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_order);
        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        // userId giả lập = 1
        List<Order> orders = db.getOrdersByUser(3);

        Log.d("ORDER", "Orders size = " + orders.size());

        adapter = new OrderAdapter(this, orders);
        recyclerView.setAdapter(adapter);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (itemId == R.id.nav_order) {
                // 👉 ĐANG Ở ĐƠN HÀNG → KHÔNG LÀM GÌ
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
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