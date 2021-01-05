package com.mobilki.covidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.api.repository.FilmGenresRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EntertainmentSettingsActivity extends AppCompatActivity {

    NumberPicker filmDigit;
    NumberPicker bookDigit;
    Button applySettings;
    Spinner bookGenresSpinner;
    Spinner filmGenresSpinner;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    String bookGenre;
    String filmGenre;


    RadioGroup sortingGroup;
    RadioButton sortByValues;
    RadioButton sortByGenres;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        sortingGroup = findViewById(R.id.sortingGroup);
        sortByValues = findViewById(R.id.sortByValues);
        sortByGenres = findViewById(R.id.sortByGenres);

        //Book Spinner initialization
        bookGenresSpinner = findViewById(R.id.bookGenresSpinner);
        ArrayAdapter<CharSequence> bookAdapter = ArrayAdapter.createFromResource(
                this, R.array.book_genres, android.R.layout.simple_spinner_item);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGenresSpinner.setAdapter(bookAdapter);
        //bookGenresSpinner.setSelection(adapter.getPosition(collectionReference.document("filters").get().toString()));
        collectionReference.document("filters").addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null)
                bookGenresSpinner.setSelection(bookAdapter.getPosition(documentSnapshot.getString("bookGenre")));
            else
                Log.d("TAG", "NULL in setter");
        });


        //Film Spinner initialization
        filmGenresSpinner = findViewById(R.id.filmGenresSpinner);
        String[] filmGenresList;
        filmGenresList = FilmGenresRepository.getGenres().values().toArray(new String[0]);
        ArrayAdapter<CharSequence> filmAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, filmGenresList
        );
        filmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filmGenresSpinner.setAdapter(filmAdapter);
        filmGenresSpinner.setEnabled(false);


        bookDigit.setMinValue(5);
        bookDigit.setMaxValue(10);
        filmDigit.setMinValue(5);
        filmDigit.setMaxValue(20);

        collectionReference.document("filters").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                filmGenresSpinner.setSelection(filmAdapter.getPosition(Optional.ofNullable(documentSnapshot.getString("filmGenre")).orElse("War")));
                bookGenresSpinner.setSelection(bookAdapter.getPosition(Optional.ofNullable(documentSnapshot.getString("bookGenre")).orElse("drama")));
                filmDigit.setValue(Optional.ofNullable(documentSnapshot.getLong("filmDigit")).orElse(10L).intValue());
                bookDigit.setValue(Optional.ofNullable(documentSnapshot.getLong("bookDigit")).orElse(10L).intValue());
            }
            else
                Log.d("TAG", "NULL in setter");
        });

        start();
    }

    private void start() {
        sortByValues.setOnCheckedChangeListener((compoundButton, b) -> {
            for (int i = 0; i < sortingGroup.getChildCount(); i++) {
                sortingGroup.getChildAt(i).setEnabled(compoundButton.isChecked());
            }

        });

        sortByGenres.setOnCheckedChangeListener((compoundButton, b) -> filmGenresSpinner.setEnabled(compoundButton.isChecked()));

        applySettings.setOnClickListener(view -> {
            saveOptions();
            startActivity(new Intent(this, EntertainmentActivity.class));
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
        DocumentReference documentReference = collectionReference.document("filters");
        Map<String, Object> settings = new HashMap<>();
        settings.put("bookGenre", bookGenre);
        settings.put("filmGenre", filmGenre);
        settings.put("bookDigit", bookDigit.getValue());
        settings.put("filmDigit", filmDigit.getValue());
        if (sortByValues.isChecked()) {
            settings.put("filmSortingMethod", "sortByValues");
            if (sortingGroup.getCheckedRadioButtonId() != -1) {
                int id = sortingGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(id);
                settings.put("filmSortingByValuesType", radioButton.getText().toString());
            }
        }
        else if (sortByGenres.isChecked()){
            settings.put("filmSortingMethod", "sortByGenres");
            settings.put("filmGenre", filmGenre);
        }

        documentReference.set(settings).addOnSuccessListener(x -> Toast.makeText(this, "Settings successfully saved", Toast.LENGTH_SHORT).show());
    }
}