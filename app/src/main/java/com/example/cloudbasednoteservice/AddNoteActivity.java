package com.example.cloudbasednoteservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNoteActivity extends AppCompatActivity {

    private NotesAPI notesApi;
    final Calendar today = Calendar.getInstance();
    EditText txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        final NoteViewModel noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        txtDate = findViewById(R.id.txt_date);
        final int yearNow = today.get(Calendar.YEAR);
        final int monthNow = today.get(Calendar.MONTH);
        final int dayNow = today.get(Calendar.DAY_OF_MONTH);
        txtDate.setText(dayNow + "-" + (monthNow + 1) + "-" + yearNow);

        txtDate.setOnClickListener(v -> {

            final DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        if (year != yearNow || monthOfYear != monthNow || dayOfMonth != dayNow) {
                            year = yearNow;
                            monthOfYear = monthNow;
                            dayOfMonth = dayNow;
                            Toast.makeText(this, R.string.todayDate, Toast.LENGTH_SHORT).show();
                        }
                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        findViewById(R.id.btn_insert).setOnClickListener(v -> {
            String content = ((EditText) findViewById(R.id.txt_content)).getText().toString();
            if(!content.isEmpty()) {
                noteViewModel.sendNoteToServer(content);
            } else {
                Toast.makeText(this, R.string.content_not_inserted, Toast.LENGTH_SHORT).show();
            }

        });

        noteViewModel.status.observe(this, toast_message -> {
            Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();
        });
    }


}