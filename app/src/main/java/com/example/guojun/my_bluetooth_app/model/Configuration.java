package com.example.guojun.my_bluetooth_app.model;

public interface Configuration {
    String BLUETOOTH_DEVICE_NAME_PARAM_NAME = "bluetooth_device_name";

    public String getName();

    public void setName(String name);

    public String getValue();

    public void setValue(String value);
}
