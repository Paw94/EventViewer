package com.example.paw.myapplication;

import com.example.paw.myapplication.model.Event;
import com.example.paw.myapplication.model.User;

import java.util.ArrayList;

public class MyApplication {

    private static MyApplication instance;
    private ArrayList<Event> eventsToDisplay;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    private MyApplication() {
        this.eventsToDisplay = new ArrayList<>();
        //this.eventsToDisplay = InitData.addEvents();
    }

    public ArrayList<Event> getEventsToDisplay() {
        return eventsToDisplay;
    }

    public void setEventsToDisplay(ArrayList<Event> eventsToDisplay) {
        this.eventsToDisplay = eventsToDisplay;
    }


}
