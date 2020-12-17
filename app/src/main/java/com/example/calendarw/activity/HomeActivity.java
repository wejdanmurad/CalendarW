package com.example.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarw.adapters.ViewPagerAdapter;
import com.example.calendarw.fragments.photos.PhotosFragment;
import com.example.calendarw.fragments.videos.VideosFragment;
import com.example.calendarw.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private PhotosFragment photosFragment;
    private VideosFragment videosFragment;
    private ImageView back;
    private TextView unHide;
    private ConstraintLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        back = findViewById(R.id.back);
        unHide = findViewById(R.id.unhide);
        topLayout = findViewById(R.id.topLayout);

        tabLayout.setVisibility(View.VISIBLE);
        topLayout.setVisibility(View.INVISIBLE);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        photosFragment = new PhotosFragment();
        videosFragment = new VideosFragment();

        adapter.AddFragment(photosFragment, getResources().getString(R.string.Photos));
        adapter.AddFragment(videosFragment, getResources().getString(R.string.Videos));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        back.setOnClickListener(v -> {
            tabLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.INVISIBLE);
            if (viewPager.getCurrentItem() == 0)
                photosFragment.back();
            if (viewPager.getCurrentItem() == 1)
                videosFragment.back();
        });

        unHide.setOnClickListener(v -> {
            tabLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.INVISIBLE);
            if (viewPager.getCurrentItem() == 0)
                photosFragment.unHidePhotos();
            if (viewPager.getCurrentItem() == 1)
                videosFragment.unHideVideos();
        });


        photosFragment.setOnItemClickListener(position -> {
            if (position == 1) {
                Toast.makeText(this, "ggggggggggggggggggggggg", Toast.LENGTH_SHORT).show();

                tabLayout.setVisibility(View.INVISIBLE);
                topLayout.setVisibility(View.VISIBLE);
            }
        });

        videosFragment.setOnItemClickListener(position -> {
            if (position == 1) {
                Toast.makeText(this, "ggggggggggggggggggggggg", Toast.LENGTH_SHORT).show();

                tabLayout.setVisibility(View.INVISIBLE);
                topLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}