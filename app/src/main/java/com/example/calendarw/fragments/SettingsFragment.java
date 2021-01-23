package com.example.calendarw.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendarw.R;
import com.example.calendarw.utils.SharedPreferencesHelper;

public class SettingsFragment extends Fragment {

    private EditText oldPass, newPass, confirmPass;
    private Drawable myimage;
    private Button changePass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        oldPass = view.findViewById(R.id.oldpass);
        newPass = view.findViewById(R.id.newpass);
        confirmPass = view.findViewById(R.id.confirmpass);
        changePass = view.findViewById(R.id.changePass);

        errorEditText(oldPass);
        errorEditText(newPass);
        errorEditText(confirmPass);


        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        Log.d("TAG", "onActivityCreated: "+ SharedPreferencesHelper.getUserPassword());

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });


    }


    public void errorEditText(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start <= 3) {
                    editText.setBackground(getResources().getDrawable(R.drawable.errorsettingfragment, null));
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);

                } else {
                    editText.setBackground(getResources().getDrawable(R.drawable.bg_radius_full_gradient_colored_alpha, null));
                    editText.setCompoundDrawablesRelative(null, null, null, null);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void confirm() {


        String mOldpass = oldPass.getText().toString();
        String mNewPass = newPass.getText().toString();
        String nConfirmPass = confirmPass.getText().toString();
        
        if (mOldpass.equals(SharedPreferencesHelper.getUserPassword())) {

            if (mNewPass.equals(nConfirmPass)) {
                Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                SharedPreferencesHelper.setUserPassword(mNewPass);
            } else {
                Toast.makeText(getActivity(), "plese confirm your password 😊", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "your old password is not correct 😒", Toast.LENGTH_SHORT).show();
        }

    }
}