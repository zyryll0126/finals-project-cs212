package com.example.yamahay_connect;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList; // correct import
import java.util.List;      // correct import

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRides;
    private RideAdapter adapter;
    private List<RideModel> rideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove default action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 1. Setup RecyclerView
        rvRides = findViewById(R.id.rvRides);
        rvRides.setLayoutManager(new LinearLayoutManager(this));

        // 2. Initialize the list
        rideList = new ArrayList<>();

        // 3. Add the data (This is the part giving you errors if placed incorrectly)
        rideList.add(new RideModel("Today", "2:30 PM", "Home - Saint Louis University", "5.2 km", "35 min"));
        rideList.add(new RideModel("Yesterday", "8:15 AM", "Home - SM Baguio", "2 km", "28 min"));
        rideList.add(new RideModel("Nov 1", "6:00 PM", "Home - Pangasinan", "20 km", "1h 24min"));
        rideList.add(new RideModel("Oct 31", "10:30 AM", "Saint Louis University - Burnham Park", "5 km", "2h 47min"));
        rideList.add(new RideModel("Oct 30", "4:15 PM", "Centermall - Burnham Park", "1 km", "22 min"));

        // 4. Pass data to adapter
        adapter = new RideAdapter(rideList);
        rvRides.setAdapter(adapter);

        // 5. Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_rides);
    }
}