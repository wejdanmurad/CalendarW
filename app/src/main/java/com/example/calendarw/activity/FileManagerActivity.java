package com.example.calendarw.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.calendarw.R;

public class FileManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tb_txt;
    private ImageButton tb_back, btn_audio, btn_others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tb_txt = toolbar.findViewById(R.id.tb_txt_center);
        tb_back = toolbar.findViewById(R.id.tb_back);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        tb_txt.setText(getResources().getString(R.string.FileManager));
        tb_back.setOnClickListener(v -> {
            this.finish();
        });

        btn_audio = findViewById(R.id.audio);
        btn_audio.setOnClickListener(v -> {
            startActivity(new Intent(this, AudioActivity.class));
        });

    }
}