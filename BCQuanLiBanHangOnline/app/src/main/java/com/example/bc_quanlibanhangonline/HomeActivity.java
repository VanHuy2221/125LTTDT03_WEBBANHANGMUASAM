package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottom_nav);

        // Xử lý sự kiện bottom navigation
        setupBottomNavigation();

        // Xử lý sự kiện click vào sản phẩm
        setupProductClickListeners();

        // THÊM SỰ KIỆN CLICK CHO THANH TÌM KIẾM
        setupSearchBarClickListener();
        // Xử lý sự kiện bottom navigation


    }

    private void setupSearchBarClickListener() {
        // Tìm thanh tìm kiếm trong layout và thêm sự kiện click
        View searchBar = findViewById(R.id.search_bar);
        if (searchBar != null) {
            searchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang SearchListActivity khi bấm vào thanh tìm kiếm
                    Intent intent = new Intent(HomeActivity.this, SearchListActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void setupProductClickListeners() {
        // Sản phẩm nổi bật - iPhone 14 Pro Max
        CardView iphoneCard = findViewById(R.id.iphone_14_pro_max_card);
        iphoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductDetail(
                        "iPhone 14 Pro Max 128GB",
                        25990000,
                        "iPhone 14 Pro Max - Flagship đến từ Apple với chip A16 Bionic mạnh mẽ, màn hình Super Retina XDR 6.7 inch, camera chính 48MP và tính năng Dynamic Island độc đáo.",
                        R.drawable.iphone_14_pro_max
                );
            }
        });

        // Sản phẩm nổi bật - Samsung Galaxy S23 Ultra
        CardView samsungCard = findViewById(R.id.samsung_s23_ultra_card);
        samsungCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductDetail(
                        "Samsung Galaxy S23 Ultra 256GB",
                        22990000,
                        "Samsung Galaxy S23 Ultra - flagship Android với bút S-Pen, camera 200MP, chip Snapdragon 8 Gen 2 và màn hình Dynamic AMOLED 2X.",
                        R.drawable.samsung_s23_ultra
                );
            }
        });

        // Sản phẩm đề xuất - AirPods Pro 2
        CardView airpodsCard = findViewById(R.id.airpods_pro_2_card);
        airpodsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductDetail(
                        "AirPods Pro 2",
                        5990000,
                        "AirPods Pro 2 - tai nghe không dây Apple với chip H2, chống ồn chủ động cải tiến và thời lượng pin lên đến 30 giờ.",
                        R.drawable.airpods_pro_2
                );
            }
        });

        // Sản phẩm đề xuất - Apple Watch Series 8
        CardView watchCard = findViewById(R.id.apple_watch_8_card);
        watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductDetail(
                        "Apple Watch Series 8",
                        10990000,
                        "Apple Watch Series 8 - đồng hồ thông minh với tính năng đo nhiệt độ, cảm biến va chạm và màn hình Retina luôn bật.",
                        R.drawable.apple_watch_8
                );
            }
        });

        // Sản phẩm đề xuất - AirPods Pro 2
        CardView airpodsCard = findViewById(R.id.airpods_pro_2_card);
        airpodsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductDetail(
                        "AirPods Pro 2",
                        5990000,
                        "AirPods Pro 2 - tai nghe không dây Apple với chip H2, chống ồn chủ động cải tiến và thời lượng pin lên đến 30 giờ.",
                        R.drawable.airpods_pro_2
                );
            }
        });

        // Sản phẩm đề xuất - Apple Watch Series 8
        CardView watchCard = findViewById(R.id.apple_watch_8_card);
        watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductDetail(
                        "Apple Watch Series 8",
                        10990000,
                        "Apple Watch Series 8 - đồng hồ thông minh với tính năng đo nhiệt độ, cảm biến va chạm và màn hình Retina luôn bật.",
                        R.drawable.apple_watch_8
                );
            }
        });
    }

    private void navigateToProductDetail(String productName, int productPrice, String productDescription, int productImage) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putExtra("PRODUCT_PRICE", productPrice);
        intent.putExtra("PRODUCT_DESCRIPTION", productDescription);
        intent.putExtra("PRODUCT_IMAGE", productImage);
        startActivity(intent);
    }

    private void navigateToProductDetail(String productName, int productPrice, String productDescription, int productImage) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putExtra("PRODUCT_PRICE", productPrice);
        intent.putExtra("PRODUCT_DESCRIPTION", productDescription);
        intent.putExtra("PRODUCT_IMAGE", productImage);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Đã ở Home rồi nên không cần làm gì
                return true;
            } else if (itemId == R.id.nav_order) {
                // Chuyển đến OrderTrackingActivity
                Intent intent = new Intent(HomeActivity.this, OrderTrackingActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Chuyển đến CartActivity
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_account) {
                // Chuyển đến AccountActivity
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}