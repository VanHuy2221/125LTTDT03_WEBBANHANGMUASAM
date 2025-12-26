package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.CartAdapter;
import com.example.bc_quanlibanhangonline.adapters.OrderAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.CartItem;
import com.example.bc_quanlibanhangonline.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {
    private BottomNavigationView bottomNav;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DatabaseHelper db;

    private TextView txtTotalPrice;
    private List<CartItem> cartItems;
    private Button btnCheckout;

    private int userId = -1;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtTotalPrice = findViewById(R.id.total_price);
        btnCheckout = findViewById(R.id.btnCheckout);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_cart);
        recyclerView = findViewById(R.id.recyclerViewCart);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");
        }

        db = new DatabaseHelper(this);

        cartItems = db.getCartByUser(userId);
        adapter = new CartAdapter(this, cartItems, db, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateTotalPrice();
        // Xử lý sự kiện bottom navigation
        setupBottomNavigation();
        btnCheckout.setOnClickListener(v -> openPaymentActivity());
    }

    @Override
    public void onCartChanged() {
        updateTotalPrice();
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
            finish();
        } catch (Exception e) {
            Log.e("OrderTrackingActivity", "ERROR in navigateToAccount: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi chuyển trang", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalPrice() {
        int total = 0;

        for (CartItem item : cartItems) {
            Product p = db.getProductById(item.getProductId());
            if (p != null) {
                total += p.getPrice() * item.getQuantity();
            }
        }

        txtTotalPrice.setText(total + "đ");
    }

    private void openPaymentActivity() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("USER_ID", userId);

        // Truyền danh sách cartItems: bạn có thể serialize qua JSON hoặc sử dụng Parcelable
        ArrayList<Integer> productIds = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<Integer> prices = new ArrayList<>();

        for (CartItem item : cartItems) {
            productIds.add(item.getProductId());
            quantities.add(item.getQuantity());
            Product p = db.getProductById(item.getProductId());
            prices.add(p != null ? (int) p.getPrice() : 0);
        }

        intent.putIntegerArrayListExtra("PRODUCT_IDS", productIds);
        intent.putIntegerArrayListExtra("QUANTITIES", quantities);
        intent.putIntegerArrayListExtra("PRICES", prices);

        startActivity(intent);
    }
}