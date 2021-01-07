package com.example.cloudbasednoteservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView lvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvResults = findViewById(R.id.lv_results);

        findViewById(R.id.btn_insert).setOnClickListener(v -> {
            Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(addNoteIntent);
        });

    }
    protected void onResume(){
        super.onResume();

        NoteViewModel noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteViewModel.getNotesFromServer().observe(this, notes -> {
            //lvResults.setAdapter(new ArrayAdapter<Note>(MainActivity.this, android.R.layout.simple_list_item_1, notes1));
            lvResults.setAdapter(new NoteAdapter(this, notes));
            lvResults.setVisibility(View.VISIBLE);

            lvResults.setOnItemClickListener((parent, view, position, id) -> {
                if (notes != null) {
                    Note note = notes.get(position);
                    startActivity(NotesDetailActivity.getIntent(MainActivity.this, note.getContent(), new Date(note.getDateInMillis()), note.getWebsafeKey()));
                }
            });
        });

        noteViewModel.status.observe(this, toast_message -> {
            Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();
        });
    }

}