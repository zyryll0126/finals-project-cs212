package com.example.yamahay_connect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText etNewPassword, etConfirmNewPassword;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;
    private String actionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password); // reset password XML

        mAuth = FirebaseAuth.getInstance();

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Get the action code from the intent's data (from the deep link)
        if (getIntent() != null && getIntent().getData() != null) {
            actionCode = getIntent().getData().getQueryParameter("oobCode");
        }

        btnResetPassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

            if (newPassword.isEmpty()) {
                etNewPassword.setError("Password is required");
                etNewPassword.requestFocus();
                return;
            }

            if (confirmNewPassword.isEmpty()) {
                etConfirmNewPassword.setError("Confirm password is required");
                etConfirmNewPassword.requestFocus();
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                etConfirmNewPassword.setError("Passwords do not match");
                etConfirmNewPassword.requestFocus();
                return;
            }

            if (actionCode != null) {
                handleResetPassword(actionCode, newPassword);
            } else {
                Toast.makeText(this, "Invalid password reset link.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResetPassword(String code, String newPassword) {
        mAuth.confirmPasswordReset(code, newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Password has been reset.", Toast.LENGTH_SHORT).show();
                            // Redirect back to login after successful reset
                            Intent intent = new Intent(ResetPassword.this, Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // Finish ResetPassword activity
                        } else {
                            Toast.makeText(ResetPassword.this, "Failed to reset password. The link may be expired.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
