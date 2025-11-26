package com.example.yamahay_connect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    TextView welcome, txtBikeName, txtLocation, txtKmToday, txtMinutes, txtAvgSpeed;
    ImageView imgBike;

    // Quick action buttons (ADD THESE)
    ImageView startBikeBtn, lockBikeBtn, findBikeBtn, navigationBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // ===== BIND MAIN UI =====
        welcome = findViewById(R.id.welcome);
        txtBikeName = findViewById(R.id.txtBikeName);
        txtLocation = findViewById(R.id.txtLocation);
        txtKmToday = findViewById(R.id.txtKmToday);
        txtMinutes = findViewById(R.id.txtMinutes);
        txtAvgSpeed = findViewById(R.id.txtAvgSpeed);
        imgBike = findViewById(R.id.imgBike);

        // Example dynamic text
        welcome.setText("Welcome Back, Rider!");

        // ===== BIND QUICK ACTION BUTTONS =====
        startBikeBtn   = findViewById(R.id.startBikeBtn);
        lockBikeBtn    = findViewById(R.id.lockBikeBtn);
        findBikeBtn    = findViewById(R.id.findBikeBtn);
        navigationBtn  = findViewById(R.id.navigateBikeBtn);

        // ===== ADD CLICK ACTIONS + ANIMATION =====
        addAction(startBikeBtn, new Runnable() {
            @Override
            public void run() {
                // TODO: Start Bike Action Here
            }
        });

        addAction(lockBikeBtn, new Runnable() {
            @Override
            public void run() {
                // TODO: Lock Bike Action Here
            }
        });

        addAction(findBikeBtn, new Runnable() {
            @Override
            public void run() {
                // TODO: Find Bike Action Here
            }
        });

        addAction(navigationBtn, new Runnable() {
            @Override
            public void run() {
                // TODO: Navigation Action Here
            }
        });
    }

    // ======================================================
    //  CLICK + BOUNCE ANIMATION FUNCTION (PLACE BELOW onCreate)
    // ======================================================
    private void addAction(ImageView image, final Runnable action) {
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.click_animation);

        image.setOnClickListener(v -> {
            image.startAnimation(anim);

            // Delay the action until animation finishes
            new Handler(Looper.getMainLooper()).postDelayed(action, 120);
        });
    }
}
