package com.example.guojun.my_bluetooth_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guojun.my_bluetooth_app.exception.DeviceNotSupportException;

public class BluetoothDeviceSelectActivity extends AppCompatActivity {

    PrepairedBluetoothDevices prepairedBluetoothDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_select_activity);

        try {
            prepairedBluetoothDevices = new PrepairedBluetoothDevices();
        } catch (DeviceNotSupportException dne) {
            Toast.makeText(getApplicationContext(), dne.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        ListView listView = findViewById(R.id.bluetooth_device_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), new Long(id).toString(), Toast.LENGTH_LONG).show();
            }
        });


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.bluetooth_device_list_item, prepairedBluetoothDevices.getDeviceNames());

        listView.setAdapter(adapter);


    }


}
