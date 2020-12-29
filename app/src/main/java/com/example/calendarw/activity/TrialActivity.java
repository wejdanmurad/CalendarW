package com.example.calendarw.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.VideoCapture;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.view.CameraView;
import androidx.camera.view.video.OnVideoSavedCallback;
import androidx.camera.view.video.OutputFileResults;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.util.TypedValue;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calendarw.R;
import com.example.calendarw.items.PersonalFileItem;
import com.example.calendarw.utils.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TrialActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        getPhotos();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getPhotos() {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        Cursor cursor;

        final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME , MediaStore.Audio.Media.ARTIST};
        final String orderBy = MediaStore.Audio.Media._ID;

        //Stores all the images from the gallery in Cursor

        if (isSDPresent) {
            cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.Audio.Media.IS_MUSIC,
                    null, orderBy);
        } else {
            cursor = getContentResolver().query(
                    MediaStore.Audio.Media.INTERNAL_CONTENT_URI, columns, MediaStore.Audio.Media.IS_MUSIC,
                    null, orderBy);
        }

        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int nameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

//            //Store the path of the image
//            arrPath[i] = cursor.getString(dataColumnIndex);
//            String test = cursor.getString(nameIndex);
//            String pathName = arrPath[i].substring(arrPath[i].lastIndexOf("/") + 1);

            Log.d("trialaudiowejdan", "name " + cursor.getString(artistIndex));
//            Log.d("audiowejdan", "" + arrPath[i]);
//            System.out.println("audio " + i + arrPath[i]);
        }

        // The cursor should be freed up after use with close()
        cursor.close();
    }
}
