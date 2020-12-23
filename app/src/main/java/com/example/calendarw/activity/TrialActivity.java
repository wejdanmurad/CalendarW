package com.example.calendarw.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.items.PersonalFileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("RestrictedApi")
public class TrialActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    boolean isRecording = false;
    CameraX.LensFacing  lensFacing = CameraX.LensFacing.BACK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        textureView = findViewById(R.id.view_finder);
        startCamera();

        findViewById(R.id.flip).setOnClickListener(v -> {
            Toast.makeText(this, "flip click", Toast.LENGTH_SHORT).show();
            if (lensFacing == CameraX.LensFacing.FRONT) {
                lensFacing = CameraX.LensFacing.BACK;
            } else {
                lensFacing = CameraX.LensFacing.FRONT;
            }
            try {
                // Only bind use cases if we can query a camera with this orientation
                CameraX.getCameraWithLensFacing(lensFacing);
                startCamera();
            } catch (Exception e) {
                // Do nothing
            }

        });

//        getPhotos();

    }

    private void startCamera() {
        CameraX.unbindAll();

//        try {
//            CameraX.getCameraWithLensFacing(CameraX.LensFacing.FRONT);
//        } catch (CameraInfoUnavailableException e) {
//            e.printStackTrace();
//        }

        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen


        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetResolution(screen)
                .setLensFacing(lensFacing)
                .build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);

                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });


//        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
//                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
//                .setLensFacing(lensFacing)
//                .build();
//        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        VideoCaptureConfig videoCaptureConfig = new VideoCaptureConfig.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .setLensFacing(lensFacing)
                .build();
        VideoCapture videoCapture = new VideoCapture(videoCaptureConfig);

        findViewById(R.id.imgCapture).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");


                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".mp4");

                if (isRecording) {
                    videoCapture.stopRecording();
                    isRecording = false;
                } else {
                    videoCapture.startRecording(file, new androidx.camera.core.VideoCapture.OnVideoSavedListener() {
                        @Override
                        public void onVideoSaved(File file) {
                            String msg = "Pic captured at " + file.getAbsolutePath();
                            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(androidx.camera.core.VideoCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause) {

                        }
                    });

                    isRecording = true;
                }


//                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
//                    @Override
//                    public void onImageSaved(@NonNull File file) {
//                        String msg = "Pic captured at " + file.getAbsolutePath();
//                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
//                        String msg = "Pic capture failed : " + message;
//                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                        if (cause != null) {
//                            cause.printStackTrace();
//                        }
//                    }
//                });
            }
        });

        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner) this, preview, videoCapture);
    }

    private void updateTransform() {
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float) rotationDgr, cX, cY);
        textureView.setTransform(mx);
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
