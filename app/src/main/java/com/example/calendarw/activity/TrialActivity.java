package com.example.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.calendarw.R;

public class TrialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);


        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        final String[] columns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
        final String orderBy = MediaStore.Video.Media._ID;

        //Stores all the images from the gallery in Cursor
        Cursor cursor;
        if (isSDPresent) {
            cursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
        } else {
            cursor = getContentResolver().query(
                    MediaStore.Video.Media.INTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
        }

        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);

            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            String pathName = arrPath[i].substring(arrPath[i].lastIndexOf("/") + 1);
            String extension = pathName.substring(pathName.lastIndexOf(".") + 1);
            String name = pathName.replace("." + extension, "");

//            photoItemList.add(new PersonalPhotoItem(arrPath[i], name, extension, false));

            //check if data exists
//            Log.i("PATH", arrPath[i]);
//            System.out.println(arrPath[i]);
//            System.out.println("name " + name);
//            System.out.println("ext " + extension);
        }

        Glide.with(this)
                .load(arrPath[1]).into((ImageView) findViewById(R.id.img));

        // The cursor should be freed up after use with close()
        cursor.close();
    }
}