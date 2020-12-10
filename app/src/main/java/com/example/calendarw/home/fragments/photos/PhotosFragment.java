package com.example.calendarw.home.fragments.photos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarw.R;
import com.example.calendarw.home.listener.DestinationListener;

public class PhotosFragment extends Fragment {

    private DestinationListener listener;

    public static PhotosFragment getInstance() {
        return new PhotosFragment();
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause2");
    }
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume2");
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