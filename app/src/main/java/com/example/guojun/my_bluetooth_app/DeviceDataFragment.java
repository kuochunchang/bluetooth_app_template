package com.example.guojun.my_bluetooth_app;

import android.app.Activity;
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

import com.example.guojun.my_bluetooth_app.bwt901cl.DeviceDataDecoder;
import com.example.guojun.my_bluetooth_app.bwt901cl.SensorData;


public class DeviceDataFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private BluetoothService mBluetoothService;
    private static final String ARG_ADDRESS = "address";
    private DeviceDataDecoder mDeviceDataDecoder;

    private static final String TAG = "DeviceDataFragment";

//    public DeviceDataFragment() {
//    }

    public static DeviceDataFragment newInstance(String deviceAddress) {
        DeviceDataFragment deviceDataFragment = new DeviceDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, deviceAddress);
        deviceDataFragment.setArguments(args);

//        deviceDataFragment.mDeviceAddress = deviceAddress;

        return deviceDataFragment;
    }


    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDeviceDataDecoder = new DeviceDataDecoder(new DeviceDataDecoder.DecodedDataListener() {
            @Override
            public void onDataDecoded(SensorData data) {
                if (mTextView == null) {
                    mTextView = DeviceDataFragment.this.getActivity().findViewById(R.id.device_data_fragment_text);
                }
                mTextView.setText(data.toString());
//                mTextView.setText(data.getTime()+" " +Double.valueOf(data.getAccelerationX()).toString());
//                Log.d(TAG, data.toString());
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_data, container, false);
    }

    /* If you run your application on a device with API 23 (marshmallow) then onAttach(Context) will be called.
     On all previous Android Versions onAttach(Activity) will be called. */
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        getActivity().bindService(
                new Intent(getActivity(), BluetoothService.class),
                new BluetoothServiceConnection(), Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBluetoothService.disconnect();
    }


    public interface OnFragmentInteractionListener {
        void onDeviceConnected();
    }

    // -----------------------------------------
    private class BluetoothServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TextView textView = DeviceDataFragment.this.getActivity().findViewById(R.id.device_data_fragment_text);
            textView.setText("Connecting...");

            mBluetoothService = ((BluetoothService.LocalBinder) service).getService(new IncomingMessageHandler());
            mBluetoothService.connect(getArguments().getString(ARG_ADDRESS));
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
                    int length = msg.arg1;
                    try {
                        mDeviceDataDecoder.putRawData(readBuf, length);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                case BluetoothService.Constants.MESSAGE_CONNECTED:
                    mListener.onDeviceConnected();
                    break;
            }
        }
    }


}
