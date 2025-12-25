package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.OrderManagementAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;

public class OrderManagementActivity extends AppCompatActivity {

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders); // tạo RecyclerView trong layout
        DatabaseHelper db = new DatabaseHelper(this);
        OrderManagementAdapter adapter = new OrderManagementAdapter(this, db.getOrders(), db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        initializeViews();
        setupEventListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
    }
    private void setupEventListeners() {
        // NÚT BACK - QUAY VỀ HOME ACTIVITY
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Tạo Intent quay về HomeActivity
                    Intent intent = new Intent(OrderManagementActivity.this, SellerProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại
                }
            });
        }
    }
}