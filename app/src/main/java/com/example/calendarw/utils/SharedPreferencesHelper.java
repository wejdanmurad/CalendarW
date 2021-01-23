package com.example.calendarw.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.calendarw.utils.AppConstants;

public class SharedPreferencesHelper {

    private static SharedPreferences userPass = CalenderApp.app().getSharedPreferences(AppConstants.USER_PASSWORD, Context.MODE_PRIVATE);
    private static SharedPreferences isFirst = CalenderApp.app().getSharedPreferences(AppConstants.USER_FIRST_TIME, Context.MODE_PRIVATE);

//    public SharedPreferencesHelper() {
//        userPass = CalenderApp.app().getSharedPreferences(AppConstants.USER_PASSWORD, Context.MODE_PRIVATE);
//        isFirst = CalenderApp.app().getSharedPreferences(AppConstants.USER_FIRST_TIME, Context.MODE_PRIVATE);
//    }

    public static boolean isFirstTime() {
        return isFirst.getBoolean(AppConstants.USER_FIRST_TIME, true);
    }

    public static void isFirstTime(Boolean isFT) {
        isFirst.edit().putBoolean(AppConstants.USER_FIRST_TIME, isFT).apply();
    }

    public static String getUserPassword() {
        return userPass.getString(AppConstants.USER_PASSWORD, "");
    }

    public static void setUserPassword(String password) {
        userPass.edit().putString(AppConstants.USER_PASSWORD, password).apply();
    }
}
