package com.example.guojun.my_bluetooth_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.example.guojun.my_bluetooth_app.exception.DeviceNotSupportException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrepairedBluetoothDevices {

    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();


//    public class BluetoothDeviceInfo{
//        String name;
//        BluetoothDevice device;
//
//    }

    public PrepairedBluetoothDevices() throws DeviceNotSupportException {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new DeviceNotSupportException("The device not support bluetooth");
        } else {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    Log.d("prepared devices", deviceName);
                    bluetoothDevices.add(device);
                }

            }
        }
    }


    public String[] getDeviceNames() {
        List<String> result = new ArrayList<>();
        for (BluetoothDevice dev : bluetoothDevices) {
            result.add((dev.getName()));
        }
        return result.toArray(new String[result.size()]);
    }

}
