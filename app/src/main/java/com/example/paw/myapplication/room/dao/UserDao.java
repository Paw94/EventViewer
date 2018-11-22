package com.example.paw.myapplication.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.paw.myapplication.model.User;

import java.util.List;


@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email = :email")
    User getUserByMail(String email);

    @Insert
    void insertUser(User user);
}
