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

import com.example.bc_quanlibanhangonline.ExchangeDetailForBuyerActivity;
import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.ReviewActivity;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;
import com.example.bc_quanlibanhangonline.models.Order;
import com.example.bc_quanlibanhangonline.models.OrderDetail;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> itemList;
    private DatabaseHelper databaseHelper;

    // THÊM: View types
    private static final int VIEW_TYPE_ORDER = 1;
    private static final int VIEW_TYPE_EXCHANGE = 2;

    public OrderAdapter(Context context, List<Object> itemList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.itemList = itemList;
        this.databaseHelper = databaseHelper;
    }

    // THÊM: Phương thức xác định loại item
    @Override
    public int getItemViewType(int position) {
        Object item = itemList.get(position);
        if (item instanceof Order) {
            return VIEW_TYPE_ORDER;
        } else if (item instanceof ExchangeRequest) {
            return VIEW_TYPE_EXCHANGE;
        }
        return VIEW_TYPE_ORDER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ORDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_exchange_tracking, parent, false);
            return new ExchangeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itemList.get(position);

        if (holder instanceof OrderViewHolder && item instanceof Order) {
            bindOrderItem((OrderViewHolder) holder, (Order) item, position);
        } else if (holder instanceof ExchangeViewHolder && item instanceof ExchangeRequest) {
            bindExchangeItem((ExchangeViewHolder) holder, (ExchangeRequest) item, position);
        }
    }

    // GIỮ NGUYÊN: Xử lý Order (code cũ của bạn)
    private void bindOrderItem(OrderViewHolder holder, Order order, int position) {
        holder.txtOrderId.setText("Đơn hàng #" + order.getOrderId());

        // Lấy tên sản phẩm từ OrderDetail
        List<OrderDetail> details = databaseHelper.getOrderDetailsByOrderId(order.getOrderId());
        String productNames = "";
        for (OrderDetail detail : details) {
            if (!productNames.isEmpty()) productNames += ", ";
            productNames += databaseHelper.getProductById(detail.getProductId()).getProductName();
        }

        // Kiểm tra nếu là đơn trao đổi
        if ("exchange".equalsIgnoreCase(order.getOrderType())) {
            holder.txtProductName.setText("Đơn trao đổi: " + productNames);
        } else {
            holder.txtProductName.setText("Mua hàng: " + productNames);
        }

        // Tổng tiền
        holder.txtTotal.setText(formatPrice(order.getTotalPrice()));

        // Trạng thái & màu
        switch (order.getStatus().toLowerCase()) {
            case "processing":
                holder.txtStatus.setText("Đang xử lý");
                holder.txtStatus.setTextColor(Color.parseColor("#FF9800"));
                holder.btnCancel.setVisibility(View.VISIBLE);
                break;
            case "paid":
                holder.txtStatus.setText("Đã thanh toán");
                holder.txtStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.btnCancel.setVisibility(View.GONE);
                break;
            case "shipping":
                holder.txtStatus.setText("Đang giao");
                holder.txtStatus.setTextColor(Color.parseColor("#2196F3"));
                holder.btnCancel.setVisibility(View.GONE);
                break;
            case "completed":
                holder.txtStatus.setText("Hoàn tất");
                holder.txtStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.btnCancel.setVisibility(View.GONE);
                break;
            case "cancelled":
                holder.txtStatus.setText("Đã hủy");
                holder.txtStatus.setTextColor(Color.parseColor("#F44336"));
                holder.btnCancel.setVisibility(View.GONE);
                break;
        }

        // Xem chi tiết / đánh giá
        holder.btnEvaluate.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderId());
            context.startActivity(intent);
        });

        // Hủy đơn
        holder.btnCancel.setOnClickListener(v -> {
            if ("processing".equalsIgnoreCase(order.getStatus())) {
                boolean cancelled = databaseHelper.cancelOrder(order.getOrderId());
                if (cancelled) {
                    order.setStatus("cancelled");
                    Toast.makeText(context, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Không thể hủy đơn này", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Chỉ đơn đang xử lý mới có thể hủy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // THÊM: Xử lý ExchangeRequest - SỬA PHẦN NÀY
    private void bindExchangeItem(ExchangeViewHolder holder, ExchangeRequest exchange, int position) {
        holder.txtExchangeId.setText("Trao đổi #" + exchange.getExchangeId().replace("EX", ""));
        holder.txtProductName.setText("Muốn: " + exchange.getProductName());
        holder.txtProductOffer.setText("Đổi: " + exchange.getExchangeItemName());

        // Hiển thị trạng thái
        String status = exchange.getStatus();
        holder.txtStatus.setText(status);

        // Đặt màu theo trạng thái
        switch(status) {
            case "Đang chờ phản hồi":
                holder.txtStatus.setTextColor(Color.parseColor("#FF9800"));
                holder.btnCancel.setVisibility(View.VISIBLE);
                break;
            case "Đã chấp nhận":
                holder.txtStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.btnCancel.setVisibility(View.GONE);
                break;
            case "Đã từ chối":
                holder.txtStatus.setTextColor(Color.parseColor("#F44336"));
                holder.btnCancel.setVisibility(View.GONE);
                break;
            default:
                holder.txtStatus.setTextColor(Color.parseColor("#9E9E9E"));
        }

        // Giá trị ước tính (tạm thời)
        holder.txtTotal.setText("Giá trị ước tính");

        // Sự kiện nút
        final int itemPosition = position;

        // Nút hủy trao đổi (CHO NGƯỜI MUA)
        holder.btnCancel.setOnClickListener(v -> {
            if ("Đang chờ phản hồi".equals(exchange.getStatus())) {
                // Cập nhật trạng thái từ chối
                databaseHelper.updateExchangeStatus(exchange.getExchangeId(), "Đã từ chối");
                // Cập nhật item trong list
                itemList.set(itemPosition, databaseHelper.getExchangeRequestById(exchange.getExchangeId()));
                notifyItemChanged(itemPosition);
                Toast.makeText(context, "Đã hủy yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Không thể hủy trao đổi này", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút xem chi tiết - SỬA: MỞ ExchangeDetailForBuyerActivity (cho người mua)
        holder.btnEvaluate.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExchangeDetailForBuyerActivity.class); // ĐÃ SỬA
            intent.putExtra("EXCHANGE_ID", exchange.getExchangeId());
            context.startActivity(intent);
        });

        // Click vào toàn bộ item để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExchangeDetailForBuyerActivity.class); // ĐÃ SỬA
            intent.putExtra("EXCHANGE_ID", exchange.getExchangeId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // GIỮ NGUYÊN: ViewHolder cho Order
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

    // THÊM: ViewHolder cho ExchangeRequest
    static class ExchangeViewHolder extends RecyclerView.ViewHolder {
        TextView txtExchangeId, txtProductName, txtProductOffer, txtStatus, txtTotal;
        Button btnEvaluate, btnCancel;

        public ExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtExchangeId = itemView.findViewById(R.id.txtExchangeId);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductOffer = itemView.findViewById(R.id.txtProductOffer);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            btnEvaluate = itemView.findViewById(R.id.btn_evaluate);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }
}