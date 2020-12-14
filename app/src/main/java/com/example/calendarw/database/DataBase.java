package com.example.calendarw.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calendarw.items.PersonalPhotoItem;

@Database(entities = {PersonalPhotoItem.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "db_Calender";
    private static DataBase sInstance;

    public static DataBase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DataBase.class,
                    DATABASE_NAME).allowMainThreadQueries().build();

        }
        return sInstance;
    }

    public abstract PersonalPhotosDao personalPhotosDao();

}