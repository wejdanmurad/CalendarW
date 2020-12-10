package com.example.calendarw.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarw.R;
import com.example.calendarw.home.ViewPagerAdapter;
import com.example.calendarw.home.fragments.photos.PhotosFragment;
import com.example.calendarw.home.fragments.videos.VideosFragment;
import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private PhotosFragment photosFragment;
    private VideosFragment videosFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause1");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        photosFragment = new PhotosFragment();
        videosFragment = new VideosFragment();

        adapter.AddFragment(photosFragment, getResources().getString(R.string.Photos));
        adapter.AddFragment(videosFragment, getResources().getString(R.string.Videos));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("destroyed");
    }
}