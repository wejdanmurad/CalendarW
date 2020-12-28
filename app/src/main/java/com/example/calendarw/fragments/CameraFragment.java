package com.example.calendarw.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.view.CameraView;
import androidx.camera.view.video.OnVideoSavedCallback;
import androidx.camera.view.video.OutputFileResults;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.activity.HomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@SuppressLint("UnsafeExperimentalUsageError")
public class CameraFragment extends Fragment {

    static Button btnVideo, btnPhoto;
    static ImageView  btnClose, btnLens, btnStop;
    private Executor executor = Executors.newSingleThreadExecutor();
    CameraSelector cameraSelector;
    CameraView mCameraView;

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"};

    public static OnItemClickListener mListener;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS);
        }

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        btnPhoto
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        btnPhoto = view.findViewById(R.id.btn_photo);
        btnVideo = view.findViewById(R.id.btn_video);
        btnStop = view.findViewById(R.id.btnPhoto);
        btnLens = view.findViewById(R.id.flip_camera);
        btnClose = view.findViewById(R.id.back_camera);
        mCameraView = view.findViewById(R.id.view_finder);
        return view;
    }


    private void startCamera() {

        mCameraView.setFlash(ImageCapture.FLASH_MODE_AUTO);
        //can set flash mode to auto,on,off...
        ImageCapture.Builder builder = new ImageCapture.Builder();
        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
        // if has hdr (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable hdr.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCameraView.bindToLifecycle((LifecycleOwner) this);

        // set click listener to all buttons

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView.isRecording()) {
                    return;
                }

                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                final File file1 = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".jpg");

                mCameraView.setCaptureMode(CameraView.CaptureMode.IMAGE);
                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file1).build();
                mCameraView.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                galleryAddPic(file1, 0);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                }); //image saved callback end

            } //onclick end
        }); //btnPhoto click listener end


        btnVideo.setOnClickListener(v -> {
            if (mCameraView.isRecording()) {
                return;
            }

            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".mp4");

            mCameraView.setCaptureMode(CameraView.CaptureMode.VIDEO);
            mCameraView.startRecording(file, executor, new OnVideoSavedCallback() {
                @Override
                public void onVideoSaved(@NonNull OutputFileResults outputFileResults) {
                    galleryAddPic(file, 1);
                }

                @Override
                public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    //Log.i("TAG",message);
                    mCameraView.stopRecording();
                }

            }); //image saved callback end
        }); //video listener end


        btnStop.setOnClickListener(v -> {
            if (mCameraView.isRecording()) {
                mCameraView.stopRecording();
            }
        });


        //close app
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCameraView.isRecording()) {
                    mCameraView.stopRecording();
                }
                Toast.makeText(getActivity(), "pop back", Toast.LENGTH_SHORT).show();
                mListener.onItemClick(1);
            }
        });// on click listener end


        btnLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView.isRecording()) {
                    return;
                }

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mCameraView.hasCameraWithLensFacing(CameraSelector.LENS_FACING_FRONT)) {
                    mCameraView.toggleCamera();
                } else {
                    return;
                }
            }//onclick end
        }); // btnLens listener end


    } //start camera end


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCameraView.isRecording()) {
            mCameraView.stopRecording();
        }
//        finish();
    }


    public boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
//                this.finish();
            }
        }
    }


    public String getBatchDirectoryName() {
        String app_folder_path;
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            app_folder_path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        } else {
            app_folder_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera";
        }

        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
        }
        return app_folder_path;
    }


    private void galleryAddPic(File originalFile, int mediaType) {
        if (!originalFile.exists()) {
            return;
        }

        int pathSeparator = String.valueOf(originalFile).lastIndexOf('/');
        int extensionSeparator = String.valueOf(originalFile).lastIndexOf('.');
        String filename = pathSeparator >= 0 ? String.valueOf(originalFile).substring(pathSeparator + 1) : String.valueOf(originalFile);
        String extension = extensionSeparator >= 0 ? String.valueOf(originalFile).substring(extensionSeparator + 1) : "";

        // Credit: https://stackoverflow.com/a/31691791/2373034
        String mimeType = extension.length() > 0 ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase(Locale.ENGLISH)) : null;

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, filename);
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);

        if (mimeType != null && mimeType.length() > 0)
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

        Uri externalContentUri;
        if (mediaType == 0)
            externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else if (mediaType == 1)
            externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        else
            externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Android 10 restricts our access to the raw filesystem, use MediaStore to save media in that case
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Camera");
            values.put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.MediaColumns.IS_PENDING, true);

            Uri uri = getActivity().getContentResolver().insert(externalContentUri, values);
            if (uri != null) {
                try {
                    if (WriteFileToStream(originalFile, getActivity().getContentResolver().openOutputStream(uri))) {
                        values.put(MediaStore.MediaColumns.IS_PENDING, false);
                        getActivity().getContentResolver().update(uri, values, null, null);
                    }
                } catch (Exception e) {
                    getActivity().getContentResolver().delete(uri, null, null);
                }
            }
            originalFile.delete();
        } else {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(originalFile));
            getActivity().sendBroadcast(mediaScanIntent);
        }

    } //gallery add end


    private static boolean WriteFileToStream(File file, OutputStream out) {
        try {
            InputStream in = new FileInputStream(file);
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    //Log.e( "Unity", "Exception:", e );
                }
            }
        } catch (Exception e) {
            //Log.e( "Unity", "Exception:", e );
            return false;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                //Log.e( "Unity", "Exception:", e );
            }
        }
        return true;
    }

}