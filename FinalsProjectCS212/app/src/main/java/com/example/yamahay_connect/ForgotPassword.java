package com.example.yamahay_connect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    EditText etForgotEmail;
    Button btnSendReset;
    TextView tvBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password); // forgot password XML

        etForgotEmail = findViewById(R.id.etForgotEmail);
        btnSendReset = findViewById(R.id.btnSendReset);
        tvBackLogin = findViewById(R.id.tvBackLogin);

        btnSendReset.setOnClickListener(v -> {
            // Send reset password link logic
            // After sending, you can redirect to ResetPasswordActivity if needed
            Intent intent = new Intent(ForgotPassword.this, ResetPassword.class);
            startActivity(intent);
        });

        tvBackLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPassword.this, Login.class);
            startActivity(intent);
        });
    }
}
