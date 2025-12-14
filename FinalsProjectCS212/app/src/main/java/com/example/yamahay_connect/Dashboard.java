package com.example.yamahay_connect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Set;

public class Dashboard extends AppCompatActivity {

    private TextView welcome, txtBikeName, txtLocation, txtKmToday, txtMinutes, txtAvgSpeed;
    private ImageView imgBike;
    private Button btnConnect;
    private boolean isBikeConnected = false;

    private ImageView startBikeBtn, lockBikeBtn, findBikeBtn, navigationBtn;

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private BluetoothAdapter bluetoothAdapter;
    private ActivityResultLauncher<Intent> enableBluetoothLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Bind views
        welcome = findViewById(R.id.welcome);
        txtBikeName = findViewById(R.id.txtBikeName);
        txtLocation = findViewById(R.id.txtLocation);
        txtKmToday = findViewById(R.id.txtKmToday);
        txtMinutes = findViewById(R.id.txtMinutes);
        txtAvgSpeed = findViewById(R.id.txtAvgSpeed);
        imgBike = findViewById(R.id.imgBike);
        btnConnect = findViewById(R.id.btnConnect);
        startBikeBtn = findViewById(R.id.startBikeBtn);
        lockBikeBtn = findViewById(R.id.lockBikeBtn);
        findBikeBtn = findViewById(R.id.findBikeBtn);
        navigationBtn = findViewById(R.id.navigateBikeBtn);

        welcome.setText("Welcome Back, Rider!");
        updateBikeConnectionState();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Bluetooth is now enabled, proceed to scan.
                        // The permission check is crucial here!
                        if (checkBluetoothPermissions()) {
                            scanForDevices();
                        }
                    } else {
                        Toast.makeText(this, "Bluetooth must be enabled to connect.", Toast.LENGTH_SHORT).show();
                    }
                });

        btnConnect.setOnClickListener(v -> handleConnectionLogic());

        // Action button listeners
        addAction(startBikeBtn, () -> { /* TODO: Start Bike Action */ });
        addAction(lockBikeBtn, () -> { /* TODO: Lock Bike Action */ });
        addAction(findBikeBtn, () -> { /* TODO: Find Bike Action */ });
        addAction(navigationBtn, () -> { /* TODO: Navigation Action */ });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_status) {
                startActivity(new Intent(getApplicationContext(), StatusActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_rides) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
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

    private void handleConnectionLogic() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show();
            return;
        }

        // This is the correct flow: first check permissions.
        if (checkBluetoothPermissions()) {
            // If permissions are granted, then check if Bluetooth is enabled.
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBluetoothLauncher.launch(enableBtIntent);
            } else {
                // Permissions are granted and Bluetooth is on, so we can safely scan.
                scanForDevices();
            }
        }
        // If checkBluetoothPermissions() returns false, it has already requested the permissions.
        // The logic will continue in onRequestPermissionsResult.
    }

    private boolean checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 and above
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
                return false;
            }
        } else {
            // For older versions (though ACCESS_FINE_LOCATION might still be needed for scanning)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    // This annotation is now safe because we guarantee the check happens before calling.
    @SuppressLint("MissingPermission")
    private void scanForDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> deviceList = new ArrayList<>();
        ArrayList<BluetoothDevice> devices = new ArrayList<>();

        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                if (deviceName == null || deviceName.isEmpty()) {
                    deviceName = "Unknown Device";
                }
                deviceList.add(deviceName + "\n" + deviceAddress);
                devices.add(device);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Your Bike");

        if (devices.isEmpty()) {
            deviceList.add("No paired devices found.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        builder.setAdapter(adapter, (dialog, which) -> {
            if (!devices.isEmpty()) {
                BluetoothDevice selectedDevice = devices.get(which);
                // TODO: Connect to the selected device
                isBikeConnected = true;
                updateBikeConnectionState();
                String connectedDeviceName = selectedDevice.getName() != null ? selectedDevice.getName() : "Unknown Device";
                Toast.makeText(Dashboard.this, "Connected to " + connectedDeviceName, Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
            } else {
                allPermissionsGranted = false;
            }

            if (allPermissionsGranted) {
                // Permissions were granted, now we can re-trigger the connection logic.
                handleConnectionLogic();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required to connect.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateBikeConnectionState() {
        if (isBikeConnected) {
            imgBike.setVisibility(View.VISIBLE);
            btnConnect.setVisibility(View.GONE);
        } else {
            imgBike.setVisibility(View.GONE);
            btnConnect.setVisibility(View.VISIBLE);
        }
    }

    private void addAction(ImageView image, final Runnable action) {
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.click_animation);
        image.setOnClickListener(v -> {
            image.startAnimation(anim);
            new Handler(Looper.getMainLooper()).postDelayed(action, 120);
        });
    }
}