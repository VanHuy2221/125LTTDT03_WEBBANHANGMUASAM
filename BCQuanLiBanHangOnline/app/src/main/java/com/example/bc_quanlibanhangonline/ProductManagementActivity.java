package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.ProductManagementAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private FloatingActionButton btnAddProduct;
    private ImageButton btnBack;
    private DatabaseHelper db;
    private List<Product> productList;
    private ProductManagementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        db = new DatabaseHelper(this);

        // Kiểm tra database
        checkDatabase();

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.btnBack);

        // Load danh sách sản phẩm
        loadProducts();

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductManagementActivity.this, CreProActivity.class);
            startActivityForResult(intent, 1); // Dùng startActivityForResult
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Gọi phương thức back mặc định
            }
        });
    }

    private void checkDatabase() {
        // Kiểm tra xem có sản phẩm nào không
        List<Product> products = db.getAllProductsCombined();
        Log.d("ProductManagement", "Total products in DB: " + products.size());
    }

    private void loadProducts() {
        // Thêm log
        productList = db.getAllProductsCombined();

        Log.d("ProductManagement", "Loaded " + productList.size() + " products");

        if (productList.isEmpty()) {
            Toast.makeText(this, "Chưa có sản phẩm nào", Toast.LENGTH_SHORT).show();
        } else {
            for (Product p : productList) {
                Log.d("ProductManagement", "Product: " + p.getProductId() + " - " + p.getProductName());
            }
        }

        adapter = new ProductManagementAdapter(this, productList, db);
        recyclerViewProducts.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("refresh_needed", false)) {
                loadProducts(); // Refresh danh sách
                int newProductId = data.getIntExtra("new_product_id", -1);
                if (newProductId != -1) {
                    Log.d("ProductManagement", "New product added with ID: " + newProductId);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Khi quay lại, load lại danh sách
        loadProducts();
    }
}
