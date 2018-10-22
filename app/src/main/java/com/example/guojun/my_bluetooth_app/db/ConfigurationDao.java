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
    List<ConfigurationEntity> getAll();

    @Query("select * from configurations where name = :name")
    ConfigurationEntity getConfiguration(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long update(ConfigurationEntity entity);

}
