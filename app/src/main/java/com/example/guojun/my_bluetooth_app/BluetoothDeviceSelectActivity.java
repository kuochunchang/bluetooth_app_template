package com.example.guojun.my_bluetooth_app;

import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojun.my_bluetooth_app.db.AppDatabase;
import com.example.guojun.my_bluetooth_app.db.ConfigurationEntity;
import com.example.guojun.my_bluetooth_app.exception.DeviceNotSupportException;
import com.example.guojun.my_bluetooth_app.model.Configuration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class BluetoothDeviceSelectActivity extends AppCompatActivity {

    private PreparedBluetoothDevices preparedBluetoothDevices;
    private AppDatabase appDatabase;
//    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d("------", deviceHardwareAddress + ":" + deviceName);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_select_activity);

        FloatingActionButton fab = findViewById(R.id.scan_device);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.startDiscovery();
            }
        });

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


        // Prepare the information of paired bluetooth devices
        try {
            preparedBluetoothDevices = new PreparedBluetoothDevices();
        } catch (DeviceNotSupportException dne) {
            Toast.makeText(getApplicationContext(), dne.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // Prepare database
        appDatabase =
                Room.databaseBuilder(
                        getApplicationContext(), AppDatabase.class, "app-db").build();

        // Initial the ListView of paired bluetooth device on this phone
        PairedBluetoothDeviceItemAdapter adapter = new PairedBluetoothDeviceItemAdapter(
                new BluetoothDeviceItemModelBuilder(preparedBluetoothDevices.getAll()).build(),
                getApplicationContext());

        ListView listView = findViewById(R.id.paired_bluetooth_device_list);
        listView.setAdapter(adapter);

        // While the bluetooth device selected, return to MainActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDevice selectedDevice = preparedBluetoothDevices.getAll().get(position);
                ConfigurationEntity configurationEntity =
                        new ConfigurationEntity(Configuration.BLUETOOTH_DEVICE_ADDRESS, selectedDevice.getAddress());

                InsertDbTask asyncTask = new InsertDbTask();
                asyncTask.execute(configurationEntity);

                Intent intent =
                        new Intent(BluetoothDeviceSelectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    //-----------------------AsyncTasks-----------------------------------
    private class InsertDbTask extends AsyncTask<ConfigurationEntity, Void, Void> {

        @Override
        protected Void doInBackground(ConfigurationEntity... entities) {
            for (ConfigurationEntity entity : entities) {
                appDatabase.configurationDao().update(entity);
            }
            return null;
        }
    }

    // --------------------ListView related classes--------------------------
    private class PairedBluetoothDeviceItemAdapter extends ArrayAdapter<BluetoothDeviceItemModel> {
        private LayoutInflater inflater = BluetoothDeviceSelectActivity.this.getLayoutInflater();
        private List<BluetoothDeviceItemModel> dataSet;


        PairedBluetoothDeviceItemAdapter(List<BluetoothDeviceItemModel> data, Context context) {
            super(context, R.layout.bluetooth_device_list_item, data);
            this.dataSet = data;
        }

        private class ViewHolder {
            TextView textName;
            TextView textAddress;
        }


        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
            View convertView = null;
            ViewHolder viewHolder;

            convertView = inflater.inflate(R.layout.bluetooth_device_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textName = convertView
                    .findViewById(R.id.btName);
            viewHolder.textAddress = convertView
                    .findViewById(R.id.btAddress);

            BluetoothDeviceItemModel device = dataSet.get(position);
            viewHolder.textName.setText(device.getName());
            viewHolder.textAddress.setText(device.getAddress());

            return convertView;
        }

    }

    private class BluetoothDeviceItemModel {
        private String name;
        private String address;

        BluetoothDeviceItemModel(String name, String address) {
            this.name = name;
            this.address = address;
        }

        String getName() {
            return name;
        }

        String getAddress() {
            return address;
        }
    }

    private class BluetoothDeviceItemModelBuilder {
        List<BluetoothDevice> deviceList;

        BluetoothDeviceItemModelBuilder(List<BluetoothDevice> deviceList) {
            this.deviceList = deviceList;
        }


        List<BluetoothDeviceItemModel> build() {
            List<BluetoothDeviceItemModel> result = new ArrayList<>();
            for (BluetoothDevice device : deviceList) {
                BluetoothDeviceItemModel item = new BluetoothDeviceItemModel(device.getName(), device.getAddress());
                result.add(item);
            }
            return result;

        }
    }
}
