package com.example.bc_quanlibanhangonline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExchangeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;

    private DatabaseHelper databaseHelper;
    private String targetProductName;

    // THÊM: Lấy userId từ Intent
    private int currentUserId = -1;

    private EditText edtProductName, edtDescription, edtEstimatedPrice;
    private ImageView imgProduct;
    private Button btnSubmitExchange;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        targetProductName = intent.getStringExtra("PRODUCT_NAME");

        // THÊM: Lấy userId từ Intent
        currentUserId = intent.getIntExtra("USER_ID", -1);

        initViews();
        setupEvents();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // TỰ ĐỘNG KIỂM TRA VÀ TẠO ẢNH MẪU
        checkAndCreateSampleImages();
    }

    private void initViews() {
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtEstimatedPrice = findViewById(R.id.edtEstimatedPrice);
        imgProduct = findViewById(R.id.imgProduct);
        btnSubmitExchange = findViewById(R.id.btnSubmitExchange);
    }

    private void setupEvents() {
        imgProduct.setOnClickListener(v -> showImageSelectionDialog());
        btnSubmitExchange.setOnClickListener(v -> submitExchange());
    }

    private void showImageSelectionDialog() {
        String[] options = {
                "📸 Chụp ảnh mới",
                "🖼️ Chọn từ thư viện",
                "📁 Chọn từ thư mục /images",
                "🎨 Tạo ảnh mẫu tự động"
        };

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh sản phẩm");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Chụp ảnh mới
                    takePhoto();
                    break;
                case 1: // Chọn từ thư viện
                    openGallery();
                    break;
                case 2: // Chọn từ thư mục /images
                    openImagesFolderOrAutoCreate();
                    break;
                case 3: // Tạo ảnh mẫu
                    createAutoImage();
                    break;
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng camera", Toast.LENGTH_SHORT).show();
        }
    }

    // KIỂM TRA VÀ TẠO ẢNH MẪU KHI APP CHẠY
    private void checkAndCreateSampleImages() {
        new Thread(() -> {
            try {
                String exactPath = "/storage/emulated/0/images";
                File imagesFolder = new File(exactPath);

                // Đảm bảo thư mục tồn tại
                if (!imagesFolder.exists()) {
                    imagesFolder.mkdirs();
                }

                // Kiểm tra có ảnh không
                File[] files = imagesFolder.listFiles();
                boolean hasImages = false;

                if (files != null) {
                    for (File file : files) {
                        String name = file.getName().toLowerCase();
                        if (name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                                name.endsWith(".png") || name.endsWith(".gif")) {
                            hasImages = true;
                            break;
                        }
                    }
                }

                // Nếu không có ảnh, tạo ảnh mẫu
                if (!hasImages) {
                    createSampleImagesInBackground();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // TẠO ẢNH MẪU TRONG BACKGROUND
    private void createSampleImagesInBackground() {
        try {
            String exactPath = "/storage/emulated/0/images";
            File imagesFolder = new File(exactPath);

            // Tạo 3 ảnh mẫu cơ bản
            createSimpleSampleImage(imagesFolder, "dien_thoai.jpg", "Điện thoại", "📱", Color.BLUE);
            createSimpleSampleImage(imagesFolder, "laptop.jpg", "Laptop", "💻", Color.RED);
            createSimpleSampleImage(imagesFolder, "dong_ho.jpg", "Đồng hồ", "⌚", Color.GREEN);

            runOnUiThread(() -> {
                Toast.makeText(this,
                        "✅ Đã tự động tạo 3 ảnh mẫu trong thư mục /images\n" +
                                "Bạn có thể chọn ảnh từ thư mục này",
                        Toast.LENGTH_LONG).show();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createSimpleSampleImage(File folder, String filename, String title, String emoji, int color) {
        try {
            File imageFile = new File(folder, filename);

            // Tạo bitmap 600x600
            Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            // Vẽ nền
            Paint paint = new Paint();
            paint.setColor(color);
            canvas.drawRect(0, 0, 600, 600, paint);

            // Vẽ hình tròn trắng
            paint.setColor(Color.WHITE);
            canvas.drawCircle(300, 200, 100, paint);

            // Vẽ emoji
            paint.setColor(color);
            paint.setTextSize(80);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(emoji, 300, 220, paint);

            // Vẽ tiêu đề
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            paint.setFakeBoldText(true);
            canvas.drawText(title, 300, 350, paint);

            // Vẽ text phụ
            paint.setTextSize(30);
            paint.setFakeBoldText(false);
            canvas.drawText("Sản phẩm trao đổi", 300, 400, paint);

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            canvas.drawText(date, 300, 450, paint);

            // Lưu file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MỞ THƯ MỤC HOẶC TỰ TẠO ẢNH
    private void openImagesFolderOrAutoCreate() {
        String exactPath = "/storage/emulated/0/images";
        File imagesFolder = new File(exactPath);

        // Đảm bảo thư mục tồn tại
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        // Lấy danh sách file ảnh
        File[] allFiles = imagesFolder.listFiles();
        List<File> imageFiles = new ArrayList<>();

        if (allFiles != null) {
            for (File file : allFiles) {
                String name = file.getName().toLowerCase();
                if (name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                        name.endsWith(".png") || name.endsWith(".gif") ||
                        name.endsWith(".bmp") || name.endsWith(".webp")) {
                    imageFiles.add(file);
                }
            }
        }

        // Nếu có ảnh, hiển thị cho chọn
        if (!imageFiles.isEmpty()) {
            showImageSelectionFromFolder(imageFiles);
            return;
        }

        // Nếu không có ảnh, tạo ảnh mẫu và hiển thị
        createSampleImagesNowAndShow();
    }

    // TẠO ẢNH MẪU NGAY VÀ HIỂN THỊ
    private void createSampleImagesNowAndShow() {
        new Thread(() -> {
            try {
                String exactPath = "/storage/emulated/0/images";
                File imagesFolder = new File(exactPath);

                // Tạo 3 ảnh mẫu nhanh
                createQuickSampleImage(imagesFolder, "mau_1.jpg", "iPhone 14", "📱", Color.rgb(66, 133, 244));
                createQuickSampleImage(imagesFolder, "mau_2.jpg", "MacBook Pro", "💻", Color.rgb(219, 68, 55));
                createQuickSampleImage(imagesFolder, "mau_3.jpg", "Apple Watch", "⌚", Color.rgb(15, 157, 88));

                runOnUiThread(() -> {
                    // Lấy lại danh sách file mới
                    File[] newFiles = imagesFolder.listFiles();
                    List<File> newImageFiles = new ArrayList<>();

                    if (newFiles != null) {
                        for (File file : newFiles) {
                            String name = file.getName().toLowerCase();
                            if (name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                                    name.endsWith(".png")) {
                                newImageFiles.add(file);
                            }
                        }
                    }

                    if (!newImageFiles.isEmpty()) {
                        showImageSelectionFromFolder(newImageFiles);
                        Toast.makeText(this,
                                "✅ Đã tạo 3 ảnh mẫu. Vui lòng chọn ảnh:",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    createAutoImage();
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void createQuickSampleImage(File folder, String filename, String title, String emoji, int color) {
        try {
            File imageFile = new File(folder, filename);

            Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Paint paint = new Paint();
            paint.setColor(color);
            canvas.drawRect(0, 0, 400, 400, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(emoji, 200, 150, paint);

            paint.setTextSize(40);
            paint.setFakeBoldText(true);
            canvas.drawText(title, 200, 250, paint);

            paint.setTextSize(25);
            paint.setFakeBoldText(false);
            canvas.drawText("Trao đổi", 200, 300, paint);

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // HIỂN THỊ DIALOG CHỌN ẢNH TỪ THƯ MỤC
    private void showImageSelectionFromFolder(List<File> imageFiles) {
        String[] fileNames = new String[imageFiles.size()];
        for (int i = 0; i < imageFiles.size(); i++) {
            File file = imageFiles.get(i);
            fileNames[i] = file.getName().replace(".jpg", "").replace("_", " ");
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ /images");
        builder.setItems(fileNames, (dialog, which) -> {
            File selectedFile = imageFiles.get(which);
            try {
                // Thử dùng Uri.fromFile trước (đơn giản nhất)
                selectedImageUri = Uri.fromFile(selectedFile);
                imgProduct.setImageURI(selectedImageUri);

                Toast.makeText(this,
                        "✅ Đã chọn: " + selectedFile.getName(),
                        Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                // Fallback: tạo ảnh tự động
                Toast.makeText(this, "Không thể mở ảnh, đang tạo ảnh mẫu...", Toast.LENGTH_SHORT).show();
                createAutoImage();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // TẠO ẢNH TỰ ĐỘNG DỰA TRÊN THÔNG TIN SẢN PHẨM
    private void createAutoImage() {
        try {
            String productName = edtProductName.getText().toString().trim();
            if (productName.isEmpty()) {
                productName = "Sản phẩm trao đổi";
            }

            String price = edtEstimatedPrice.getText().toString().trim();
            if (price.isEmpty()) {
                price = "Thương lượng";
            }

            // Tạo bitmap
            Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            // Nền gradient đẹp
            Paint paint = new Paint();
            paint.setShader(new android.graphics.LinearGradient(
                    0, 0, 600, 600,
                    Color.rgb(66, 133, 244), Color.rgb(15, 157, 88),
                    android.graphics.Shader.TileMode.CLAMP
            ));
            canvas.drawRect(0, 0, 600, 600, paint);

            // Vẽ icon
            paint.setShader(null);
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("🔄", 300, 180, paint);

            // Vẽ tiêu đề
            paint.setTextSize(50);
            paint.setFakeBoldText(true);
            canvas.drawText("TRAO ĐỔI", 300, 280, paint);

            // Vẽ tên sản phẩm
            paint.setTextSize(40);
            canvas.drawText(productName, 300, 340, paint);

            // Vẽ giá trị
            paint.setTextSize(30);
            paint.setFakeBoldText(false);
            canvas.drawText("Giá trị: " + price, 300, 400, paint);

            // Vẽ ngày
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            canvas.drawText(date, 300, 450, paint);

            // Vẽ trạng thái
            canvas.drawText("Đang chờ phê duyệt", 300, 500, paint);

            // Hiển thị ảnh
            imgProduct.setImageBitmap(bitmap);

            // Lưu vào cache
            File tempFile = File.createTempFile("auto_generated", ".jpg", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            selectedImageUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    tempFile);

            Toast.makeText(this, "✅ Đã tạo ảnh mẫu tự động", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        imgProduct.setImageURI(selectedImageUri);
                        Toast.makeText(this, "Đã chọn ảnh từ thư viện", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Lỗi khi hiển thị ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == TAKE_PHOTO && data != null) {
                if (data.getExtras() != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (photo != null) {
                        imgProduct.setImageBitmap(photo);
                        selectedImageUri = saveBitmapToTempFile(photo);
                        Toast.makeText(this, "Đã chụp ảnh mới", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private Uri saveBitmapToTempFile(Bitmap bitmap) {
        try {
            File tempFile = File.createTempFile("exchange_image", ".jpg", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    tempFile);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void submitExchange() {
        String name = edtProductName.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String price = edtEstimatedPrice.getText().toString().trim();

        if (name.isEmpty() || desc.isEmpty() || price.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập đầy đủ thông tin sản phẩm",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            // Tự động tạo ảnh nếu chưa có
            createAutoImage();

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Đã tạo ảnh mẫu tự động");
            builder.setMessage("Tiếp tục gửi đề nghị trao đổi với ảnh mẫu này?");
            builder.setPositiveButton("Gửi ngay", (dialog, which) -> {
                createExchangeRequest(name, desc, price);
            });
            builder.setNegativeButton("Chọn ảnh khác", (dialog, which) -> {
                dialog.dismiss();
                showImageSelectionDialog();
            });
            builder.show();
        } else {
            createExchangeRequest(name, desc, price);
        }
    }

    private void createExchangeRequest(String name, String desc, String price) {
        try {
            // Đảm bảo có userId
            if (currentUserId == -1) {
                Toast.makeText(this, "Vui lòng đăng nhập để gửi yêu cầu", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Tạo ExchangeRequest với userId
            ExchangeRequest exchange = databaseHelper.createExchange(
                    targetProductName,
                    name,
                    desc,
                    currentUserId  // TRUYỀN userId vào đây
            );

            // 2. TẠO TIN NHẮN ĐẦU TIÊN
            int sellerId = 1; // Người bán mặc định

            String messageContent = "Xin chào! Tôi muốn trao đổi sản phẩm \"" +
                    targetProductName + "\" với sản phẩm \"" +
                    name + "\" của tôi. " +
                    "Giá trị ước tính: " + price + "đ. " +
                    "Mô tả: " + desc;

            databaseHelper.createExchangeMessage(
                    currentUserId,    // senderId (người mua)
                    sellerId,         // receiverId (người bán)
                    exchange.getExchangeId(),  // exchangeId
                    messageContent     // nội dung tin nhắn
            );

            // 3. Cập nhật status
            databaseHelper.updateExchangeStatus(exchange.getExchangeId(), "Đang chờ phản hồi");

            Toast.makeText(this,
                    "✅ Đã gửi đề nghị trao đổi!\n" +
                            "Mã trao đổi: " + exchange.getExchangeId() + "\n" +
                            "Bạn có thể theo dõi trong mục Đơn hàng của tôi.",
                    Toast.LENGTH_LONG).show();

            // 4. Chuyển sang màn hình thành công
            Intent intent = new Intent(this, ExchangeSuccessActivity.class);
            intent.putExtra("PRODUCT_NAME", targetProductName);
            intent.putExtra("YOUR_PRODUCT", name);
            intent.putExtra("ESTIMATED_PRICE", price);
            intent.putExtra("DESCRIPTION", desc);
            intent.putExtra("EXCHANGE_ID", exchange.getExchangeId());
            intent.putExtra("EXCHANGE_STATUS", "Đang chờ phản hồi");
            intent.putExtra("USER_ID", currentUserId);  // THÊM userId
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "❌ Lỗi gửi đề nghị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}