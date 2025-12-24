package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private EditText etSearch;
    private ImageView btnClearSearch;
    private RecyclerView rvProducts;
    private LinearLayout layoutEmptyState;
    private TextView tvEmptyMessage;
    private TextView tvEmptySuggestion;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    
    private int categoryId;
    private String categoryName;
    private List<Product> allCategoryProducts;
    private List<Product> filteredProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName == null) categoryName = "Danh mục";

        initViews();
        initDatabase();
        setupRecyclerView();
        loadProducts();
        setupSearch();
        setupBackButton();
    }

    private void initViews() {
        tvCategoryTitle = findViewById(R.id.tv_category_title);
        etSearch = findViewById(R.id.et_search);
        btnClearSearch = findViewById(R.id.btn_clear_search);
        rvProducts = findViewById(R.id.rv_products);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        tvEmptyMessage = findViewById(R.id.tv_empty_message);
        tvEmptySuggestion = findViewById(R.id.tv_empty_suggestion);
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
            allCategoryProducts = databaseHelper.getProductsByCategory(categoryId);
            filteredProducts = allCategoryProducts;
            
            if (allCategoryProducts == null || allCategoryProducts.isEmpty()) {
                // Nếu không có sản phẩm, hiển thị thông báo
                showEmptyState("Danh mục này chưa có sản phẩm nào", "Vui lòng quay lại sau");
                tvCategoryTitle.setText(categoryName + " (Không có sản phẩm)");
                return;
            }
            
            setupProductAdapter();
            showProductList();
            updateTitle("");
            
        } catch (Exception e) {
            e.printStackTrace();
            showEmptyState("Có lỗi xảy ra", "Vui lòng thử lại sau");
            tvCategoryTitle.setText("Lỗi: " + e.getMessage());
        }
    }

    private void setupProductAdapter() {
        productAdapter = new ProductAdapter(this, filteredProducts);
        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                navigateToProductDetail(product);
            }
        });
        rvProducts.setAdapter(productAdapter);
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

    private void setupSearch() {
        // TextWatcher để theo dõi thay đổi text
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                
                // Hiển thị/ẩn nút clear
                if (query.isEmpty()) {
                    btnClearSearch.setVisibility(View.GONE);
                } else {
                    btnClearSearch.setVisibility(View.VISIBLE);
                }
                
                // Thực hiện search
                performSearch(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        // Nút clear search
        btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                etSearch.clearFocus();
            }
        });
    }

    private void performSearch(String query) {
        if (allCategoryProducts == null) return;
        
        if (query.isEmpty()) {
            // Nếu không có query, hiển thị tất cả sản phẩm trong category
            filteredProducts = allCategoryProducts;
        } else {
            // Tìm kiếm trong category
            filteredProducts = databaseHelper.searchProductsInCategory(categoryId, query);
        }
        
        // Kiểm tra kết quả và hiển thị UI phù hợp
        if (filteredProducts == null || filteredProducts.isEmpty()) {
            if (query.isEmpty()) {
                showEmptyState("Danh mục này chưa có sản phẩm nào", "Vui lòng quay lại sau");
            } else {
                showEmptyState("Không tìm thấy sản phẩm thích hợp", "Hãy thử tìm kiếm với từ khóa khác");
            }
        } else {
            showProductList();
            // Cập nhật adapter
            if (productAdapter != null) {
                productAdapter.updateData(filteredProducts);
            } else {
                setupProductAdapter();
            }
        }
        
        // Cập nhật title với số lượng kết quả
        updateTitle(query);
    }

    private void updateTitle(String query) {
        if (filteredProducts == null || filteredProducts.isEmpty()) {
            if (query.isEmpty()) {
                tvCategoryTitle.setText(categoryName + " (0 sản phẩm)");
            } else {
                tvCategoryTitle.setText(categoryName + " (0 kết quả)");
            }
        } else {
            if (query.isEmpty()) {
                tvCategoryTitle.setText(categoryName + " (" + filteredProducts.size() + " sản phẩm)");
            } else {
                tvCategoryTitle.setText(categoryName + " (" + filteredProducts.size() + " kết quả)");
            }
        }
    }
    private void showEmptyState(String message, String suggestion) {
        rvProducts.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
        
        if (tvEmptyMessage != null) {
            tvEmptyMessage.setText(message);
        }
        if (tvEmptySuggestion != null) {
            tvEmptySuggestion.setText(suggestion);
        }
    }

    private void showProductList() {
        rvProducts.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
    }
}