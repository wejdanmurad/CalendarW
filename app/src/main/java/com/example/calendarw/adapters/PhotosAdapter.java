package com.example.calendarw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calendarw.R;
import com.example.calendarw.items.PersonalPhotoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    Context context;
    public List<PersonalPhotoItem> mData = new ArrayList<>();
    public boolean longClicked = false;
    private OnItemClickListener mListener;

    public PhotosAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_photo, parent, false);
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
            for (PersonalPhotoItem item : mData) {
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
            imageView.setOnLongClickListener(v -> {
                longClicked = true;
                selectPhoto();
                mListener.onItemClick(getAdapterPosition());
                return false;
            });
            imageView.setOnClickListener(v -> {
                if (longClicked)
                    selectPhoto();
            });
        }

        private void selectPhoto() {
            int position = getAdapterPosition();
            PersonalPhotoItem photoItem = mData.get(position);
            photoItem.setChecked(!photoItem.isChecked());
            notifyItemChanged(position);
        }

        public void bind(int position) {
            PersonalPhotoItem photoItem = mData.get(position);
            File imgFile = new File(photoItem.getImgPathNew());
            if (imgFile.exists()) {
//                String imgPath = photoItem.getImgPathNew().replace(".wejdan", "." + photoItem.getImgExt());
                Glide.with(context).load(photoItem.getImgPathNew()).into(imageView);
                System.out.println("glide path :" + photoItem.getImgPathNew());
            }
//                imageView.setImageURI(Uri.parse(photoItem.getImgPathNew()));
            if (photoItem.isChecked()) {
                view.setVisibility(View.VISIBLE);
                imgChecked.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
                imgChecked.setVisibility(View.INVISIBLE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
