package com.example.calendarw.fragments.photos;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarw.database.DataBase;
import com.example.calendarw.R;
import com.example.calendarw.dialog.FileDialog;
import com.example.calendarw.adapters.PersonalFilesAdapter;
import com.example.calendarw.database.PersonalFilesDao;
import com.example.calendarw.items.PersonalFileItem;
import com.example.calendarw.utils.AppConstants;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalPhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private PersonalFilesAdapter adapter;
    private ProgressBar progressBar;
    private Button button;
    private int max = 0;
    private PersonalFilesDao personalPhotosDao;
    private TextView tv_no_pics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_photos, container, false);
        recyclerView = view.findViewById(R.id.personalPhotosRecycler);
        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        button = view.findViewById(R.id.btn_hide);
        button.setOnClickListener(v -> {
            hidePhotos();
        });
        tv_no_pics = view.findViewById(R.id.tv_no_pics);
        tv_no_pics.setVisibility(View.INVISIBLE);
        return view;
    }

    private List<PersonalFileItem> hidePhotos() {
        List<PersonalFileItem> selectedItems = new ArrayList<>();

        max = adapter.getSelectedCount();
        FileDialog dialog = new FileDialog(() -> {
            Toast.makeText(getContext(), "you clicked cancel", Toast.LENGTH_SHORT).show();
        }, "Hide", "0/" + max, max);
        dialog.show(getParentFragmentManager(), "personal photos hide");

        new Thread() {
            @Override
            public void run() {
                super.run();
                if (adapter != null) {
                    for (PersonalFileItem item : adapter.mData) {
                        if (item.isChecked()) {
                            // selectedItems.add(item);
                            copyPhotos(item);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        --max;
                                        int val = adapter.getSelectedCount() - max;
                                        dialog.setProgress(val);
                                        dialog.setNumber(val);
                                    }
                                });
                            }
                        }
                    }
                }
                getParentFragmentManager().popBackStack();
                dialog.dismiss();
            }
        }.start();

        return selectedItems;
    }

    private void copyPhotos(PersonalFileItem item) {

        File fMain = new File(Environment.getExternalStorageDirectory(), AppConstants.MAIN_DIR);
        if (!fMain.exists()) {
            fMain.mkdirs();
        }
        File fVideo = new File(Environment.getExternalStorageDirectory() + "/" + AppConstants.MAIN_DIR, AppConstants.PHOTO_DIR);
        if (!fVideo.exists()) {
            fVideo.mkdirs();
        }

        String path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.PHOTO_DIR;
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
        ContentResolver contentResolver = getActivity().getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?", new String[]{item.getItemPathOld()});

        // save item to db
        personalPhotosDao.addItem(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personalPhotosDao = DataBase.getInstance(getActivity()).personalFilesDao();
        adapter = new PersonalFilesAdapter(PersonalFilesAdapter.HolderConstants.PHOTO);
        if (adapter.mData.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        showRecycler();
    }

    private void showRecycler() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                final List<PersonalFileItem> li = getPhotos();
                try {

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
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

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;

        //Stores all the images from the gallery in Cursor
        Cursor cursor;
        if (isSDPresent) {
            cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
        } else {
            cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
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

            photoItemList.add(new PersonalFileItem(arrPath[i], name, extension, false));

        }

        // The cursor should be freed up after use with close()
        cursor.close();
        return photoItemList;
    }


}