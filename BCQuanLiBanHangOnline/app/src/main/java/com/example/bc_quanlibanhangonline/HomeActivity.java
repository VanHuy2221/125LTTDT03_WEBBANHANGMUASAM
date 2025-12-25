package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.CategoryAdapter;
import com.example.bc_quanlibanhangonline.adapters.ProductHorizontalAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Category;
import com.example.bc_quanlibanhangonline.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.Toast;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private RecyclerView rvCategories, rvFeaturedProducts, rvRecommendedProducts;
    private CategoryAdapter categoryAdapter;
    private ProductHorizontalAdapter featuredProductAdapter, recommendedProductAdapter;
    private DatabaseHelper databaseHelper;
    private OnBackPressedCallback backCallback;

    private int userId = -1;
    private String userRole = "";

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("HomeActivity", "=== START HomeActivity ===");

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load thông tin user từ Intent trước (khi từ Login/Register về)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");
            Log.d("HomeActivity", "From Intent - UserID: " + userId + ", Role: " + userRole);

            if (userId != -1) {
                saveUserToPrefs(userId, userRole);
            }
        } else {
            // Nếu không có từ Intent, lấy từ SharedPreferences
            loadUserFromPrefs();
            Log.d("HomeActivity", "From Prefs - UserID: " + userId + ", Role: " + userRole);
        }

        initViews();
        initDatabase();
        setupRecyclerViews();
        loadData();
        setupBottomNavigation();
        setupSearchBarClickListener();
        setupBackPressedHandler();

        bottomNav.setSelectedItemId(R.id.nav_home);
        Log.d("HomeActivity", "Setup completed");
    }

    private void initViews() {
        bottomNav = findViewById(R.id.bottom_nav);
        rvCategories = findViewById(R.id.rv_categories);
        rvFeaturedProducts = findViewById(R.id.rv_featured_products);
        rvRecommendedProducts = findViewById(R.id.rv_recommended_products);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerViews() {
        rvCategories.setLayoutManager(new GridLayoutManager(this, 4));
        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecommendedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadData() {
        new Thread(() -> {
            List<Category> categories = databaseHelper.getCategories();
            List<Product> featuredProducts = databaseHelper.getFeaturedProducts();
            List<Product> recommendedProducts = databaseHelper.getRecommendedProducts();

            runOnUiThread(() -> {
                categoryAdapter = new CategoryAdapter(this, categories);
                categoryAdapter.setOnCategoryClickListener(category -> {
                    Intent intent = new Intent(HomeActivity.this, CategoryProductsActivity.class);
                    intent.putExtra("CATEGORY_ID", category.getCategoryId());
                    intent.putExtra("CATEGORY_NAME", category.getCategoryName());
                    if (userId != -1) intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                rvCategories.setAdapter(categoryAdapter);

                featuredProductAdapter = new ProductHorizontalAdapter(this, featuredProducts);
                featuredProductAdapter.setOnProductClickListener(this::navigateToProductDetail);
                rvFeaturedProducts.setAdapter(featuredProductAdapter);

                recommendedProductAdapter = new ProductHorizontalAdapter(this, recommendedProducts);
                recommendedProductAdapter.setOnProductClickListener(this::navigateToProductDetail);
                rvRecommendedProducts.setAdapter(recommendedProductAdapter);
            });
        }).start();
    }

    private void setupSearchBarClickListener() {
        View searchBar = findViewById(R.id.search_bar);
        if (searchBar != null) {
            searchBar.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, GlobalSearchActivity.class);
                if (userId != -1) intent.putExtra("USER_ID", userId);
                startActivity(intent);
            });
        }
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", product.getProductName());
        intent.putExtra("PRODUCT_PRICE", (int) product.getPrice());
        intent.putExtra("PRODUCT_DESCRIPTION", product.getDescription());
        intent.putExtra("PRODUCT_IMAGE", product.getImageResource());
        if (userId != -1) intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d("HomeActivity", "Bottom nav clicked: " + itemId);

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_order) {
                if (userId == -1) {
                    // Chưa đăng nhập
                    Intent intent = new Intent(this, UnProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent orderIntent = new Intent(this, OrderTrackingActivity.class);
                    orderIntent.putExtra("USER_ID", userId);
                    orderIntent.putExtra("USER_ROLE", userRole);
                    startActivity(orderIntent);
                }
                return true;
            } else if (itemId == R.id.nav_cart) {
                if (userId == -1) {
                    // Chưa đăng nhập
                    Intent intent = new Intent(this, UnProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent cartIntent = new Intent(this, CartActivity.class);
                    cartIntent.putExtra("USER_ID", userId);
                    startActivity(cartIntent);
                }
                return true;
            } else if (itemId == R.id.nav_account) {
                navigateToAccount();
                return true;
            }
            return false;
        });
    }

    private void navigateToAccount() {
        Log.d("HomeActivity", "navigateToAccount called - UserID: " + userId + ", Role: " + userRole);

        try {
            Intent intent;

            if (userId == -1) {
                Log.d("HomeActivity", "User not logged in, going to UnProfileActivity");
                intent = new Intent(this, UnProfileActivity.class);
            } else if ("admin".equalsIgnoreCase(userRole)) {
                Log.d("HomeActivity", "User is admin, going to AdminDashboardActivity");
                intent = new Intent(this, com.example.bc_quanlibanhangonline.admin.AdminDashboardActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
            } else if ("seller".equalsIgnoreCase(userRole)) {
                Log.d("HomeActivity", "User is seller, going to SellerProfileActivity");
                intent = new Intent(this, SellerProfileActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
            } else {
                Log.d("HomeActivity", "User is customer, going to ProfileActivity");
                intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
            }

            Log.d("HomeActivity", "Starting activity: " + intent.getComponent().getClassName());
            startActivity(intent);

        } catch (Exception e) {
            Log.e("HomeActivity", "ERROR in navigateToAccount: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi chuyển trang", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToPrefs(int userId, String userRole) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_ROLE, userRole != null ? userRole : "customer");
        editor.apply();
        Log.d("HomeActivity", "Saved to Prefs - UserID: " + userId + ", Role: " + userRole);
    }

    private void loadUserFromPrefs() {
        userId = sharedPreferences.getInt(KEY_USER_ID, -1);
        userRole = sharedPreferences.getString(KEY_USER_ROLE, "customer");
    }

    private void setupBackPressedHandler() {
        backCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backCallback);
    }

    private void showExitConfirmationDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Thoát ứng dụng")
                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng?")
                .setPositiveButton("Thoát", (dialog, which) -> finishAffinity())
                .setNegativeButton("Ở lại", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("HomeActivity", "onStart - UserID: " + userId);
    }
}