package com.mobilki.covidapp.entertainment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntertainmentSettingsActivity extends AppCompatActivity {

    NumberPicker filmDigit;
    NumberPicker bookDigit;
    Button applySettings;
    Spinner bookGenresSpinner;
    Spinner filmGenresSpinner;
    Spinner filmOtherSpinner;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    String bookGenre;
    String filmGenre;
    String filmOtherFilters;


    RadioButton sortByValues;
    RadioButton sortByGenres;

    SharedPreferences settings;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_settings);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        collectionReference = mFirestore
                .collection("users").document(mUser.getUid())
                .collection("settings");


        filmDigit = findViewById(R.id.filmDigit);
        bookDigit = findViewById(R.id.bookDigit);
        applySettings = findViewById(R.id.applyEntertainmentSettings);
        sortByValues = findViewById(R.id.sortByValues);
        sortByGenres = findViewById(R.id.sortByGenres);

        //Book Genres Spinner initialization
        bookGenresSpinner = findViewById(R.id.bookGenresSpinner);
        ArrayAdapter<CharSequence> bookAdapter = ArrayAdapter.createFromResource(
                this, R.array.book_genres, android.R.layout.simple_spinner_item);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGenresSpinner.setAdapter(bookAdapter);


        //Film Genres Spinner initialization
        filmGenresSpinner = findViewById(R.id.filmGenresSpinner);
        ArrayAdapter<CharSequence> filmAdapter = ArrayAdapter.createFromResource(
                this, R.array.film_genres, android.R.layout.simple_spinner_item);
        filmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filmGenresSpinner.setAdapter(filmAdapter);
        filmGenresSpinner.setEnabled(false);

        //Film other initialization
        filmOtherSpinner = findViewById(R.id.filmOtherSpinner);
        ArrayAdapter<CharSequence> filmOtherAdapter = ArrayAdapter.createFromResource(
                this, R.array.film_other, android.R.layout.simple_spinner_item);
        filmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filmOtherSpinner.setAdapter(filmOtherAdapter);
        filmOtherSpinner.setEnabled(true);

        bookDigit.setMinValue(5);
        bookDigit.setMaxValue(10);
        filmDigit.setMinValue(5);
        filmDigit.setMaxValue(20);

        collectionReference.document("filters").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                filmGenresSpinner.setSelection(filmAdapter.getPosition(Optional.ofNullable(documentSnapshot.getString("filmGenre")).orElse("War")));
                filmOtherSpinner.setSelection(filmOtherAdapter.getPosition(Optional.ofNullable(documentSnapshot.getString("filmSortingByValuesType")).orElse("Most popular")));
                bookGenresSpinner.setSelection(bookAdapter.getPosition(Optional.ofNullable(documentSnapshot.getString("bookGenre")).orElse("drama")));
                filmDigit.setValue(Optional.ofNullable(documentSnapshot.getLong("filmDigit")).orElse(10L).intValue());
                bookDigit.setValue(Optional.ofNullable(documentSnapshot.getLong("bookDigit")).orElse(10L).intValue());
                sortByGenres.setChecked(documentSnapshot.getString("filmSortingMethod").equals("sortByGenres"));
                sortByValues.setChecked(documentSnapshot.getString("filmSortingMethod").equals("sortByValues"));
                filmGenresSpinner.setEnabled(sortByGenres.isChecked());
                filmOtherSpinner.setEnabled(sortByValues.isChecked());
            }
            else
                Log.d("TAG", "NULL in setter");
        });

        start();
    }

    private void start() {
        sortByGenres.setOnCheckedChangeListener((compoundButton, b) -> filmGenresSpinner.setEnabled(compoundButton.isChecked()));
        sortByValues.setOnCheckedChangeListener((compoundButton, b) -> filmOtherSpinner.setEnabled(compoundButton.isChecked()));

        applySettings.setOnClickListener(view -> {
            saveOptions();
            Intent intent = new Intent(this, EntertainmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        bookGenresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case("Dramat"): {
                        bookGenre = "drama";
                        break;
                    }
                    case("Fikcja"): {
                        bookGenre = "fiction";
                        break;
                    }
                    case("Historia"): {
                        bookGenre = "history";
                        break;
                    }
                    case("Sztuka"): {
                        bookGenre = "art";
                        break;
                    }
                    case("Psychologia"): {
                        bookGenre = "psychology";
                        break;
                    }
                    case("Medycyna"): {
                        bookGenre = "medical";
                        break;
                    }
                    case("Biografia"): {
                        bookGenre = "biography";
                        break;
                    }
                    case("Poezja"): {
                        bookGenre = "poetry";
                        break;
                    }
                    default: {
                        bookGenre = adapterView.getItemAtPosition(i).toString();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        filmGenresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case ("Akcja"): {
                        filmGenre = "Action";
                        break;
                    }
                    case ("Animacja"): {
                        filmGenre = "Animation";
                        break;
                    }
                    case ("Komedia"): {
                        filmGenre = "Comedy";
                        break;
                    }
                    case ("Kryminał"): {
                        filmGenre = "Crime";
                        break;
                    }
                    case ("Dokument"): {
                        filmGenre = "Documentary";
                        break;
                    }
                    case ("Dramat"): {
                        filmGenre = "Drama";
                        break;
                    }
                    case ("Fantastyczny"): {
                        filmGenre = "Fantasy";
                        break;
                    }
                    case ("Familijny"): {
                        filmGenre = "Family";
                        break;
                    }
                    case ("Historyczny"): {
                        filmGenre = "History";
                        break;
                    }
                    case ("Horror"): {
                        filmGenre = "Horror";
                        break;
                    }
                    case ("Muzyczny"): {
                        filmGenre = "Music";
                        break;
                    }
                    case ("Detektywistyczny"): {
                        filmGenre = "Mystery";
                        break;
                    }
                    case ("Romans"): {
                        filmGenre = "Romance";
                        break;
                    }
                    case ("Science fiction"): {
                        filmGenre = "Science Fiction";
                        break;
                    }
                    case ("Dreszczowiec"): {
                        filmGenre = "Thriller";
                        break;
                    }
                    case ("Film TV"): {
                        filmGenre = "TV Movie";
                        break;
                    }
                    case ("Wojenny"): {
                        filmGenre = "War";
                        break;
                    }
                    case ("Western"): {
                        filmGenre = "Western";
                        break;
                    }
                    default: {
                        filmGenre = adapterView.getItemAtPosition(i).toString();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        filmOtherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case ("Najwyżej oceniane"): {
                        filmOtherFilters = "Top rated";
                        break;
                    }
                    case ("Najbardziej popularne"): {
                        filmOtherFilters = "Most popular";
                        break;
                    }
                    case ("Nadchodzące"): {
                        filmOtherFilters = "Upcoming";
                        break;
                    }
                    case ("Teraz grane"): {
                        filmOtherFilters = "Now playing";
                        break;
                    }
                    default: {
                        filmOtherFilters = adapterView.getItemAtPosition(i).toString();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        bookDigit.setOnValueChangedListener((numberPicker, i, i1) -> { });
        filmDigit.setOnValueChangedListener((numberPicker, i, i1) -> { });
    }


    private void saveOptions() {
        DocumentReference documentReference = collectionReference.document("filters");
        Map<String, Object> settings = new HashMap<>();
        settings.put("bookGenre", bookGenre);
        settings.put("filmGenre", filmGenre);
        settings.put("bookDigit", bookDigit.getValue());
        settings.put("filmDigit", filmDigit.getValue());
        if (sortByValues.isChecked()) {
            settings.put("filmSortingMethod", "sortByValues");
            settings.put("filmSortingByValuesType", filmOtherFilters);
        }
        else if (sortByGenres.isChecked()){
            settings.put("filmSortingMethod", "sortByGenres");
            settings.put("filmGenre", filmGenre);
        }

        documentReference.set(settings).addOnSuccessListener(x -> Toast.makeText(this, "Settings successfully saved", Toast.LENGTH_SHORT).show());
    }
}