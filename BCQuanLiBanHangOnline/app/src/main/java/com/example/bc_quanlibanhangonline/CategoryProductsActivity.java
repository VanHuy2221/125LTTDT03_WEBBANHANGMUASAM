package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bc_quanlibanhangonline.adapters.ProductAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Product;
import java.util.List;

public class CategoryProductsActivity extends AppCompatActivity {
    private TextView tvCategoryTitle;
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_category_products);

            // Nhận dữ liệu từ Intent
            categoryId = getIntent().getIntExtra("CATEGORY_ID", 1);
            categoryName = getIntent().getStringExtra("CATEGORY_NAME");

            if (categoryName == null) {
                categoryName = "Danh mục";
            }

            initViews();
            initDatabase();
            setupRecyclerView();
            loadProducts();
            setupBackButton();
            
        } catch (Exception e) {
            e.printStackTrace();
            finish(); // Đóng activity nếu có lỗi
        }
    }

    private void initViews() {
        tvCategoryTitle = findViewById(R.id.tv_category_title);
        rvProducts = findViewById(R.id.rv_products);
        
        if (tvCategoryTitle == null || rvProducts == null) {
            throw new RuntimeException("Cannot find required views in layout");
        }
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
    }

    private void loadProducts() {
        tvCategoryTitle.setText(categoryName);
        
        try {
            List<Product> products = databaseHelper.getProductsByCategory(categoryId);
            
            if (products == null || products.isEmpty()) {
                // Nếu không có sản phẩm, hiển thị thông báo
                tvCategoryTitle.setText(categoryName + " (Không có sản phẩm)");
                return;
            }
            
            productAdapter = new ProductAdapter(this, products);
            productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
                @Override
                public void onProductClick(Product product) {
                    navigateToProductDetail(product);
                }
            });
            rvProducts.setAdapter(productAdapter);
            
        } catch (Exception e) {
            e.printStackTrace();
            tvCategoryTitle.setText("Lỗi: " + e.getMessage());
        }
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(CategoryProductsActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", product.getProductName());
        intent.putExtra("PRODUCT_PRICE", (int) product.getPrice());
        intent.putExtra("PRODUCT_DESCRIPTION", product.getDescription());
        intent.putExtra("PRODUCT_IMAGE", product.getImageResource());
        startActivity(intent);
    }

    private void setupBackButton() {
        View backButton = findViewById(R.id.btn_back);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}