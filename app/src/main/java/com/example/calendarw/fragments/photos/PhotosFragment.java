package com.example.calendarw.fragments.photos;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.calendarw.adapters.PhotosAdapter;
import com.example.calendarw.database.DataBase;
import com.example.calendarw.R;

import com.example.calendarw.database.PersonalPhotosDao;
import com.example.calendarw.dialog.ShMyDialog;
import com.example.calendarw.fragments.MainFragment;
import com.example.calendarw.items.PersonalPhotoItem;
import com.example.calendarw.listener.DestinationListener;
import com.example.calendarw.listener.TrialListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotosFragment extends Fragment {

    private DestinationListener listener;
    private RecyclerView recyclerView;
    private PhotosAdapter adapter;
    private ProgressBar progressBar;
    private PersonalPhotosDao personalPhotosDao;
    private Group group;

    private int max = 0;

    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PhotosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        view.findViewById(R.id.fab_photo).setOnClickListener(v -> {
            listener.onFragmentDestination(R.id.personalPhotosFragment);
        });
        recyclerView = view.findViewById(R.id.photosRecycler);
        progressBar = view.findViewById(R.id.progress);
        group = view.findViewById(R.id.group_empty_photo);

        progressBar.setVisibility(View.INVISIBLE);
        group.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personalPhotosDao = DataBase.getInstance(getActivity()).personalPhotosDao();
        adapter = new PhotosAdapter();
        adapter.setOnItemClickListener(position -> {
            Toast.makeText(getContext(), "wejdan " + position, Toast.LENGTH_SHORT).show();
            mListener.onItemClick(1);
        });
        if (adapter.mData.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        new Thread() {
            @Override
            public void run() {
                super.run();
                final List<PersonalPhotoItem> li = getPhotos();
                try {

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.mData = li;
                                adapter.notifyDataSetChanged();
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

        MainFragment.setOnItemClickListener(position -> {
            switch (position) {
                case 1:
                    Toast.makeText(getContext(), "case 1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //// TODO: 13-Dec-20 call unhide method
                    unHidePhotos();
                    Toast.makeText(getContext(), "case 2", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getContext(), "unrecognised case", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<PersonalPhotoItem> getPhotos() {
        List<PersonalPhotoItem> photoItems = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString() + "/WejdanFolder";
        System.out.println("Path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            System.out.println("Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                PersonalPhotoItem personalPhotoItem = personalPhotosDao.getItem(files[i].getAbsolutePath());
                if (personalPhotoItem != null) {
                    photoItems.add(personalPhotoItem);
                }
            }
        }
        return photoItems;
    }

//            System.out.println("FileName:" + files[i].getName());

//            System.out.println("file path : " + files[i].getAbsolutePath());

//                String imgPath = personalPhotoItem.getImgPathNew().replace(".wejdan", "." + personalPhotoItem.getImgExt());
//                System.out.println("extintion :" + personalPhotoItem.getImgExt());
//                System.out.println("extintion img path :" + imgPath);

    private void unHidePhotos() {

        max = adapter.getSelectedCount();
        ShMyDialog dialog = new ShMyDialog(() -> {
            Toast.makeText(getContext(), "you clicked cancel", Toast.LENGTH_SHORT).show();
        }, "Hide", "0/" + max, max);
        dialog.show(getParentFragmentManager(), "personal photos hide");

        new Thread() {
            @Override
            public void run() {
                super.run();

                if (adapter != null) {
                    for (PersonalPhotoItem item : adapter.mData) {
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

    }

    private void copyPhotos(PersonalPhotoItem item) {
        String folder_main = "unhide"; // name of the directory
        File f = //
                new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/", // path of directory
                        folder_main);

        if (!f.exists()) {
            f.mkdirs(); // if the directory ( unhide) is not existed , create it
        }
//        String imgPath = item.getImgPathOld().replace(item.getImgName() + "." + item.getImgExt(), "");

        String path = Environment.getExternalStorageDirectory().toString() + "/"
                // not really sure but it brings the path to the external storage of the phone
                + Environment.DIRECTORY_DCIM + "/" // directory in gallery
                + "/unhide"; // the directory i want to save photos in


        File file = new File(path, //path of photo - inside (unhide) directory
                item.getImgName() + "." //name of photo with its extension
                        + item.getImgExt());

        // save new path
        item.setChecked(false);
        try {

            OutputStream stream = null;
            stream = new FileOutputStream(file);
            File file1 = new File(item.getImgPathNew());
            stream.write(FileUtils.readFileToByteArray(file1)); // copy image as a file
            stream.flush();
            stream.close();
            Log.d("TAG", "copyPhotos: " + file1.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
        // refresh and rescan DCIM and gallery for showing images after copying them

        System.out.println(" new path sohaib hassan : " + file.getAbsolutePath());

        //TODO: remove from db

        // this code is for deleting the image as a file - it works
//        File fileN = new File(item.getImgPathNew());
//        if (fileN.exists())
//            fileN.delete();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DestinationListener) {
            listener = (DestinationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}