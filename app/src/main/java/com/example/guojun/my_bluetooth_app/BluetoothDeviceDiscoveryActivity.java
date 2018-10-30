package com.example.guojun.my_bluetooth_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.guojun.my_bluetooth_app.ui.bluetoothdevicediscovery.BluetoothDeviceDiscoveryFragment;

public class BluetoothDeviceDiscoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_discovery_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BluetoothDeviceDiscoveryFragment.newInstance())
                    .commitNow();
        }
    }
}
