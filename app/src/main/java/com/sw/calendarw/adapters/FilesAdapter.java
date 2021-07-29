package com.sw.calendarw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sw.calendarw.R;
import com.sw.calendarw.items.PersonalFileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    Context context;
    public List<PersonalFileItem> mData = new ArrayList<>();
    public boolean longClicked = false;
    private OnItemClickListener mListener;
    private final HolderConstants holderConstants;

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

    public void unselectItems() {
        for (PersonalFileItem item : mData) {
            if (item.isChecked())
                item.setChecked(false);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagePlay;
        private ImageView imageView;
        private CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePlay = itemView.findViewById(R.id.play);

            if (holderConstants == HolderConstants.PHOTO)
                imagePlay.setVisibility(View.INVISIBLE);
            else if (holderConstants == HolderConstants.VIDEO)
                imagePlay.setVisibility(View.VISIBLE);

            imageView = itemView.findViewById(R.id.item_img);
            checkBox = itemView.findViewById(R.id.check_box);
            checkBox.setVisibility(View.INVISIBLE);

            if (longClicked)
                checkBox.setVisibility(View.VISIBLE);

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
            File imgFile = new File(photoItem.getItemPathNew());
            if (imgFile.exists()) {
                Glide.with(context).load(photoItem.getItemPathNew()).into(imageView);
                imageView.setClipToOutline(true);
                System.out.println("glide path :" + photoItem.getItemPathNew());
            }
            checkBox.setChecked(photoItem.isChecked());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public enum HolderConstants {
        PHOTO, VIDEO
    }


    public FilesAdapter(HolderConstants holderConstants) {
        this.holderConstants = holderConstants;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
