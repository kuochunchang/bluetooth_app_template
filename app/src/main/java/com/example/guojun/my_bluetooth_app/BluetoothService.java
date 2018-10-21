package com.example.guojun.my_bluetooth_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BluetoothService extends Service {
    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
