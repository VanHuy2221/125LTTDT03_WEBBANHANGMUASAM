package com.example.bc_quanlibanhangonline;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.ExchangeRequest;

public class ExchangeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    private DatabaseHelper databaseHelper;
    private String targetProductName;

    private EditText edtProductName, edtDescription, edtEstimatedPrice;
    private ImageView imgProduct;
    private Button btnSubmitExchange;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        targetProductName = intent.getStringExtra("PRODUCT_NAME");

        initViews();
        setupEvents();
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtEstimatedPrice = findViewById(R.id.edtEstimatedPrice);
        imgProduct = findViewById(R.id.imgProduct);
        btnSubmitExchange = findViewById(R.id.btnSubmitExchange);
    }

    private void setupEvents() {

        imgProduct.setOnClickListener(v -> openGallery());

        btnSubmitExchange.setOnClickListener(v -> submitExchange());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgProduct.setImageURI(selectedImageUri);
        }
    }

    private void submitExchange() {

        String name = edtProductName.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String price = edtEstimatedPrice.getText().toString().trim();

        if (name.isEmpty() || desc.isEmpty() || price.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this,
                    "Vui lòng nhập đầy đủ thông tin và hình ảnh",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ExchangeRequest exchange = databaseHelper.createExchange(
                targetProductName,
                name,
                desc
        );

        Toast.makeText(this,
                "Đã gửi đề nghị trao đổi, chờ người bán phản hồi",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra("ORDER_ID", exchange.getExchangeId());
        intent.putExtra("ORDER_TOTAL", 0);
        intent.putExtra("PAYMENT_METHOD", "Trao đổi");
        intent.putExtra("ORDER_DATE", "Hôm nay");
        startActivity(intent);
        finish();
    }
}
