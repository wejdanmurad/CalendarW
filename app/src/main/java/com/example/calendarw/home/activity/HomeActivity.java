package com.example.calendarw.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.example.calendarw.home.listener.DestinationListener;
import com.example.calendarw.R;

public class HomeActivity extends AppCompatActivity implements DestinationListener {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

    }

    @Override
    public void onFragmentDestination(int id) {
        switch (id) {
//            case R.id.photosFragment:
//                navController.navigate(R.id.photosFragment);
//                break;
//            case R.id.videosFragment:
//                navController.navigate(R.id.videosFragment);
//                break;
            case R.id.personalPhotosFragment:
                navController.navigate(R.id.personalPhotosFragment);
                break;
            case R.id.personalVideosFragment:
                navController.navigate(R.id.personalVideosFragment);
                break;
        }
    }
}