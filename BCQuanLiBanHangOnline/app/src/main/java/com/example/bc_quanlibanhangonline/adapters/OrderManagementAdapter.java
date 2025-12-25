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
import com.example.bc_quanlibanhangonline.ChatDetailActivity; // THÊM
import com.example.bc_quanlibanhangonline.ExchangeDetailActivity;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;
import com.example.bc_quanlibanhangonline.models.Order;
import com.example.bc_quanlibanhangonline.models.OrderDetail;
import com.example.bc_quanlibanhangonline.models.Product;

import java.util.List;

public class OrderManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> itemList;
    private DatabaseHelper databaseHelper;

    private static final int VIEW_TYPE_ORDER = 1;
    private static final int VIEW_TYPE_EXCHANGE = 2;

    // THÊM: Lưu userId của người bán
    private int currentUserId = 1; // Mặc định người bán = 1

    public OrderManagementAdapter(Context context, List<Object> itemList, DatabaseHelper db) {
        this.context = context;
        this.itemList = itemList;
        this.databaseHelper = db;
    }

    // THÊM: Constructor với userId
    public OrderManagementAdapter(Context context, List<Object> itemList, DatabaseHelper db, int currentUserId) {
        this.context = context;
        this.itemList = itemList;
        this.databaseHelper = db;
        this.currentUserId = currentUserId;
    }

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
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_management, parent, false);
            return new OrderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_exchange_request, parent, false);
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

    private void bindOrderItem(OrderViewHolder holder, Order order, int position) {
        holder.txtOrderId.setText("#" + order.getOrderId());
        holder.txtUserDate.setText("User " + order.getUserId() + " • " + order.getOrderDate());

        // Lấy tên sản phẩm
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

        // Trạng thái & màu
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

        final int itemPosition = position;

        // Nút duyệt
        holder.btnApprove.setOnClickListener(v -> {
            Order currentOrder = (Order) itemList.get(itemPosition);
            databaseHelper.updateOrderStatus(currentOrder.getOrderId(), "paid");
            currentOrder.setStatus("shipping");
            notifyItemChanged(itemPosition);
            Toast.makeText(context, "Đơn hàng đã được duyệt", Toast.LENGTH_SHORT).show();
        });

        // Nút từ chối
        holder.btnReject.setOnClickListener(v -> {
            Order currentOrder = (Order) itemList.get(itemPosition);
            databaseHelper.updateOrderStatus(currentOrder.getOrderId(), "cancelled");
            currentOrder.setStatus("cancelled");
            notifyItemChanged(itemPosition);
            Toast.makeText(context, "Đơn hàng đã bị từ chối", Toast.LENGTH_SHORT).show();
        });

        // Nút hoàn thành
        holder.btnComplete.setOnClickListener(v -> {
            Order currentOrder = (Order) itemList.get(itemPosition);
            databaseHelper.updateOrderStatus(currentOrder.getOrderId(), "completed");
            currentOrder.setStatus("completed");
            notifyItemChanged(itemPosition);
            Toast.makeText(context, "Đơn hàng đã hoàn tất", Toast.LENGTH_SHORT).show();
        });
    }

    private void bindExchangeItem(ExchangeViewHolder holder, ExchangeRequest exchange, int position) {
        holder.txtExchangeId.setText("TD#" + exchange.getExchangeId().replace("EX", ""));
        holder.txtProductWant.setText("Muốn: " + exchange.getProductName());
        holder.txtProductOffer.setText("Đổi: " + exchange.getExchangeItemName());

        // Rút gọn message nếu quá dài
        String message = exchange.getMessage();
        if (message.length() > 50) {
            message = message.substring(0, 47) + "...";
        }
        holder.txtMessage.setText(message);

        // Hiển thị trạng thái
        String status = exchange.getStatus();
        holder.txtStatus.setText(status);

        // Đặt màu theo trạng thái
        switch(status) {
            case "Đang chờ phản hồi":
                holder.txtStatus.setBackgroundColor(Color.parseColor("#FF9800"));
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnMessage.setVisibility(View.VISIBLE);
                break;
            case "Đã chấp nhận":
                holder.txtStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnMessage.setVisibility(View.VISIBLE);
                break;
            case "Đã từ chối":
                holder.txtStatus.setBackgroundColor(Color.parseColor("#F44336"));
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnMessage.setVisibility(View.VISIBLE);
                break;
            default:
                holder.txtStatus.setBackgroundColor(Color.parseColor("#9E9E9E"));
        }

        final int itemPosition = position;

        // NÚT XEM CHI TIẾT - GIỮ NGUYÊN: MỞ ExchangeDetailActivity (cho người bán)
        holder.btnViewDetail.setOnClickListener(v -> {
            ExchangeRequest currentExchange = (ExchangeRequest) itemList.get(itemPosition);
            Intent intent = new Intent(context, ExchangeDetailActivity.class);
            intent.putExtra("EXCHANGE_ID", currentExchange.getExchangeId());
            context.startActivity(intent);
        });

        // Sự kiện nút chấp nhận
        holder.btnAccept.setOnClickListener(v -> {
            ExchangeRequest currentExchange = (ExchangeRequest) itemList.get(itemPosition);
            databaseHelper.updateExchangeStatus(currentExchange.getExchangeId(), "Đã chấp nhận");
            itemList.set(itemPosition, databaseHelper.getExchangeRequestById(currentExchange.getExchangeId()));
            notifyItemChanged(itemPosition);
            Toast.makeText(context, "Đã chấp nhận yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện nút từ chối
        holder.btnReject.setOnClickListener(v -> {
            ExchangeRequest currentExchange = (ExchangeRequest) itemList.get(itemPosition);
            databaseHelper.updateExchangeStatus(currentExchange.getExchangeId(), "Đã từ chối");
            itemList.set(itemPosition, databaseHelper.getExchangeRequestById(currentExchange.getExchangeId()));
            notifyItemChanged(itemPosition);
            Toast.makeText(context, "Đã từ chối yêu cầu trao đổi", Toast.LENGTH_SHORT).show();
        });

        // SỬA: Sự kiện nút nhắn tin - MỞ ChatDetailActivity
        holder.btnMessage.setOnClickListener(v -> {
            ExchangeRequest currentExchange = (ExchangeRequest) itemList.get(itemPosition);
            openChatForExchange(currentExchange);
        });
    }

    // THÊM: Phương thức mở chat cho trao đổi
    private void openChatForExchange(ExchangeRequest exchange) {
        try {
            Intent intent = new Intent(context, ChatDetailActivity.class);
            intent.putExtra("EXCHANGE_ID", exchange.getExchangeId());

            // Người bán (currentUserId) là sender, người mua (exchange.getUserId()) là receiver
            intent.putExtra("SENDER_ID", currentUserId); // Người bán
            intent.putExtra("RECEIVER_ID", exchange.getUserId()); // Người mua (lấy từ exchange)
            intent.putExtra("CHAT_TYPE", "exchange");

            context.startActivity(intent);

            Toast.makeText(context, "Mở chat trao đổi #" + exchange.getExchangeId(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Không thể mở chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder cho Order
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtStatus, txtUserDate, txtProductName, txtQuantity, txtTotal;
        Button btnApprove, btnReject, btnComplete;

        public OrderViewHolder(@NonNull View itemView) {
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

    // ViewHolder cho ExchangeRequest
    public static class ExchangeViewHolder extends RecyclerView.ViewHolder {
        TextView txtExchangeId, txtProductWant, txtProductOffer, txtMessage, txtStatus;
        Button btnViewDetail, btnAccept, btnReject, btnMessage;

        public ExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtExchangeId = itemView.findViewById(R.id.txtExchangeId);
            txtProductWant = itemView.findViewById(R.id.txtProductWant);
            txtProductOffer = itemView.findViewById(R.id.txtProductOffer);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }

    // Format tiền
    private String formatPrice(int price) {
        return String.format("%,dđ", price);
    }
}