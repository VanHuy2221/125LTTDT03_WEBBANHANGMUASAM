-- Thêm dữ liệu mẫu vào database MuaBanSanPham
USE MuaBanSanPham;
GO

-- Thêm users mẫu
INSERT INTO users (full_name, email, password, phone, address, role, avatar) VALUES
(N'Nguyễn Văn Admin', 'admin@shop.com', 'admin123', '0901234567', N'123 Nguyễn Huệ, Q1, TP.HCM', 'admin', 'avatar_admin.jpg'),
(N'Trần Thị Seller', 'seller@shop.com', 'seller123', '0902345678', N'456 Lê Lợi, Q1, TP.HCM', 'seller', 'avatar_seller.jpg'),
(N'Lê Văn Customer', 'customer@shop.com', 'customer123', '0903456789', N'789 Hai Bà Trưng, Q3, TP.HCM', 'customer', 'avatar_customer.jpg');

-- Thêm categories
INSERT INTO categories (category_name, description) VALUES
(N'Điện thoại', N'Smartphone và thiết bị di động'),
(N'Laptop', N'Máy tính xách tay và phụ kiện'),
(N'Thời trang', N'Quần áo và phụ kiện thời trang'),
(N'Gia dụng', N'Đồ gia dụng và nội thất');

-- Thêm products
INSERT INTO products (seller_id, category_id, product_name, price, description, image, quantity, status) VALUES
-- Điện thoại
(2, 1, N'iPhone 14 Pro Max 128GB', 25990000, N'iPhone 14 Pro Max - Flagship đến từ Apple với chip A16 Bionic mạnh mẽ, màn hình Super Retina XDR 6.7 inch, camera chính 48MP và tính năng Dynamic Island độc đáo.', 'iphone_14_pro_max.webp', 10, 'active'),
(2, 1, N'Samsung Galaxy S23 Ultra 256GB', 22990000, N'Samsung Galaxy S23 Ultra - flagship Android với bút S-Pen, camera 200MP, chip Snapdragon 8 Gen 2 và màn hình Dynamic AMOLED 2X.', 'samsung_s23_ultra.webp', 8, 'active'),
(2, 1, N'iPhone 13 Pro Max 256GB', 21990000, N'iPhone 13 Pro Max với chip A15 Bionic, camera ProRAW và ProRes, màn hình ProMotion 120Hz.', 'iphone_13_pro_max.jpg', 15, 'active'),
(2, 1, N'iPhone 14 Pro 128GB', 23990000, N'iPhone 14 Pro với Dynamic Island, camera 48MP và chip A16 Bionic mạnh mẽ.', 'iphone_14_pro.webp', 20, 'active'),
(2, 1, N'AirPods Pro 2', 5990000, N'AirPods Pro 2 - tai nghe không dây Apple với chip H2, chống ồn chủ động cải tiến và thời lượng pin lên đến 30 giờ.', 'airpods_pro_2.jpg', 15, 'active'),
(2, 1, N'Apple Watch Series 8', 10990000, N'Apple Watch Series 8 - đồng hồ thông minh với tính năng đo nhiệt độ, cảm biến va chạm và màn hình Retina luôn bật.', 'apple_watch_8.jpg', 12, 'active'),

-- Laptop
(2, 2, N'MacBook Pro 14 inch M2 Pro', 52990000, N'MacBook Pro 14 inch với chip M2 Pro, màn hình Liquid Retina XDR và thời lượng pin lên đến 18 giờ.', 'macbook_pro_14.jpg', 5, 'active'),
(2, 2, N'Dell XPS 13 Plus', 35990000, N'Dell XPS 13 Plus với Intel Core i7 thế hệ 12, màn hình OLED 13.4 inch và thiết kế siêu mỏng.', 'dell_xps_13.jpg', 8, 'active'),
(2, 2, N'MacBook Air M2 13 inch', 28990000, N'MacBook Air M2 với thiết kế mỏng nhẹ, chip M2 mạnh mẽ và thời lượng pin cả ngày.', 'macbook_air_m2.jpg', 12, 'active'),
(2, 2, N'Asus ROG Strix G15', 25990000, N'Laptop gaming Asus ROG với RTX 3060, AMD Ryzen 7 và màn hình 144Hz.', 'asus_rog_g15.jpg', 6, 'active'),

-- Thời trang
(2, 3, N'Áo thun nam basic', 299000, N'Áo thun nam chất liệu cotton 100%, form regular fit, nhiều màu sắc.', 'ao_thun_nam.jpg', 50, 'active'),
(2, 3, N'Quần jeans nam slim fit', 599000, N'Quần jeans nam form slim fit, chất liệu denim cao cấp, bền đẹp.', 'quan_jeans_nam.jpg', 30, 'active'),
(2, 3, N'Áo sơ mi nữ công sở', 450000, N'Áo sơ mi nữ phong cách công sở, chất liệu thoáng mát, dễ phối đồ.', 'ao_so_mi_nu.jpg', 25, 'active'),
(2, 3, N'Váy maxi nữ', 750000, N'Váy maxi nữ dáng dài, chất liệu voan mềm mại, phù hợp dạo phố.', 'vay_maxi.jpg', 20, 'active'),

-- Gia dụng
(2, 4, N'Nồi cơm điện Panasonic 1.8L', 2990000, N'Nồi cơm điện Panasonic 1.8L với công nghệ nấu 3D, lòng nồi chống dính cao cấp.', 'noi_com_panasonic.jpg', 25, 'active'),
(2, 4, N'Máy lọc nước RO Kangaroo', 8990000, N'Máy lọc nước RO 9 cấp lọc, công nghệ Nhật Bản, nước tinh khiết an toàn.', 'may_loc_nuoc.jpg', 15, 'active'),
(2, 4, N'Tủ lạnh Samsung Inverter 236L', 12990000, N'Tủ lạnh Samsung công nghệ Inverter tiết kiệm điện, ngăn đông mềm tiện lợi.', 'tu_lanh_samsung.jpg', 8, 'active'),
(2, 4, N'Máy giặt LG 9kg', 15990000, N'Máy giặt LG cửa trước 9kg, công nghệ AI DD, giặt sạch và bảo vệ vải.', 'may_giat_lg.jpg', 10, 'active');

-- Thêm reviews mẫu
INSERT INTO reviews (user_id, product_id, rating, comment) VALUES
(3, 1, 5, N'Sản phẩm tuyệt vời, chất lượng cao, giao hàng nhanh!'),
(3, 2, 4, N'Điện thoại đẹp, camera chụp ảnh rất tốt. Giá hơi cao nhưng xứng đáng.'),
(3, 3, 5, N'iPhone 13 Pro Max vẫn rất tốt, pin trâu, màn hình đẹp.'),
(3, 5, 5, N'AirPods Pro 2 chống ồn rất tốt, âm thanh trong trẻo.'),
(3, 7, 4, N'MacBook Pro M2 Pro rất mạnh mẽ, phù hợp cho công việc thiết kế.'),
(3, 9, 4, N'Áo thun chất lượng tốt, form đẹp, giá cả hợp lý.'),
(3, 13, 5, N'Nồi cơm nấu cơm rất ngon, dễ sử dụng và vệ sinh.');

-- Thêm cart mẫu
INSERT INTO cart (user_id, product_id, quantity) VALUES
(3, 1, 1),
(3, 5, 2);

-- Thêm orders mẫu
INSERT INTO orders (user_id, total_price, status) VALUES
(3, 31980000, 'completed'),
(3, 5990000, 'shipping');

-- Thêm order_details mẫu
INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 25990000),
(1, 5, 1, 5990000),
(2, 5, 1, 5990000);

-- Thêm payments mẫu
INSERT INTO payments (order_id, payment_method, payment_status, payment_date) VALUES
(1, 'banking', 'paid', GETDATE()),
(2, 'cash', 'pending', NULL);

GO