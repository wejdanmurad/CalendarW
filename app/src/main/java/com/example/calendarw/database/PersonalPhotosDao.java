package com.example.calendarw.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.calendarw.items.PersonalPhotoItem;

import java.util.List;

@Dao
public interface PersonalPhotosDao {
    @Query("SELECT * FROM personal_photo_table")
    LiveData<List<PersonalPhotoItem>> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(PersonalPhotoItem item);

    @Query("SELECT COUNT(*) FROM personal_photo_table")
    int getDataCount();

    @Query("SELECT * FROM personal_photo_table WHERE imgPathNew = :searchString")
    PersonalPhotoItem getItem(String searchString);
}
