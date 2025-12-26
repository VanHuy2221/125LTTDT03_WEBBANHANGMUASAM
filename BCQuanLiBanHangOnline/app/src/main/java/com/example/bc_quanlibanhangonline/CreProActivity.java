package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Product;

public class CreProActivity extends AppCompatActivity {

    // Khai báo các view
    private EditText etProductName, etBrand, etPrice, etQuantity, etDescription;
    private Button btnSaveProduct, btnChooseImage;
    private ImageView imgProduct;
    private Spinner spinnerCategory, spinnerStatus;

    // Biến lưu dữ liệu
    private Uri selectedImageUri = null;
    private int productId = -1; // -1 = thêm mới, > 0 = chỉnh sửa
    private String selectedCategory = "";
    private String selectedStatus = "";
    private Product existingProduct = null;

    private DatabaseHelper db;

    // Activity result launcher cho chọn ảnh
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgProduct.setImageURI(uri);
                    // Có thể thay đổi background khi đã có ảnh
                    View imageContainer = findViewById(R.id.cardImageContainer);
                    if (imageContainer != null) {
                        imageContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cre_pro);

        db = new DatabaseHelper(this);

        // Ánh xạ view
        initViews();

        // Thiết lập Spinner
        setupSpinners();

        // Kiểm tra nếu là chỉnh sửa sản phẩm
        checkForEditMode();

        // Xử lý sự kiện
        setupEventListeners();
    }

    private void initViews() {
        etProductName = findViewById(R.id.etProductName);
        etBrand = findViewById(R.id.etBrand);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etDescription = findViewById(R.id.etDescription);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgProduct = findViewById(R.id.imgProduct);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerStatus = findViewById(R.id.spinnerStatus);
    }

    private void setupSpinners() {
        // Thiết lập Spinner Danh mục
        String[] categories = {
                "Chọn danh mục",
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
                "Sách",
                "Thể thao",
                "Đồ chơi",
                "Mẹ và bé",
                "Ô tô - Xe máy",
                "Khác"
        };

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Xử lý chọn danh mục
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCategory = categories[position];
                } else {
                    selectedCategory = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "";
            }
        });

        // Thiết lập Spinner Trạng thái
        String[] statuses = {
                "Chọn trạng thái",
                "Đang bán",
                "Ngừng bán",
                "Hết hàng",
                "Sắp về hàng"
        };

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Xử lý chọn trạng thái
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedStatus = statuses[position];
                } else {
                    selectedStatus = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStatus = "";
            }
        });
    }

    private void checkForEditMode() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product_id")) {
            productId = intent.getIntExtra("product_id", -1);
            if (productId > 0) {
                // Lấy sản phẩm từ database
                existingProduct = db.getProductByProductId(productId);

                if (existingProduct != null) {
                    // Điền dữ liệu vào form
                    fillProductData(existingProduct);
                    btnSaveProduct.setText("CẬP NHẬT SẢN PHẨM");

                    // Cập nhật title
                    TextView title = findViewById(R.id.toolbar_title);
                    if (title != null) {
                        title.setText("Chỉnh sửa sản phẩm");
                    }
                }
            }
        }
    }

    private void fillProductData(Product product) {
        etProductName.setText(product.getProductName());
        etBrand.setText(product.getBrand());
        etPrice.setText(String.valueOf(product.getPrice()));
        etQuantity.setText(String.valueOf(product.getQuantity()));
        etDescription.setText(product.getDescription());

        // Thiết lập danh mục
        String categoryName = getCategoryNameById(product.getCategoryId());
        if (categoryName != null && !categoryName.isEmpty()) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
            if (adapter != null) {
                int position = adapter.getPosition(categoryName);
                if (position >= 0) {
                    spinnerCategory.setSelection(position);
                    selectedCategory = categoryName;
                }
            }
        }

        // Thiết lập trạng thái
        if (product.getStatus() != null && !product.getStatus().isEmpty()) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerStatus.getAdapter();
            if (adapter != null) {
                int position = adapter.getPosition(product.getStatus());
                if (position >= 0) {
                    spinnerStatus.setSelection(position);
                    selectedStatus = product.getStatus();
                }
            }
        }

        // Thiết lập hình ảnh
        if (product.getImageResource() != 0) {
            imgProduct.setImageResource(product.getImageResource());
        }
    }

    private String getCategoryNameById(int categoryId) {
        switch (categoryId) {
            case 1: return "Điện thoại";
            case 2: return "Laptop";
            case 3: return "Tablet";
            case 4: return "Phụ kiện điện tử";
            case 5: return "Thời trang nam";
            case 6: return "Thời trang nữ";
            case 7: return "Giày dép";
            case 8: return "Túi xách";
            case 9: return "Đồng hồ";
            case 10: return "Mỹ phẩm";
            case 11: return "Nội thất";
            case 12: return "Đồ gia dụng";
            case 13: return "Thiết bị nhà bếp";
            case 14: return "Sách";
            case 15: return "Thể thao";
            case 16: return "Đồ chơi";
            case 17: return "Mẹ và bé";
            case 18: return "Ô tô - Xe máy";
            case 19: return "Khác";
            default: return "Khác";
        }
    }

    private void loadProductData(int id) {
        Product product = db.getProductByProductId(id);
        if (product != null) {
            // Điền dữ liệu vào form
            etProductName.setText(product.getProductName());
            etBrand.setText(product.getBrand());
            etPrice.setText(String.valueOf(product.getPrice()));
            etQuantity.setText(String.valueOf(product.getQuantity()));
            etDescription.setText(product.getDescription());

            // Thiết lập danh mục
            String category = product.getCategory();
            if (category != null && !category.isEmpty()) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
                int position = adapter.getPosition(category);
                if (position >= 0) {
                    spinnerCategory.setSelection(position);
                }
            }

            // Thiết lập trạng thái
            String status = product.getStatus();
            if (status != null && !status.isEmpty()) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerStatus.getAdapter();
                int position = adapter.getPosition(status);
                if (position >= 0) {
                    spinnerStatus.setSelection(position);
                }
            }

            // Thiết lập hình ảnh
            if (product.getImageResource() != 0) {
                imgProduct.setImageResource(product.getImageResource());
            }
        }
    }

    private void setupEventListeners() {
        // Nút chọn ảnh
        btnChooseImage.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

        // Nút lưu sản phẩm
        btnSaveProduct.setOnClickListener(v -> {
            saveProduct();
        });

        // Xử lý khi nhấn vào hình ảnh để chọn
        View imageContainer = findViewById(R.id.cardImageContainer);
        if (imageContainer != null) {
            imageContainer.setOnClickListener(v -> {
                pickImageLauncher.launch("image/*");
            });
        }
    }

    private void saveProduct() {
        // Lấy dữ liệu từ form
        String name = etProductName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validate dữ liệu
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            etProductName.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
            etPrice.requestFocus();
            return;
        }

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            etQuantity.requestFocus();
            return;
        }

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStatus.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn trạng thái", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse số
        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                Toast.makeText(this, "Giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                Toast.makeText(this, "Số lượng không được âm", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy categoryId từ tên danh mục
        int categoryId = getCategoryIdFromName(selectedCategory);

        // Tạo đối tượng Product
        Product product = new Product();
        product.setProductName(name);
        product.setBrand(brand.isEmpty() ? "Không có" : brand);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDescription(description.isEmpty() ? "Không có mô tả" : description);
        product.setSellerId(1); // Mặc định sellerId = 1
        product.setCategoryId(categoryId);
        product.setStatus(selectedStatus);
        product.setImageResource(R.drawable.ic_add_image);
        product.setRating(0.0f);

        Log.d("CreProDebug", "Saving product: " + product.getProductName());
        Log.d("CreProDebug", "Mode: " + (productId == -1 ? "ADD" : "UPDATE"));
        Log.d("CreProDebug", "Product ID: " + productId);

        // PHÂN BIỆT: Thêm mới vs Cập nhật
        long result;
        if (productId == -1) {
            // THÊM MỚI
            result = db.addProduct(product);
            if (result != -1) {
                Toast.makeText(this, "Thêm sản phẩm thành công! ID: " + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // CẬP NHẬT - QUAN TRỌNG: Set productId cho sản phẩm
            product.setProductId(productId);

            // Giữ lại các giá trị không thay đổi nếu cần
            if (existingProduct != null) {
                product.setSellerId(existingProduct.getSellerId());
                product.setRating(existingProduct.getRating());
                // Giữ nguyên image resource nếu không thay đổi
                if (selectedImageUri == null) {
                    product.setImageResource(existingProduct.getImageResource());
                }
            }

            int updateResult = db.updateProduct(product);
            if (updateResult > 0) {
                Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                result = productId;
            } else {
                Toast.makeText(this, "Cập nhật sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Trả về kết quả
        Intent resultIntent = new Intent();
        resultIntent.putExtra("refresh_needed", true);
        resultIntent.putExtra("product_id", (int) result);
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private int getCategoryIdFromName(String categoryName) {
        // Ánh xạ tên danh mục sang ID
        switch (categoryName) {
            case "Điện thoại": return 1;
            case "Laptop": return 2;
            case "Tablet": return 3;
            case "Phụ kiện điện tử": return 4;
            case "Thời trang nam": return 5;
            case "Thời trang nữ": return 6;
            case "Giày dép": return 7;
            case "Túi xách": return 8;
            case "Đồng hồ": return 9;
            case "Mỹ phẩm": return 10;
            case "Nội thất": return 11;
            case "Đồ gia dụng": return 12;
            case "Thiết bị nhà bếp": return 13;
            case "Sách": return 14;
            case "Thể thao": return 15;
            case "Đồ chơi": return 16;
            case "Mẹ và bé": return 17;
            case "Ô tô - Xe máy": return 18;
            default: return 19; // Khác
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}