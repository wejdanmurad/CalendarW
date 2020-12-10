package com.example.calendarw.home.fragments.videos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarw.R;
import com.example.calendarw.home.listener.DestinationListener;

public class VideosFragment extends Fragment {

    private DestinationListener listener;

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
        view.findViewById(R.id.fab_video).setOnClickListener(v -> {
            listener.onFragmentDestination(R.id.personalVideosFragment);
        });
        return view;
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