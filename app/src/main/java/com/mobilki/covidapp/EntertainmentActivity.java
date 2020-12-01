package com.mobilki.covidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilki.covidapp.api.*;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class EntertainmentActivity extends AppCompatActivity {

    Button notificationsSettingsBtn;
    Button preferencesBtn;

    LinearLayout filmsLayout;
    LinearLayout booksLayout;
    LinearLayout gamesLayout;
    LayoutInflater inflater;


    TextView []filmTitleList = new TextView[10];
    TextView []filmGenresList = new TextView[10];

    ImageButton []filmPhotosList = new ImageButton[10];

    TextView []filmDirectorList = new TextView[10];
    TextView []filmDirectorTxtList = new TextView[10];

    TextView []filmReleaseYearList = new TextView[10];
    TextView []filmReleaseYearTxtList = new TextView[10];

    TextView []filmDurationList = new TextView[10];
    TextView []filmDurationTxtList = new TextView[10];

    TextView []filmRatingList = new TextView[10];
    TextView []filmRatingTxtList = new TextView[10];


    ConstraintLayout []filmConstraintLayoutList = new ConstraintLayout[10];

    private FilmDatabaseApi imdbApi;
    private List synchedList = Collections.synchronizedList(new LinkedList<>());

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        int filmReleaseYearTxtInitiateId = 2500;

        int filmPhotoInitiateId = 3000;

        int filmDirectorInitiateId = 4000;
        int filmDirectorTxtInitiateId = 4500;

        int filmDurationInitiateId = 5000;
        int filmDurationTxtInitiateId = 5500;

        int filmRatingInitiateId = 6000;
        int filmRatingTxtInitiateId = 6500;

        int filmGenresInitiateId = 7000;

        int filmConstraintLayoutInitiateId = 10000;

        ConstraintLayout constraintLayout;



        for (int i = 0; i < 10; i++) {
            View anotherLayout = inflater.inflate(R.layout.film_overview, null, true);
            filmsLayout.addView(anotherLayout);

            filmConstraintLayoutList[i] = findViewById(R.id.movieConstraintLayout);
            filmTitleList[i] = findViewById(R.id.filmTitle);
            filmPhotosList[i] = findViewById(R.id.mainFilmPhoto);
            filmGenresList[i] = findViewById(R.id.filmGenres);

            filmDirectorList[i] = findViewById(R.id.filmDirector);
            filmDirectorTxtList[i] = findViewById(R.id.filmDirectorTxt);

            filmRatingList[i] = findViewById(R.id.filmRating);
            filmRatingTxtList[i] = findViewById(R.id.filmRatingTxt);

            filmReleaseYearList[i] = findViewById(R.id.filmReleaseDate);
            filmReleaseYearTxtList[i] = findViewById(R.id.filmReleaseDateTxt);

            filmDurationList[i] = findViewById(R.id.filmDuration);
            filmDurationTxtList[i] = findViewById(R.id.filmDurationTxt);


            filmConstraintLayoutList[i].setId(filmConstraintLayoutInitiateId + i);
            filmTitleList[i].setId(filmTitleInitiateId + i);
            filmPhotosList[i].setId(filmPhotoInitiateId + i);
            filmGenresList[i].setId(filmGenresInitiateId + i);

            filmReleaseYearList[i].setId(filmReleaseYearInitiateId + i);
            filmReleaseYearTxtList[i].setId(filmReleaseYearTxtInitiateId + i);

            filmDirectorList[i].setId(filmDirectorInitiateId + i);
            filmDirectorTxtList[i].setId(filmDirectorTxtInitiateId + i);

            filmDurationList[i].setId(filmDurationInitiateId + i);
            filmDurationTxtList[i].setId(filmDurationTxtInitiateId + i);

            filmRatingList[i].setId(filmRatingInitiateId + i);
            filmRatingTxtList[i].setId(filmRatingTxtInitiateId + i);

            constraintLayout = findViewById(filmConstraintLayoutInitiateId + i);
            constraintLayout.setMinWidth(1200);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            //IMAGE
            constraintSet.connect(filmPhotoInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.START);
            constraintSet.connect(filmPhotoInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);

            //TITLE
            constraintSet.connect(filmTitleInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmTitleInitiateId + i, ConstraintSet.END, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmTitleInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.START);
            constraintSet.connect(filmTitleInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.BOTTOM);


            //DIRECTOR TXT
            constraintSet.connect(filmDirectorTxtInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmDirectorTxtInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDirectorTxtInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDirectorTxtInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmDirectorTxtInitiateId + i, 0.05f);

            //DIRECTOR
            constraintSet.connect(filmDirectorInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmDirectorInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDirectorInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDirectorInitiateId + i, ConstraintSet.TOP, filmDirectorTxtInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.setVerticalBias(filmDirectorInitiateId + i, 0.01f);

            //RELEASE YEAR TXT
            constraintSet.connect(filmReleaseYearTxtInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmReleaseYearTxtInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmReleaseYearTxtInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmReleaseYearTxtInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmReleaseYearTxtInitiateId + i, 0.3f);
            constraintSet.setHorizontalBias(filmReleaseYearTxtInitiateId + i, 0.1f);

            //RELEASE YEAR
            constraintSet.connect(filmReleaseYearInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmReleaseYearInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmReleaseYearInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmReleaseYearInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmReleaseYearInitiateId + i, 0.3f);
            constraintSet.setHorizontalBias(filmReleaseYearInitiateId + i, 0.6f);

            //DURATION TXT
            constraintSet.connect(filmDurationTxtInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmDurationTxtInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDurationTxtInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDurationTxtInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmDurationTxtInitiateId + i, 0.4f);
            constraintSet.setHorizontalBias(filmDurationTxtInitiateId + i, 0.1f);

            //DURATION
            constraintSet.connect(filmDurationInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmDurationInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDurationInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmDurationInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmDurationInitiateId + i, 0.4f);
            constraintSet.setHorizontalBias(filmDurationInitiateId + i, 0.6f);

            //RATINGS TXT
            constraintSet.connect(filmRatingTxtInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmRatingTxtInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmRatingTxtInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmRatingTxtInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmRatingTxtInitiateId + i, 0.5f);
            constraintSet.setHorizontalBias(filmRatingTxtInitiateId + i, 0.1f);

            //RATINGS
            constraintSet.connect(filmRatingInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmRatingInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmRatingInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmRatingInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmRatingInitiateId + i, 0.5f);
            constraintSet.setHorizontalBias(filmRatingInitiateId + i, 0.6f);

            //Genres
            constraintSet.connect(filmGenresInitiateId + i, ConstraintSet.BOTTOM, filmConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(filmGenresInitiateId + i, ConstraintSet.END, filmConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmGenresInitiateId + i, ConstraintSet.START, filmPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(filmGenresInitiateId + i, ConstraintSet.TOP, filmPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmGenresInitiateId + i, 0.8f);
            constraintSet.setHorizontalBias(filmGenresInitiateId + i, 0.1f);

            constraintSet.applyTo(constraintLayout);
        }

        imdbApi = new ImdbApi();

        start();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            filmPhotosList[i].setOnClickListener(view -> {
                Intent intent = new Intent(this, FilmDetailsActivity.class);

                intent.putExtra("film", imdbApi.getFilms().get(finalI));
                startActivity(intent);
            });
        }

        imdbApi.getGenres();
        imdbApi.getTopRatedOrPopularFilms(true);

        notificationsSettingsBtn.setOnClickListener(view -> {
        });
        preferencesBtn.setOnClickListener(view -> {
            for (int i = 0; i < 10; i++) {
                imdbApi.manageEmptyFields(i);
                filmTitleList[i].setText(imdbApi.getFilms().get(i).getTitle());
                filmReleaseYearList[i].setText(String.valueOf(imdbApi.getFilms().get(i).getDateOfRelease()));
                filmDurationList[i].setText(String.valueOf(imdbApi.getFilms().get(i).getDuration()));
                filmRatingList[i].setText(String.valueOf(imdbApi.getFilms().get(i).getRatings()));
                filmGenresList[i].setText(String.valueOf(imdbApi.getFilms().get(i).getGenres())
                .replace("[", "")
                .replace("]", "")
                .replace(", ", "\n"));
                filmDirectorList[i].setText(String.valueOf(imdbApi.getFilms().get(i).getDirectors().values())
                        .replace("[", "")
                        .replace("]", ""));
                Picasso.get().load(imdbApi.getFilms().get(i).getImageUrl()).into(filmPhotosList[i]);
            }
        });
    }
}