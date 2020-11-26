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
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
    TextView []filmReleaseYearList = new TextView[10];

    private FilmDatabaseApi imdbApi;
    private List synchedList = Collections.synchronizedList(new LinkedList<>());

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

        int filmTitleInitiateId = 1000;
        int filmReleaseYearInitiateId = 2000;
        int filmPhotoInitiateId = 2000;

        for (int i = 0; i < 10; i++) {
            View anotherLayout = inflater.inflate(R.layout.film_overview, null, true);
            filmsLayout.addView(anotherLayout);
            filmTitleTxtList[i] = findViewById(R.id.filmTitleTxt);
            //filmPhotosList[i] = findViewById(R.id.mainFilmPhoto);
//            filmDirectorList[i] = findViewById(R.id.filmDirector);
            filmReleaseYearList[i] = findViewById(R.id.filmReleaseYear);

            filmTitleTxtList[i].setId(filmTitleInitiateId + i);
            //filmPhotosList[i].setId(R.id.mainFilmPhoto + i);
//            filmDirectorList[i].setId(R.id.filmDirector + i + 1);
            filmReleaseYearList[i].setId(filmReleaseYearInitiateId + i);
            //
        }

        imdbApi = new ImdbApi();

        start();
    }

    private void start() {
        imdbApi.getGenres();
        imdbApi.getTopRatedOrPopularFilms(false);
        notificationsSettingsBtn.setOnClickListener(view -> {
//            ExecutorService executorService = Executors.newFixedThreadPool(10);
//            executorService.submit((Runnable) imdbApi);
//                //imdbApi.fetchOverviewData(imdbApi.getFilms());
//            executorService.shutdown();
        });
        preferencesBtn.setOnClickListener(view -> {
            for (int i = 0; i < 10; i++) {
                filmTitleTxtList[i].setText(imdbApi.getFilms().get(i).getTitle());
                filmReleaseYearList[i].setText(String.valueOf(imdbApi.getFilms().get(i).getDateOfRelease()));
                //filmPhotosList[i].setImageResource(Uri.parse(imdbApi.getFilms().get(i).getImageUrl()));
                //Picasso.get().load(imdbApi.getFilms().get(i).getImageUrl()).into(filmPhotosList[i]);
                //filmDirectorList[i].setText(Uri.parse(`imdbApi.getFilms().get(i).get()));
            }
        });
    }
}