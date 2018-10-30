package com.example.guojun.my_bluetooth_app.ui.bluetoothdevicediscovery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class BluetoothDeviceDiscoveryViewModel extends ViewModel {
    private MutableLiveData<LinkedList<String[]>> availableDevices;

    public MutableLiveData<LinkedList<String[]>> getAvailableDevices() {
        if(availableDevices == null){
            availableDevices = new MutableLiveData<>();
            availableDevices.setValue(new LinkedList<String[]>());
        }
        return availableDevices;
    }

    public void addAvailableDevice(String name, String address){
        availableDevices.getValue().add(new String[]{name, address});
        availableDevices.setValue(availableDevices.getValue());
    }
}
