package com.example.calendarw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calendarw.R;
import com.example.calendarw.items.PersonalFileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    private final HolderConstants holderConstants;
    private OnItemClickListener mListener;
    Context context;
    public List<PersonalFileItem> mData = new ArrayList<>();
    public boolean longClicked = false;

    public AudioAdapter(HolderConstants holderConstants) {
        this.holderConstants = holderConstants;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, singer;
        private CheckBox checkBox;
        private View view_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.audio_name);
            singer = itemView.findViewById(R.id.audio_singer);
            checkBox = itemView.findViewById(R.id.check_box);
            view_item = itemView.findViewById(R.id.view_item);
            name.setSelected(true);

            if (holderConstants == HolderConstants.H_Audio) {
                checkBox.setVisibility(View.INVISIBLE);

                if (longClicked)
                    checkBox.setVisibility(View.VISIBLE);

                view_item.setOnLongClickListener(v -> {
                    longClicked = true;
                    selectItem();
                    mListener.onItemClick();
                    return false;
                });

                view_item.setOnClickListener(v -> {
                    if (longClicked)
                        selectItem();
                });

                checkBox.setOnClickListener(v -> {
                    selectItem();
                });
            } else if (holderConstants == HolderConstants.P_Audio) {

                checkBox.setVisibility(View.VISIBLE);

                view_item.setOnClickListener(v -> {
                    selectItem();
                });

                checkBox.setOnClickListener(v -> {
                    selectItem();
                });
            }

        }

        public void bind(int position) {
            PersonalFileItem item = mData.get(position);
            File itemFile;
            if (holderConstants == HolderConstants.H_Audio)
                itemFile = new File(item.getItemPathNew());
            else
                itemFile = new File(item.getItemPathOld());
            if (itemFile.exists()) {
                name.setText(item.getItemName());
                singer.setText(item.getItemArtist());
            }
            checkBox.setChecked(item.isChecked());
        }

        private void selectItem() {
            int position = getAdapterPosition();
            PersonalFileItem photoItem = mData.get(position);
            photoItem.setChecked(!photoItem.isChecked());
            notifyItemChanged(position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    public enum HolderConstants {
        H_Audio, P_Audio
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

}
