package com.example.bc_quanlibanhangonline;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class SearchListActivity extends AppCompatActivity {
    private LinearLayout layoutCategories, layoutPriceRanges, layoutProducts;
    private ChipGroup chipGroupBrands, chipGroupRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        initViews();
        setupCategories();
        setupPriceRanges();
        setupBrands();
        setupRecentSearches();
        setupProducts();
    }

    private void initViews() {
        layoutCategories = findViewById(R.id.layoutCategories);
        layoutPriceRanges = findViewById(R.id.layoutPriceRanges);
        chipGroupBrands = findViewById(R.id.chipGroupBrands);
        chipGroupRecent = findViewById(R.id.chipGroupRecent);
        layoutProducts = findViewById(R.id.layoutProducts);
    }

    private void setupCategories() {
        String[] categories = {"Tất cả", "Điện thoại", "Laptop", "Phụ kiện", "Thời trang"};

        for (String category : categories) {
            Button button = new Button(this);
            button.setText(category);
            button.setBackgroundResource(R.drawable.button_category_background);
            button.setTextColor(Color.BLACK);
            button.setTextSize(14f);
            button.setPadding(32, 12, 32, 12);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 0);
            button.setLayoutParams(params);

            layoutCategories.addView(button);
        }
    }

    private void setupPriceRanges() {
        String[] priceRanges = {"Dưới 5 triệu", "5-10 triệu", "10-20 triệu", "Trên 20 triệu"};

        for (String priceRange : priceRanges) {
            Button button = new Button(this);
            button.setText(priceRange);
            button.setBackgroundResource(R.drawable.button_category_background);
            button.setTextColor(Color.BLACK);
            button.setTextSize(14f);
            button.setPadding(32, 12, 32, 12);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 0);
            button.setLayoutParams(params);

            layoutPriceRanges.addView(button);
        }
    }

    private void setupBrands() {
        String[] brands = {"Apple", "Samsung", "Xiaomi", "Oppo", "Dell", "HP"};

        for (String brand : brands) {
            Chip chip = new Chip(this);
            chip.setText(brand);
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.chip_background);
            chip.setTextColor(Color.BLACK);
            chip.setChipStrokeWidth(0);

            ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 8);
            chip.setLayoutParams(params);

            chipGroupBrands.addView(chip);
        }
    }

    private void setupRecentSearches() {
        String[] recentSearches = {"iPhone 14", "Laptop gaming", "Tai nghe Bluetooth", "Đồng hồ thông minh"};

        for (String search : recentSearches) {
            Chip chip = new Chip(this);
            chip.setText(search);
            chip.setCheckable(false);
            chip.setChipBackgroundColorResource(R.color.chip_background);
            chip.setTextColor(Color.BLACK);
            chip.setChipStrokeWidth(0);

            ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 8);
            chip.setLayoutParams(params);

            chipGroupRecent.addView(chip);
        }
    }

    private void setupProducts() {
        // Sample product data - chính xác như trong ảnh
        String[] productNames = {
                "iPhone 14 Pro Max 128GB",
                "Samsung Galaxy S23 Ultra\n256GB",
                "MacBook Pro 14\" M2",
                "Dell XPS 13"
        };

        String[] brands = {"Apple", "Samsung", "Apple", "Dell"};
        String[] prices = {"25.990.000đ", "22.990.000đ", "42.990.000đ", "28.990.000đ"};
        String[] ratings = {"***** (4.5)", "***** (4.0)", "***** (5.0)", "***** (4.2)"};

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < productNames.length; i++) {
            View productView = inflater.inflate(R.layout.item_product, layoutProducts, false);

            TextView tvProductName = productView.findViewById(R.id.tvProductName);
            TextView tvBrand = productView.findViewById(R.id.tvBrand);
            TextView tvPrice = productView.findViewById(R.id.tvPrice);
            TextView tvRating = productView.findViewById(R.id.tvRating);

            tvProductName.setText(productNames[i]);
            tvBrand.setText(brands[i]);
            tvPrice.setText(prices[i]);
            tvRating.setText(ratings[i]);

            // Thêm divider giữa các sản phẩm (trừ sản phẩm cuối)
            if (i < productNames.length - 1) {
                View divider = new View(this);
                divider.setBackgroundColor(Color.parseColor("#EEEEEE"));
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );
                dividerParams.setMargins(0, 8, 0, 8);
                divider.setLayoutParams(dividerParams);

                layoutProducts.addView(productView);
                layoutProducts.addView(divider);
            } else {
                layoutProducts.addView(productView);
            }
        }
    }
}
