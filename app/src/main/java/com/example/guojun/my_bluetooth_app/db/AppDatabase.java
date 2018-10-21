package com.example.guojun.my_bluetooth_app.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ConfigurationEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ConfigurationDao configurationDao();
}
