package com.example.notesapplication;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.notesapplication.data.Note;
import com.example.notesapplication.data.NoteRepository;


public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;
    private final ExecutorService executorService;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void addNote(Note note) {
        executorService.execute(() -> {
            noteRepository.addNote(note);
            refreshNotes();
        });
    }

    public void updateNote(Note note) {
        executorService.execute(() -> {
            noteRepository.updateNote(note);
            refreshNotes();
        });
    }

    public void deleteNote(int noteId) {
        executorService.execute(() -> {
            noteRepository.deleteNote(noteId);
            refreshNotes();
        });
    }

    public void refreshNotes() {
        executorService.execute(() -> {
            allNotes = noteRepository.getAllNotes();
        });
    }
}

