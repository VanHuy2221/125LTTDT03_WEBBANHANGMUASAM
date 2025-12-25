package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.ReviewActivity;
import com.example.bc_quanlibanhangonline.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
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

        // Tạm hiển thị loại đơn
        holder.txtProductName.setText(
                order.getOrderType().equals("exchange")
                        ? "Đơn trao đổi"
                        : "Đơn mua hàng"
        );

        holder.txtTotal.setText(formatPrice(order.getTotalPrice()));

        holder.txtStatus.setText(order.getStatus());

        if ("completed".equalsIgnoreCase(order.getStatus())) {
            holder.txtStatus.setTextColor(
                    context.getColor(android.R.color.holo_green_dark)
            );
        } else {
            holder.txtStatus.setTextColor(
                    context.getColor(android.R.color.holo_orange_dark)
            );
        }

        holder.btnEvaluate.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ===== ViewHolder =====
    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderId, txtProductName, txtTotal, txtStatus;
        Button btnEvaluate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnEvaluate = itemView.findViewById(R.id.btn_evaluate);
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }
}
