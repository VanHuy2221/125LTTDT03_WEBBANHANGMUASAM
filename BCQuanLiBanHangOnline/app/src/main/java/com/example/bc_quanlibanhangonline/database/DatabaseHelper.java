package com.example.bc_quanlibanhangonline.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.CartItem;
import com.example.bc_quanlibanhangonline.models.Category;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;
import com.example.bc_quanlibanhangonline.models.Message;
import com.example.bc_quanlibanhangonline.models.Order;
import com.example.bc_quanlibanhangonline.models.OrderDetail;
import com.example.bc_quanlibanhangonline.models.Payment;
import com.example.bc_quanlibanhangonline.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 1;

    // Danh sách lưu tạm trong bộ nhớ (cho đơn hàng, trao đổi, tin nhắn...)
    private static final List<Order> orders = new ArrayList<>();
    private static final List<OrderDetail> orderDetails = new ArrayList<>();
    private static final List<Payment> payments = new ArrayList<>();
    private static final List<ExchangeRequest> exchangeRequests = new ArrayList<>();
    private static final List<Message> messages = new ArrayList<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng sản phẩm (dùng tạm để join với cart lấy giá)
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS products (" +
                        "product_id INTEGER PRIMARY KEY, " +
                        "seller_id INTEGER, " +
                        "category_id INTEGER, " +
                        "product_name TEXT, " +
                        "brand TEXT, " +
                        "price REAL, " +
                        "description TEXT, " +
                        "image_resource INTEGER, " +
                        "quantity INTEGER, " +
                        "status TEXT, " +
                        "rating REAL" +
                        ")"
        );

        // Bảng giỏ hàng
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS cart (" +
                        "cart_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_id INTEGER, " +
                        "product_id INTEGER, " +
                        "quantity INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    // ====================== DANH MỤC & SẢN PHẨM ======================

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Điện thoại", "Smartphone và thiết bị di động", R.drawable.ic_smartphone));
        categories.add(new Category(2, "Laptop", "Máy tính xách tay và phụ kiện", R.drawable.ic_laptop));
        categories.add(new Category(3, "Thời trang", "Quần áo và phụ kiện thời trang", R.drawable.ic_fashion));
        categories.add(new Category(4, "Gia dụng", "Đồ gia dụng và nội thất", R.drawable.ic_home));
        return categories;
    }

    public List<Product> getFeaturedProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, 1, 1, "iPhone 14 Pro Max 128GB phiên bản đặc biệt siêu dài", "Apple", 25990000,
                "iPhone 14 Pro Max - Flagship đến từ Apple với chip A16 Bionic mạnh mẽ, màn hình Super Retina XDR 6.7 inch, camera chính 48MP và tính năng Dynamic Island độc đáo.",
                R.drawable.iphone_14_pro_max, 10, "active", 4.8f));
        products.add(new Product(2, 1, 1, "Samsung Galaxy S23 Ultra 256GB", "Samsung", 22990000,
                "Samsung Galaxy S23 Ultra - flagship Android với bút S-Pen, camera 200MP, chip Snapdragon 8 Gen 2 và màn hình Dynamic AMOLED 2X.",
                R.drawable.samsung_s23_ultra, 8, "active", 4.6f));
        products.add(new Product(5, 1, 1, "iPhone 13 Pro Max 256GB", "Apple", 21990000,
                "iPhone 13 Pro Max với chip A15 Bionic, camera ProRAW và ProRes, màn hình ProMotion 120Hz.",
                R.drawable.iphone_13_pro_max, 15, "active", 4.7f));
        return products;
    }

    public List<Product> getRecommendedProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(3, 1, 1, "AirPods Pro 2 thế hệ mới với nhiều tính năng nâng cao", "Apple", 5990000,
                "AirPods Pro 2 - tai nghe không dây Apple với chip H2, chống ồn chủ động cải tiến và thời lượng pin lên đến 30 giờ.",
                R.drawable.airpods_pro_2, 15, "active", 4.9f));
        products.add(new Product(4, 1, 1, "Apple Watch Series 8 phiên bản GPS + Cellular", "Apple", 10990000,
                "Apple Watch Series 8 - đồng hồ thông minh với tính năng đo nhiệt độ, cảm biến va chạm và màn hình Retina luôn bật.",
                R.drawable.apple_watch_8, 12, "active", 4.5f));
        products.add(new Product(6, 1, 1, "iPhone 14 Pro 128GB", "Apple", 23990000,
                "iPhone 14 Pro với Dynamic Island, camera 48MP và chip A16 Bionic mạnh mẽ.",
                R.drawable.iphone_14_pro, 20, "active", 4.7f));
        return products;
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(getFeaturedProducts());
        allProducts.addAll(getRecommendedProducts());

        allProducts.add(new Product(7, 2, 2, "MacBook Pro 14 inch M2 Pro", "Apple", 52990000,
                "MacBook Pro 14 inch với chip M2 Pro, màn hình Liquid Retina XDR và thời lượng pin lên đến 18 giờ.",
                R.drawable.ic_laptop, 5, "active", 4.8f));
        allProducts.add(new Product(8, 2, 2, "Dell XPS 13 Plus", "Dell", 35990000,
                "Dell XPS 13 Plus với Intel Core i7 thế hệ 12, màn hình OLED 13.4 inch và thiết kế siêu mỏng.",
                R.drawable.ic_laptop, 8, "active", 4.4f));
        allProducts.add(new Product(9, 2, 3, "Áo thun nam basic", "Uniqlo", 299000,
                "Áo thun nam chất liệu cotton 100%, form regular fit, nhiều màu sắc.",
                R.drawable.ic_fashion, 50, "active", 4.2f));
        allProducts.add(new Product(10, 2, 4, "Nồi cơm điện Panasonic 1.8L", "Panasonic", 2990000,
                "Nồi cơm điện Panasonic 1.8L với công nghệ nấu 3D, lòng nồi chống dính cao cấp.",
                R.drawable.ic_home, 25, "active", 4.3f));
        allProducts.add(new Product(11, 2, 2, "MacBook Air M2 13 inch", "Apple", 28990000,
                "MacBook Air M2 với thiết kế mỏng nhẹ, chip M2 mạnh mẽ và thời lượng pin cả ngày.",
                R.drawable.ic_laptop, 12, "active", 4.6f));
        allProducts.add(new Product(12, 2, 1, "Xiaomi 13 Pro", "Xiaomi", 18990000,
                "Xiaomi 13 Pro với camera Leica, chip Snapdragon 8 Gen 2 và sạc nhanh 120W.",
                R.drawable.ic_smartphone, 20, "active", 4.3f));
        allProducts.add(new Product(13, 2, 3, "Quần jeans nam Levi's", "Levi's", 1290000,
                "Quần jeans nam Levi's 501 Original Fit, chất liệu denim cao cấp, phong cách cổ điển.",
                R.drawable.ic_fashion, 30, "active", 4.4f));
        allProducts.add(new Product(14, 2, 4, "Máy lọc không khí Xiaomi", "Xiaomi", 3990000,
                "Máy lọc không khí Xiaomi Mi Air Purifier 3H với công nghệ HEPA và điều khiển thông minh.",
                R.drawable.ic_home, 15, "active", 4.5f));

        return allProducts;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : getAllProducts()) {
            if (p.getCategoryId() == categoryId) filtered.add(p);
        }
        return filtered;
    }

    public List<Product> searchProducts(String query) {
        List<Product> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase(Locale.getDefault());
        for (Product p : getAllProducts()) {
            if (p.getProductName().toLowerCase(Locale.getDefault()).contains(lowerQuery) ||
                    p.getBrand().toLowerCase(Locale.getDefault()).contains(lowerQuery)) {
                results.add(p);
            }
        }
        return results;
    }

    public List<Product> searchProductsInCategory(int categoryId, String query) {
        List<Product> categoryProducts = getProductsByCategory(categoryId);
        List<Product> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase(Locale.getDefault());
        for (Product p : categoryProducts) {
            if (p.getProductName().toLowerCase(Locale.getDefault()).contains(lowerQuery) ||
                    p.getBrand().toLowerCase(Locale.getDefault()).contains(lowerQuery)) {
                results.add(p);
            }
        }
        return results;
    }

    public Product getProductById(int productId) {
        for (Product p : getAllProducts()) {
            if (p.getProductId() == productId) return p;
        }
        return null;
    }

    // ====================== QUẢN LÝ ĐƠN HÀNG ======================

    public Order createOrder(int userId, int totalPrice, String orderType, String paymentMethod, String status) {
        int orderId = (int) (System.currentTimeMillis() / 1000);
        String orderDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Order order = new Order(orderId, userId, null, totalPrice, orderType, paymentMethod, status, orderDate);
        orders.add(order);
        return order;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUserId() == userId) result.add(o);
        }
        return result;
    }

    public Order getOrderById(int orderId) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId) return o;
        }
        return null;
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId) {
                o.setStatus(newStatus);
                break;
            }
        }
    }

    public boolean cancelOrder(int orderId) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId && "processing".equalsIgnoreCase(o.getStatus())) {
                o.setStatus("cancelled");
                return true;
            }
        }
        return false;
    }

    // ====================== CHI TIẾT ĐƠN HÀNG ======================

    public OrderDetail createOrderDetail(int orderId, int productId, int quantity, int price) {
        int orderDetailId = orderDetails.size() + 1;
        OrderDetail detail = new OrderDetail(orderDetailId, orderId, productId, quantity, price);
        orderDetails.add(detail);
        return detail;
    }

    public OrderDetail createOrderDetailByName(int orderId, String productName, int quantity, int price) {
        int productId = -1;
        for (Product p : getAllProducts()) {
            if (p.getProductName().equalsIgnoreCase(productName)) {
                productId = p.getProductId();
                break;
            }
        }
        return createOrderDetail(orderId, productId, quantity, price);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> result = new ArrayList<>();
        for (OrderDetail d : orderDetails) {
            if (d.getOrderId() == orderId) result.add(d);
        }
        return result;
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetails;
    }

    // ====================== THANH TOÁN ======================

    public Payment createPayment(int orderId, String paymentMethod, String paymentStatus) {
        int paymentId = payments.size() + 1;
        String paymentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        Payment payment = new Payment(paymentId, orderId, paymentMethod, paymentStatus, paymentDate);
        payments.add(payment);
        return payment;
    }

    public Payment getPaymentByOrderId(int orderId) {
        for (Payment p : payments) {
            if (p.getOrderId() == orderId) return p;
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        return payments;
    }

    // ====================== CẬP NHẬT SỐ LƯỢNG SẢN PHẨM ======================

    public void updateProductQuantity(int productId, int quantity) {
        Product p = getProductById(productId);
        if (p != null) {
            int newQty = p.getQuantity() - quantity;
            p.setQuantity(Math.max(newQty, 0));
        }
    }

    public void updateProductQuantityNew(int productId, int newQuantity) {
        Product p = getProductById(productId);
        if (p != null) {
            p.setQuantity(Math.max(newQuantity, 0));
        }
    }

    // ====================== TRAO ĐỔI ======================

    public ExchangeRequest createExchange(String productName, String exchangeItemName, String message, int userId) {
        String exchangeId = "EX" + System.currentTimeMillis();
        ExchangeRequest exchange = new ExchangeRequest(exchangeId, productName, exchangeItemName, message, "Đang chờ phản hồi", userId);
        exchangeRequests.add(exchange);
        return exchange;
    }

    // Giữ phương thức cũ để tương thích
    public ExchangeRequest createExchange(String productName, String exchangeItemName, String message) {
        return createExchange(productName, exchangeItemName, message, -1);
    }

    public List<ExchangeRequest> getExchangeRequests() {
        return exchangeRequests;
    }

    public List<ExchangeRequest> getExchangeRequestsByUser(int userId) {
        List<ExchangeRequest> result = new ArrayList<>();
        for (ExchangeRequest e : exchangeRequests) {
            if (e.getUserId() == userId) result.add(e);
        }
        return result;
    }

    public ExchangeRequest getExchangeRequestById(String exchangeId) {
        for (ExchangeRequest e : exchangeRequests) {
            if (e.getExchangeId().equals(exchangeId)) return e;
        }
        return null;
    }

    public boolean updateExchangeStatus(String exchangeId, String newStatus) {
        for (int i = 0; i < exchangeRequests.size(); i++) {
            ExchangeRequest e = exchangeRequests.get(i);
            if (e.getExchangeId().equals(exchangeId)) {
                ExchangeRequest updated = new ExchangeRequest(
                        e.getExchangeId(), e.getProductName(), e.getExchangeItemName(),
                        e.getMessage(), newStatus, e.getUserId());
                exchangeRequests.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // ====================== TIN NHẮN TRAO ĐỔI ======================

    public Message createExchangeMessage(int senderId, int receiverId, String exchangeId, String content) {
        int messageId = messages.size() + 1;
        String sentAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException ignored) {}

        Message message = new Message(messageId, senderId, receiverId, exchangeIdInt, content, null, sentAt);
        messages.add(message);
        return message;
    }

    public Message createSystemMessage(String content, String exchangeId) {
        int messageId = messages.size() + 1;
        String sentAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException ignored) {}

        Message message = new Message(messageId, 0, 0, exchangeIdInt, content, null, sentAt);
        messages.add(message);
        return message;
    }

    public List<Message> getMessagesByExchangeId(String exchangeId) {
        List<Message> result = new ArrayList<>();
        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException ignored) {}

        for (Message m : messages) {
            Integer msgExId = m.getExchangeId();
            if ((exchangeIdInt == null && msgExId == null) ||
                    (exchangeIdInt != null && exchangeIdInt.equals(msgExId))) {
                result.add(m);
            }
        }
        return result;
    }

    // ====================== GIỎ HÀNG (SQLite) ======================

    public void addToCart(int userId, int productId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(productId)}
        );

        if (cursor.moveToFirst()) {
            int currentQty = cursor.getInt(0);
            db.execSQL("UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?",
                    new Object[]{currentQty + 1, userId, productId});
        } else {
            db.execSQL("INSERT INTO cart(user_id, product_id, quantity) VALUES (?, ?, 1)",
                    new Object[]{userId, productId});
        }
        cursor.close();
    }

    public List<CartItem> getCartByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT cart_id, product_id, quantity FROM cart WHERE user_id = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(new CartItem(
                        cursor.getInt(0),
                        userId,
                        cursor.getInt(1),
                        cursor.getInt(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void updateCartQuantity(int cartId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE cart SET quantity = ? WHERE cart_id = ?", new Object[]{quantity, cartId});
    }

    public void removeFromCart(int cartId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE cart_id = ?", new Object[]{cartId});
    }

    public void cleanInvalidCartItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "product_id <= 0", null);
    }

    public int getTotalCartPrice(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(p.price * c.quantity) FROM cart c JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?",
                new String[]{String.valueOf(userId)}
        );
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    public void clearCartByUser(int userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "user_id = ?", new String[]{String.valueOf(userId)});
    }
}