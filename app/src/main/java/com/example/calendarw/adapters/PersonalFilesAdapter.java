package com.example.calendarw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.calendarw.R;
import com.example.calendarw.items.PersonalFileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonalFilesAdapter extends RecyclerView.Adapter<PersonalFilesAdapter.MyViewHolder> {

    Context context;
    public List<PersonalFileItem> mData = new ArrayList<>();
    private final HolderConstants holderConstants;

    public PersonalFilesAdapter(HolderConstants holderConstants) {
        this.holderConstants = holderConstants;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_file, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public int getSelectedCount() {
        if (mData.isEmpty())
            return -1;
        else {
            int count = 0;
            for (PersonalFileItem item : mData) {
                if (item.isChecked())
                    count++;
            }
            return count;
        }

    }

    public void unSelectAll() {
        if (!mData.isEmpty()) {
            for (PersonalFileItem item : mData) {
                item.setChecked(false);
            }
        }
    }

    public void selectAll() {
        if (!mData.isEmpty()) {
            for (PersonalFileItem item : mData) {
                item.setChecked(true);
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagePlay;
        private ImageView imageView;
        private CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePlay = itemView.findViewById(R.id.play);
            checkBox = itemView.findViewById(R.id.check_box);

            if (holderConstants == HolderConstants.PHOTO)
                imagePlay.setVisibility(View.INVISIBLE);
            else if (holderConstants == HolderConstants.VIDEO)
                imagePlay.setVisibility(View.VISIBLE);

            imageView = itemView.findViewById(R.id.item_img);

            imageView.setOnClickListener(v -> {
                selectPhoto();
            });

            checkBox.setOnClickListener(v -> {
                selectPhoto();
            });
        }

        private void selectPhoto() {
            int position = getAdapterPosition();
            PersonalFileItem photoItem = mData.get(position);
            photoItem.setChecked(!photoItem.isChecked());
            notifyItemChanged(position);
        }

        public void bind(int position) {
            PersonalFileItem photoItem = mData.get(position);
            File imgFile = new File(photoItem.getItemPathOld());
            if (imgFile.exists())
                Glide.with(context).load(photoItem.getItemPathOld()).into(imageView);
            imageView.setClipToOutline(true);
            checkBox.setChecked(photoItem.isChecked());

        }
    }

    public enum HolderConstants {
        PHOTO, VIDEO
    }
}
