package com.example.bc_quanlibanhangonline.database;

import android.content.Context;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.Category;
import com.example.bc_quanlibanhangonline.models.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    // Lấy danh sách categories
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        
        categories.add(new Category(1, "Điện thoại", "Smartphone và thiết bị di động", R.drawable.ic_smartphone));
        categories.add(new Category(2, "Laptop", "Máy tính xách tay và phụ kiện", R.drawable.ic_laptop));
        categories.add(new Category(3, "Thời trang", "Quần áo và phụ kiện thời trang", R.drawable.ic_fashion));
        categories.add(new Category(4, "Gia dụng", "Đồ gia dụng và nội thất", R.drawable.ic_home));
        
        return categories;
    }

    // Lấy sản phẩm nổi bật (top rated hoặc best seller)
    public List<Product> getFeaturedProducts() {
        List<Product> products = new ArrayList<>();
        
        products.add(new Product(
            1, 1, 1, 
            "iPhone 14 Pro Max 128GB phiên bản đặc biệt siêu dài",
            "Apple",
            25990000,
            "iPhone 14 Pro Max - Flagship đến từ Apple với chip A16 Bionic mạnh mẽ, màn hình Super Retina XDR 6.7 inch, camera chính 48MP và tính năng Dynamic Island độc đáo.",
            R.drawable.iphone_14_pro_max,
            10,
            "active",
            4.8f
        ));
        
        products.add(new Product(
            2, 1, 1,
            "Samsung Galaxy S23 Ultra 256GB",
            "Samsung",
            22990000,
            "Samsung Galaxy S23 Ultra - flagship Android với bút S-Pen, camera 200MP, chip Snapdragon 8 Gen 2 và màn hình Dynamic AMOLED 2X.",
            R.drawable.samsung_s23_ultra,
            8,
            "active",
            4.6f
        ));

        products.add(new Product(
            5, 1, 1,
            "iPhone 13 Pro Max 256GB",
            "Apple",
            21990000,
            "iPhone 13 Pro Max với chip A15 Bionic, camera ProRAW và ProRes, màn hình ProMotion 120Hz.",
            R.drawable.iphone_13_pro_max,
            15,
            "active",
            4.7f
        ));
        
        return products;
    }

    // Lấy sản phẩm đề xuất (recommended based on user behavior)
    public List<Product> getRecommendedProducts() {
        List<Product> products = new ArrayList<>();
        
        products.add(new Product(
            3, 1, 1,
            "AirPods Pro 2 thế hệ mới với nhiều tính năng nâng cao",
            "Apple",
            5990000,
            "AirPods Pro 2 - tai nghe không dây Apple với chip H2, chống ồn chủ động cải tiến và thời lượng pin lên đến 30 giờ.",
            R.drawable.airpods_pro_2,
            15,
            "active",
            4.9f
        ));
        
        products.add(new Product(
            4, 1, 1,
            "Apple Watch Series 8 phiên bản GPS + Cellular",
            "Apple",
            10990000,
            "Apple Watch Series 8 - đồng hồ thông minh với tính năng đo nhiệt độ, cảm biến va chạm và màn hình Retina luôn bật.",
            R.drawable.apple_watch_8,
            12,
            "active",
            4.5f
        ));

        products.add(new Product(
            6, 1, 1,
            "iPhone 14 Pro 128GB",
            "Apple",
            23990000,
            "iPhone 14 Pro với Dynamic Island, camera 48MP và chip A16 Bionic mạnh mẽ.",
            R.drawable.iphone_14_pro,
            20,
            "active",
            4.7f
        ));
        
        return products;
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(getFeaturedProducts());
        allProducts.addAll(getRecommendedProducts());
        
        // Thêm sản phẩm khác
        allProducts.add(new Product(
            7, 2, 2,
            "MacBook Pro 14 inch M2 Pro",
            "Apple",
            52990000,
            "MacBook Pro 14 inch với chip M2 Pro, màn hình Liquid Retina XDR và thời lượng pin lên đến 18 giờ.",
            R.drawable.ic_laptop,
            5,
            "active",
            4.8f
        ));

        allProducts.add(new Product(
            8, 2, 2,
            "Dell XPS 13 Plus",
            "Dell",
            35990000,
            "Dell XPS 13 Plus với Intel Core i7 thế hệ 12, màn hình OLED 13.4 inch và thiết kế siêu mỏng.",
            R.drawable.ic_laptop,
            8,
            "active",
            4.4f
        ));

        allProducts.add(new Product(
            9, 2, 3,
            "Áo thun nam basic",
            "Uniqlo",
            299000,
            "Áo thun nam chất liệu cotton 100%, form regular fit, nhiều màu sắc.",
            R.drawable.ic_fashion,
            50,
            "active",
            4.2f
        ));

        allProducts.add(new Product(
            10, 2, 4,
            "Nồi cơm điện Panasonic 1.8L",
            "Panasonic",
            2990000,
            "Nồi cơm điện Panasonic 1.8L với công nghệ nấu 3D, lòng nồi chống dính cao cấp.",
            R.drawable.ic_home,
            25,
            "active",
            4.3f
        ));

        // Thêm sản phẩm để test search
        allProducts.add(new Product(
            11, 2, 2,
            "MacBook Air M2 13 inch",
            "Apple",
            28990000,
            "MacBook Air M2 với thiết kế mỏng nhẹ, chip M2 mạnh mẽ và thời lượng pin cả ngày.",
            R.drawable.ic_laptop,
            12,
            "active",
            4.6f
        ));

        allProducts.add(new Product(
            12, 2, 1,
            "Xiaomi 13 Pro",
            "Xiaomi",
            18990000,
            "Xiaomi 13 Pro với camera Leica, chip Snapdragon 8 Gen 2 và sạc nhanh 120W.",
            R.drawable.ic_smartphone,
            20,
            "active",
            4.3f
        ));

        allProducts.add(new Product(
            13, 2, 3,
            "Quần jeans nam Levi's",
            "Levi's",
            1290000,
            "Quần jeans nam Levi's 501 Original Fit, chất liệu denim cao cấp, phong cách cổ điển.",
            R.drawable.ic_fashion,
            30,
            "active",
            4.4f
        ));

        allProducts.add(new Product(
            14, 2, 4,
            "Máy lọc không khí Xiaomi",
            "Xiaomi",
            3990000,
            "Máy lọc không khí Xiaomi Mi Air Purifier 3H với công nghệ HEPA và điều khiển thông minh.",
            R.drawable.ic_home,
            15,
            "active",
            4.5f
        ));
        
        return allProducts;
    }

    // Lấy sản phẩm theo category
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> allProducts = getAllProducts();
        List<Product> filteredProducts = new ArrayList<>();
        
        for (Product product : allProducts) {
            if (product.getCategoryId() == categoryId) {
                filteredProducts.add(product);
            }
        }
        
        return filteredProducts;
    }

    // Tìm kiếm sản phẩm theo tên
    public List<Product> searchProducts(String query) {
        List<Product> allProducts = getAllProducts();
        List<Product> searchResults = new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        for (Product product : allProducts) {
            if (product.getProductName().toLowerCase().contains(lowerQuery) ||
                product.getBrand().toLowerCase().contains(lowerQuery)) {
                searchResults.add(product);
            }
        }
        
        return searchResults;
    }

    // Tìm kiếm sản phẩm trong category cụ thể
    public List<Product> searchProductsInCategory(int categoryId, String query) {
        List<Product> categoryProducts = getProductsByCategory(categoryId);
        List<Product> searchResults = new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        for (Product product : categoryProducts) {
            if (product.getProductName().toLowerCase().contains(lowerQuery) ||
                product.getBrand().toLowerCase().contains(lowerQuery)) {
                searchResults.add(product);
            }
        }
        
        return searchResults;
    }
}