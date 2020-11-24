package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilki.covidapp.api.*;

import java.net.URI;
import java.util.List;

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

    TextView []filmTitleTxtList = new TextView[10];
    ImageButton []filmPhotosList = new ImageButton[10];
    TextView []filmDirectorList = new TextView[10];
    TextView []filmReleaseYear = new TextView[10];

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

        int titleInitiateId = 1000;
        int releaseYearInitiateId = 2000;

        for (int i = 0; i < 10; i++) {
            View anotherLayout = inflater.inflate(R.layout.film_overview, null, true);
            filmsLayout.addView(anotherLayout);
            filmTitleTxtList[i] = findViewById(R.id.filmTitleTxt);
//            filmPhotosList[i] = findViewById(R.id.mainFilmPhoto);
//            filmDirectorList[i] = findViewById(R.id.filmDirector);
            filmReleaseYear[i] = findViewById(R.id.filmReleaseYear);

            filmTitleTxtList[i].setId(titleInitiateId + i);
//            filmPhotosList[i].setId(R.id.mainFilmPhoto + i + 1);
//            filmDirectorList[i].setId(R.id.filmDirector + i + 1);
            filmReleaseYear[i].setId(releaseYearInitiateId + i);
            //
        }

        imdbApi = new ImdbApi();

        start();
    }

    private void start() {
        imdbApi.getMostPopularFilms();
        notificationsSettingsBtn.setOnClickListener(view -> {
                imdbApi.fetchOverviewData(imdbApi.getFilms());
        });
        preferencesBtn.setOnClickListener(view -> {
            for (int i = 0; i < 10; i++) {
                filmTitleTxtList[i].setText(imdbApi.getFilms().get(i).getTitle());
                filmReleaseYear[i].setText(String.valueOf(imdbApi.getFilms().get(i).getYearOfRelease()));
                //filmPhotosList[i].setImageURI(Uri.parse(imdbApi.getFilms().get(i).getImageUrl()));
                //filmDirectorList[i].setText(Uri.parse(imdbApi.getFilms().get(i).get()));
            }
        });
    }
}