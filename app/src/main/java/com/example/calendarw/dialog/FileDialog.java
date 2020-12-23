package com.example.calendarw.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.calendarw.R;


public class FileDialog extends DialogFragment {

    ProgressBar progressBar;
    private Dialog builder;
    private DialogListener listener;
    private String s_hide, s_number;
    private TextView tv_hide, tv_number, tv_cancel;
    private int max;

    public FileDialog(DialogListener listener, String s_hide, String s_number, int max) {
        this.listener = listener;
        this.s_hide = s_hide;
        this.s_number = s_number;
        this.max = max;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new Dialog(requireContext());
        System.out.println(" dialog is created");
        View root = requireActivity().getLayoutInflater().inflate(R.layout.dialog_file, null);

        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        builder.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        init(root);
        clicked();
        System.out.println(" init is created");
        builder.setCanceledOnTouchOutside(false);
        builder.setContentView(root);

        return builder;
    }

    public void init(View root) {
        tv_hide = root.findViewById(R.id.tv_hide);
        tv_number = root.findViewById(R.id.tv_number);
        tv_cancel = root.findViewById(R.id.tv_cancel);
        progressBar = root.findViewById(R.id.progressBar);
        tv_hide.setText(s_hide);
        tv_number.setText(s_number);
        progressBar.setMax(max);
    }

    public void setProgress(int progressVal) {
        progressBar.setProgress(progressVal);
        System.out.println("progress" + progressVal);
        Log.d("MAX", "progress" + progressVal);
    }

    public void setNumber(int numberVal) {
        tv_number.setText(numberVal + "/" + max);
        Log.d("MAX", "no of progress" + numberVal + "/" + max);
    }

    public void clicked() {
        tv_cancel.setOnClickListener(v -> {
            listener.cancel();
        });
    }

    public interface DialogListener {
        void cancel();
    }
}
