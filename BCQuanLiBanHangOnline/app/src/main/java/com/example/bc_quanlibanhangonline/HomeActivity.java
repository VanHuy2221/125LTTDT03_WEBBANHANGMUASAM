package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.CategoryAdapter;
import com.example.bc_quanlibanhangonline.adapters.ProductHorizontalAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Category;
import com.example.bc_quanlibanhangonline.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private RecyclerView rvCategories;
    private RecyclerView rvFeaturedProducts;
    private RecyclerView rvRecommendedProducts;

    private CategoryAdapter categoryAdapter;
    private ProductHorizontalAdapter featuredProductAdapter;
    private ProductHorizontalAdapter recommendedProductAdapter;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        initViews();
        initDatabase();
        setupRecyclerViews();
        loadData();
        setupBottomNavigation();
        setupSearchBarClickListener();
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
        // Setup Categories RecyclerView
        GridLayoutManager categoryLayoutManager = new GridLayoutManager(this, 4);
        rvCategories.setLayoutManager(categoryLayoutManager);

        // Setup Featured Products RecyclerView
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedProducts.setLayoutManager(featuredLayoutManager);

        // Setup Recommended Products RecyclerView
        LinearLayoutManager recommendedLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecommendedProducts.setLayoutManager(recommendedLayoutManager);
    }

    private void loadData() {
        // Load Categories
        List<Category> categories = databaseHelper.getCategories();
        categoryAdapter = new CategoryAdapter(this, categories);
        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                // Navigate to category products page
                Intent intent = new Intent(HomeActivity.this, CategoryProductsActivity.class);
                intent.putExtra("CATEGORY_ID", category.getCategoryId());
                intent.putExtra("CATEGORY_NAME", category.getCategoryName());
                startActivity(intent);
            }
        });
        rvCategories.setAdapter(categoryAdapter);

        // Load Featured Products
        List<Product> featuredProducts = databaseHelper.getFeaturedProducts();
        featuredProductAdapter = new ProductHorizontalAdapter(this, featuredProducts);
        featuredProductAdapter.setOnProductClickListener(new ProductHorizontalAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                navigateToProductDetail(product);
            }
        });
        rvFeaturedProducts.setAdapter(featuredProductAdapter);

        // Load Recommended Products
        List<Product> recommendedProducts = databaseHelper.getRecommendedProducts();
        recommendedProductAdapter = new ProductHorizontalAdapter(this, recommendedProducts);
        recommendedProductAdapter.setOnProductClickListener(new ProductHorizontalAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                navigateToProductDetail(product);
            }
        });
        rvRecommendedProducts.setAdapter(recommendedProductAdapter);
    }

    private void setupSearchBarClickListener() {
        // Tìm thanh tìm kiếm trong layout và thêm sự kiện click
        View searchBar = findViewById(R.id.search_bar);
        if (searchBar != null) {
            searchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang SearchListActivity khi bấm vào thanh tìm kiếm
                    Intent intent = new Intent(HomeActivity.this, GlobalSearchActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", product.getProductName());
        intent.putExtra("PRODUCT_PRICE", (int) product.getPrice());
        intent.putExtra("PRODUCT_DESCRIPTION", product.getDescription());
        intent.putExtra("PRODUCT_IMAGE", product.getImageResource());
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;

            } else if (itemId == R.id.nav_order) {
                startActivity(new Intent(this, OrderTrackingActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(this, SellerProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }

            return false;
        });
    }
}