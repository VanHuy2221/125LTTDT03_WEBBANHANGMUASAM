package com.example.bc_quanlibanhangonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerProfileActivity extends AppCompatActivity {

    LinearLayout menuContainer;
    private BottomNavigationView bottomNav;

    // Khai báo các view cho 4 ô chức năng
    private CardView cardProductList, cardPendingOrders, cardRevenueStats, cardStore;
    private TextView txtProductCount, txtPendingOrderCount, txtRevenue;
    private TextView txtUserName, txtFollowerCount, txtFollowingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_seller);

        bottomNav = findViewById(R.id.bottom_nav);
        menuContainer = findViewById(R.id.menuContainer);

        // Ánh xạ các view cho 4 ô chức năng
        initFeatureViews();

        // Ánh xạ các view cho header
        initHeaderViews();

        // Thiết lập sự kiện click cho 4 ô chức năng
        setupFeatureClicks();

        // Thiết lập bottom navigation
        setupBottomNavigation();

        // Đặt item "Account" là selected (vì đang ở ProfileActivity)
        bottomNav.setSelectedItemId(R.id.nav_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        addMenuItems();

        // Tải dữ liệu cho các ô chức năng
        loadFeatureData();

        // Tải thông tin người dùng
        loadUserData();
    }

    private void initFeatureViews() {
        cardProductList = findViewById(R.id.cardProductList);
        cardPendingOrders = findViewById(R.id.cardPendingOrders);
        cardRevenueStats = findViewById(R.id.cardRevenueStats);
        cardStore = findViewById(R.id.cardStore);

        txtProductCount = findViewById(R.id.txtProductCount);
        txtPendingOrderCount = findViewById(R.id.txtPendingOrderCount);
        txtRevenue = findViewById(R.id.txtRevenue);
    }

    private void initHeaderViews() {
        txtUserName = findViewById(R.id.txtUserName);
        // Nếu có TextView cho follower/following
        // txtFollowerCount = findViewById(R.id.txtFollowerCount);
        // txtFollowingCount = findViewById(R.id.txtFollowingCount);
    }

    private void setupFeatureClicks() {
        // 1. Danh sách sản phẩm
        cardProductList.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfileActivity.this, OrderManagementActivity.class);
            startActivity(intent);
        });

        // 2. Đơn hàng chờ
        cardPendingOrders.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfileActivity.this, OrderManagementActivity.class);
            intent.putExtra("tab", "pending");
            startActivity(intent);
        });

        // 3. Doanh thu & Thống kê
        cardRevenueStats.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfileActivity.this, OrderManagementActivity.class);
            startActivity(intent);
        });

        // 4. Gian hàng
        cardStore.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProfileActivity.this, OrderManagementActivity.class);
            startActivity(intent);
        });

        // Thêm hiệu ứng click
        setClickEffect(cardProductList);
        setClickEffect(cardPendingOrders);
        setClickEffect(cardRevenueStats);
        setClickEffect(cardStore);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickEffect(CardView cardView) {
        cardView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.7f);
                    v.setScaleX(0.98f);
                    v.setScaleY(0.98f);
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1f);
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                    break;
            }
            return false;
        });
    }

    private void loadFeatureData() {
        // TODO: Thay thế bằng API call thực tế
        // Giả lập dữ liệu cho seller

        // Giả lập dữ liệu từ API
        int productCount = 42;
        int pendingOrderCount = 8;
        double revenue = 87500000; // 87.5 triệu

        // Cập nhật UI
        txtProductCount.setText(productCount + " sản phẩm");
        txtPendingOrderCount.setText(pendingOrderCount + " đơn chờ");
        txtRevenue.setText(formatCurrency(revenue));

        // Nếu có API, gọi ở đây:
        // loadDataFromAPI();
    }

    private void loadUserData() {
        // TODO: Thay thế bằng API call thực tế
        // Giả lập dữ liệu người dùng

        String userName = "Shop XYZ";
        int followerCount = 1250;
        int followingCount = 45;

        // Cập nhật UI
        txtUserName.setText(userName);

        // Nếu có TextView cho follower/following
        // txtFollowerCount.setText(followerCount + " Người theo dõi");
        // txtFollowingCount.setText(followingCount + " Đang theo dõi");
    }

    private String formatCurrency(double amount) {
        if (amount >= 1000000000) {
            return String.format("%.1f tỷ", amount / 1000000000);
        } else if (amount >= 1000000) {
            return String.format("%.0f triệu", amount / 1000000);
        } else if (amount >= 1000) {
            return String.format("%.0fK", amount / 1000);
        }
        return String.format("%.0f", amount);
    }

    private void addMenuItems() {
        // Các menu dành cho seller
        addItem("Khuyến mãi & Voucher", R.drawable.ic_sale, v -> {

        });

        addItem("Tin nhắn", R.drawable.ic_message, v -> {
            Intent intent = new Intent(SellerProfileActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        addItem("Đánh giá & Phản hồi", R.drawable.ic_positive_review, v -> {

        });

        addItem("Hỗ trợ & Liên hệ", R.drawable.ic_support, v -> {

        });

        addItem("Cài đặt tài khoản", R.drawable.ic_settings, v -> {

        });

        addItem("Đăng xuất", R.drawable.ic_logout, v -> {
            // Xử lý đăng xuất
            performLogout();
        });
    }

    private void addItem(String title, int iconRes, View.OnClickListener clickListener) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_menu, menuContainer, false);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(iconRes);
        txtTitle.setText(title);

        // Click handler - sử dụng listener được truyền vào
        view.setOnClickListener(clickListener);

        menuContainer.addView(view);

        // Add divider
        View divider = getLayoutInflater().inflate(R.layout.divider_item, menuContainer, false);
        menuContainer.addView(divider);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Chuyển về Home và kết thúc Activity hiện tại
                Intent intent = new Intent(SellerProfileActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến OrderTrackingActivity
                Intent intent = new Intent(SellerProfileActivity.this, OrderTrackingActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(SellerProfileActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_account) {
                // Đã ở Profile rồi, không làm gì cả
                return true;
            }
            return false;
        });
    }

    private void performLogout() {
        // TODO: Xử lý logic đăng xuất
        // Ví dụ: xóa token, clear shared preferences, v.v.

        // Hiển thị dialog xác nhận
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    // Xử lý đăng xuất
                    // SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    // preferences.edit().clear().apply();

                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(SellerProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật dữ liệu mỗi khi quay lại màn hình
        loadFeatureData();
        loadUserData();
    }

    // Phương thức để cập nhật số liệu từ bên ngoài
    public void updateProductCount(int count) {
        txtProductCount.setText(count + " sản phẩm");
    }

    public void updatePendingOrderCount(int count) {
        txtPendingOrderCount.setText(count + " đơn chờ");
    }

    public void updateRevenue(double amount) {
        txtRevenue.setText(formatCurrency(amount));
    }
}