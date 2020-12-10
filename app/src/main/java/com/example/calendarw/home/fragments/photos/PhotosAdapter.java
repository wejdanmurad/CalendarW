package com.example.calendarw.home.fragments.photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.calendarw.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    Context context;
    List<PhotoItem> mData = new ArrayList<>();

    public PhotosAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
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
            for (PhotoItem item : mData) {
                if (item.isChecked())
                    count++;
            }
            return count;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private View view;
        private ImageView imgChecked;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_img);
            view = itemView.findViewById(R.id.shadow_view);
            imgChecked = itemView.findViewById(R.id.check);
            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                PhotoItem photoItem = mData.get(position);
                photoItem.setChecked(!photoItem.isChecked());
                notifyItemChanged(position);
            });

        }

        public void bind(int position) {
            PhotoItem photoItem = mData.get(position);
            File imgFile = new File(photoItem.getImgPathOld());
            if (imgFile.exists())
                Glide.with(context).load(photoItem.getImgPathOld()).into(imageView);
            if (photoItem.isChecked()) {
                view.setVisibility(View.VISIBLE);
                imgChecked.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
                imgChecked.setVisibility(View.INVISIBLE);
            }

        }
    }
}
