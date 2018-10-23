package com.example.guojun.my_bluetooth_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojun.my_bluetooth_app.db.AppDatabase;
import com.example.guojun.my_bluetooth_app.db.ConfigurationEntity;
import com.example.guojun.my_bluetooth_app.exception.DeviceNotSupportException;
import com.example.guojun.my_bluetooth_app.model.Configuration;


public class MainActivity extends AppCompatActivity implements DeviceDataFragment.OnFragmentInteractionListener {

    private PreparedBluetoothDevices mPreparedBluetoothDevices;
    private BluetoothDevice mCurrentBluetoothDevice;
    private DeviceDataFragment mFragment;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            mPreparedBluetoothDevices = new PreparedBluetoothDevices();
        } catch (DeviceNotSupportException dne) {
            Toast.makeText(getApplicationContext(), dne.getMessage(), Toast.LENGTH_LONG).show();
        }


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
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.hide();

            String deviceAddress = bluetoothDevice.getValue();

            mCurrentBluetoothDevice = mPreparedBluetoothDevices.findByAddress(deviceAddress);
            deviceName.setText(String.format("%s (%s)", mCurrentBluetoothDevice.getName(), mCurrentBluetoothDevice.getAddress()));

            // Load device data fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mFragment = DeviceDataFragment.newInstance(mCurrentBluetoothDevice.getAddress());
            fragmentTransaction.add(R.id.device_data_fragment_container, mFragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public void onFragmentInteraction(String data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "releaseService(): unbound.");
    }

    //--------------------------------------------------


}
