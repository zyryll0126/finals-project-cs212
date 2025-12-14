package com.example.yamahay_connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRides;
    private RideAdapter adapter;
    private List<RideModel> rideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rvRides = findViewById(R.id.rvRides);
        rvRides.setLayoutManager(new LinearLayoutManager(this));

        rideList = new ArrayList<>();
        rideList.add(new RideModel("Today", "2:30 PM", "Home - Saint Louis University", "5.2 km", "35 min"));
        rideList.add(new RideModel("Yesterday", "8:15 AM", "Home - SM Baguio", "2 km", "28 min"));
        rideList.add(new RideModel("Nov 1", "6:00 PM", "Home - Pangasinan", "20 km", "1h 24min"));
        rideList.add(new RideModel("Oct 31", "10:30 AM", "Saint Louis University - Burnham Park", "5 km", "2h 47min"));
        rideList.add(new RideModel("Oct 30", "4:15 PM", "Centermall - Burnham Park", "1 km", "22 min"));

        adapter = new RideAdapter(rideList);
        rvRides.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_rides);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_status) {
                startActivity(new Intent(getApplicationContext(), StatusActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_rides) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
