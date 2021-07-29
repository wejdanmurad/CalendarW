package com.sw.calendarw.utils;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;

public class CalenderApp extends Application {

    private static CalenderApp sw;

    public static CalenderApp app() {
        return sw;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
        sw = this;
    }
}
