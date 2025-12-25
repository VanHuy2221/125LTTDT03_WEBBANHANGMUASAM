package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Order;
import com.example.bc_quanlibanhangonline.models.OrderDetail;
import com.example.bc_quanlibanhangonline.models.Product;

import java.util.List;

public class OrderManagementAdapter extends RecyclerView.Adapter<OrderManagementAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private DatabaseHelper databaseHelper;

    public OrderManagementAdapter(Context context, List<Order> orderList, DatabaseHelper db) {
        this.context = context;
        this.orderList = orderList;
        this.databaseHelper = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("#" + order.getOrderId());
        holder.txtUserDate.setText("User " + order.getUserId() + " • " + order.getOrderDate());

        // ===== Lấy tên sản phẩm =====
        List<OrderDetail> details = databaseHelper.getOrderDetailsByOrderId(order.getOrderId());
        String productNames = "";
        int totalQuantity = 0;
        for (OrderDetail d : details) {
            Product p = databaseHelper.getProductById(d.getProductId());
            if (p != null) {
                if (!productNames.isEmpty()) productNames += ", ";
                productNames += p.getProductName();
            }
            totalQuantity += d.getQuantity();
        }
        holder.txtProductName.setText(productNames);
        holder.txtQuantity.setText("Số lượng: " + totalQuantity);
        holder.txtTotal.setText(formatPrice(order.getTotalPrice()));

        // ===== Trạng thái & màu =====
        switch(order.getStatus().toLowerCase()) {
            case "processing":
                holder.txtStatus.setText("Chờ duyệt");
                holder.txtStatus.setBackgroundColor(Color.parseColor("#FFC107"));
                holder.btnApprove.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnComplete.setVisibility(View.GONE);
                break;
            case "paid":
            case "shipping":
                holder.txtStatus.setText("Đang giao");
                holder.txtStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.btnApprove.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnComplete.setVisibility(View.VISIBLE);
                break;
            case "completed":
                holder.txtStatus.setText("Hoàn tất");
                holder.txtStatus.setBackgroundColor(Color.parseColor("#2E7D32"));
                holder.btnApprove.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnComplete.setVisibility(View.GONE);
                break;
            case "cancelled":
                holder.txtStatus.setText("Đã hủy");
                holder.txtStatus.setBackgroundColor(Color.parseColor("#F44336"));
                holder.btnApprove.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnComplete.setVisibility(View.GONE);
                break;
        }

        // ===== Nút duyệt =====
        holder.btnApprove.setOnClickListener(v -> {
            databaseHelper.updateOrderStatus(order.getOrderId(), "paid");
            order.setStatus("shipping");
            notifyItemChanged(position);
            Toast.makeText(context, "Đơn hàng đã được duyệt", Toast.LENGTH_SHORT).show();
        });

        // ===== Nút từ chối =====
        holder.btnReject.setOnClickListener(v -> {
            databaseHelper.updateOrderStatus(order.getOrderId(), "cancelled");
            order.setStatus("cancelled");
            notifyItemChanged(position);
            Toast.makeText(context, "Đơn hàng đã bị từ chối", Toast.LENGTH_SHORT).show();
        });

        // ===== Nút hoàn thành =====
        holder.btnComplete.setOnClickListener(v -> {
            databaseHelper.updateOrderStatus(order.getOrderId(), "completed");
            order.setStatus("completed");
            notifyItemChanged(position);
            Toast.makeText(context, "Đơn hàng đã hoàn tất", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtStatus, txtUserDate, txtProductName, txtQuantity, txtTotal;
        Button btnApprove, btnReject, btnComplete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtUserDate = itemView.findViewById(R.id.txtUserDate);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }

    // ===== Format tiền =====
    private String formatPrice(int price) {
        return String.format("%,dđ", price);
    }
}
