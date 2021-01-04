package com.example.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calendarw.fragments.AudioFragment;
import com.example.calendarw.fragments.CameraFragment;
import com.example.calendarw.fragments.PhotosFragment;
import com.example.calendarw.fragments.VideosFragment;
import com.example.calendarw.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView back;
    private TextView unHide;
    //    private ConstraintLayout topLayout;
    public NavController navController;

    NavHostFragment navHostFragment;
    private boolean isPhotoFragment = true;
    private boolean isVideoFragment = false;
    private int tabIndex = R.id.photosFragment;

    public static OnItemClickListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout = findViewById(R.id.tabs);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        navController.popBackStack();
                        navController.navigate(R.id.photosFragment);
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        navController.popBackStack();
                        navController.navigate(R.id.cameraFragment);
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        navController.popBackStack();
                        navController.navigate(R.id.videosFragment);
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        navController.popBackStack();
                        navController.navigate(R.id.audioFragment);
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        navController.popBackStack();
                        navController.navigate(R.id.settingsFragment);
                        tabLayout.setVisibility(View.VISIBLE);
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

        CameraFragment.setOnItemClickListener(position -> {
            goBack();
        });


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.photosFragment:
                    tabIndex = R.id.photosFragment;
//                isPhotoFragment = true;
//                isVideoFragment = false;
                    break;
                case R.id.videosFragment:
                    tabIndex = R.id.videosFragment;
                    break;
                case R.id.audioFragment:
                    tabIndex = R.id.audioFragment;
                    break;
            }


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
        switch (tabIndex){
            case R.id.photosFragment:
                if (PhotosFragment.isEditing) {
                    mListener.onItemClick(1);
                } else {
                    super.onBackPressed();
                }
                break;
            case R.id.videosFragment:
                if (VideosFragment.isEditing) {
                    mListener.onItemClick(2);
                } else {
                    goBack();
                }
                break;
            case R.id.audioFragment:
                if (AudioFragment.isEditing) {
                    mListener.onItemClick(3);
                } else {
                    goBack();
                }
                break;
            default:
                goBack();
        }
//        if (isPhotoFragment) {
//            Log.d("isediting", "onBackPressed: " + PhotosFragment.isEditing);
//            if (PhotosFragment.isEditing) {
//                mListener.onItemClick(1);
//            } else {
//                super.onBackPressed();
//            }
//
//        } else if (isVideoFragment && VideosFragment.isEditing)
//            mListener.onItemClick(2);
//        else {
//            goBack();
//        }

    }

    private void goBack() {
        tabLayout.setVisibility(View.VISIBLE);
        navController.popBackStack();
        navController.navigate(R.id.photosFragment);
        tabLayout.selectTab(tabLayout.getTabAt(0), true);
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}