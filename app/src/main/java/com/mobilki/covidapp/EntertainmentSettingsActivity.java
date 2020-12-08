package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EntertainmentSettingsActivity extends AppCompatActivity {

    LinearLayout filmsNumberLayout;
    LayoutInflater inflater;
    TextView filmDigit;
    TextView bookDigit;
    Spinner bookGenresSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_settings);

//        filmsNumberLayout = findViewById(R.id.filmsNumberLayout);
//        inflater = LayoutInflater.from(this);

//        filmDigit = findViewById(R.id.digit);
//
//        for (int i = 0; i < 20; i++) {
//            View filmNumber = inflater.inflate(R.layout.digit, null, true);
//            filmsNumberLayout.addView(filmNumber);
//        }

        bookGenresSpinner = findViewById(R.id.bookGenresSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.book_genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGenresSpinner.setAdapter(adapter);

    }
}