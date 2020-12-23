package com.example.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.calendarw.R;
import com.example.calendarw.items.PersonalFileItem;

import java.util.ArrayList;
import java.util.List;

public class TrialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        getPhotos();
    }

    private void getPhotos() {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        Cursor cursor;

            final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID};
            final String orderBy = MediaStore.Audio.Media._ID;

            //Stores all the images from the gallery in Cursor

            if (isSDPresent) {
                cursor = getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
            } else {
                cursor = getContentResolver().query(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
            }

        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            String pathName = arrPath[i].substring(arrPath[i].lastIndexOf("/") + 1);
            String extension = pathName.substring(pathName.lastIndexOf(".") + 1);
            String name = pathName.replace("." + extension, "");

            Log.d("audio"+i, ""+ arrPath[i]);
            System.out.println("audio "+i+arrPath[i]);
        }

        // The cursor should be freed up after use with close()
        cursor.close();
    }
}
