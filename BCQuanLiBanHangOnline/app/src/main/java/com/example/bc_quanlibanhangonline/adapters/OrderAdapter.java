package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.bc_quanlibanhangonline.ReviewActivity;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private DatabaseHelper databaseHelper;

    public OrderAdapter(Context context, List<Order> orderList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.orderList = orderList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Đơn hàng #" + order.getOrderId());

        holder.txtProductName.setText(
                order.getOrderType().equals("exchange")
                        ? "Đơn trao đổi"
                        : "Đơn mua hàng"
        );

        holder.txtTotal.setText(formatPrice(order.getTotalPrice()));

        // Hiển thị trạng thái và màu
        switch (order.getStatus().toLowerCase()) {
            case "processing":
                holder.txtStatus.setText("Đang xử lý");
                holder.txtStatus.setTextColor(Color.parseColor("#FF9800"));
                holder.btnCancel.setEnabled(true);
                break;
            case "shipping":
                holder.txtStatus.setText("Đang giao");
                holder.txtStatus.setTextColor(Color.parseColor("#2196F3"));
                holder.btnCancel.setEnabled(false);
                break;
            case "completed":
                holder.txtStatus.setText("Hoàn tất");
                holder.txtStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.btnCancel.setEnabled(false);
                break;
            case "cancelled":
                holder.txtStatus.setText("Đã hủy");
                holder.txtStatus.setTextColor(Color.parseColor("#F44336"));
                holder.btnCancel.setEnabled(false);
                break;
        }

        // Xử lý click xem chi tiết / đánh giá
        holder.btnEvaluate.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderId());
            context.startActivity(intent);
        });

        // Xử lý hủy đơn
        holder.btnCancel.setOnClickListener(v -> {
            if ("chờ duyệt".equalsIgnoreCase(order.getStatus())) {
                boolean cancelled = databaseHelper.cancelOrder(order.getOrderId());
                if (cancelled) {
                    order.setStatus("cancelled");
                    Toast.makeText(context, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(position); // cập nhật UI
                } else {
                    Toast.makeText(context, "Không thể hủy đơn này", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Chỉ đơn đang xử lý mới có thể hủy", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ===== ViewHolder =====
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtProductName, txtTotal, txtStatus;
        Button btnEvaluate, btnCancel;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnEvaluate = itemView.findViewById(R.id.btn_evaluate);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }
}
