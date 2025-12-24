package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.bc_quanlibanhangonline.adapters.CategoryAdapter;
import com.example.bc_quanlibanhangonline.adapters.ProductHorizontalAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Category;
import com.example.bc_quanlibanhangonline.models.Product;
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

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", product.getProductName());
        intent.putExtra("PRODUCT_PRICE", (int) product.getPrice());
        intent.putExtra("PRODUCT_DESCRIPTION", product.getDescription());
        intent.putExtra("PRODUCT_IMAGE", product.getImageResource());
        startActivity(intent);
    }

    private void setupSearchBarClickListener() {
        View searchBar = findViewById(R.id.search_bar);
        if (searchBar != null) {
            searchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển đến GlobalSearchActivity để tìm kiếm trong tất cả danh mục
                    Intent intent = new Intent(HomeActivity.this, GlobalSearchActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Đã ở Home rồi nên không cần làm gì
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến OrderTrackingActivity
                Intent intent = new Intent(HomeActivity.this, OrderTrackingActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_account) {
                // Chuyển đến AccountActivity
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}