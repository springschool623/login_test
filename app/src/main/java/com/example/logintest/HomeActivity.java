package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    Button btnBack;
    TextView txtHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnBack = findViewById(R.id.btnBack);
        txtHello = findViewById(R.id.txtHello);

        // Nhận thông tin từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");

            // Hiển thị thông tin đăng nhập
            txtHello.setText("Chào mừng, " + username);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến LoginActivity khi nút được nhấn
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
