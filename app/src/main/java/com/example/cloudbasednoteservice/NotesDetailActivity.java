package com.example.cloudbasednoteservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotesDetailActivity extends AppCompatActivity {

    String content;

    private static final String EXTRA_CONTENT = "content";
    private static final String EXTRA_DATE = "date";
    private static final String EXTRA_KEY = "key";

    public static Intent getIntent(Context context, String content, Date date, String key) {
        Intent intent = new Intent(context, NotesDetailActivity.class);
        intent.putExtra(EXTRA_CONTENT, content);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateString = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        intent.putExtra(EXTRA_DATE, dateString);
        intent.putExtra(EXTRA_KEY, key);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        final NoteViewModel noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        Intent intent = getIntent();
        content = intent.getStringExtra(EXTRA_CONTENT);
        String date = intent.getStringExtra(EXTRA_DATE);
        String key = intent.getStringExtra(EXTRA_KEY);

        TextView tvContent = findViewById(R.id.tv_content);
        TextView tvDate = findViewById(R.id.tv_date);

        tvContent.setText("Content: " + content);
        tvDate.setText("Date: " + date);

        findViewById(R.id.btn_delete).setOnClickListener(v -> noteViewModel.deleteNoteFromServer(key));

        noteViewModel.status.observe(this, toast_message -> {
            Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();
        });
    }
}