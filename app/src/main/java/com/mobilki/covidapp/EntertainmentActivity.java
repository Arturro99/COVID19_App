package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mobilki.covidapp.api.*;

import org.json.JSONException;

import java.util.concurrent.atomic.AtomicBoolean;

public class EntertainmentActivity extends AppCompatActivity {

    Button notificationsSettingsBtn;
    Button preferencesBtn;
    TextView firstMovieTxt;

    private FilmDatabaseApi imdbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);

        notificationsSettingsBtn = findViewById(R.id.entertainmentNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.entertainmentPreferencesBtn);
        firstMovieTxt = findViewById(R.id.firstMovieTextView);

        imdbApi = new ImdbApi();

        start();
    }

    private void start() {
        notificationsSettingsBtn.setOnClickListener(view -> {
            try {
                imdbApi.getTitle("title/get-details", "tt0133093");
                firstMovieTxt.setText(imdbApi.getFilms().get(0).getTitle());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        preferencesBtn.setOnClickListener(view -> {
//            Log.println(Log.WARN, "ss", Result.kupa);
        });
    }
}