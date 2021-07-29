package com.sw.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sw.calendarw.R;
import com.sw.calendarw.adapters.PersonalFilesAdapter;
import com.sw.calendarw.database.DataBase;
import com.sw.calendarw.database.PersonalFilesDao;
import com.sw.calendarw.dialog.FileDialog;
import com.sw.calendarw.items.PersonalFileItem;
import com.sw.calendarw.utils.AppConstants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalFileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonalFilesAdapter adapter;
    private ProgressBar progressBar;
    private Button button;
    private int max = 0;
    private int init = 0;
    private PersonalFilesDao personalPhotosDao;
    private TextView tv_no_pics;
    private Boolean isPhotoFile;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_file);

        isPhotoFile = getIntent().getBooleanExtra("isPhotoFile", true);
        init();

    }

    private void init() {
        checkBox = findViewById(R.id.check_all);
        TextView title = findViewById(R.id.tb_txt_start);
        if (isPhotoFile)
            title.setText(getResources().getString(R.string.select_photos));
        else
            title.setText(getResources().getString(R.string.select_videos));
        recyclerView = findViewById(R.id.personalPhotosRecycler);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.btn_hide);
        button.setOnClickListener(v -> {
            hidePhotos();
        });
        tv_no_pics = findViewById(R.id.tv_no_pics);
        tv_no_pics.setVisibility(View.INVISIBLE);

        personalPhotosDao = DataBase.getInstance(this).personalFilesDao();
        if (isPhotoFile)
            adapter = new PersonalFilesAdapter(PersonalFilesAdapter.HolderConstants.PHOTO);
        else
            adapter = new PersonalFilesAdapter(PersonalFilesAdapter.HolderConstants.VIDEO);
        if (adapter.mData.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        showRecycler();

        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked())
                selectAll();
            else
                unSelectAll();
        });
    }

    private void unSelectAll() {
        adapter.unSelectAll();
        adapter.notifyDataSetChanged();
    }

    private void selectAll() {
        adapter.selectAll();
        adapter.notifyDataSetChanged();
    }

    private void hidePhotos() {
        max = adapter.getSelectedCount();
        FileDialog dialog = new FileDialog(() -> {
            Toast.makeText(this, "you clicked cancel", Toast.LENGTH_SHORT).show();
        }, "Hide", "0/" + max, max);
        dialog.show(getSupportFragmentManager(), "personal files hide");

        new Thread() {
            @Override
            public void run() {
                super.run();
                if (adapter != null) {
                    for (PersonalFileItem item : adapter.mData) {
                        if (item.isChecked()) {
                            // selectedItems.add(item);
                            copyPhotos(item);
                            if (this != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        init++;
                                        dialog.setProgress(init);
                                        dialog.setNumber(init);
                                    }
                                });
                            }
                        }
                    }
                }
                dialog.dismiss();
                finish();

            }
        }.start();

    }

    private void copyPhotos(PersonalFileItem item) {

        File fMain = new File(Environment.getExternalStorageDirectory(), AppConstants.MAIN_DIR);
        if (!fMain.exists()) {
            fMain.mkdirs();
        }
        File fVideo;
        if (isPhotoFile)
            fVideo = new File(Environment.getExternalStorageDirectory() + "/" + AppConstants.MAIN_DIR, AppConstants.PHOTO_DIR);
        else
            fVideo = new File(Environment.getExternalStorageDirectory() + "/" + AppConstants.MAIN_DIR, AppConstants.VIDEO_DIR);
        if (!fVideo.exists()) {
            fVideo.mkdirs();
        }

        String path;
        if (isPhotoFile)
            path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.PHOTO_DIR;
        else
            path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.VIDEO_DIR;
        File file = new File(path, item.getItemName() + "." + AppConstants.FILE_EXT);

        // save new path
        item.setItemPathNew(file.getAbsolutePath());
        item.setChecked(false);

        try {
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            File file1 = new File(item.getItemPathOld());
            stream.write(FileUtils.readFileToByteArray(file1));
            stream.flush();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // delete the image
        ContentResolver contentResolver = getContentResolver();
        if (isPhotoFile)
            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?", new String[]{item.getItemPathOld()});
        else
            contentResolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.VideoColumns.DATA + "=?", new String[]{item.getItemPathOld()});

        // save item to db
        personalPhotosDao.addItem(item);
    }

    private void showRecycler() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                final List<PersonalFileItem> li = getPhotos();
                try {
                    if (this != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.mData = li;
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                                if (adapter.mData.isEmpty())
                                    tv_no_pics.setVisibility(View.VISIBLE);
                                else
                                    tv_no_pics.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    private List<PersonalFileItem> getPhotos() {

        List<PersonalFileItem> photoItemList = new ArrayList<>();

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        Cursor cursor;
        if (isPhotoFile) {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            //Stores all the images from the gallery in Cursor

            if (isSDPresent) {
                cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
            } else {
                cursor = getContentResolver().query(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
            }
        } else {
            final String[] columns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
            final String orderBy = MediaStore.Video.Media._ID;

            //Stores all the images from the gallery in Cursor

            if (isSDPresent) {
                cursor = getContentResolver().query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
            } else {
                cursor = getContentResolver().query(
                        MediaStore.Video.Media.INTERNAL_CONTENT_URI, columns, null,
                        null, orderBy);
            }
        }
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            String pathName = arrPath[i].substring(arrPath[i].lastIndexOf("/") + 1);
            String extension = pathName.substring(pathName.lastIndexOf(".") + 1);
            String name = pathName.replace("." + extension, "");

            Log.d("sohaib_my_love", "getPhotos: "+arrPath[i]);
            photoItemList.add(new PersonalFileItem(arrPath[i], name, extension, false));

        }

        // The cursor should be freed up after use with close()
        cursor.close();
        return photoItemList;
    }

}