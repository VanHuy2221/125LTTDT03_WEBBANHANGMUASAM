package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UnProfileActivity extends AppCompatActivity {
    LinearLayout menuContainer;
    private BottomNavigationView bottomNav;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_unlogin); // ĐẢM BẢO ĐÚNG LAYOUT

        Log.d("UnProfileActivity", "Activity started with layout: activity_profile_unlogin");

        try {
            bottomNav = findViewById(R.id.bottom_nav);
            menuContainer = findViewById(R.id.menuContainer);
            btnLogin = findViewById(R.id.btnLogin);

            if (btnLogin == null) {
                Log.e("UnProfileActivity", "ERROR: btnLogin is NULL!");
            } else {
                Log.d("UnProfileActivity", "btnLogin found: " + btnLogin.getText().toString());
            }

            setupLoginClickListener();
            setupBottomNavigation();
            addMenuItems();

            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_account);
            }

        } catch (Exception e) {
            Log.e("UnProfileActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khởi tạo giao diện", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupLoginClickListener() {
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("UnProfileActivity", "Login button clicked");
                    LoginAction();
                }
            });
            Log.d("UnProfileActivity", "Login click listener set");
        } else {
            Log.e("UnProfileActivity", "Cannot set click listener - btnLogin is null!");
            // Thử tìm lại
            View loginView = findViewById(R.id.btnLogin);
            if (loginView instanceof TextView) {
                btnLogin = (TextView) loginView;
                btnLogin.setOnClickListener(v -> LoginAction());
            }
        }
    }

    private void addMenuItems() {
        if (menuContainer == null) {
            Log.e("UnProfileActivity", "menuContainer is null!");
            return;
        }

        try {
            String[] menuTitles = {
                    "Thông tin cá nhân",
                    "Tin nhắn",
                    "Địa chỉ giao hàng",
                    "Đơn hàng của tôi",
                    "Sản phẩm yêu thích",
                    "Phương thức thanh toán",
                    "Cài đặt thông báo",
                    "Cài đặt"
            };

            int[] menuIcons = {
                    R.drawable.ic_person,
                    R.drawable.ic_message,
                    R.drawable.ic_location,
                    R.drawable.ic_cart,
                    R.drawable.ic_heart,
                    R.drawable.ic_payment,
                    R.drawable.ic_bell,
                    R.drawable.ic_settings
            };

            for (int i = 0; i < menuTitles.length; i++) {
                addItem(menuTitles[i], menuIcons[i]);
            }

            Log.d("UnProfileActivity", "Added " + menuTitles.length + " menu items");

        } catch (Exception e) {
            Log.e("UnProfileActivity", "Error adding menu items: " + e.getMessage(), e);
        }
    }

    private void addItem(String title, int iconRes) {
        try {
            // Tạo view từ layout item_menu
            View view = LayoutInflater.from(this).inflate(R.layout.item_menu, null);

            ImageView imgIcon = view.findViewById(R.id.imgIcon);
            TextView txtTitle = view.findViewById(R.id.txtTitle);

            if (imgIcon != null) {
                imgIcon.setImageResource(iconRes);
            }
            if (txtTitle != null) {
                txtTitle.setText(title);
            }

            view.setOnClickListener(v -> {
                Toast.makeText(UnProfileActivity.this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                LoginAction();
            });

            if (menuContainer != null) {
                menuContainer.addView(view);
            }

        } catch (Exception e) {
            Log.e("UnProfileActivity", "Error adding item: " + title + " - " + e.getMessage());
        }
    }

    private void setupBottomNavigation() {
        if (bottomNav == null) {
            Log.e("UnProfileActivity", "bottomNav is null!");
            return;
        }

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d("UnProfileActivity", "Bottom nav item clicked: " + itemId);

            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(UnProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_order) {
                Toast.makeText(this, "Vui lòng đăng nhập để xem đơn hàng", Toast.LENGTH_SHORT).show();
                LoginAction();
                return true;
            } else if (itemId == R.id.nav_cart) {
                Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
                LoginAction();
                return true;
            } else if (itemId == R.id.nav_account) {
                // Đã ở UnProfile rồi
                return true;
            }
            return false;
        });

        Log.d("UnProfileActivity", "Bottom navigation setup completed");
    }

    private void LoginAction() {
        Log.d("UnProfileActivity", "Starting LoginActivity");
        try {
            Intent intent = new Intent(UnProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("UnProfileActivity", "Error starting LoginActivity: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi mở trang đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}