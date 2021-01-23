package com.example.calendarw.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.dialog.CompleteProfileDialog;
import com.example.calendarw.dialog.PasswordDialog;
import com.example.calendarw.utils.AppConstants;
import com.example.calendarw.utils.SharedPreferencesHelper;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPreferencesHelper.isFirstTime()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
                    finish();
                }
            }, 3000);
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
}