package com.example.calendarw.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.calendarw.R;

public class NotesActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView tb_txt;
    private ImageButton tb_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tb_txt = toolbar.findViewById(R.id.tb_txt_center);
        tb_back = toolbar.findViewById(R.id.tb_back);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        tb_txt.setText(getResources().getString(R.string.Notes));
        tb_back.setOnClickListener(v -> {
            this.finish();
        });
    }
}