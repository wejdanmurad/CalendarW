package com.example.calendarw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarw.R;
import com.example.calendarw.items.NoteItem;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    public List<NoteItem> mData = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
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

        private ImageButton imgB;
        private TextView tv_name, tv_subject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgB = itemView.findViewById(R.id.tb_edit);
            tv_name = itemView.findViewById(R.id.note_title);
            tv_subject = itemView.findViewById(R.id.note_subject);

            imgB.setOnClickListener(v -> {

            });

        }

        public void bind(int position) {
            NoteItem noteItem = mData.get(position);
            tv_name.setText(noteItem.getItemName());
            tv_subject.setText(noteItem.getItemSubject());
        }
    }
}
