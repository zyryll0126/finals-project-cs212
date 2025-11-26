package com.example.yamahay_connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button btnLogin, btnGoogle, btnFacebook;
    TextView forgotPassword, signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // login XML

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUpText = findViewById(R.id.signUpText);

        // Navigate to Register screen
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(new Intent(this,Register.class));
        });

        // Navigate to Forgot Password screen
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });

        // Login button
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);
        });

        // Social buttons (placeholders)
        btnGoogle.setOnClickListener(v -> {
            // Google login logic
        });

        btnFacebook.setOnClickListener(v -> {
            // Facebook login logic
        });
    }
}
