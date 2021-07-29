package com.sw.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sw.calendarw.fragments.AudioFragment;
import com.sw.calendarw.fragments.CameraFragment;
import com.sw.calendarw.fragments.PhotosFragment;
import com.sw.calendarw.fragments.VideosFragment;
import com.sw.calendarw.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public NavController navController;
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

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> tabIndex = destination.getId());
    }

    @Override
    public void onBackPressed() {
        switch (tabIndex) {
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