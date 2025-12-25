package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Order;
import com.example.bc_quanlibanhangonline.models.Product;

public class PaymentActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private String productName;

    private TextView tvFinalTotal;
    private Button btnProcessPayment;
    private ImageView btnBack;

    // Main method
    private RadioGroup mainMethodGroup;
    private RadioButton radioBuyMoney, radioExchange;

    // Payment methods
    private RadioGroup paymentMethodGroup;
    private RadioButton radioQR, radioCreditCard, radioCOD;

    private int quantity;
    private int productTotal;
    private int finalTotal;
    private final int shippingFee = 30000;
    private final int discount = 500000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        quantity = intent.getIntExtra("QUANTITY", 1);
        productTotal = intent.getIntExtra("TOTAL_PRICE", 0);
        productName = intent.getStringExtra("PRODUCT_NAME");

        databaseHelper = new DatabaseHelper(this);

        initViews();
        setupEvents();
        calculateFinalTotal();
    }

    private void initViews() {
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        btnProcessPayment = findViewById(R.id.btnProcessPayment);
        btnBack = findViewById(R.id.btnBack);

        mainMethodGroup = findViewById(R.id.mainMethodGroup);
        radioBuyMoney = findViewById(R.id.radioBuyMoney);
        radioExchange = findViewById(R.id.radioExchange);

        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        radioQR = findViewById(R.id.radioQR);
        radioCreditCard = findViewById(R.id.radioCreditCard);
        radioCOD = findViewById(R.id.radioCOD);

        // mặc định
        radioBuyMoney.setChecked(true);
        radioQR.setChecked(true);
        paymentMethodGroup.setVisibility(RadioGroup.VISIBLE);
    }



    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Chọn Mua bằng tiền / Trao đổi
        mainMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBuyMoney) {
                paymentMethodGroup.setVisibility(RadioGroup.VISIBLE);
            } else if (checkedId == R.id.radioExchange) {
                paymentMethodGroup.setVisibility(RadioGroup.GONE);
            }
        });

        // Quản lý chọn 1 trong 3 phương thức thanh toán
        radioQR.setOnClickListener(v -> selectPayment(radioQR));
        radioCreditCard.setOnClickListener(v -> selectPayment(radioCreditCard));
        radioCOD.setOnClickListener(v -> selectPayment(radioCOD));

        // Xử lý nút xác nhận phương thức
        btnProcessPayment.setOnClickListener(v -> {
            if (radioExchange.isChecked()) {
                processOrder("exchange", true); // order trao đổi
                return;
            }

            if (radioBuyMoney.isChecked()) {
                if (radioQR.isChecked()) {
                    processOrder("cad", false); // thanh toán QR
                } else if (radioCreditCard.isChecked()) {
                    processOrder("banking", false); // thanh toán thẻ
                } else if (radioCOD.isChecked()) {
                    processOrder("cash", false); // thanh toán khi nhận hàng
                } else {
                    Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Đảm bảo chỉ 1 RadioButton được chọn
    private void selectPayment(RadioButton selected) {
        radioQR.setChecked(false);
        radioCreditCard.setChecked(false);
        radioCOD.setChecked(false);
        selected.setChecked(true);
    }

    private void calculateFinalTotal() {
        finalTotal = productTotal + shippingFee - discount;
        tvFinalTotal.setText(formatPrice(finalTotal));
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }

    private void processOrder(String paymentMethod, boolean isExchange) {
        int userId = 3; // giả lập userId, sau này thay bằng session thật

        Product product = databaseHelper.getAllProducts()
                .stream()
                .filter(p -> p.getProductName().equals(productName))
                .findFirst()
                .orElse(null);

        if (product == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isExchange && quantity > product.getQuantity()) {
            Toast.makeText(this, "Số lượng đặt vượt quá tồn kho", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xác định status
        String orderType = isExchange ? "exchange" : "normal";
        String orderStatus;
        if (paymentMethod.equals("cash") || orderType.equals("exchange")) {
            orderStatus = "processing"; // chờ duyệt
        } else {
            orderStatus = "paid"; // QR / CreditCard
        }

        // Tạo order
        Order order = databaseHelper.createOrder(userId, finalTotal, orderType, paymentMethod, orderStatus);

        if (!isExchange) {
            // Tạo order detail
            databaseHelper.createOrderDetailByName(order.getOrderId(), productName, quantity, productTotal);

            // Giảm tồn kho
            databaseHelper.updateProductQuantity(product.getProductId(), quantity);
        }

        // Tạo payment
        databaseHelper.createPayment(order.getOrderId(), paymentMethod, orderStatus.equals("paid") ? "paid" : "pending");

        // Chuyển sang PaymentSuccessActivity
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra("ORDER_ID", order.getOrderId());
        intent.putExtra("ORDER_TOTAL", order.getTotalPrice());
        intent.putExtra("PAYMENT_METHOD", order.getPaymentMethod());
        intent.putExtra("ORDER_DATE", order.getOrderDate());
        startActivity(intent);
        finish();
    }
}
