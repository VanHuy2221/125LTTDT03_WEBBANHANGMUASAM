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
    private int productId = -1;
    private int userId = -1;

    private TextView tvFinalTotal;
    private Button btnProcessPayment;
    private ImageView btnBack;

    private RadioGroup mainMethodGroup;
    private RadioButton radioBuyMoney, radioExchange;

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
        productId = intent.getIntExtra("PRODUCT_ID", -1);
        userId = intent.getIntExtra("USER_ID", -1);

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

        radioBuyMoney.setChecked(true);
        radioQR.setChecked(true);
        paymentMethodGroup.setVisibility(RadioGroup.VISIBLE);

        btnProcessPayment.setText("XÁC NHẬN PHƯƠNG THỨC");
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        mainMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBuyMoney) {
                paymentMethodGroup.setVisibility(RadioGroup.VISIBLE);
                btnProcessPayment.setText("XÁC NHẬN PHƯƠNG THỨC");
            } else {
                paymentMethodGroup.setVisibility(RadioGroup.GONE);
                btnProcessPayment.setText("TRAO ĐỔI SẢN PHẨM");
            }
        });

        radioQR.setOnClickListener(v -> selectPayment(radioQR));
        radioCreditCard.setOnClickListener(v -> selectPayment(radioCreditCard));
        radioCOD.setOnClickListener(v -> selectPayment(radioCOD));

        btnProcessPayment.setOnClickListener(v -> {
            if (radioExchange.isChecked()) {
                openExchangeActivity();
                return;
            }

            if (radioBuyMoney.isChecked()) {
                if (radioQR.isChecked()) {
                    openQRPaymentActivity();
                } else if (radioCreditCard.isChecked()) {
                    processOrder("banking", false);
                } else if (radioCOD.isChecked()) {
                    processOrder("cash", false);
                } else {
                    Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectPayment(RadioButton selected) {
        radioQR.setChecked(false);
        radioCreditCard.setChecked(false);
        radioCOD.setChecked(false);
        selected.setChecked(true);
    }

    private void calculateFinalTotal() {
        finalTotal = productTotal + shippingFee - discount;
        tvFinalTotal.setText(String.format("%,dđ", finalTotal).replace(",", "."));
    }

    private void openQRPaymentActivity() {
        Intent intent = new Intent(this, QRPaymentActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("TOTAL_PRICE", finalTotal);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void openExchangeActivity() {
        Intent intent = new Intent(this, ExchangeActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("TOTAL_PRICE", finalTotal);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void processOrder(String paymentMethod, boolean isExchange) {
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = null;
        if (productId != -1) {
            product = databaseHelper.getProductById(productId);
        }
        if (product == null) {
            product = databaseHelper.getAllProducts()
                    .stream()
                    .filter(p -> p.getProductName().equals(productName))
                    .findFirst()
                    .orElse(null);
        }

        if (product == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isExchange && quantity > product.getQuantity()) {
            Toast.makeText(this, "Số lượng đặt vượt quá tồn kho", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderType = isExchange ? "exchange" : "normal";
        String orderStatus = (paymentMethod.equals("cash") || orderType.equals("exchange")) ? "processing" : "paid";

        Order order = databaseHelper.createOrder(userId, finalTotal, orderType, paymentMethod, orderStatus);

        if (!isExchange) {
            databaseHelper.createOrderDetail(order.getOrderId(), product.getProductId(), quantity, productTotal);
            databaseHelper.updateProductQuantity(product.getProductId(), product.getQuantity() - quantity);
        }

        databaseHelper.createPayment(order.getOrderId(), paymentMethod, orderStatus.equals("paid") ? "paid" : "pending");

        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra("ORDER_ID", order.getOrderId());
        intent.putExtra("ORDER_TOTAL", order.getTotalPrice());
        intent.putExtra("PAYMENT_METHOD", order.getPaymentMethod());
        intent.putExtra("ORDER_DATE", order.getOrderDate());
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }
}
