package com.sw.calendarw.fragments;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sw.calendarw.R;
import com.sw.calendarw.activity.HomeActivity;
import com.sw.calendarw.activity.PersonalAudioActivity;
import com.sw.calendarw.adapters.AudioAdapter;
import com.sw.calendarw.database.DataBase;
import com.sw.calendarw.database.PersonalFilesDao;
import com.sw.calendarw.dialog.FileDialog;
import com.sw.calendarw.items.PersonalFileItem;
import com.sw.calendarw.utils.AppConstants;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AudioFragment extends Fragment {

    private RecyclerView recyclerView;
    public final AudioAdapter adapter = new AudioAdapter(AudioAdapter.HolderConstants.H_Audio);
    private ProgressBar progressBar;
    private PersonalFilesDao filesDao;
    private Group group;

    private int max = 0;
    private int init = 0;
    public static boolean isEditing = false;

    private ImageButton tb_edit;
    private Button btn;
    private boolean isHide = true;

    AdView adView;
    LinearLayout bannerContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        bannerContainer = view.findViewById(R.id.banner_container);
        btn = view.findViewById(R.id.btn_audio);
        recyclerView = view.findViewById(R.id.audioRecycler);
        progressBar = view.findViewById(R.id.progress);
        group = view.findViewById(R.id.group_empty_audio);
        tb_edit = view.findViewById(R.id.tb_edit);

        progressBar.setVisibility(View.INVISIBLE);
        group.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //fb ads
        adView = new AdView(getContext(), getResources().getString(R.string.fb_banner1), AdSize.BANNER_HEIGHT_50);
        bannerContainer.addView(adView);
        adView.loadAd();

        general();
    }

    @Override
    public void onResume() {
        super.onResume();
        showRecycler();
    }

    private void general() {
        filesDao = DataBase.getInstance(getContext()).personalFilesDao();

        adapter.setOnItemClickListener(this::editPhotos);
        if (adapter.mData.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);

        setRecyclerLM();
        showRecycler();

        tb_edit.setOnClickListener(v -> {
            if (adapter.longClicked)
                back();
            else {
                adapter.longClicked = true;
                editPhotos();
            }
        });

        HomeActivity.setOnItemClickListener(position -> {
            if (position == 3)
                back();
        });

        btn.setOnClickListener(v -> {
            if (isHide) {
                Intent intent = new Intent(getActivity(), PersonalAudioActivity.class);
                intent.putExtra("isPhotoFile", true);
                startActivity(intent);
            } else {
                unHidePhotos();
            }
        });
    }

    public void unHidePhotos() {
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
            dialog.show(getChildFragmentManager(), "personal photos hide");

            new Thread() {
                @Override
                public void run() {
                    super.run();

                    if (adapter != null) {
                        for (PersonalFileItem item : adapter.mData) {
                            if (item.isChecked()) {
                                copyPhotos(item);
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

                    MediaScannerConnection.scanFile(getActivity(), new String[]{pathname}, myArray, (path, uri) -> {
                        Toast.makeText(getActivity(), "you are doing great", Toast.LENGTH_SHORT).show();
                    });

                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> {
                            changeBtn(true, R.drawable.bg_radius_red, R.string.HideAudio);
                        });

                }
            }.start();
        }
    }

    private void copyPhotos(PersonalFileItem item) {
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
            stream.write(FileUtils.readFileToByteArray(file1));
            stream.flush();
            stream.close();
            Log.d("TAG", "copyPhotos: " + file1.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        filesDao.deleteItem(item);
        File fileN = new File(item.getItemPathNew());
        if (fileN.exists())
            fileN.delete();


    }

    private void changeBtn(boolean b, int bg_color, int txt) {
        isHide = b;
        btn.setBackground(getResources().getDrawable(bg_color, getResources().newTheme()));
        btn.setText(getResources().getString(txt));
    }

    public void back() {
        isEditing = false;
        changeBtn(true, R.drawable.bg_radius_red, R.string.HideAudio);
        adapter.unselectItems();
        adapter.longClicked = false;
        showRecycler();
    }

    public void editPhotos() {
        changeBtn(false, R.drawable.bg_radius_color, R.string.unhide);
        isEditing = true;
        setRecyclerLM();
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
                                setRecyclerLM();
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

    private void setRecyclerLM() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private List<PersonalFileItem> getPhotos() {
        List<PersonalFileItem> photoItems = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString() + "/" + AppConstants.MAIN_DIR + "/" + AppConstants.AUDIO_DIR;
        System.out.println("Path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            System.out.println("Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                PersonalFileItem personalPhotoItem = filesDao.getItem(files[i].getAbsolutePath());
                if (personalPhotoItem != null) {
                    photoItems.add(personalPhotoItem);
                }
            }
        }
        return photoItems;
    }

}