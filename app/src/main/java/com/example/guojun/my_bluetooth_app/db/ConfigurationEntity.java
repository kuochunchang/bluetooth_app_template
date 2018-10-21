package com.example.guojun.my_bluetooth_app.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.guojun.my_bluetooth_app.model.Configuration;

@Entity(tableName = "configurations")
public class ConfigurationEntity implements Configuration {
    @PrimaryKey
    @NonNull
    private String name;

    private String value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }




}
