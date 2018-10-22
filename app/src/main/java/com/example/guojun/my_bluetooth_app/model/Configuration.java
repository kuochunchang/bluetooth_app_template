package com.example.guojun.my_bluetooth_app.model;

public interface Configuration {
    String BLUETOOTH_DEVICE_ADDRESS = "bluetooth_device_address";

    public String getName();

    public void setName(String name);

    public String getValue();

    public void setValue(String value);
}
