package com.example.notesapplication.data;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {

    // get all notes from the server
    @GET("notes")
    Call<List<Note>> getNotes();

    // get a specific note from the server
    @GET("notes/{id}")
    Call<Note> getNoteById(@Path("id") int id);

    // add new note to the server
    @POST("notes")
    Call<Void> addNote(@Body Note note);

    // update the whole note
    @PUT("notes/{id}")
    Call<Void> updateNote(@Path("id") int id, @Body Note note);

    // update specific parts of the note
    @PATCH("notes/{id}")
    Call<Void> patchNote(@Path("id") int id, @Body Note note);

    // delete note by id
    @DELETE("notes/{id}")
    Call<Void> deleteNoteById(@Path("id") int id);


    class RetrofitInstance {
        private static Retrofit retrofit = null;

        public static ApiService getService() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://ca41b124809ebd951953.free.beeceptor.com/api/notes/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(ApiService.class);
        }
    }
}

