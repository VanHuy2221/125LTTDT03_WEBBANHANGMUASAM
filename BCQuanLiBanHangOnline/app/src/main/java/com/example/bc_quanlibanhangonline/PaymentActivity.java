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

import java.util.ArrayList;

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

    private ArrayList<Integer> productIds;
    private ArrayList<Integer> quantities;
    private ArrayList<Integer> prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        if (intent.hasExtra("PRODUCT_IDS")) {
            productIds = intent.getIntegerArrayListExtra("PRODUCT_IDS");
            quantities = intent.getIntegerArrayListExtra("QUANTITIES");
            prices = intent.getIntegerArrayListExtra("PRICES");
        } else if (intent.hasExtra("PRODUCT_ID")) {
            productId = intent.getIntExtra("PRODUCT_ID", -1);
            productName = intent.getStringExtra("PRODUCT_NAME");
            quantity = intent.getIntExtra("QUANTITY", 1);
            productTotal = intent.getIntExtra("TOTAL_PRICE", 0);

            productIds = new ArrayList<>();
            quantities = new ArrayList<>();
            prices = new ArrayList<>();
            productIds.add(productId);
            quantities.add(quantity);
            prices.add(productTotal / quantity); // giả sử productTotal = price * quantity
        } else {
            Toast.makeText(this, "Không có sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
        int subtotal = 0;
        for (int i = 0; i < productIds.size(); i++) {
            subtotal += prices.get(i) * quantities.get(i);
        }
        finalTotal = subtotal + shippingFee - discount;
        tvFinalTotal.setText(String.format("%,dđ", finalTotal).replace(",", "."));
    }

    private void openQRPaymentActivity() {
        Intent intent = new Intent(this, QRPaymentActivity.class);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putIntegerArrayListExtra("PRODUCT_IDS", productIds);
        intent.putIntegerArrayListExtra("QUANTITIES", quantities);
        intent.putIntegerArrayListExtra("PRICES", prices);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void openExchangeActivity() {
        Intent intent = new Intent(this, ExchangeActivity.class);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putIntegerArrayListExtra("PRODUCT_IDS", productIds);
        intent.putIntegerArrayListExtra("QUANTITIES", quantities);
        intent.putIntegerArrayListExtra("PRICES", prices);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void processOrder(String paymentMethod, boolean isExchange) {
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderType = isExchange ? "exchange" : "normal";
        String orderStatus = (paymentMethod.equals("cash") || isExchange) ? "processing" : "paid";

        Order order = databaseHelper.createOrder(userId, finalTotal, orderType, paymentMethod, orderStatus);

        for (int i = 0; i < productIds.size(); i++) {
            int pid = productIds.get(i);
            int qty = quantities.get(i);
            int price = prices.get(i);

            Product product = databaseHelper.getProductById(pid);

            // Nếu product null, tạo tạm dùng productName đã truyền
            if (product == null) {
                product = new Product();
                product.setProductId(pid);
                product.setProductName("Sản phẩm #" + pid); // fallback nếu bạn không có tên
                product.setPrice(price / qty); // tạm tính
            }

            // Kiểm tra tồn kho
            if (!isExchange && qty > product.getQuantity()) {
                Toast.makeText(this, "Sản phẩm " + product.getProductName() + " vượt quá tồn kho", Toast.LENGTH_SHORT).show();
                continue;
            }

            // Cập nhật tồn kho
            if (!isExchange) {
                int newQty = Math.max(0, product.getQuantity() - qty);
                databaseHelper.updateProductQuantity(pid, newQty);
            }

            // Tạo order detail
            databaseHelper.createOrderDetail(order.getOrderId(), pid, qty, price);
        }

        // Tạo bản ghi thanh toán
        databaseHelper.createPayment(order.getOrderId(), paymentMethod, orderStatus.equals("paid") ? "paid" : "pending");

        // Nếu đặt từ giỏ hàng, xóa giỏ hàng
        if (productIds.size() > 1) {
            databaseHelper.clearCartByUser(userId);
        }

        // Chuyển sang PaymentSuccessActivity
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
