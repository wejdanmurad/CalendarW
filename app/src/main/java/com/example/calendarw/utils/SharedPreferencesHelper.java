package com.example.calendarw.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.calendarw.utils.AppConstants;

public class SharedPreferencesHelper {
    public static boolean isFirstTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppConstants.USER_FIRST_TIME, Context.MODE_PRIVATE);
        return preferences.getBoolean(AppConstants.USER_FIRST_TIME, true);
    }

    public static String getUserPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppConstants.USER_PASSWORD, Context.MODE_PRIVATE);
        return preferences.getString(AppConstants.USER_PASSWORD,"");
    }
}
