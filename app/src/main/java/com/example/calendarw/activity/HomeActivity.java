package com.example.calendarw.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    //    private ConstraintLayout topLayout;
    public NavController navController;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = toolbar.findViewById(R.id.tb_txt_center);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        tabLayout = findViewById(R.id.tabs);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        navController.navigate(R.id.photosFragment);
                        break;
                    case 1:
                        toolbar.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        navController.navigate(R.id.cameraFragment);
                        break;
                    case 2:
                        navController.navigate(R.id.videosFragment);
                        break;
                    case 3:
                        navController.navigate(R.id.settingsFragment);
                        break;
                    case 4:
                        navController.navigate(R.id.moreFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            toolbarTitle.setText(destination.getLabel());
//            switch (destination.getId()) {
//                case R.id.photosFragment:
//                    tabLayout.selectTab(tabLayout.getTabAt(0), true);
//                    break;
//                case R.id.cameraFragment:
//                    tabLayout.selectTab(tabLayout.getTabAt(1), true);
//                    break;
//                case R.id.videosFragment:
//                    tabLayout.selectTab(tabLayout.getTabAt(2), true);
//                    break;
//                case R.id.settingsFragment:
//                    tabLayout.selectTab(tabLayout.getTabAt(3), true);
//                    break;
//                case R.id.moreFragment:
//                    tabLayout.selectTab(tabLayout.getTabAt(4), true);
//                    break;
//            }

        });
//
//        tabLayout = findViewById(R.id.tabs);
//        viewPager = findViewById(R.id.view_pager);
//        back = findViewById(R.id.back);
//        unHide = findViewById(R.id.unhide);
////        topLayout = findViewById(R.id.topLayout);
//
//        tabLayout.setVisibility(View.VISIBLE);
////        topLayout.setVisibility(View.INVISIBLE);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        photosFragment = new PhotosFragment();
//        videosFragment = new VideosFragment();
//
//        adapter.AddFragment(photosFragment, getResources().getString(R.string.Photos));
//        adapter.AddFragment(videosFragment, getResources().getString(R.string.Videos));
//
////        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//
//
//        back.setOnClickListener(v -> {
//            tabLayout.setVisibility(View.VISIBLE);
////            topLayout.setVisibility(View.INVISIBLE);
//            if (viewPager.getCurrentItem() == 0)
//                photosFragment.back();
//            if (viewPager.getCurrentItem() == 1)
//                videosFragment.back();
//        });
//
//        unHide.setOnClickListener(v -> {
//            tabLayout.setVisibility(View.VISIBLE);
////            topLayout.setVisibility(View.INVISIBLE);
//            if (viewPager.getCurrentItem() == 0)
//                photosFragment.unHidePhotos();
//            if (viewPager.getCurrentItem() == 1)
//                videosFragment.unHideVideos();
//        });
//
//
//        photosFragment.setOnItemClickListener(position -> {
//            if (position == 1) {
//                Toast.makeText(this, "ggggggggggggggggggggggg", Toast.LENGTH_SHORT).show();
//
//                tabLayout.setVisibility(View.INVISIBLE);
////                topLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//        videosFragment.setOnItemClickListener(position -> {
//            if (position == 1) {
//                Toast.makeText(this, "ggggggggggggggggggggggg", Toast.LENGTH_SHORT).show();
//
//                tabLayout.setVisibility(View.INVISIBLE);
////                topLayout.setVisibility(View.VISIBLE);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navController.navigate(R.id.photosFragment);
        tabLayout.selectTab(tabLayout.getTabAt(0), true);
    }
}