package com.example.calendarw.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.calendarw.R;

public class PasswordDialog extends DialogFragment {

    private Dialog builder;
    private DialogListener listener;
    private Button cancel, sure;

    public PasswordDialog(DialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new Dialog(requireContext());
        View root = requireActivity().getLayoutInflater().inflate(R.layout.dialog_password, null);

        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        builder.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        init(root);
        clicked();
        builder.setCanceledOnTouchOutside(false);
        builder.setContentView(root);

        return builder;
    }

    public void init(View root) {
        sure = root.findViewById(R.id.sure);
        cancel = root.findViewById(R.id.cancel);
    }

    public void clicked() {
        sure.setOnClickListener(v -> {
            listener.sure();
        });
        cancel.setOnClickListener(v -> {
            listener.cancel();
        });
    }

    public interface DialogListener {
        void sure();

        void cancel();
    }
}
