package com.example.paw.myapplication.room.config;


import com.example.paw.myapplication.model.Event;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class InitData {

    public static ArrayList<Event> addEvents() {
        ArrayList<Event> eventsToAdd = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Event event = new Event();
            event.setTitle(randomString(10));
            event.setDescription(randomString(100));
            event.setDate(randomDate());

            eventsToAdd.add(event);
        }
        return eventsToAdd;
    }

    public static String randomString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static Date randomDate() {
        Random rnd;
        Date date;
        long ms;

        rnd = new Random();
        ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        date = new Date(ms);

        return date;
    }

}
