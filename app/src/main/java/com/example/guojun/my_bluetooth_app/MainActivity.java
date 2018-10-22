package com.example.guojun.my_bluetooth_app;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojun.my_bluetooth_app.db.AppDatabase;
import com.example.guojun.my_bluetooth_app.db.ConfigurationEntity;
import com.example.guojun.my_bluetooth_app.exception.DeviceNotSupportException;
import com.example.guojun.my_bluetooth_app.model.Configuration;

public class MainActivity extends AppCompatActivity {

    private PreparedBluetoothDevices preparedBluetoothDevices;
//    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            preparedBluetoothDevices = new PreparedBluetoothDevices();
        } catch (DeviceNotSupportException dne) {
            Toast.makeText(getApplicationContext(), dne.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        ConfigurationEntity bluetoothDevice =
                appDatabase.configurationDao().getConfiguration(Configuration.BLUETOOTH_DEVICE_ADDRESS);

        TextView deviceName = findViewById(R.id.bluetooth_device_name);

        if (bluetoothDevice == null) {
            deviceName.setText("The bluetooth device has not set up. Please tap the icon at bottom of screen to set up.");
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, BluetoothDeviceSelectActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            String deviceAddress = bluetoothDevice.getValue();

            BluetoothDevice device = preparedBluetoothDevices.findByAddress(deviceAddress);
            deviceName.setText(String.format("%s (%s)", device.getName(), device.getAddress()));

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bluetooth_device_settings) {
            Intent intent = new Intent(this, BluetoothDeviceSelectActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
