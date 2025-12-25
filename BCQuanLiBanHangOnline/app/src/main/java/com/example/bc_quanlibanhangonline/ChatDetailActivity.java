package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.adapters.MessageAdapter;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;
import com.example.bc_quanlibanhangonline.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatDetailActivity extends AppCompatActivity {

    private EditText edtMessage;
    private Button btnSend;
    private ImageView btnBack;
    private TextView tvUserName, tvUserStatus;
    private RecyclerView rvMessages;

    private DatabaseHelper databaseHelper;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();

    private String exchangeId;
    private int currentUserId;
    private int otherUserId;
    private String chatType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        databaseHelper = new DatabaseHelper(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        exchangeId = intent.getStringExtra("EXCHANGE_ID");
        currentUserId = intent.getIntExtra("SENDER_ID", -1);
        otherUserId = intent.getIntExtra("RECEIVER_ID", -1);
        chatType = intent.getStringExtra("CHAT_TYPE");

        initViews();
        setupRecyclerView();
        setupUserInfo();

        // Nếu có exchangeId, hiển thị tin nhắn trao đổi
        if (exchangeId != null && "exchange".equals(chatType)) {
            loadExchangeMessages();
            addExchangeInfoToMessages();
        }

        setupEventListeners();
    }

    private void initViews() {
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserStatus = findViewById(R.id.tvUserStatus);
        rvMessages = findViewById(R.id.rvMessages);
    }

    private void setupRecyclerView() {
        // THÊM RecyclerView vào layout activity_chat_detail.xml
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(this, messageList, currentUserId);
        rvMessages.setAdapter(messageAdapter);

        // Tự động scroll xuống cuối khi có tin nhắn mới
        rvMessages.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    rvMessages.postDelayed(() -> {
                        if (messageList.size() > 0) {
                            rvMessages.smoothScrollToPosition(messageList.size() - 1);
                        }
                    }, 100);
                }
            }
        });
    }

    private void setupUserInfo() {
        if (exchangeId != null) {
            ExchangeRequest exchange = databaseHelper.getExchangeRequestById(exchangeId);
            if (exchange != null) {
                tvUserName.setText("Trao đổi #" + exchange.getExchangeId().replace("EX", ""));
                tvUserStatus.setText("Sản phẩm: " + exchange.getProductName());
            }
        }
    }

    private void addExchangeInfoToMessages() {
        if (exchangeId == null) return;

        ExchangeRequest exchange = databaseHelper.getExchangeRequestById(exchangeId);
        if (exchange != null) {
            String infoText = "📦 Trao đổi #" + exchangeId.replace("EX", "") +
                    "\n📱 Muốn: " + exchange.getProductName() +
                    "\n🔄 Đổi: " + exchange.getExchangeItemName() +
                    "\n📝 Nội dung: " + exchange.getMessage();

            // Thêm tin nhắn hệ thống
            Message systemMessage = new Message(
                    0, // messageId
                    0, // senderId = 0 (hệ thống)
                    0, // receiverId
                    null, // exchangeId
                    infoText, // content
                    null, // image
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()) // sentAt
            );

            // Thêm vào đầu danh sách
            messageList.add(0, systemMessage);
            messageAdapter.notifyItemInserted(0);
        }
    }

    private void loadExchangeMessages() {
        if (exchangeId == null) return;

        List<Message> messages = databaseHelper.getMessagesByExchangeId(exchangeId);
        if (messages != null && !messages.isEmpty()) {
            messageList.addAll(messages);
            messageAdapter.notifyDataSetChanged();

            // Scroll xuống cuối
            rvMessages.post(() -> {
                if (messageList.size() > 0) {
                    rvMessages.smoothScrollToPosition(messageList.size() - 1);
                }
            });
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> sendMessage());

        // Gửi khi nhấn Enter
        edtMessage.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == 66) { // Enter key
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendMessage() {
        String messageText = edtMessage.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu tin nhắn vào database
        Message newMessage = databaseHelper.createExchangeMessage(
                currentUserId,
                otherUserId,
                exchangeId,
                messageText
        );

        // Thêm vào adapter
        messageAdapter.addMessage(newMessage);

        // Xóa nội dung input
        edtMessage.setText("");

        // Scroll xuống cuối
        if (messageList.size() > 0) {
            rvMessages.smoothScrollToPosition(messageList.size() - 1);
        }

        Toast.makeText(this, "Đã gửi tin nhắn", Toast.LENGTH_SHORT).show();
    }
}