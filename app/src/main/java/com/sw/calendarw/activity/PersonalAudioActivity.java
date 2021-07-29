package com.sw.calendarw.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sw.calendarw.R;
import com.sw.calendarw.adapters.AudioAdapter;
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


@RequiresApi(api = Build.VERSION_CODES.R)
public class PersonalAudioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AudioAdapter adapter;
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
        setContentView(R.layout.activity_personal_audio);

        init();
    }

    private void init() {
        checkBox = findViewById(R.id.check_all);
        TextView title = findViewById(R.id.tb_txt_start);
        title.setText(getResources().getString(R.string.select_audio));
        recyclerView = findViewById(R.id.personalAudioRecycler);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.btn_hide);
        button.setOnClickListener(v -> {
            hidePhotos();
        });
        tv_no_pics = findViewById(R.id.tv_no_audio);
        tv_no_pics.setVisibility(View.INVISIBLE);

        personalPhotosDao = DataBase.getInstance(this).personalFilesDao();
        adapter = new AudioAdapter(AudioAdapter.HolderConstants.P_Audio);
        if (adapter.mData.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
                                Toast.makeText(PersonalAudioActivity.this, "adapter size " + adapter.mData.size(), Toast.LENGTH_SHORT).show();
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

        final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ARTIST};
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

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int nameColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            //Store the path of the image
            String pathName = cursor.getString(dataColumnIndex).substring(cursor.getString(dataColumnIndex).lastIndexOf("/") + 1);
            String extension = pathName.substring(pathName.lastIndexOf(".") + 1);

            photoItemList.add(new PersonalFileItem(cursor.getString(dataColumnIndex), cursor.getString(nameColumnIndex), cursor.getString(artistColumnIndex), extension, false));

        }

        // The cursor should be freed up after use with close()
        cursor.close();
        return photoItemList;
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
        fVideo = new File(Environment.getExternalStorageDirectory() + "/" + AppConstants.MAIN_DIR, AppConstants.AUDIO_DIR);
        if (!fVideo.exists()) {
            fVideo.mkdirs();
        }

        String path;
        path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.AUDIO_DIR;
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
            contentResolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.AudioColumns.DATA + "=?", new String[]{item.getItemPathOld()});


        File fdelete = new File(item.getItemPathOld());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted ");
            } else {
                System.out.println("file not Deleted ");
            }
        }

        // save item to db
        personalPhotosDao.addItem(item);
    }


}