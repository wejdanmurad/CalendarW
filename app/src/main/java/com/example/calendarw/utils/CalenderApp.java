package com.example.calendarw.utils;

import android.app.Application;

public class CalenderApp extends Application {

    private static CalenderApp sw;

    public static CalenderApp app() {
        return sw;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sw = this;
    }
}
