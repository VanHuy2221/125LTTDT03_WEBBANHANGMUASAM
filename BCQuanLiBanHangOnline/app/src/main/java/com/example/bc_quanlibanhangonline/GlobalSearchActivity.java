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

public class GlobalSearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private ImageView btnClearSearch;
    private TextView tvSearchTitle;
    private RecyclerView rvSearchResults;
    private LinearLayout layoutEmptyState;
    private TextView tvEmptyMessage;
    private TextView tvEmptySuggestion;
    
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private List<Product> searchResults;
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);

        // Nhận query từ Intent nếu có
        String initialQuery = getIntent().getStringExtra("SEARCH_QUERY");
        if (initialQuery == null) {
            initialQuery = "";
        }

        initViews();
        initDatabase();
        setupRecyclerView();
        setupSearch();
        setupBackButton();
        
        // Thực hiện search ban đầu nếu có query
        if (!initialQuery.isEmpty()) {
            etSearch.setText(initialQuery);
            etSearch.setSelection(initialQuery.length()); // Đặt cursor ở cuối
        } else {
            showInitialState();
        }
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        btnClearSearch = findViewById(R.id.btn_clear_search);
        tvSearchTitle = findViewById(R.id.tv_search_title);
        rvSearchResults = findViewById(R.id.rv_search_results);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        tvEmptyMessage = findViewById(R.id.tv_empty_message);
        tvEmptySuggestion = findViewById(R.id.tv_empty_suggestion);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvSearchResults.setLayoutManager(layoutManager);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                currentQuery = query;
                
                // Hiển thị/ẩn nút clear
                if (query.isEmpty()) {
                    btnClearSearch.setVisibility(View.GONE);
                    showInitialState();
                } else {
                    btnClearSearch.setVisibility(View.VISIBLE);
                    performSearch(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Nút clear search
        btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                etSearch.clearFocus();
            }
        });

        // Focus vào search bar
        etSearch.requestFocus();
    }

    private void setupBackButton() {
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showInitialState() {
        tvSearchTitle.setText("Tìm kiếm sản phẩm");
        rvSearchResults.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
        tvEmptyMessage.setText("Nhập từ khóa để tìm kiếm");
        tvEmptySuggestion.setText("Tìm kiếm trong tất cả danh mục sản phẩm");
    }

    private void performSearch(String query) {
        searchResults = databaseHelper.searchProducts(query);
        
        if (searchResults == null || searchResults.isEmpty()) {
            showEmptySearchResult();
        } else {
            showSearchResults();
        }
        
        updateTitle();
    }

    private void showEmptySearchResult() {
        rvSearchResults.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
        tvEmptyMessage.setText("Không tìm thấy sản phẩm thích hợp");
        tvEmptySuggestion.setText("Hãy thử tìm kiếm với từ khóa khác");
    }

    private void showSearchResults() {
        rvSearchResults.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
        
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(this, searchResults);
            productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
                @Override
                public void onProductClick(Product product) {
                    navigateToProductDetail(product);
                }
            });
            rvSearchResults.setAdapter(productAdapter);
        } else {
            productAdapter.updateData(searchResults);
        }
    }

    private void updateTitle() {
        if (currentQuery.isEmpty()) {
            tvSearchTitle.setText("Tìm kiếm sản phẩm");
        } else if (searchResults == null || searchResults.isEmpty()) {
            tvSearchTitle.setText("Kết quả tìm kiếm \"" + currentQuery + "\" (0 sản phẩm)");
        } else {
            tvSearchTitle.setText("Kết quả tìm kiếm \"" + currentQuery + "\" (" + searchResults.size() + " sản phẩm)");
        }
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(GlobalSearchActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", product.getProductName());
        intent.putExtra("PRODUCT_PRICE", (int) product.getPrice());
        intent.putExtra("PRODUCT_DESCRIPTION", product.getDescription());
        intent.putExtra("PRODUCT_IMAGE", product.getImageResource());
        startActivity(intent);
    }
}