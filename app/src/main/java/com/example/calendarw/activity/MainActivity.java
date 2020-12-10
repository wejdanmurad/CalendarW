package com.example.calendarw.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.home.activity.HomeActivity;
import com.example.calendarw.utils.AppConstants;
import com.example.calendarw.utils.SharedPreferencesHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lastView, tvNoEvent;
    private ImageView imgDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastView = null;
        tvNoEvent = findViewById(R.id.tv_no_event);
        imgDel = findViewById(R.id.img_del);
        imgDel.setVisibility(View.INVISIBLE);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
        findViewById(R.id.tv_9).setOnClickListener(this);

        findViewById(R.id.img_del).setOnClickListener(this);
        findViewById(R.id.fab_add).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_del:
                if (tvNoEvent.getText() != null && tvNoEvent.length() > 0) {
                    if (tvNoEvent.length() == 1) {
                        imgDel.setVisibility(View.INVISIBLE);
                        tvNoEvent.setText("No events");
                        lastView.setBackground(getDrawable(R.drawable.bg_gray));
                        lastView = null;
                    }
                    tvNoEvent.setText(tvNoEvent.getText().toString().substring(0, tvNoEvent.length() - 1));
                }
                break;
            case R.id.fab_add:
                if (tvNoEvent.getText().length() < 4)
                    Toast.makeText(this, "enter at least 4 numbers", Toast.LENGTH_SHORT).show();
                else {
                    if (SharedPreferencesHelper.isFirstTime(this))
                        showMyDialog();
                    else {
                        if (SharedPreferencesHelper.getUserPassword(this).contentEquals(tvNoEvent.getText())) {
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else
                            Toast.makeText(this, "password incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                if (tvNoEvent.length() <= 9) {
                    if (lastView != null) {
                        lastView.setBackground(getDrawable(R.drawable.bg_gray));
                        tvNoEvent.append(((TextView) v).getText());
                    } else {
                        imgDel.setVisibility(View.VISIBLE);
                        tvNoEvent.setText(((TextView) v).getText());
                    }
                } else {
                    lastView.setBackground(getDrawable(R.drawable.bg_gray));
                    Toast.makeText(this, "sorry, too much long for a password", Toast.LENGTH_SHORT).show();
                }

                lastView = findViewById(v.getId());
                v.setBackground(getDrawable(R.drawable.circle_select));


        }
    }

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want this to be yur password?");

        builder.setPositiveButton("Yes, sure", (dialog, which) -> {

            SharedPreferences.Editor firstTime = getSharedPreferences(AppConstants.USER_FIRST_TIME, MODE_PRIVATE).edit();
            firstTime.putBoolean(AppConstants.USER_FIRST_TIME, false);
            firstTime.apply();

            SharedPreferences.Editor userPass = getSharedPreferences(AppConstants.USER_PASSWORD, MODE_PRIVATE).edit();
            userPass.putString(AppConstants.USER_PASSWORD, tvNoEvent.getText().toString());
            userPass.apply();
            userPass.commit();

            Toast.makeText(getApplicationContext(), "your password is set!", Toast.LENGTH_SHORT).show();

        });

        builder.setNegativeButton("No", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Please edit your password!", Toast.LENGTH_SHORT).show();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}