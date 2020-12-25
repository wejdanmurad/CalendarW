package com.example.calendarw.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.calendarw.R;
import com.example.calendarw.activity.AppLockActivity;
import com.example.calendarw.activity.BreakingAlertActivity;
import com.example.calendarw.activity.DownloadActivity;
import com.example.calendarw.activity.FileManagerActivity;
import com.example.calendarw.activity.NotesActivity;

public class MoreFragment extends Fragment {

    private ImageButton download, file_manager, breaking_alert, notes, app_lock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        download = view.findViewById(R.id.download);
        file_manager = view.findViewById(R.id.file_manager);
        breaking_alert = view.findViewById(R.id.breaking_alert);
        notes = view.findViewById(R.id.notes);
        app_lock = view.findViewById(R.id.app_lock);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        download.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DownloadActivity.class));
        });

        file_manager.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FileManagerActivity.class));
        });

        breaking_alert.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), BreakingAlertActivity.class));
        });

        notes.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NotesActivity.class));
        });
        app_lock.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AppLockActivity.class));
        });

    }

}