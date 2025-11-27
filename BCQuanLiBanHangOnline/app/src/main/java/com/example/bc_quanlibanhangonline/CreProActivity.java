package com.example.bc_quanlibanhangonline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreProActivity extends AppCompatActivity {

    private EditText etProductName, etProductPrice, etProductDescription;
    private Button btnSelectCategory, btnConfirm;
    private TextView tvSelectedCategory;
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cre_pro);

        // Ánh xạ view
        initViews();

        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });

        // Xử lý sự kiện nút xác nhận
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmProduct();
            }
        });
    }

    private void initViews() {
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnSelectCategory = findViewById(R.id.btnSelectCategory);
        tvSelectedCategory = findViewById(R.id.tvSelectedCategory);
        btnConfirm = findViewById(R.id.btnConfirm);
    }

    private void showCategoryDialog() {
        final String[] categories = {
                "Điện thoại",
                "Laptop",
                "Tablet",
                "Phụ kiện điện tử",
                "Thời trang nam",
                "Thời trang nữ",
                "Giày dép",
                "Túi xách",
                "Đồng hồ",
                "Mỹ phẩm",
                "Nội thất",
                "Đồ gia dụng",
                "Thiết bị nhà bếp",
                "Sách vở",
                "Thể thao",
                "Đồ chơi",
                "Mẹ và bé",
                "Ô tô - Xe máy",
                "Khác"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục sản phẩm");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategory = categories[which];
                tvSelectedCategory.setText("Đã chọn: " + selectedCategory);
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void confirmProduct() {
        String productName = etProductName.getText().toString().trim();
        String productPrice = etProductPrice.getText().toString().trim();
        String productDescription = etProductDescription.getText().toString().trim();

        // Kiểm tra validation
        if (productName.isEmpty()) {
            showToast("Vui lòng nhập tên sản phẩm");
            return;
        }

        if (productPrice.isEmpty()) {
            showToast("Vui lòng nhập giá sản phẩm");
            return;
        }

        if (productDescription.isEmpty()) {
            showToast("Vui lòng nhập mô tả sản phẩm");
            return;
        }

        if (selectedCategory.isEmpty()) {
            showToast("Vui lòng chọn danh mục sản phẩm");
            return;
        }

        String message = "Đăng bán thành công!\n" +
                "Tên: " + productName + "\n" +
                "Giá: " + productPrice + " VND\n" +
                "Danh mục: " + selectedCategory;

        showToast(message);

        // Reset form (tùy chọn)
        resetForm();
    }

    private void resetForm() {
        etProductName.setText("");
        etProductPrice.setText("");
        etProductDescription.setText("");
        tvSelectedCategory.setText("");
        selectedCategory = "";
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}