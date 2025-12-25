package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.OrderManagementAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;
import com.example.bc_quanlibanhangonline.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText edtSearch;
    private RecyclerView recyclerViewOrders;
    private TextView tvEmptyState;
    private View emptyState;

    private DatabaseHelper databaseHelper;
    private OrderManagementAdapter adapter;
    private List<Object> combinedList = new ArrayList<>();
    private List<Object> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        databaseHelper = new DatabaseHelper(this);

        initializeViews();
        setupEventListeners();
        loadData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        emptyState = findViewById(R.id.emptyState);
    }

    private void setupEventListeners() {
        // NÚT BACK
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(OrderManagementActivity.this, SellerProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // TÌM KIẾM
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadData() {
        // Lấy đơn hàng
        List<Order> orders = databaseHelper.getOrders();

        // Lấy yêu cầu trao đổi
        List<ExchangeRequest> exchangeRequests = databaseHelper.getExchangeRequests();

        // Kết hợp
        combinedList.clear();
        combinedList.addAll(orders);
        combinedList.addAll(exchangeRequests);

        // Copy to filtered list
        filteredList.clear();
        filteredList.addAll(combinedList);

        updateUI();
    }

    private void filterOrders(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(combinedList);
        } else {
            String lowerQuery = query.toLowerCase();

            for (Object item : combinedList) {
                if (item instanceof Order) {
                    Order order = (Order) item;
                    // Tìm theo ID, trạng thái
                    if (String.valueOf(order.getOrderId()).contains(lowerQuery) ||
                            order.getStatus().toLowerCase().contains(lowerQuery)) {
                        filteredList.add(item);
                    }
                } else if (item instanceof ExchangeRequest) {
                    ExchangeRequest exchange = (ExchangeRequest) item;
                    // Tìm theo tên sản phẩm, trạng thái
                    if (exchange.getProductName().toLowerCase().contains(lowerQuery) ||
                            exchange.getExchangeItemName().toLowerCase().contains(lowerQuery) ||
                            exchange.getStatus().toLowerCase().contains(lowerQuery) ||
                            exchange.getExchangeId().toLowerCase().contains(lowerQuery)) {
                        filteredList.add(item);
                    }
                }
            }
        }

        updateUI();
    }

    private void updateUI() {
        if (filteredList.isEmpty()) {
            showEmptyState(true, "Không tìm thấy đơn hàng");
        } else {
            showEmptyState(false, null);

            if (adapter == null) {
                adapter = new OrderManagementAdapter(this, filteredList, databaseHelper);
                recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewOrders.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showEmptyState(boolean show, String message) {
        if (show) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerViewOrders.setVisibility(View.GONE);
            if (message != null) {
                tvEmptyState.setText(message);
            }
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerViewOrders.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}