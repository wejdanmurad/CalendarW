package com.example.calendarw.fragments;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.activity.HomeActivity;
import com.example.calendarw.activity.PersonalFileActivity;
import com.example.calendarw.adapters.FilesAdapter;
import com.example.calendarw.database.DataBase;
import com.example.calendarw.database.PersonalFilesDao;
import com.example.calendarw.dialog.FileDialog;
import com.example.calendarw.items.PersonalFileItem;
import com.example.calendarw.utils.AppConstants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment {

    private RecyclerView recyclerView;
    public final FilesAdapter adapter = new FilesAdapter(FilesAdapter.HolderConstants.VIDEO);
    private ProgressBar progressBar;
    private PersonalFilesDao personalPhotosDao;
    private Group group;

    private int max = 0;
    private int init = 0;

    public static boolean isEditing = false;

    private OnItemClickListener mListener;

    private ImageButton tb_edit;

    private Button btn;
    private boolean isHide = true;

    @Override
    public void onResume() {
        super.onResume();
        showRecycler();
//        Toast.makeText(getContext(), "adapter notify success", Toast.LENGTH_SHORT).show();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static VideosFragment getInstance() {
        return new VideosFragment();
    }

    public VideosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        btn = view.findViewById(R.id.btn_video);

        recyclerView = view.findViewById(R.id.videosRecycler);
        progressBar = view.findViewById(R.id.progress);
        group = view.findViewById(R.id.group_empty_photo);

        progressBar.setVisibility(View.INVISIBLE);
        group.setVisibility(View.INVISIBLE);

        tb_edit = view.findViewById(R.id.tb_edit);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personalPhotosDao = DataBase.getInstance(getActivity()).personalFilesDao();

        adapter.setOnItemClickListener(position -> {
//            Toast.makeText(getContext(), "wejdan " + position, Toast.LENGTH_SHORT).show();
//            mListener.onItemClick(1);
            editVideos();
            changeBtn(false, R.drawable.bg_radius_color, R.string.unhide);
        });
        if (adapter.mData.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        showRecycler();

        tb_edit.setOnClickListener(v -> {
            if (adapter.longClicked)
                back();
            else {
                adapter.longClicked = true;
                editVideos();
            }
        });

        HomeActivity.setOnItemClickListener(position -> {
            if (position == 2)
                back();
        });

        btn.setOnClickListener(v -> {
            if (isHide) {
                Intent intent = new Intent(getActivity(), PersonalFileActivity.class);
                intent.putExtra("isPhotoFile", false);
                startActivity(intent);
            } else {
                unHideVideos();
            }
        });
    }

    private void changeBtn(boolean b, int bg_color, int txt) {
        isHide = b;
        btn.setBackground(getResources().getDrawable(bg_color, getResources().newTheme()));
        btn.setText(getResources().getString(txt));
    }

    public void editVideos() {
        changeBtn(false, R.drawable.bg_radius_color, R.string.unhide);
        isEditing = true;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    private void showRecycler() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                final List<PersonalFileItem> li = getVideos();
                try {

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.mData = li;
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                                progressBar.setVisibility(View.INVISIBLE);
                                if (adapter.mData.isEmpty())
                                    group.setVisibility(View.VISIBLE);
                                else
                                    group.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }


    private List<PersonalFileItem> getVideos() {
        List<PersonalFileItem> photoItems = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.VIDEO_DIR;
        System.out.println("Path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            System.out.println("Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                PersonalFileItem personalPhotoItem = personalPhotosDao.getItem(files[i].getAbsolutePath());
                if (personalPhotoItem != null) {
                    photoItems.add(personalPhotoItem);
                }
            }
        }
        return photoItems;
    }

    public void back() {
        isEditing = false;
        changeBtn(true, R.drawable.bg_radius_red, R.string.HideVideos);
        adapter.unselectItems();
        adapter.longClicked = false;
        showRecycler();
    }

    public void unHideVideos() {
        if (adapter.getSelectedCount() <= 0)
            back();
        else {
            adapter.longClicked = false;
            List<String> exts = new ArrayList<>();
            max = adapter.getSelectedCount();
            init = 0;
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
                                copyVideos(item);
                                exts.add(item.getItemExt());
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
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

                    showRecycler();

                    dialog.dismiss();
                    String pathname = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.CALENDAR_DIR;

                    String[] myArray = new String[exts.size()];
                    exts.toArray(myArray);

                    MediaScannerConnection.scanFile(getContext(), new String[]{pathname}, myArray, (path, uri) -> {
                        Toast.makeText(getContext(), "you are doing great", Toast.LENGTH_SHORT).show();
                    });

                    changeBtn(true, R.drawable.bg_radius_red, R.string.HidePhotos);

                }
            }.start();
        }
    }

    private void copyVideos(PersonalFileItem item) {
        File f = new File(Environment.getExternalStorageDirectory(), AppConstants.CALENDAR_DIR);

        if (!f.exists()) {
            f.mkdirs();
        }

        String path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.CALENDAR_DIR;


        File file = new File(path, item.getItemName() + "." + item.getItemExt());

        // save new path
        item.setChecked(false);

        try {

            OutputStream stream = null;
            stream = new FileOutputStream(file);
            File file1 = new File(item.getItemPathNew());
            stream.write(FileUtils.readFileToByteArray(file1)); // copy image as a file
            stream.flush();
            stream.close();
            Log.d("TAG", "copyPhotos: " + file1.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        personalPhotosDao.deleteItem(item);
        File fileN = new File(item.getItemPathNew());
        if (fileN.exists())
            fileN.delete();

    }


}