package com.example.paw.myapplication.room.config;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.paw.myapplication.model.Event;
import com.example.paw.myapplication.model.User;
import com.example.paw.myapplication.room.dao.EventDao;
import com.example.paw.myapplication.room.dao.UserDao;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Event.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, BooleanConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract EventDao eventDao();


    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            System.out.println("###### building");
            INSTANCE = buildDatabase(context);
        }
        System.out.println("###### exiting");
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "my-database").allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        System.out.println("###### on create");
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("########### inserting");
                            }
                        });
                    }
                })
                .build();
    }


}