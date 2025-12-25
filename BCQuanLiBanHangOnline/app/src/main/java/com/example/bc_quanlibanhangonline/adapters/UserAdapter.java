package com.example.bc_quanlibanhangonline.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;

    // THÊM MỚI: Interface cho các hành động
    public interface OnUserActionListener {
        void onItemClick(User user);
        void onViewDetails(User user);
        void onToggleStatus(User user);
        void onEditUser(User user);
    }

    // THÊM MỚI: Biến listener
    private OnUserActionListener listener;

    // THÊM MỚI: Constructor cập nhật
    public UserAdapter(List<User> userList, OnUserActionListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    // Giữ nguyên constructor cũ để tương thích ngược
    public UserAdapter(List<User> userList) {
        this.userList = userList;
        this.listener = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        // CODE CŨ: Hiển thị thông tin user
        holder.tvUserName.setText(user.getFullName());
        holder.tvUserEmail.setText(user.getEmail());
        holder.tvUserRole.setText(getRoleDisplay(user.getRole()));
        holder.tvUserStatus.setText(getStatusDisplay(user.getStatus()));

        // CODE CŨ: Đổi màu theo trạng thái
        if (isUserActive(user)) {
            holder.tvUserStatus.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvUserStatus.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_red_dark));
        }

        // THÊM MỚI: Đổi màu background cho role
        String role = user.getRole();
        if (role != null) {
            int roleColor;
            switch (role.toLowerCase()) {
                case "admin":
                    roleColor = holder.itemView.getContext()
                            .getResources().getColor(android.R.color.holo_red_dark);
                    holder.tvUserRole.setBackgroundColor(roleColor);
                    holder.tvUserRole.setText("Quản trị");
                    break;
                case "seller":
                    roleColor = holder.itemView.getContext()
                            .getResources().getColor(android.R.color.holo_orange_dark);
                    holder.tvUserRole.setBackgroundColor(roleColor);
                    holder.tvUserRole.setText("Người bán");
                    break;
                case "customer":
                default:
                    roleColor = holder.itemView.getContext()
                            .getResources().getColor(android.R.color.holo_green_dark);
                    holder.tvUserRole.setBackgroundColor(roleColor);
                    holder.tvUserRole.setText("Người mua");
                    break;
            }
        }

        // THÊM MỚI: Thiết lập nút Khóa/Mở khóa
        setupToggleButton(holder, user);

        // THÊM MỚI: Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user);
            }
        });

        // THÊM MỚI: Xử lý sự kiện long click
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onEditUser(user);
                return true;
            }
            return false;
        });
    }

    // THÊM MỚI: Thiết lập nút Khóa/Mở khóa
    private void setupToggleButton(ViewHolder holder, User user) {
        if (holder.btnToggleStatus != null) {
            boolean isActive = isUserActive(user);

            if (isActive) {
                // Tài khoản đang hoạt động -> hiển thị nút "KHÓA"
                holder.btnToggleStatus.setText("KHÓA");
                holder.btnToggleStatus.setBackgroundColor(
                        holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark)
                );
            } else {
                // Tài khoản bị khóa -> hiển thị nút "MỞ KHÓA"
                holder.btnToggleStatus.setText("MỞ KHÓA");
                holder.btnToggleStatus.setBackgroundColor(
                        holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark)
                );
            }

            // Sự kiện click nút
            holder.btnToggleStatus.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onToggleStatus(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // THÊM MỚI: Phương thức cập nhật danh sách
    public void updateUsers(List<User> newUsers) {
        this.userList = newUsers;
        notifyDataSetChanged();
    }

    // THÊM MỚI: Phương thức lọc danh sách
    public void filterUsers(List<User> filteredList) {
        this.userList = filteredList;
        notifyDataSetChanged();
    }

    // THÊM MỚI: Phương thức thêm user
    public void addUser(User user) {
        this.userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    // THÊM MỚI: Phương thức xóa user
    public void removeUser(int position) {
        if (position >= 0 && position < userList.size()) {
            userList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // THÊM MỚI: Phương thức cập nhật user
    public void updateUser(int position, User user) {
        if (position >= 0 && position < userList.size()) {
            userList.set(position, user);
            notifyItemChanged(position);
        }
    }

    private String getRoleDisplay(String role) {
        if (role == null) return "Người mua";

        switch (role.toLowerCase()) {
            case "admin": return "Quản trị viên";
            case "seller": return "Người bán";
            case "customer": return "Người mua";
            default: return role;
        }
    }

    private String getStatusDisplay(String status) {
        if (status == null) return "Không xác định";
        return "active".equalsIgnoreCase(status) ? "Đang hoạt động" : "Đã khóa";
    }

    // THÊM MỚI: Kiểm tra trạng thái user
    private boolean isUserActive(User user) {
        return user != null && "active".equalsIgnoreCase(user.getStatus());
    }

    // THÊM MỚI: Phương thức helper để lấy đối tượng user theo vị trí
    public User getUserAtPosition(int position) {
        if (position >= 0 && position < userList.size()) {
            return userList.get(position);
        }
        return null;
    }

    // THÊM MỚI: Phương thức lấy toàn bộ danh sách
    public List<User> getUserList() {
        return userList;
    }

    // THÊM MỚI: Phương thức xóa toàn bộ
    public void clearUsers() {
        int size = userList.size();
        userList.clear();
        notifyItemRangeRemoved(0, size);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // CODE CŨ: Các TextView có trong layout
        TextView tvUserName, tvUserEmail, tvUserRole, tvUserStatus;

        // THÊM MỚI: Nút Khóa/Mở khóa
        Button btnToggleStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // CODE CŨ: Ánh xạ các TextView từ layout item_user.xml
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            tvUserRole = itemView.findViewById(R.id.tv_user_role);
            tvUserStatus = itemView.findViewById(R.id.tv_user_status);

            // THÊM MỚI: Ánh xạ nút Khóa/Mở khóa
            btnToggleStatus = itemView.findViewById(R.id.btn_toggle_status);
        }
    }
}