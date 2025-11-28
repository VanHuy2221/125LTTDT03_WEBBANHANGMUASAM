package com.example.bc_quanlibanhangonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        setupBackClickListener();
    }

    private void setupBackClickListener(){
        View Back = findViewById(R.id.btnBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToOrder();
            }
        });
    }

    private void backToOrder(){
        Intent intent = new Intent(ReviewActivity.this, OrderTrackingActivity.class);
        startActivity(intent);
    }
}