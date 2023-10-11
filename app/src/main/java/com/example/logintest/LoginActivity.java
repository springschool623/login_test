package com.example.logintest;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button cirLoginButton;
    private ImageButton ibClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputUsername = findViewById(R.id.textInputUsername);
        textInputPassword = findViewById(R.id.textInputPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        cirLoginButton = findViewById(R.id.cirLoginButton);
        ibClose = findViewById(R.id.ibClose);

        cirLoginButton.setOnClickListener(v -> loginUser());

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    private void loginUser() {
        String enteredUsername = editTextUsername.getText().toString().trim();
        String enteredPassword = editTextPassword.getText().toString().trim();

        // Kiểm tra xem username và mật khẩu có hợp lệ không
        if (!validateUsername(enteredUsername) || !validatePassword(enteredPassword)) {
            return;
        } else {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");

            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Kiểm tra xem có bất kỳ user nào tồn tại không
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Lấy thông tin của mỗi user từ userSnapshot
                            String username = userSnapshot.child("username").getValue(String.class);
                            String password = userSnapshot.child("password").getValue(String.class);

                            // Kiểm tra xem thông tin đăng nhập có khớp với database hay không
                            if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);

                                // Đưa thông tin đăng nhập vào Intent
                                i.putExtra("username", enteredUsername);
                                i.putExtra("password", enteredPassword);

                                startActivity(i);
                                return; // Nếu đăng nhập thành công, thoát khỏi vòng lặp
                            }
                        }
                        // Nếu đến đây, có nghĩa là không có user nào khớp với thông tin đăng nhập
                        Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                        editTextUsername.setText("");
                        editTextPassword.setText("");
                    } else {
                        Toast.makeText(LoginActivity.this, "Không có user nào tồn tại", Toast.LENGTH_SHORT).show();
                        editTextUsername.setText("");
                        editTextPassword.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    editTextUsername.setText("");
                    editTextPassword.setText("");
                }
            });
        }
    }



    private boolean validateUsername(String username) {
        // Thêm kiểm tra hợp lệ cho username ở đây
        if (username.isEmpty()) {
            textInputUsername.setError("Username không được để trống");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        // Thêm kiểm tra hợp lệ cho mật khẩu ở đây
        if (password.isEmpty()) {
            textInputPassword.setError("Mật khẩu không được để trống");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
}