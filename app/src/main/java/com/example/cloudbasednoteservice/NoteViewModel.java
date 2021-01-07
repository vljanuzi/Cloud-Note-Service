package com.example.cloudbasednoteservice;

import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoteViewModel extends ViewModel {

    private MutableLiveData<List<Note>> notes;
    List<Note> notesList = new LinkedList<>();
    public MutableLiveData<Integer> status;

    private NotesAPI notesAPI = retrofit();

    public NoteViewModel() {

        notes = new MutableLiveData<>();
        status = new MutableLiveData<>();
    }

    public LiveData<List<Note>> getNotesFromServer() {

        notesAPI.getAllNotes()
                .enqueue(new Callback<ListResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                        if (response.isSuccessful()) {
                            notesList = response.body().getNotes();
                            Comparator<Note> compareById = (Note o1, Note o2) -> Long.toString(o1.getDateInMillis()).compareTo( Long.toString(o2.getDateInMillis())  );
                            Collections.sort(notesList, compareById.reversed());
                            notes.setValue(notesList);
                        } else {
                            status.setValue(R.string.msg_notes_not_fetched);
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResponse> call, Throwable t) {
                        status.setValue(R.string.msg_server_error);
                    }
                });
        return notes;
    }

    public void sendNoteToServer(String content) {
        final Calendar today = Calendar.getInstance();
        long date = today.getTimeInMillis();

        notesAPI.createNote(new Note(content, date))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            notesList.add(new Note(content, date));
                            notes.setValue(notesList);
                            status.setValue(R.string.msg_note_created);
                        } else {
                            status.setValue(R.string.msg_note_not_created);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        status.setValue(R.string.msg_server_error);
                    }
                });

    }

    public void deleteNoteFromServer(String webKey) {

        notesAPI.deleteNote(webKey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            status.setValue(R.string.msg_note_deleted);
                        } else {
                            status.setValue(R.string.msg_note_not_deleted);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        status.setValue(R.string.msg_server_error);
                    }
                });

    }

    private NotesAPI retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NotesAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        notesAPI = retrofit.create(NotesAPI.class);
        return notesAPI;
    }
}
