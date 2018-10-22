package com.example.guojun.my_bluetooth_app;

import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    PreparedBluetoothDevices prepairedBluetoothDevices;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_select_activity);

        // Prepare the information of paired bluetooth devices
        try {
            prepairedBluetoothDevices = new PreparedBluetoothDevices();
        } catch (DeviceNotSupportException dne) {
            Toast.makeText(getApplicationContext(), dne.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // Prepare database
        appDatabase =
                Room.databaseBuilder(
                        getApplicationContext(), AppDatabase.class, "app-db").build();

        // Initial the ListView of paired bluetooth device on this phone
        BluetoothDeviceItemAdapter adapter = new BluetoothDeviceItemAdapter(
                new BluetoothDeviceItemModelBuilder(prepairedBluetoothDevices.getAll()).build(),
                getApplicationContext());

        ListView listView = findViewById(R.id.bluetooth_device_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                BluetoothDevice selectedDevice = prepairedBluetoothDevices.getAll().get(position);
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
    private class BluetoothDeviceItemAdapter extends ArrayAdapter<BluetoothDeviceItemModel> {
        private LayoutInflater inflater = BluetoothDeviceSelectActivity.this.getLayoutInflater();
        private List<BluetoothDeviceItemModel> dataSet;


        BluetoothDeviceItemAdapter(List<BluetoothDeviceItemModel> data, Context context) {
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

            if (view == null) {
                convertView = inflater.inflate(R.layout.bluetooth_device_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textName = convertView
                        .findViewById(R.id.btName);
                viewHolder.textAddress = convertView
                        .findViewById(R.id.btAddress);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


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
        List<BluetoothDevice> btdeviceList;

        BluetoothDeviceItemModelBuilder(List<BluetoothDevice> btdeviceList) {
            this.btdeviceList = btdeviceList;
        }


        List<BluetoothDeviceItemModel> build() {
            List<BluetoothDeviceItemModel> result = new ArrayList<>();
            for (BluetoothDevice bd : btdeviceList) {
                BluetoothDeviceItemModel item = new BluetoothDeviceItemModel(bd.getName(), bd.getAddress());
                result.add(item);
            }
            return result;

        }
    }
}
