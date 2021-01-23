package com.example.calendarw.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.calendarw.R;

public class SettingsFragment extends Fragment {

    private Button fingerprint, faceprint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        fingerprint = view.findViewById(R.id.btn_finger_print);
        faceprint = view.findViewById(R.id.btn_face_print);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fingerprint.setOnClickListener(v -> {
            //
        });

        faceprint.setOnClickListener(v -> {
            //
        });
    }
}