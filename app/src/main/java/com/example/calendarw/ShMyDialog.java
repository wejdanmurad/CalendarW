package com.example.calendarw;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class ShMyDialog extends DialogFragment {

    ProgressBar progressBar;
    private Dialog builder;
    private DialogListener listener;
    private String s_hide, s_number;
    private TextView tv_hide, tv_number, tv_cancel;
    private int max;

    public ShMyDialog(DialogListener listener, String s_hide, String s_number, int max) {
        this.listener = listener;
        this.s_hide = s_hide;
        this.s_number = s_number;
        this.max = max;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new Dialog(requireContext());

        View root = requireActivity().getLayoutInflater().inflate(R.layout.sh_my_dialog, null);
        init(root);
        clicked();

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
        System.out.println(progressBar.getProgress() + " progress is initialized");
    }

    public void setProgress(int progressVal) {
//        progressBar.setProgress(progressVal);
        if (progressBar != null)
            progressBar.setProgress(progressVal);
        else
            System.out.println("progress is null " + progressVal);
        System.out.println("progress is " + progressVal);
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
