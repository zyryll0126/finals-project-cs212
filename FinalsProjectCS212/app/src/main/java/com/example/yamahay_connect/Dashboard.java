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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Set;

public class Dashboard extends AppCompatActivity {

    private TextView welcome, txtBikeName, txtLocation, txtKmToday, txtMinutes, txtAvgSpeed;
    private ImageView imgBike;
    private Button btnConnect;
    private boolean isBikeConnected = false; // Initially, no bike is connected

    // Quick action buttons
    private ImageView startBikeBtn, lockBikeBtn, findBikeBtn, navigationBtn;

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private BluetoothAdapter bluetoothAdapter;
    private ActivityResultLauncher<Intent> enableBluetoothLauncher;

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
        btnConnect = findViewById(R.id.btnConnect);

        // Example dynamic text
        welcome.setText("Welcome Back, Rider!");

        // ===== BIND QUICK ACTION BUTTONS =====
        startBikeBtn   = findViewById(R.id.startBikeBtn);
        lockBikeBtn    = findViewById(R.id.lockBikeBtn);
        findBikeBtn    = findViewById(R.id.findBikeBtn);
        navigationBtn  = findViewById(R.id.navigateBikeBtn);

        // ===== INITIAL UI STATE =====
        updateBikeConnectionState();

        // Initialize Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize the launcher for enabling Bluetooth
        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Bluetooth has been enabled
                        scanForDevices();
                    } else {
                        // User did not enable Bluetooth
                        Toast.makeText(this, "Bluetooth is required to connect to your bike.", Toast.LENGTH_SHORT).show();
                    }
                });

        // ===== CLICK LISTENERS =====
        btnConnect.setOnClickListener(v -> {
            if (checkBluetoothPermissions()) {
                if (bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show();
                } else if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBluetoothLauncher.launch(enableBtIntent);
                } else {
                    scanForDevices();
                }
            }
        });

        // ===== ADD CLICK ACTIONS + ANIMATION =====
        addAction(startBikeBtn, () -> {
            // TODO: Start Bike Action Here
        });

        addAction(lockBikeBtn, () -> {
            // TODO: Lock Bike Action Here
        });

        addAction(findBikeBtn, () -> {
            // TODO: Find Bike Action Here
        });

        addAction(navigationBtn, () -> {
            // TODO: Navigation Action Here
        });
    }

    private boolean checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_BLUETOOTH_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private void scanForDevices() {
        @SuppressLint("MissingPermission")
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> deviceList = new ArrayList<>();
        ArrayList<BluetoothDevice> devices = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device.getName() + "\n" + device.getAddress());
                devices.add(device);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Your Bike");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        builder.setAdapter(adapter, (dialog, which) -> {
            // TODO: Connect to the selected device
            isBikeConnected = true;
            updateBikeConnectionState();
            Toast.makeText(Dashboard.this, "Connected to " + devices.get(which).getName(), Toast.LENGTH_SHORT).show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btnConnect.callOnClick();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required to connect to your bike.", Toast.LENGTH_SHORT).show();
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

    // ======================================================
    //  CLICK + BOUNCE ANIMATION FUNCTION
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
