package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilki.covidapp.api.*;

public class EntertainmentActivity extends AppCompatActivity {

    Button notificationsSettingsBtn;
    Button preferencesBtn;

    LinearLayout filmsLayout;
    LinearLayout booksLayout;
    LinearLayout gamesLayout;
    LayoutInflater inflater;

    ImageButton mainFilmPhoto;
    ImageButton mainBookPhoto;
    ImageButton mainGamePhoto;
    TextView filmTitleTxt;

    private FilmDatabaseApi imdbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);

        notificationsSettingsBtn = findViewById(R.id.entertainmentNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.entertainmentPreferencesBtn);

        filmsLayout = findViewById(R.id.filmsLinearLayout);
        booksLayout = findViewById(R.id.booksLinearLayout);
        gamesLayout = findViewById(R.id.gamesLinearLayout);

        inflater = LayoutInflater.from(this);

        for (int i = 0; i < 10; i++) {
            View anotherLayout = inflater.inflate(R.layout.film_overview, null, true);
            filmsLayout.addView(anotherLayout);
        }

        mainFilmPhoto = findViewById(R.id.mainFilmPhoto);
        mainBookPhoto = findViewById(R.id.mainBookPhoto);
        mainGamePhoto = findViewById(R.id.mainGamePhoto);

        filmTitleTxt = findViewById(R.id.filmTitleTxt);


        imdbApi = new ImdbApi();


        for (int i = 0; i < 10; i++) {
//            mainFilmPhoto.setId(R.id.mainFilmPhoto + i);
//            mainBookPhoto.setId(R.id.mainBookPhoto + i);
//            mainGamePhoto.setId(R.id.mainGamePhoto + i);

            filmTitleTxt.setId(R.id.filmTitleTxt + i);
        }



        start();
    }

    private void start() {
        notificationsSettingsBtn.setOnClickListener(view -> {
                imdbApi.fetchOverviewData("tt0133093");
        });
        preferencesBtn.setOnClickListener(view -> {
            TextView tmp = null;
            for (int i = 9; i >= 0; i--)
                tmp = findViewById(R.id.filmTitleTxt - i);
                tmp.setText(imdbApi.getFilms().get(0).getGenres().toString());
        });
    }
}