package com.example.guojun.my_bluetooth_app;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DeviceDataFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private BluetoothService mBluetoothService;
    private String mDeviceAddress;
    private static final String TAG = "DeviceDataFragment";

    public DeviceDataFragment() {
        // Required empty public constructor
    }

    public static DeviceDataFragment newInstance(String deviceAddress) {
        DeviceDataFragment deviceDataFragment = new DeviceDataFragment();
        deviceDataFragment.mDeviceAddress = deviceAddress;

        return deviceDataFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().bindService(
                new Intent(getActivity(), BluetoothService.class),
                new BluetoothServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_data, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBluetoothService.diconnected();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String data);
    }

    // -----------------------------------------
    private class BluetoothServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TextView textView = DeviceDataFragment.this.getActivity().findViewById(R.id.device_data_fragment_text);
            textView.setText("Connecting...");

            mBluetoothService = ((BluetoothService.LocalBinder) service).getService(new IncomingMessageHandler());
            mBluetoothService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case BluetoothService.Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    try {
                        TextView textView = DeviceDataFragment.this.getActivity().findViewById(R.id.device_data_fragment_text);
                        textView.setText(readMessage);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
            }
        }
    }


}
