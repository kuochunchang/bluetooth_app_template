package com.example.guojun.my_bluetooth_app.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ConfigurationDao {
    @Query("select * from configurations")
    LiveData<List<ConfigurationEntity>> getAll();

    @Query("select * from configurations where name = :name")
    LiveData<ConfigurationEntity> getConfiguration(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(ConfigurationEntity entity);

}
