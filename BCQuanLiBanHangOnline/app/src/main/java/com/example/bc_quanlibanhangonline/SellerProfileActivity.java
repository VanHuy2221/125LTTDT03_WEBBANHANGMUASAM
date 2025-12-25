package com.example.bc_quanlibanhangonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bc_quanlibanhangonline.database.UserDatabase;
import com.example.bc_quanlibanhangonline.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.View;
public class SellerProfileActivity extends AppCompatActivity {

    LinearLayout menuContainer;
    private BottomNavigationView bottomNav;
    private UserDatabase userDatabase;
    private CardView cardProductList, cardPendingOrders, cardRevenueStats, cardStore;
    private TextView txtProductCount, txtPendingOrderCount, txtRevenue;
    private TextView txtUserName, txtUserType, txtShopInfo;

    // 🔥 THÊM: Biến lưu userId
    private int userId = -1;
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_seller);
        userDatabase = new UserDatabase(this);

        // 🔥 LẤY userId từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("USER_ID", -1);
            userRole = intent.getStringExtra("USER_ROLE");
            if (userRole == null) {
                userRole = "";
            }
        }

        bottomNav = findViewById(R.id.bottom_nav);
        menuContainer = findViewById(R.id.menuContainer);

        initFeatureViews();
        initHeaderViews();
        setupFeatureClicks();
        setupBottomNavigation();

        bottomNav.setSelectedItemId(R.id.nav_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        addMenuItems();
        loadFeatureData();
        loadUserData(); // 🔥 LOAD DỮ LIỆU TỪ DATABASE
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
        txtUserType = findViewById(R.id.txtUserType);
        txtShopInfo = findViewById(R.id.txtShopInfo);
    }

    private void setupFeatureClicks() {
        cardProductList.setOnClickListener(v -> {
            Toast.makeText(this, "Danh sách sản phẩm", Toast.LENGTH_SHORT).show();
        });

        cardPendingOrders.setOnClickListener(v -> {
            Toast.makeText(this, "Đơn hàng chờ xử lý", Toast.LENGTH_SHORT).show();
        });

        cardRevenueStats.setOnClickListener(v -> {
            Toast.makeText(this, "Thống kê doanh thu", Toast.LENGTH_SHORT).show();
        });

        cardStore.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý gian hàng", Toast.LENGTH_SHORT).show();
        });

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
        // TODO: Load dữ liệu thực tế từ database
        int productCount = 0; // Sẽ load từ database sau
        int pendingOrderCount = 0; // Sẽ load từ database sau
        double revenue = 0; // Sẽ load từ database sau

        txtProductCount.setText(productCount + " sản phẩm");
        txtPendingOrderCount.setText(pendingOrderCount + " đơn chờ");
        txtRevenue.setText(formatCurrency(revenue));
    }

    private void loadUserData() {
        if (userId == -1) {
            txtUserName.setText("Đang tải...");
            return;
        }

        try {
            User user = userDatabase.getUserById(userId);

            if (user != null) {
                // Hiển thị tên người dùng
                txtUserName.setText(user.getFullName() != null ? user.getFullName() : user.getEmail());

                // 🔥 LẤY ROLE TỪ DATABASE VÀ HIỂN THỊ
                String role = user.getRole() != null ? user.getRole() : "seller";

                if (txtUserType != null) {
                    txtUserType.setVisibility(View.VISIBLE);

                    // Hiển thị role theo tiếng Việt
                    String roleDisplay;
                    int roleColor;

                    switch (role.toLowerCase()) {
                        case "admin":
                            roleDisplay = "Quản trị viên";
                            roleColor = getResources().getColor(android.R.color.holo_red_dark);
                            break;
                        case "seller":
                            roleDisplay = "Người bán";
                            roleColor = getResources().getColor(android.R.color.holo_orange_dark);
                            break;
                        case "customer":
                        default:
                            roleDisplay = "Khách hàng";
                            roleColor = getResources().getColor(android.R.color.holo_green_dark);
                            break;
                    }

                    txtUserType.setText(roleDisplay);
                    txtUserType.setTextColor(roleColor);
                }

                // Hiển thị thông tin shop
                if (txtShopInfo != null) {
                    String shopInfo = "Chào mừng đến với gian hàng của bạn!";
                    if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                        shopInfo = "Địa chỉ: " + user.getAddress();
                    }
                    txtShopInfo.setText(shopInfo);
                }

            } else {
                txtUserName.setText("Người dùng không tồn tại");
                if (txtUserType != null) {
                    txtUserType.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            txtUserName.setText("Lỗi tải dữ liệu");
            if (txtUserType != null) {
                txtUserType.setVisibility(View.GONE);
            }
        }
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
        addItem("Khuyến mãi & Voucher", R.drawable.ic_sale, v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        addItem("Tin nhắn", R.drawable.ic_message, v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        addItem("Đánh giá & Phản hồi", R.drawable.ic_positive_review, v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        addItem("Hỗ trợ & Liên hệ", R.drawable.ic_support, v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        addItem("Cài đặt tài khoản", R.drawable.ic_settings, v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        addItem("Đăng xuất", R.drawable.ic_logout, v -> {
            performLogout();
        });
    }

    private void addItem(String title, int iconRes, View.OnClickListener clickListener) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_menu, menuContainer, false);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(iconRes);
        txtTitle.setText(title);

        view.setOnClickListener(clickListener);
        menuContainer.addView(view);

        View divider = getLayoutInflater().inflate(R.layout.divider_item, menuContainer, false);
        menuContainer.addView(divider);
    }

    // 🔥 SỬA: Thêm truyền userId khi chuyển Activity
    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(SellerProfileActivity.this, HomeActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_order) {
                Intent intent = new Intent(SellerProfileActivity.this, OrderTrackingActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", userRole);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                Intent intent = new Intent(SellerProfileActivity.this, CartActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_account) {
                return true;
            }
            return false;
        });
    }

    private void performLogout() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {

                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    prefs.edit().clear().apply();

                    Intent intent = new Intent(SellerProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finishAffinity();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDatabase != null) {
            userDatabase.close();
        }
    }
}