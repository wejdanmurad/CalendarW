package com.example.calendarw.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.calendarw.R;
import com.example.calendarw.adapters.PhotosAdapter;
import com.example.calendarw.adapters.ViewPagerAdapter;
import com.example.calendarw.fragments.videos.VideosFragment;
import com.example.calendarw.fragments.photos.PhotosFragment;
import com.example.calendarw.listener.DestinationListener;
import com.example.calendarw.listener.TrialListener;
import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private PhotosFragment photosFragment;
    private VideosFragment videosFragment;
    private ImageView back;
    private TextView unHide;
    private ConstraintLayout topLayout;

    private static OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        back = view.findViewById(R.id.back);
        unHide = view.findViewById(R.id.unhide);
        topLayout = view.findViewById(R.id.topLayout);

        tabLayout.setVisibility(View.VISIBLE);
        topLayout.setVisibility(View.INVISIBLE);

        back.setOnClickListener(v -> {
            tabLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.INVISIBLE);
            mListener.onItemClick(1);
        });

        unHide.setOnClickListener(v -> {
            tabLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.INVISIBLE);
            mListener.onItemClick(2);
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        photosFragment = new PhotosFragment();
        videosFragment = new VideosFragment();

        adapter.AddFragment(photosFragment, getResources().getString(R.string.Photos));
        adapter.AddFragment(videosFragment, getResources().getString(R.string.Videos));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        photosFragment.setOnItemClickListener(position -> {
            if (position == 1) {
                Toast.makeText(getContext(), "ggggggggggggggggggggggg", Toast.LENGTH_SHORT).show();

                tabLayout.setVisibility(View.INVISIBLE);
                topLayout.setVisibility(View.VISIBLE);
            }
        });

    }

}