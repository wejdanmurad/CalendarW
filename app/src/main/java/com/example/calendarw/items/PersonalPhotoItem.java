package com.example.calendarw.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personal_photo_table")
public class PersonalPhotoItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int imageId;
    private String imgPathOld;
    private String imgPathNew;
    private String imgName;
    private String imgExt;
    private boolean isChecked;

    public PersonalPhotoItem(String imgPathOld, String imgName, String imgExt, boolean isChecked) {
        this.imgPathOld = imgPathOld;
        this.imgName = imgName;
        this.imgExt = imgExt;
        this.isChecked = isChecked;
    }

    public PersonalPhotoItem() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImgPathOld() {
        return imgPathOld;
    }

    public void setImgPathOld(String imgPathOld) {
        this.imgPathOld = imgPathOld;
    }

    public String getImgPathNew() {
        return imgPathNew;
    }

    public void setImgPathNew(String imgPathNew) {
        this.imgPathNew = imgPathNew;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgExt() {
        return imgExt;
    }

    public void setImgExt(String imgExt) {
        this.imgExt = imgExt;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
