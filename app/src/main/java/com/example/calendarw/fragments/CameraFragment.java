package com.example.calendarw.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.database.DataBase;
import com.example.calendarw.database.PersonalFilesDao;
import com.example.calendarw.items.PersonalFileItem;
import com.example.calendarw.utils.AppConstants;

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
    static ImageView btnClose, btnLens;
    static ImageButton btnMain;
    private Executor executor = Executors.newSingleThreadExecutor();
    CameraSelector cameraSelector;
    CameraView mCameraView;

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"};

    public static OnItemClickListener mListener;
    private boolean isPhoto = true;
    private PersonalFilesDao personalPhotosDao;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS);
        }

        personalPhotosDao = DataBase.getInstance(getActivity()).personalFilesDao();
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
        btnMain = view.findViewById(R.id.btnPhoto);

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
            return;
        }
        mCameraView.bindToLifecycle((LifecycleOwner) this);

        select(btnPhoto, btnVideo);

        // set click listener to all buttons

        btnPhoto.setOnClickListener(v -> {
            isPhoto = true;
            select(btnPhoto, btnVideo);
        }); //btnPhoto click listener end


        btnVideo.setOnClickListener(v -> {
            isPhoto = false;
            select(btnVideo, btnPhoto);
        }); //video listener end


        btnMain.setOnClickListener(v -> {
            if (isPhoto)
                takePhoto();
            else if (mCameraView.isRecording())
                stopRecording();
            else
                takeVideo();
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


        btnLens.setOnClickListener(v -> {
            if (mCameraView.isRecording()) {
                return;
            }
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (mCameraView.hasCameraWithLensFacing(CameraSelector.LENS_FACING_FRONT)) {
                mCameraView.toggleCamera();
            } else {
                return;
            }
        }); // btnLens listener end


    } //start camera end

    private void select(Button btn1, Button btn2) {
        btn1.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_200, getResources().newTheme())));
        btn1.setTextColor(getResources().getColor(R.color.color_200, getResources().newTheme()));

        btn2.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white, getResources().newTheme())));
        btn2.setTextColor(getResources().getColor(R.color.white, getResources().newTheme()));
    }

    private void stopRecording() {
        btnMain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white, getResources().newTheme())));
        mCameraView.stopRecording();
    }

    private void takeVideo() {
        if (mCameraView.isRecording()) {
            return;
        }

        btnMain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_red, getResources().newTheme())));

        // file location
        PersonalFileItem item = new PersonalFileItem();
        File fMain = new File(Environment.getExternalStorageDirectory(), AppConstants.MAIN_DIR);
        if (!fMain.exists()) {
            fMain.mkdirs();
        }
        File fVideo;

        fVideo = new File(Environment.getExternalStorageDirectory() + "/" + AppConstants.MAIN_DIR, AppConstants.VIDEO_DIR);
        if (!fVideo.exists()) {
            fVideo.mkdirs();
        }

        String path;
        path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.VIDEO_DIR;

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        item.setItemName(mDateFormat.format(new Date()));
        item.setItemExt("mp4");
        File file = new File(path, item.getItemName() + "." + AppConstants.FILE_EXT);
        item.setItemPathNew(file.getAbsolutePath());
        item.setChecked(false);
        personalPhotosDao.addItem(item);

//        File file = new File(getBatchDirectoryName(),  + );

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
    }

    private void takePhoto() {
        if (mCameraView.isRecording()) {
            return;
        }

        // file location
        PersonalFileItem item = new PersonalFileItem();
        File fMain = new File(Environment.getExternalStorageDirectory(), AppConstants.MAIN_DIR);
        if (!fMain.exists()) {
            fMain.mkdirs();
        }
        File fVideo;

        fVideo = new File(Environment.getExternalStorageDirectory() + "/" + AppConstants.MAIN_DIR, AppConstants.PHOTO_DIR);
        if (!fVideo.exists()) {
            fVideo.mkdirs();
        }

        String path;
        path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.PHOTO_DIR;

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        item.setItemName(mDateFormat.format(new Date()));
        item.setItemExt("jpg");
        final File file1 = new File(path, item.getItemName() + "." + AppConstants.FILE_EXT);
        item.setItemPathNew(file1.getAbsolutePath());
        item.setChecked(false);
        personalPhotosDao.addItem(item);

//        final File file1 = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + );

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
    }


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