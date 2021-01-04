package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.mobilki.covidapp.api.repository.FilmGenresRepository;

public class EntertainmentSettingsActivity extends AppCompatActivity {

    NumberPicker filmDigit;
    NumberPicker bookDigit;
    Button applySettings;
    Spinner bookGenresSpinner;
    Spinner filmGenresSpinner;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    String bookGenre;
    String filmGenre;


    RadioGroup sortingGroup;
    RadioButton sortByValues;
    RadioButton sortByGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_settings);

        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        filmDigit = findViewById(R.id.filmDigit);
        bookDigit = findViewById(R.id.bookDigit);
        applySettings = findViewById(R.id.applyEntertainmentSettings);

        sortingGroup = findViewById(R.id.sortingGroup);

        sortByValues = findViewById(R.id.sortByValues);
        sortByGenres = findViewById(R.id.sortByGenres);

        //Book Spinner initialization
        bookGenresSpinner = findViewById(R.id.bookGenresSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.book_genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGenresSpinner.setAdapter(adapter);
        bookGenresSpinner.setSelection(adapter.getPosition(sharedPreferences.getString("bookGenre", "drama")));


        //Film Spinner initialization
        filmGenresSpinner = findViewById(R.id.filmGenresSpinner);
        String[] filmGenresList;
        filmGenresList = FilmGenresRepository.getGenres().values().toArray(new String[0]);
        ArrayAdapter<CharSequence> filmAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, filmGenresList
        );
        filmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filmGenresSpinner.setAdapter(filmAdapter);
        filmGenresSpinner.setSelection(filmAdapter.getPosition(sharedPreferences.getString("filmGenre", "Drama")));
        filmGenresSpinner.setEnabled(false);


        bookDigit.setMinValue(5);
        bookDigit.setMaxValue(10);
        filmDigit.setMinValue(5);
        filmDigit.setMaxValue(20);
        filmDigit.setValue(sharedPreferences.getInt("filmDigit", 10));
        bookDigit.setValue(sharedPreferences.getInt("bookDigit", 10));

        start();

    }

    private void start() {
        sortByValues.setOnCheckedChangeListener((compoundButton, b) -> {
            for (int i = 0; i < sortingGroup.getChildCount(); i++) {
                sortingGroup.getChildAt(i).setEnabled(compoundButton.isChecked());
            }

        });

        sortByGenres.setOnCheckedChangeListener((compoundButton, b) -> filmGenresSpinner.setEnabled(compoundButton.isChecked()));

        applySettings.setOnClickListener(view -> saveOptions());

        bookGenresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookGenre = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        filmGenresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filmGenre = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bookDigit.setOnValueChangedListener((numberPicker, i, i1) -> { });
        filmDigit.setOnValueChangedListener((numberPicker, i, i1) -> { });
    }


    private void saveOptions() {
        editor.putInt("bookDigit", bookDigit.getValue());
        editor.putInt("filmDigit", filmDigit.getValue());
        editor.putString("bookGenre", bookGenre);

        if (sortByValues.isChecked()) {
            if (sortingGroup.getCheckedRadioButtonId() != -1) {
                int id = sortingGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(id);
                editor.putString("filmSortingByValuesType", radioButton.getText().toString());
            }
        }

        if (sortByGenres.isChecked()) {
            editor.putString("filmSortingMethod", "sortByGenres");
            editor.putString("filmGenre", filmGenre);
        }
        else if (sortByValues.isChecked()) {
            editor.putString("filmSortingMethod", "sortByValues");
        }
        editor.apply();
    }
}