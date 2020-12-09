package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EntertainmentSettingsActivity extends AppCompatActivity {

    LinearLayout filmsNumberLayout;
    LayoutInflater inflater;
    TextView filmDigit;
    TextView bookDigit;
    Button applySettings;
    Spinner bookGenresSpinner;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    String bookGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_settings);

        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



//        filmsNumberLayout = findViewById(R.id.filmsNumberLayout);
//        inflater = LayoutInflater.from(this);

//        filmDigit = findViewById(R.id.digit);
//
//        for (int i = 0; i < 20; i++) {
//            View filmNumber = inflater.inflate(R.layout.digit, null, true);
//            filmsNumberLayout.addView(filmNumber);
//        }

        filmDigit = findViewById(R.id.filmDigit);
        bookDigit = findViewById(R.id.bookDigit);
        applySettings = findViewById(R.id.applyEntertainmentSettings);


        bookGenresSpinner = findViewById(R.id.bookGenresSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.book_genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGenresSpinner.setAdapter(adapter);
        bookGenresSpinner.setSelection(adapter.getPosition(sharedPreferences.getString("bookGenre", "drama")));

        start();

    }

    private void start() {
        applySettings.setOnClickListener(view -> {
            saveOptions();
            System.out.println("SHARED BDIGIT: " + sharedPreferences.getInt("bookDigit", 10));
        });

        bookGenresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookGenre = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void saveOptions() {
        editor.putInt("bookDigit", Integer.parseInt(bookDigit.getText().toString()));
        editor.putInt("filmDigit", Integer.parseInt(filmDigit.getText().toString()));
        editor.putString("bookGenre", bookGenre);

        editor.apply();
    }
}