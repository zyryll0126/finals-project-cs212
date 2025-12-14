package com.example.yamahay_connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is signed in, go to main activity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User is not signed in, go to login
            startActivity(new Intent(this, Login.class));
        }

        // Finish this activity so the user can't go back to it
        finish();
    }
}