package com.example.paw.myapplication.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.paw.myapplication.model.Event;

import java.util.List;

@Dao
public interface EventDao {


    @Query("SELECT * FROM Event")
    List<Event> getAll();

    @Query("UPDATE Event SET " +
            "title = :title " +
            "WHERE id = :id")
    void updateTitle (long id, String title);

    @Insert
    void insert(Event event);

    @Insert
    void insertAll(Event... events);

    @Query("DELETE FROM Event WHERE id = :id")
    void deleteEvent(long id);
}
