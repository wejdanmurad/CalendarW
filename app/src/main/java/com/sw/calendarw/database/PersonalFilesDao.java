package com.sw.calendarw.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sw.calendarw.items.PersonalFileItem;

import java.util.List;

@Dao
public interface PersonalFilesDao {
    @Query("SELECT * FROM personal_file_table")
    LiveData<List<PersonalFileItem>> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(PersonalFileItem item);

    @Query("SELECT COUNT(*) FROM personal_file_table")
    int getDataCount();

    @Query("SELECT * FROM personal_file_table WHERE itemPathNew = :searchString")
    PersonalFileItem getItem(String searchString);

    @Delete
    void  deleteItem(PersonalFileItem item);

}
