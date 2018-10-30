package com.example.guojun.my_bluetooth_app.ui.bluetoothdevicediscovery;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guojun.my_bluetooth_app.R;

public class BluetoothDeviceDiscoveryFragment extends Fragment {

    private BluetoothDeviceDiscoveryViewModel mViewModel;

    public static BluetoothDeviceDiscoveryFragment newInstance() {
        return new BluetoothDeviceDiscoveryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bluetooth_device_discovery_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BluetoothDeviceDiscoveryViewModel.class);
        // TODO: Use the ViewModel
    }

}
