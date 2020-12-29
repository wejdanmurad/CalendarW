package com.example.calendarw.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personal_file_table")
public class PersonalFileItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int itemId;
    private String itemPathOld;
    private String itemPathNew;
    private String itemName;
    private String itemExt;
    private boolean isChecked;
    private String itemArtist;

    public PersonalFileItem(String imgPathOld, String imgName, String imgExt, boolean isChecked) {
        this.itemPathOld = imgPathOld;
        this.itemName = imgName;
        this.itemExt = imgExt;
        this.isChecked = isChecked;
    }

    public PersonalFileItem() {
    }

    public PersonalFileItem(String imgPathOld, String imgName, String artist, String imgExt, boolean isChecked) {
        this.itemPathOld = imgPathOld;
        this.itemName = imgName;
        this.itemExt = imgExt;
        this.isChecked = isChecked;
        this.itemArtist = artist;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemPathOld() {
        return itemPathOld;
    }

    public void setItemPathOld(String itemPathOld) {
        this.itemPathOld = itemPathOld;
    }

    public String getItemPathNew() {
        return itemPathNew;
    }

    public void setItemPathNew(String itemPathNew) {
        this.itemPathNew = itemPathNew;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemExt() {
        return itemExt;
    }

    public void setItemExt(String itemExt) {
        this.itemExt = itemExt;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getItemArtist() {
        return itemArtist;
    }

    public void setItemArtist(String itemArtist) {
        this.itemArtist = itemArtist;
    }
}
