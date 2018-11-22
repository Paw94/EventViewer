package com.example.paw.myapplication.room.config;

import android.arch.persistence.room.TypeConverter;

public class BooleanConverter {

        @TypeConverter
        public static Boolean stringToBoolean(String value) {
            if (value == "true") return true;
            else return false;
        }

        @TypeConverter
        public static String booleanToString(Boolean value) {
            if (value) return "true";
            else return "false";
        }
}
