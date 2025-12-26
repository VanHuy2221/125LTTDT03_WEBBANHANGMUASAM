package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> messageList;
    private int currentUserId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_SYSTEM = 3;

    public MessageAdapter(Context context, List<Message> messageList, int currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        // Kiểm tra nếu là tin nhắn hệ thống (senderId = 0)
        if (message.getSenderId() == 0) {
            return VIEW_TYPE_SYSTEM;
        }

        // Kiểm tra nếu là tin nhắn gửi
        if (message.getSenderId() == currentUserId) {
            return VIEW_TYPE_SENT;
        }

        // Tin nhắn nhận
        return VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_system_message, parent, false);
            return new SystemMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        } else if (holder instanceof SystemMessageViewHolder) {
            ((SystemMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // THÊM: Phương thức để thêm tin nhắn mới
    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    // THÊM: Phương thức để cập nhật toàn bộ danh sách
    public void updateMessages(List<Message> newMessages) {
        messageList.clear();
        messageList.addAll(newMessages);
        notifyDataSetChanged();
    }

    // ViewHolder cho tin nhắn gửi
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        LinearLayout messageLayout;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            tvTime.setText(formatTime(message.getSentAt()));
        }
    }

    // ViewHolder cho tin nhắn nhận
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvInitials, tvMessage, tvTime, tvSenderName;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInitials = itemView.findViewById(R.id.tvInitials);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
        }

        public void bind(Message message) {
            String initials = getInitialsFromUserId(message.getSenderId());
            String senderName = getSenderNameFromUserId(message.getSenderId());

            tvInitials.setText(initials);
            tvSenderName.setText(senderName);
            tvMessage.setText(message.getContent());
            tvTime.setText(formatTime(message.getSentAt()));
        }

        private String getInitialsFromUserId(int userId) {
            switch (userId) {
                case 1: return "NB";
                case 3: return "NM";
                default: return "U" + userId;
            }
        }

        private String getSenderNameFromUserId(int userId) {
            switch (userId) {
                case 1: return "Người bán";
                case 3: return "Người mua";
                default: return "Người dùng " + userId;
            }
        }
    }

    // ViewHolder cho tin nhắn hệ thống
    static class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSystemText;

        public SystemMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSystemText = itemView.findViewById(R.id.tvSystemText);
        }

        public void bind(Message message) {
            tvSystemText.setText(message.getContent());
        }
    }

    private static String formatTime(String sentAt) {
        try {
            if (sentAt == null || sentAt.isEmpty()) {
                return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            }

            // Nếu sentAt là timestamp dài
            if (sentAt.length() > 10) {
                try {
                    // Thử parse từ timestamp
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(sentAt);
                    return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
                } catch (Exception e) {
                    // Nếu không parse được, trả về sentAt nguyên bản
                    return sentAt.length() > 5 ? sentAt.substring(11, 16) : sentAt;
                }
            }

            return sentAt;
        } catch (Exception e) {
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        }
    }
}