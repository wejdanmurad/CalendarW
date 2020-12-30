package com.example.calendarw.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class NoteItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int itemId;
    private String itemName;
    private String itemSubject;

    public NoteItem() {
    }

    public NoteItem(int itemId, String itemName, String itemSubject) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemSubject = itemSubject;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSubject() {
        return itemSubject;
    }

    public void setItemSubject(String itemSubject) {
        this.itemSubject = itemSubject;
    }
}
