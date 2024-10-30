package com.example.notesapplication.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.lifecycle.LiveData;
import android.util.Log;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private final NoteDao noteDao;
    private final ApiService apiService;
    private final ExecutorService executorService;
    private final Context context;

    // init DAO and API
    public NoteRepository(Context context) {
        NoteDatabase database = NoteDatabase.getInstance(context);
        noteDao = database.noteDao();
        apiService = ApiService.RetrofitInstance.getService();
        executorService = Executors.newFixedThreadPool(2);
        this.context = context.getApplicationContext();
    }

    // checks online connection
    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // get all notes from the local database
    public LiveData<List<Note>> getAllNotes() {
        Log.e("NoteRepository", "got to allNotes");
        return noteDao.getAllNotes();
    }

    // adds note to the local database and server if online
    public void addNote(final Note note) {
        executorService.execute(() -> {
            noteDao.insert(note); // local
        });

        // add to server - if online
        if (isOnline()) {
            apiService.addNote(note).enqueue(new Callback<Void>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("NoteRepository", "Note added with server successfully");
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("NoteRepository", "Failed to add note with server", t);
                }
            });
        }
    }

    // updates note in the local database and server if online, by ID
    public void updateNote(final Note note) {
        executorService.execute(() -> {
            noteDao.update(note.getId(), note.getTitle(), note.getContent());
        });

        if (isOnline()) {
            apiService.updateNote(note.getId(), note).enqueue(new Callback<Void>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("NoteRepository", "Note updated in server successfully");
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("NoteRepository", "Failed to update note with server", t);
                }
            });
        }
    }

    // deletes note from the local database and server if online, by ID
    public void deleteNote(final int noteId) {
        executorService.execute(() -> {
            noteDao.deleteById(noteId);
        });

        if (isOnline()) {
            apiService.deleteNoteById(noteId).enqueue(new Callback<Void>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("NoteRepository", "Note deleted from server successfully");
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("NoteRepository", "Failed to delete note from server", t);
                }
            });
        }
    }

    // syncs notes from the server to the local database
    public void syncNotes() {
        if (isOnline()) {
            apiService.getNotes().enqueue(new Callback<List<Note>>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        executorService.execute(() -> {
                            noteDao.insertAll(response.body());
                        });
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<List<Note>> call, Throwable t) {
                    Log.e("NoteRepository", "Failed to sync note with localDB", t);
                }
            });
        }
    }
}


