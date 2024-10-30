package com.example.notesapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.OnConflictStrategy;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    // insert a new note into the local database
    @Insert
    void insert(Note note);

    // insert a list of notes into the local database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Note> notes);

    // update note by id in the local database
    @Query("UPDATE notes SET title = :title, content = :content WHERE id = :id")
    void update(int id, String title, String content);

    // delete note by id from the local database
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteById(int noteId);

    // get all notes from the local database
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAllNotes();
}

