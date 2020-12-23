package com.example.calendarw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.calendarw.R;


public class CompleteProfileDialog extends Dialog {
    private final Context context;

    public CompleteProfileDialog(@NonNull final Context context) {
        super(context);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.complete_profile_dialog);
        initViews();
    }

    private void initViews() {
        Button btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(v -> {
            dismiss();
        });
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> dismiss());
    }
}
