package com.example.bc_quanlibanhangonline.database;

import android.content.Context;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.Category;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;
import com.example.bc_quanlibanhangonline.models.Order;
import com.example.bc_quanlibanhangonline.models.OrderDetail;
import com.example.bc_quanlibanhangonline.models.Payment;
import com.example.bc_quanlibanhangonline.models.Product;
import com.example.bc_quanlibanhangonline.models.Message;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper {
    private Context context;

    private static final List<Order> orders = new ArrayList<>();
    private static final List<OrderDetail> orderDetails = new ArrayList<>();
    private static final List<Payment> payments = new ArrayList<>();
    private static final List<ExchangeRequest> exchangeRequests = new ArrayList<>();

    // THÊM: Danh sách tin nhắn
    private static final List<Message> messages = new ArrayList<>();

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

    public Order createOrder(
            int userId,
            int totalPrice,
            String orderType,       // normal | exchange
            String paymentMethod,
            String status
    ) {
        int orderId = (int) (System.currentTimeMillis() / 1000); // tạo id từ timestamp

        String orderDate = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault()
        ).format(new Date());

        Order order = new Order(
                orderId,
                userId,
                null,                // exchangeId
                totalPrice,
                orderType,           // orderType
                paymentMethod,
                status,        // status mặc định
                orderDate
        );

        orders.add(order);
        return order;
    }

    public List<Order> getOrders() {
        return orders;
    }

    // SỬA: Thêm userId vào phương thức createExchange
    public ExchangeRequest createExchange(
            String productName,
            String exchangeItemName,
            String message,
            int userId  // THÊM userId
    ) {
        String exchangeId = "EX" + System.currentTimeMillis();

        ExchangeRequest exchange = new ExchangeRequest(
                exchangeId,
                productName,
                exchangeItemName,
                message,
                "Đang chờ phản hồi",
                userId  // THÊM userId
        );

        exchangeRequests.add(exchange);
        return exchange;
    }

    // GIỮ NGUYÊN: Phương thức cũ để không break code hiện có
    public ExchangeRequest createExchange(
            String productName,
            String exchangeItemName,
            String message
    ) {
        return createExchange(productName, exchangeItemName, message, -1); // -1 = unknown user
    }

    // ========== THÊM PHƯƠNG THỨC QUẢN LÝ TRAO ĐỔI ==========

    // THÊM: Lấy ExchangeRequest theo ID
    public ExchangeRequest getExchangeRequestById(String exchangeId) {
        for (ExchangeRequest exchange : exchangeRequests) {
            if (exchange.getExchangeId().equals(exchangeId)) {
                return exchange;
            }
        }
        return null;
    }

    // SỬA: Lấy ExchangeRequest theo userId (cập nhật để dùng userId mới)
    public List<ExchangeRequest> getExchangeRequestsByUser(int userId) {
        List<ExchangeRequest> result = new ArrayList<>();
        for (ExchangeRequest exchange : exchangeRequests) {
            // Kiểm tra nếu exchange có userId (có thể là -1 nếu từ phương thức cũ)
            if (exchange.getUserId() == userId) {
                result.add(exchange);
            }
        }
        return result;
    }

    // THÊM: Cập nhật trạng thái ExchangeRequest
    public boolean updateExchangeStatus(String exchangeId, String newStatus) {
        for (int i = 0; i < exchangeRequests.size(); i++) {
            ExchangeRequest exchange = exchangeRequests.get(i);
            if (exchange.getExchangeId().equals(exchangeId)) {
                // Tạo ExchangeRequest mới với status mới
                ExchangeRequest updatedExchange = new ExchangeRequest(
                        exchange.getExchangeId(),
                        exchange.getProductName(),
                        exchange.getExchangeItemName(),
                        exchange.getMessage(),
                        newStatus,
                        exchange.getUserId()  // GIỮ NGUYÊN userId
                );
                exchangeRequests.set(i, updatedExchange);
                return true;
            }
        }
        return false;
    }

    // THÊM: Tạo tin nhắn cho trao đổi
    public Message createExchangeMessage(
            int senderId,
            int receiverId,
            String exchangeId,
            String content
    ) {
        int messageId = messages.size() + 1;
        String sentAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date());

        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException e) {
            // Nếu exchangeId không phải số, giữ null
        }

        Message message = new Message(
                messageId, senderId, receiverId, exchangeIdInt, content, null, sentAt
        );

        messages.add(message);
        return message;
    }

    // SỬA: Lấy tin nhắn theo exchangeId - DÙNG GETTER THAY VÌ REFLECTION
    public List<Message> getMessagesByExchangeId(String exchangeId) {
        List<Message> result = new ArrayList<>();

        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException e) {
            // KHÔNG return, vì exchangeId có thể là null trong Message
            exchangeIdInt = null;
        }

        for (Message message : messages) {
            // DÙNG GETTER THAY VÌ REFLECTION
            Integer msgExchangeId = message.getExchangeId();

            if (exchangeIdInt == null && msgExchangeId == null) {
                // Cả hai đều null -> khớp
                result.add(message);
            } else if (exchangeIdInt != null && exchangeIdInt.equals(msgExchangeId)) {
                // Khớp giá trị Integer
                result.add(message);
            }
            // Nếu exchangeIdInt = null nhưng msgExchangeId != null: không khớp
            // Nếu exchangeIdInt != null nhưng msgExchangeId = null: không khớp
        }

        return result;
    }

    // THÊM: Phương thức đơn giản hơn không dùng Reflection
    public List<Message> getMessagesByExchangeIdSimple(String exchangeId) {
        List<Message> result = new ArrayList<>();

        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException e) {
            return result;
        }

        for (Message message : messages) {
            Integer msgExchangeId = message.getExchangeId();

            if (msgExchangeId != null && msgExchangeId.equals(exchangeIdInt)) {
                result.add(message);
            }
        }
        return result;
    }

    // Lớp nội bộ để truy cập exchangeId của Message (KHÔNG CÒN DÙNG)
    private class MessageAccessor {
        private Message message;

        public MessageAccessor(Message message) {
            this.message = message;
        }

        public Integer getExchangeId() {
            try {
                // Vẫn phải dùng Reflection vì Message không có getter
                Field field = Message.class.getDeclaredField("exchangeId");
                field.setAccessible(true);
                return (Integer) field.get(message);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public List<ExchangeRequest> getExchangeRequests() {
        return exchangeRequests;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> result = new ArrayList<>();

        for (Order order : orders) {
            if (order.getUserId() == userId) {
                result.add(order);
            }
        }

        return result;
    }

    public OrderDetail createOrderDetailByName(
            int orderId,
            String productName,
            int quantity,
            int price
    ) {
        int orderDetailId = orderDetails.size() + 1;
        int productId = -1;

        // tìm productId từ productName
        for (Product product : getAllProducts()) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                productId = product.getProductId();
                break;
            }
        }

        OrderDetail detail = new OrderDetail(
                orderDetailId,
                orderId,
                productId,
                quantity,
                price
        );

        orderDetails.add(detail);
        return detail;
    }

    // THÊM MỚI: Tạo OrderDetail bằng productId
    public OrderDetail createOrderDetail(
            int orderId,
            int productId,
            int quantity,
            int price
    ) {
        int orderDetailId = orderDetails.size() + 1;

        OrderDetail detail = new OrderDetail(
                orderDetailId,
                orderId,
                productId,
                quantity,
                price
        );

        orderDetails.add(detail);
        return detail;
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> result = new ArrayList<>();

        for (OrderDetail detail : orderDetails) {
            if (detail.getOrderId() == orderId) {
                result.add(detail);
            }
        }
        return result;
    }

    public Payment createPayment(
            int orderId,
            String paymentMethod,
            String paymentStatus
    ) {
        int paymentId = payments.size() + 1;

        String paymentDate = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        ).format(new Date());

        Payment payment = new Payment(
                paymentId,
                orderId,
                paymentMethod,
                paymentStatus,
                paymentDate
        );

        payments.add(payment);
        return payment;
    }

    public Payment getPaymentByOrderId(int orderId) {
        for (Payment payment : payments) {
            if (payment.getOrderId() == orderId) {
                return payment;
            }
        }
        return null;
    }

    public void updateProductQuantity(int productId, int quantity) {
        for (Product product : getAllProducts()) {
            if (product.getProductId() == productId) {
                int newQuantity = product.getQuantity() - quantity;
                if (newQuantity < 0) {
                    newQuantity = 0; // tránh âm
                }
                product.setQuantity(newQuantity); // cần setter trong Product
                break;
            }
        }
    }

    // THÊM MỚI: Cập nhật số lượng sản phẩm theo giá trị mới
    public void updateProductQuantityNew(int productId, int newQuantity) {
        for (Product product : getAllProducts()) {
            if (product.getProductId() == productId) {
                if (newQuantity < 0) {
                    newQuantity = 0; // tránh âm
                }
                product.setQuantity(newQuantity);
                break;
            }
        }
    }

    public boolean cancelOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                if ("processing".equalsIgnoreCase(order.getStatus())) {
                    order.setStatus("cancelled");
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public Product getProductById(int productId) {
        for (Product product : getAllProducts()) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null; // không tìm thấy
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId) {
                o.setStatus(newStatus);
                break;
            }
        }
    }

    // THÊM MỚI: Lấy Order theo ID
    public Order getOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    // THÊM MỚI: Lấy danh sách OrderDetail
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetails;
    }

    // THÊM MỚI: Lấy danh sách Payments
    public List<Payment> getAllPayments() {
        return payments;
    }

    public Message createSystemMessage(String content, String exchangeId) {
        int messageId = messages.size() + 1;
        String sentAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date());

        Integer exchangeIdInt = null;
        try {
            exchangeIdInt = Integer.parseInt(exchangeId.replace("EX", ""));
        } catch (NumberFormatException e) {
            // Nếu exchangeId không phải số, giữ null
        }

        Message message = new Message(
                messageId,
                0,  // senderId = 0 là hệ thống
                0,  // receiverId = 0
                exchangeIdInt,
                content,
                null,
                sentAt
        );

        messages.add(message);
        return message;
    }
}