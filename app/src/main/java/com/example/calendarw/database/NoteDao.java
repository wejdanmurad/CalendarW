package com.example.calendarw.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.calendarw.items.NoteItem;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note_table")
    LiveData<List<NoteItem>> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(NoteItem item);

    @Query("SELECT COUNT(*) FROM note_table")
    int getDataCount();

//    @Query("SELECT * FROM personal_file_table WHERE itemPathNew = :searchString")
//    NoteItem getItem(String searchString);

    @Delete
    void deleteItem(NoteItem item);

}
