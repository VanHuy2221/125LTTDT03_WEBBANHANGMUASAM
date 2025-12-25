package com.example.bc_quanlibanhangonline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bc_quanlibanhangonline.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "ShoppingApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "user_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CREATED_AT = "created_at";

    // Table Followers (mới thêm)
    private static final String TABLE_FOLLOWERS = "followers";
    private static final String COLUMN_FOLLOWER_ID = "follower_id";
    private static final String COLUMN_FOLLOWER_USER_ID = "follower_user_id";
    private static final String COLUMN_FOLLOWED_USER_ID = "followed_user_id";
    private static final String COLUMN_FOLLOWED_AT = "followed_at";

    // Create Statements
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FULL_NAME + " TEXT NOT NULL,"
                    + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
                    + COLUMN_PASSWORD + " TEXT NOT NULL,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_ROLE + " TEXT CHECK(" + COLUMN_ROLE + " IN ('customer', 'seller', 'admin')) DEFAULT 'customer',"
                    + COLUMN_STATUS + " TEXT CHECK(" + COLUMN_STATUS + " IN ('active', 'locked')) DEFAULT 'active',"
                    + COLUMN_CREATED_AT + " TEXT"
                    + ")";

    private static final String CREATE_TABLE_FOLLOWERS =
            "CREATE TABLE " + TABLE_FOLLOWERS + "("
                    + COLUMN_FOLLOWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FOLLOWER_USER_ID + " INTEGER,"
                    + COLUMN_FOLLOWED_USER_ID + " INTEGER,"
                    + COLUMN_FOLLOWED_AT + " TEXT DEFAULT (datetime('now')),"
                    + "FOREIGN KEY(" + COLUMN_FOLLOWER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE,"
                    + "FOREIGN KEY(" + COLUMN_FOLLOWED_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE,"
                    + "UNIQUE(" + COLUMN_FOLLOWER_USER_ID + ", " + COLUMN_FOLLOWED_USER_ID + ")"
                    + ")";

    // Constructor
    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        db.execSQL(CREATE_TABLE_USERS);
        // Tạo bảng followers
        db.execSQL(CREATE_TABLE_FOLLOWERS);

        // Thêm dữ liệu mẫu (chỉ chạy lần đầu khi tạo database)
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Tạo lại bảng
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Bật khóa ngoại
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    // ============ CHÈN DỮ LIỆU MẪU ============
    private void insertSampleData(SQLiteDatabase db) {
        // Chỉ chèn nếu bảng trống
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) return; // Đã có data, không chèn nữa

        // SQL để chèn 3 tài khoản mẫu
        String[] insertQueries = {
                "INSERT INTO " + TABLE_USERS + "("
                        + COLUMN_FULL_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", "
                        + COLUMN_PHONE + ", " + COLUMN_ADDRESS + ", " + COLUMN_ROLE + ", "
                        + COLUMN_STATUS + ", " + COLUMN_CREATED_AT + ") "
                        + "VALUES ('Quản trị viên', 'admin@gmail.com', 'admin123', "
                        + "'0123456789', 'Trụ sở chính', 'admin', 'active', '2024-01-01')",

                "INSERT INTO " + TABLE_USERS + "("
                        + COLUMN_FULL_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", "
                        + COLUMN_PHONE + ", " + COLUMN_ADDRESS + ", " + COLUMN_ROLE + ", "
                        + COLUMN_STATUS + ", " + COLUMN_CREATED_AT + ") "
                        + "VALUES ('Nguyễn Văn Bán', 'seller@gmail.com', '123456', "
                        + "'0987654321', '123 Đường ABC, Quận 1, TP.HCM', 'seller', 'active', '2024-01-15')",

                "INSERT INTO " + TABLE_USERS + "("
                        + COLUMN_FULL_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", "
                        + COLUMN_PHONE + ", " + COLUMN_ADDRESS + ", " + COLUMN_ROLE + ", "
                        + COLUMN_STATUS + ", " + COLUMN_CREATED_AT + ") "
                        + "VALUES ('Trần Thị Mua', 'customer@gmail.com', '123456', "
                        + "'0901234567', '456 Đường XYZ, Quận 2, TP.HCM', 'customer', 'active', '2024-02-01')",

                "INSERT INTO " + TABLE_USERS + "("
                        + COLUMN_FULL_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", "
                        + COLUMN_PHONE + ", " + COLUMN_ADDRESS + ", " + COLUMN_ROLE + ", "
                        + COLUMN_STATUS + ", " + COLUMN_CREATED_AT + ") "
                        + "VALUES ('Lê Văn C', 'customer2@gmail.com', '123456', "
                        + "'0912345678', '789 Đường DEF, Quận 3, TP.HCM', 'customer', 'active', '2024-02-10')"
        };

        for (String query : insertQueries) {
            db.execSQL(query);
        }

        // Thêm dữ liệu mẫu cho followers
        String[] followerQueries = {
                "INSERT INTO " + TABLE_FOLLOWERS + "(" + COLUMN_FOLLOWER_USER_ID + ", " + COLUMN_FOLLOWED_USER_ID + ") "
                        + "VALUES (3, 2)", // customer1 follows seller
                "INSERT INTO " + TABLE_FOLLOWERS + "(" + COLUMN_FOLLOWER_USER_ID + ", " + COLUMN_FOLLOWED_USER_ID + ") "
                        + "VALUES (4, 2)", // customer2 follows seller
                "INSERT INTO " + TABLE_FOLLOWERS + "(" + COLUMN_FOLLOWER_USER_ID + ", " + COLUMN_FOLLOWED_USER_ID + ") "
                        + "VALUES (2, 3)", // seller follows customer1
        };

        for (String query : followerQueries) {
            try {
                db.execSQL(query);
            } catch (Exception e) {
                // Bỏ qua nếu đã tồn tại
            }
        }
    }

    // ============ PUBLIC METHODS - PHƯƠNG THỨC BẠN CẦN ============

    // 🔥 1. Lấy user theo ID (dùng cho ProfileActivity)
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }
        db.close();
        return user;
    }

    // 🔥 2. Lấy số lượng followers (người theo dõi)
    public int getFollowersCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_FOLLOWERS +
                " WHERE " + COLUMN_FOLLOWED_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    // 🔥 3. Lấy số lượng following (đang theo dõi)
    public int getFollowingCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_FOLLOWERS +
                " WHERE " + COLUMN_FOLLOWER_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    // 🔥 4. Kiểm tra đã follow chưa
    public boolean isFollowing(int followerUserId, int followedUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOLLOWERS +
                " WHERE " + COLUMN_FOLLOWER_USER_ID + " = ?" +
                " AND " + COLUMN_FOLLOWED_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query,
                new String[]{String.valueOf(followerUserId), String.valueOf(followedUserId)});

        boolean isFollowing = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isFollowing;
    }

    // 🔥 5. Thêm follower
    public boolean addFollower(int followerUserId, int followedUserId) {
        // Kiểm tra đã follow chưa
        if (isFollowing(followerUserId, followedUserId)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FOLLOWER_USER_ID, followerUserId);
        values.put(COLUMN_FOLLOWED_USER_ID, followedUserId);

        long result = db.insert(TABLE_FOLLOWERS, null, values);
        db.close();

        return result != -1;
    }

    // 🔥 6. Xóa follower (unfollow)
    public boolean removeFollower(int followerUserId, int followedUserId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_FOLLOWERS,
                COLUMN_FOLLOWER_USER_ID + " = ? AND " + COLUMN_FOLLOWED_USER_ID + " = ?",
                new String[]{String.valueOf(followerUserId), String.valueOf(followedUserId)});

        db.close();
        return result > 0;
    }

    // 🔥 7. Lấy danh sách người đang theo dõi user
    public List<User> getFollowersList(int userId) {
        List<User> followers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u.* FROM " + TABLE_USERS + " u " +
                "INNER JOIN " + TABLE_FOLLOWERS + " f ON u." + COLUMN_ID + " = f." + COLUMN_FOLLOWER_USER_ID +
                " WHERE f." + COLUMN_FOLLOWED_USER_ID + " = ?" +
                " ORDER BY f." + COLUMN_FOLLOWED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                followers.add(cursorToUser(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return followers;
    }

    // 🔥 8. Lấy danh sách người mà user đang theo dõi
    public List<User> getFollowingList(int userId) {
        List<User> following = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u.* FROM " + TABLE_USERS + " u " +
                "INNER JOIN " + TABLE_FOLLOWERS + " f ON u." + COLUMN_ID + " = f." + COLUMN_FOLLOWED_USER_ID +
                " WHERE f." + COLUMN_FOLLOWER_USER_ID + " = ?" +
                " ORDER BY f." + COLUMN_FOLLOWED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                following.add(cursorToUser(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return following;
    }

    // ============ CÁC PHƯƠNG THỨC CŨ (GIỮ NGUYÊN) ============

    // 9. Đăng ký user mới
    public boolean registerUser(User newUser) {
        // Kiểm tra email đã tồn tại chưa
        if (isEmailExists(newUser.getEmail())) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, newUser.getFullName());
        values.put(COLUMN_EMAIL, newUser.getEmail());
        values.put(COLUMN_PASSWORD, newUser.getPassword());
        values.put(COLUMN_PHONE, newUser.getPhone());
        values.put(COLUMN_ADDRESS, newUser.getAddress());
        values.put(COLUMN_ROLE, newUser.getRole());
        values.put(COLUMN_STATUS, newUser.getStatus());
        values.put(COLUMN_CREATED_AT, getCurrentDate());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    // 10. Đăng nhập
    public User checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS
                + " WHERE " + COLUMN_EMAIL + " = ?"
                + " AND " + COLUMN_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }

        cursor.close();
        db.close();
        return user;
    }

    // 11. Kiểm tra email đã tồn tại
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS
                + " WHERE " + COLUMN_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return exists;
    }

    // 12. Lấy tất cả users (cho admin)
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null, null, null, null, null,
                COLUMN_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                users.add(cursorToUser(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    // 13. Cập nhật trạng thái user
    public boolean updateUserStatus(int userId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        int rowsAffected = db.update(
                TABLE_USERS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        db.close();
        return rowsAffected > 0;
    }

    // 14. Đếm tổng số users
    public int getTotalUsers() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);

        cursor.close();
        db.close();
        return count;
    }

    // 15. Lấy users theo role
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_ROLE + " = ?",
                new String[]{role},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                users.add(cursorToUser(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    // 16. Lấy tất cả customers
    public List<User> getAllCustomers() {
        return getUsersByRole("customer");
    }

    // 17. Lấy tất cả sellers
    public List<User> getAllSellers() {
        return getUsersByRole("seller");
    }

    // 18. Lấy active users
    public List<User> getActiveUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_STATUS + " = ?",
                new String[]{"active"},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                users.add(cursorToUser(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    // 19. Cập nhật thông tin user
    public boolean updateUserProfile(int userId, String fullName, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ADDRESS, address);

        int rowsAffected = db.update(
                TABLE_USERS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        db.close();
        return rowsAffected > 0;
    }

    // 20. Đổi mật khẩu
    public boolean changePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rowsAffected = db.update(
                TABLE_USERS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        db.close();
        return rowsAffected > 0;
    }

    // ============ HELPER METHODS ============
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
        user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
        user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
        return user;
    }

    // Phương thức lấy ngày hiện tại
    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}