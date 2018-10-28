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
    private static final String ARG_ADDRESS = "address";

    private static final String TAG = "DeviceDataFragment";


    public static DeviceDataFragment newInstance(String deviceAddress) {
        DeviceDataFragment deviceDataFragment = new DeviceDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, deviceAddress);
        deviceDataFragment.setArguments(args);

        return deviceDataFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }




}
