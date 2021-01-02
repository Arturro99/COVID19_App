package com.mobilki.covidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobilki.covidapp.api.*;
import com.mobilki.covidapp.api.customThreads.FilmByGenresSorter;
import com.mobilki.covidapp.api.customThreads.FilmByValuesSorter;
import com.mobilki.covidapp.api.customThreads.GenresSetter;
import com.mobilki.covidapp.api.model.Game;
import com.mobilki.covidapp.api.repository.GameRepository;
import com.mobilki.covidapp.exceptions.BookDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import lombok.SneakyThrows;


public class EntertainmentActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    Button notificationsSettingsBtn;
    Button preferencesBtn;

    LinearLayout filmsLayout;
    LinearLayout booksLayout;
    LinearLayout gamesLayout;
    LayoutInflater inflater;


    //FILMS
    TextView[] filmTitleList;
    TextView[] filmGenresList;

    ImageButton[] filmPhotosList;

    TextView[] filmDirectorList;
    TextView[] filmDirectorTxtList;

    TextView[] filmReleaseYearList;
    TextView[] filmReleaseYearTxtList;

    TextView[] filmDurationList;
    TextView[] filmDurationTxtList;

    TextView[] filmRatingList;
    TextView[] filmRatingTxtList;

    ConstraintLayout[] filmConstraintLayoutList;
    /////////////////////////////////////////////////////////

    //BOOKS
    TextView[] bookTitleList;
    TextView[] bookGenresList;

    ImageButton[] bookPhotosList;

    TextView[] bookAuthorList;
    TextView[] bookAuthorTxtList;

    TextView[] bookPublicationDateList;
    TextView[] bookPublicationDateTxtList;

    TextView[] bookPagesList;
    TextView[] bookPagesTxtList;

    TextView[] bookRatingList;
    TextView[] bookRatingTxtList;

    ConstraintLayout[] bookConstraintLayoutList;
    /////////////////////////////////////////////////////////////

    //GAMES
    TextView[] gameTitleList;
    TextView[] gameGenresList;

    ImageButton[] gamePhotosList;

    TextView[] gamePlayersList;
    TextView[] gamePlayersTxtList;

    TextView[] gameAgeList;
    TextView[] gameAgeTxtList;

    TextView[] gameTimeList;
    TextView[] gameTimeTxtList;

    ConstraintLayout[] gameConstraintLayoutList;
    //////////////////////////////////////////////////////////////

    private Toolbar mToolbar;

    private ImdbApi imdbApi;
    private GoogleBooksApi googleApi;
    private Thread genresSetter;
    private Thread filmByGenresSorter;
    private Thread filmByValuesSorter;
    private List synchedList = Collections.synchronizedList(new LinkedList<>());

    private int bookDigit;
    private int filmDigit;

    private GameRepository gameRepository;

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);

        sharedPreferences = getApplication().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        gameRepository = new GameRepository();

        mToolbar = findViewById(R.id.finalToolbar);
        setSupportActionBar(mToolbar);

        notificationsSettingsBtn = findViewById(R.id.entertainmentNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.entertainmentPreferencesBtn);

        filmsLayout = findViewById(R.id.filmsLinearLayout);
        booksLayout = findViewById(R.id.booksLinearLayout);
        gamesLayout = findViewById(R.id.gamesLinearLayout);

        inflater = LayoutInflater.from(this);

        bookDigit = sharedPreferences.getInt("bookDigit", 10);
        filmDigit = sharedPreferences.getInt("filmDigit", 10);

        booksFieldInitialization(bookDigit);
        filmsFieldInitialization(filmDigit);
        gamesFieldInitialization();

        setFilms(filmDigit);
        setBooks(bookDigit);
        setGames();
        imdbApi = new ImdbApi();
        googleApi = new GoogleBooksApi();

        genresSetter = new Thread(new GenresSetter(imdbApi), "genresSetter");
        genresSetter.start();
        genresSetter.join();

        if (sharedPreferences.getString("filmSortingMethod", "KK").equals("sortByValues")) {
            filmByValuesSorter = new Thread(new FilmByValuesSorter(imdbApi, getSortingValue(Objects.requireNonNull(sharedPreferences.getString("filmSortingByValuesType", "Most popular"))), filmDigit), "filmValuesSorter");
            filmByValuesSorter.start();
            filmByValuesSorter.join(3000L);
        } else {
            filmByGenresSorter = new Thread(new FilmByGenresSorter(imdbApi, sharedPreferences.getString("filmGenre", "Drama"), filmDigit), "filmGenresSorter");
            filmByGenresSorter.start();
            filmByGenresSorter.join(3000L);
        }
        googleApi.getByGenre(sharedPreferences.getString("bookGenre", "drama"), bookDigit);

        initiateFilms(sharedPreferences.getInt("filmDigit", 10));
        initiateBooks(sharedPreferences.getInt("bookDigit", 10));

        fetchGames();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {

        for (int i = 0; i < sharedPreferences.getInt("filmDigit", 10); i++) {
            int finalFilmI = i;
            filmPhotosList[i].setOnClickListener(view -> {
                Intent intent = new Intent(this, FilmDetailsActivity.class);

                intent.putExtra("film", imdbApi.getAll().get(finalFilmI));
                startActivity(intent);
            });
        }

        for (int i = 0; i < sharedPreferences.getInt("bookDigit", 10); i++) {
            int finalBookI = i;
            bookPhotosList[i].setOnClickListener(view -> {
                Intent intent = new Intent(this, BookDetailsActivity.class);

                intent.putExtra("book", googleApi.getAll().get(finalBookI));
                startActivity(intent);
            });
        }

        notificationsSettingsBtn.setOnClickListener(view -> {

        });
        preferencesBtn.setOnClickListener(view -> {

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entertainment, menu);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        filmDigit = sharedPreferences.getInt("filmDigit", 10);
        bookDigit = sharedPreferences.getInt("bookDigit", 10);

        if (id == R.id.menuEntertainmentSettings) {
            startActivity(new Intent(this, EntertainmentSettingsActivity.class));
            return true;
        } else if (id == R.id.menuEntertainmentRefresh) {
            if (sharedPreferences.getString("filmSortingMethod", "KK").equals("sortByValues")) {
                filmByValuesSorter = new FilmByValuesSorter(imdbApi, getSortingValue(Objects.requireNonNull(sharedPreferences.getString("filmSortingByValuesType", "Most popular"))), filmDigit);
                filmByValuesSorter.start();
                try {
                    filmByValuesSorter.join(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                filmByGenresSorter = new FilmByGenresSorter(imdbApi, sharedPreferences.getString("filmGenre", "Drama"), filmDigit);
                filmByGenresSorter.start();
                try {
                    filmByGenresSorter.join(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            filmsFieldInitialization(filmDigit);
            setFilms(filmDigit);
            initiateFilms(filmDigit);

            googleApi.getByGenre(sharedPreferences.getString("bookGenre", "drama"), bookDigit);
            booksFieldInitialization(bookDigit);
            setBooks(bookDigit);
            initiateBooks(bookDigit);
        }

        return super.onOptionsItemSelected(item);
    }

    private FilmSortingType getSortingValue(String type) {
        switch (type) {
            case ("Top rated"):
                return FilmSortingType.TOP_RATED;
            case ("Most popular"):
                return FilmSortingType.MOST_POPULAR;
            case ("Upcoming"):
                return FilmSortingType.UPCOMING;
            case ("Now playing"):
                return FilmSortingType.NOW_PLAYING;
        }
        return FilmSortingType.MOST_POPULAR;
    }

    private void booksFieldInitialization(int number) {
        bookTitleList = new TextView[number];
        bookGenresList = new TextView[number];

        bookPhotosList = new ImageButton[number];

        bookAuthorList = new TextView[number];
        bookAuthorTxtList = new TextView[number];

        bookPublicationDateList = new TextView[number];
        bookPublicationDateTxtList = new TextView[number];

        bookPagesList = new TextView[number];
        bookPagesTxtList = new TextView[number];

        bookRatingList = new TextView[number];
        bookRatingTxtList = new TextView[number];

        bookConstraintLayoutList = new ConstraintLayout[number];
    }

    private void filmsFieldInitialization(int number) {
        filmTitleList = new TextView[number];
        filmGenresList = new TextView[number];

        filmPhotosList = new ImageButton[number];

        filmDirectorList = new TextView[number];
        filmDirectorTxtList = new TextView[number];

        filmReleaseYearList = new TextView[number];
        filmReleaseYearTxtList = new TextView[number];

        filmDurationList = new TextView[number];
        filmDurationTxtList = new TextView[number];

        filmRatingList = new TextView[number];
        filmRatingTxtList = new TextView[number];

        filmConstraintLayoutList = new ConstraintLayout[number];
    }

    private void gamesFieldInitialization() {
        gameTitleList = new TextView[8];
        gameGenresList = new TextView[8];

        gamePhotosList = new ImageButton[8];

        gameAgeList = new TextView[8];
        gameAgeTxtList = new TextView[8];

        gamePlayersList = new TextView[8];
        gamePlayersTxtList = new TextView[8];

        gameTimeList = new TextView[8];
        gameTimeTxtList = new TextView[8];

        gameConstraintLayoutList = new ConstraintLayout[8];
    }

    private void initiateFilms(int number) {
        for (int i = 0; i < number; i++) {
            filmTitleList[i].setText(imdbApi.getAll().get(i).getTitle());
            filmReleaseYearList[i].setText(String.valueOf(imdbApi.getAll().get(i).getDateOfRelease()));
            filmDurationList[i].setText(String.valueOf(imdbApi.getAll().get(i).getDuration()));
            filmRatingList[i].setText(String.valueOf(imdbApi.getAll().get(i).getRatings()));
            filmGenresList[i].setText(String.valueOf(imdbApi.getAll().get(i).getGenres())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n"));
            filmDirectorList[i].setText(String.valueOf(imdbApi.getAll().get(i).getDirectors().values())
                    .replace("[", "")
                    .replace("]", ""));
            Picasso.get().load(imdbApi.getAll().get(i).getImageUrl()).placeholder(R.drawable.placeholder).into(filmPhotosList[i]);
        }
    }

    private void initiateBooks(int number) {
        for (int i = 0; i < number; i++) {
            bookTitleList[i].setText(googleApi.getAll().get(i).getTitle());
            bookPublicationDateList[i].setText(String.valueOf(googleApi.getAll().get(i).getPublicationDate()));
            bookPagesList[i].setText(String.valueOf(googleApi.getAll().get(i).getPages()));
            bookRatingList[i].setText(String.valueOf(googleApi.getAll().get(i).getRatings()));
            bookAuthorList[i].setText(String.valueOf(googleApi.getAll().get(i).getAuthors())
                    .replace("[", "")
                    .replace("]", ""));
            bookGenresList[i].setText(String.valueOf(googleApi.getAll().get(i).getGenres())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n"));
            Glide.with(this).load(googleApi.getAll().get(i).getImageUrl()).placeholder(R.drawable.placeholder).into(bookPhotosList[i]);
        }
    }

    private void initiateGames() {
        for (int i = 0; i < 8; i++) {
            gameTitleList[i].setText(gameRepository.getAll().get(i).getTitleEn());
            gameAgeList[i].setText(gameRepository.getAll().get(i).getAgeMin() + "-" + gameRepository.getAll().get(i).getAgeMax());
            gamePlayersList[i].setText(gameRepository.getAll().get(i).getPlayersMin() + "-" + gameRepository.getAll().get(i).getPlayersMax());
            gameTimeList[i].setText(gameRepository.getAll().get(i).getTime() + "seconds");
            bookGenresList[i].setText(String.valueOf(gameRepository.getAll().get(i).getGenreEn())
                    .replace(", ", "\n"));
            Glide.with(this).load(gameRepository.getAll().get(i).getImgLink()).placeholder(R.drawable.placeholder).into(gamePhotosList[i]);
        }
    }

    private void setFilms(int filmDigit) throws InterruptedException {
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


        filmsLayout.removeAllViews();
        for (int i = 0; i < filmDigit; i++) {
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
    }

    private void setBooks(int bookDigit) {
        int bookTitleInitiateId = 11000;

        int bookPublicationDateInitiateId = 12000;
        int bookPublicationDateTxtInitiateId = 12500;

        int bookPhotoInitiateId = 13000;

        int bookAuthorInitiateId = 14000;
        int bookAuthorTxtInitiateId = 14500;

        int bookPagesInitiateId = 15000;
        int bookPagesTxtInitiateId = 15500;

        int bookRatingInitiateId = 16000;
        int bookRatingTxtInitiateId = 16500;

        int bookGenresInitiateId = 17000;

        int bookConstraintLayoutInitiateId = 20000;

        ConstraintLayout constraintLayout;

        booksLayout.removeAllViews();
        for (int i = 0; i < bookDigit; i++) {
            View anotherLayout = inflater.inflate(R.layout.book_overview, null, true);
            booksLayout.addView(anotherLayout);

            bookConstraintLayoutList[i] = findViewById(R.id.bookConstraintLayout);
            bookTitleList[i] = findViewById(R.id.bookTitle);
            bookPhotosList[i] = findViewById(R.id.bookPhoto);
            bookGenresList[i] = findViewById(R.id.bookGenres);

            bookAuthorList[i] = findViewById(R.id.bookAuthor);
            bookAuthorTxtList[i] = findViewById(R.id.bookAuthorTxt);

            bookRatingList[i] = findViewById(R.id.bookRating);
            bookRatingTxtList[i] = findViewById(R.id.bookRatingTxt);

            bookPublicationDateList[i] = findViewById(R.id.bookPublicationDate);
            bookPublicationDateTxtList[i] = findViewById(R.id.bookPublicationDateTxt);

            bookPagesList[i] = findViewById(R.id.bookPages);
            bookPagesTxtList[i] = findViewById(R.id.bookPagesTxt);


            bookConstraintLayoutList[i].setId(bookConstraintLayoutInitiateId + i);
            bookTitleList[i].setId(bookTitleInitiateId + i);
            bookPhotosList[i].setId(bookPhotoInitiateId + i);
            bookGenresList[i].setId(bookGenresInitiateId + i);

            bookPublicationDateList[i].setId(bookPublicationDateInitiateId + i);
            bookPublicationDateTxtList[i].setId(bookPublicationDateTxtInitiateId + i);

            bookAuthorList[i].setId(bookAuthorInitiateId + i);
            bookAuthorTxtList[i].setId(bookAuthorTxtInitiateId + i);

            bookPagesList[i].setId(bookPagesInitiateId + i);
            bookPagesTxtList[i].setId(bookPagesTxtInitiateId + i);

            bookRatingList[i].setId(bookRatingInitiateId + i);
            bookRatingTxtList[i].setId(bookRatingTxtInitiateId + i);

            constraintLayout = findViewById(bookConstraintLayoutInitiateId + i);
            constraintLayout.setMinWidth(1200);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            //IMAGE
            constraintSet.connect(bookPhotoInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.START);
            constraintSet.connect(bookPhotoInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);

            //TITLE
            constraintSet.connect(bookTitleInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookTitleInitiateId + i, ConstraintSet.END, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookTitleInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.START);
            constraintSet.connect(bookTitleInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.BOTTOM);


            //AUTHOR TXT
            constraintSet.connect(bookAuthorTxtInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookAuthorTxtInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookAuthorTxtInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookAuthorTxtInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookAuthorTxtInitiateId + i, 0.05f);

            //DIRECTOR
            constraintSet.connect(bookAuthorInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookAuthorInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookAuthorInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookAuthorInitiateId + i, ConstraintSet.TOP, bookAuthorTxtInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.setVerticalBias(bookAuthorInitiateId + i, 0.01f);

            //PUBLICATION DATE TXT
            constraintSet.connect(bookPublicationDateTxtInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookPublicationDateTxtInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPublicationDateTxtInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPublicationDateTxtInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPublicationDateTxtInitiateId + i, 0.3f);
            constraintSet.setHorizontalBias(bookPublicationDateTxtInitiateId + i, 0.1f);

            //PUBLICATION DATE
            constraintSet.connect(bookPublicationDateInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookPublicationDateInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPublicationDateInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPublicationDateInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPublicationDateInitiateId + i, 0.3f);
            constraintSet.setHorizontalBias(bookPublicationDateInitiateId + i, 0.6f);

            //PAGES TXT
            constraintSet.connect(bookPagesTxtInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookPagesTxtInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPagesTxtInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPagesTxtInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPagesTxtInitiateId + i, 0.4f);
            constraintSet.setHorizontalBias(bookPagesTxtInitiateId + i, 0.1f);

            //PAGES
            constraintSet.connect(bookPagesInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookPagesInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPagesInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookPagesInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPagesInitiateId + i, 0.4f);
            constraintSet.setHorizontalBias(bookPagesInitiateId + i, 0.6f);

            //RATINGS TXT
            constraintSet.connect(bookRatingTxtInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookRatingTxtInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookRatingTxtInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookRatingTxtInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookRatingTxtInitiateId + i, 0.5f);
            constraintSet.setHorizontalBias(bookRatingTxtInitiateId + i, 0.1f);

            //RATINGS
            constraintSet.connect(bookRatingInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookRatingInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookRatingInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookRatingInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookRatingInitiateId + i, 0.5f);
            constraintSet.setHorizontalBias(bookRatingInitiateId + i, 0.6f);

            //Genres
            constraintSet.connect(bookGenresInitiateId + i, ConstraintSet.BOTTOM, bookConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(bookGenresInitiateId + i, ConstraintSet.END, bookConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookGenresInitiateId + i, ConstraintSet.START, bookPhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(bookGenresInitiateId + i, ConstraintSet.TOP, bookPhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookGenresInitiateId + i, 0.8f);
            constraintSet.setHorizontalBias(bookGenresInitiateId + i, 0.1f);

            constraintSet.applyTo(constraintLayout);
        }
    }

    private void setGames() {
        int gameTitleInitiateId = 21000;

        int gameAgeInitiateId = 22000;
        int gameAgeTxtInitiateId = 22500;

        int gamePhotoInitiateId = 23000;

        int gamePlayersInitiateId = 24000;
        int gamePlayersTxtInitiateId = 24500;

        int gameTimeInitiateId = 25000;
        int gameTimeTxtInitiateId = 25500;

        int gameGenresInitiateId = 26000;

        int gameConstraintLayoutInitiateId = 20000;

        ConstraintLayout constraintLayout;

        gamesLayout.removeAllViews();
        for (int i = 0; i < 8; i++) {
            View anotherLayout = inflater.inflate(R.layout.game_overview, null, true);
            gamesLayout.addView(anotherLayout);

            gameConstraintLayoutList[i] = findViewById(R.id.gameConstraintLayout);
            gameTitleList[i] = findViewById(R.id.gameTitle);
            gamePhotosList[i] = findViewById(R.id.gamePhoto);
            gameGenresList[i] = findViewById(R.id.gameGenres);

            gameAgeList[i] = findViewById(R.id.gameAge);
            gameAgeTxtList[i] = findViewById(R.id.gameAgeTxt);

            gamePlayersList[i] = findViewById(R.id.gamePlayers);
            gamePlayersTxtList[i] = findViewById(R.id.gamePlayersTxt);

            gameTimeList[i] = findViewById(R.id.gameTime);
            gameTimeTxtList[i] = findViewById(R.id.gameTimeTxt);

            gameConstraintLayoutList[i].setId(gameConstraintLayoutInitiateId + i);
            gameTitleList[i].setId(gameTitleInitiateId + i);
            gamePhotosList[i].setId(gamePhotoInitiateId + i);
            gameGenresList[i].setId(gameGenresInitiateId + i);

            gameAgeList[i].setId(gameAgeInitiateId + i);
            gameAgeTxtList[i].setId(gameAgeTxtInitiateId + i);

            gamePlayersList[i].setId(gamePlayersInitiateId + i);
            gamePlayersTxtList[i].setId(gamePlayersTxtInitiateId + i);

            gameTimeList[i].setId(gameTimeInitiateId + i);
            gameTimeTxtList[i].setId(gameTimeTxtInitiateId + i);


            constraintLayout = findViewById(gameConstraintLayoutInitiateId + i);
            constraintLayout.setMinWidth(1200);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            //IMAGE
            constraintSet.connect(gamePhotoInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.START);
            constraintSet.connect(gamePhotoInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);

            //TITLE
            constraintSet.connect(gameTitleInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gameTitleInitiateId + i, ConstraintSet.END, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameTitleInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.START);
            constraintSet.connect(gameTitleInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.BOTTOM);


            //AGE TXT
            constraintSet.connect(gameAgeTxtInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gameAgeTxtInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameAgeTxtInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameAgeTxtInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameAgeTxtInitiateId + i, 0.2f);
            constraintSet.setHorizontalBias(gameAgeTxtInitiateId + i, 0.1f);

            //AGE
            constraintSet.connect(gameAgeInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gameAgeInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameAgeInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameAgeInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameAgeInitiateId + i, 0.2f);
            constraintSet.setHorizontalBias(gameAgeInitiateId + i, 0.6f);

            //Players TXT
            constraintSet.connect(gamePlayersTxtInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gamePlayersTxtInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gamePlayersTxtInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gamePlayersTxtInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gamePlayersTxtInitiateId + i, 0.3f);
            constraintSet.setHorizontalBias(gamePlayersTxtInitiateId + i, 0.1f);

            //Players
            constraintSet.connect(gamePlayersInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gamePlayersInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gamePlayersInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gamePlayersInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gamePlayersInitiateId + i, 0.3f);
            constraintSet.setHorizontalBias(gamePlayersInitiateId + i, 0.6f);

            //TIME TXT
            constraintSet.connect(gameTimeTxtInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gameTimeTxtInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameTimeTxtInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameTimeTxtInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameTimeTxtInitiateId + i, 0.4f);
            constraintSet.setHorizontalBias(gameTimeTxtInitiateId + i, 0.1f);

            //TIME
            constraintSet.connect(gameTimeInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gameTimeInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameTimeInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameTimeInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameTimeInitiateId + i, 0.4f);
            constraintSet.setHorizontalBias(gameTimeInitiateId + i, 0.6f);

            //Genres
            constraintSet.connect(gameGenresInitiateId + i, ConstraintSet.BOTTOM, gameConstraintLayoutInitiateId + i, ConstraintSet.BOTTOM);
            constraintSet.connect(gameGenresInitiateId + i, ConstraintSet.END, gameConstraintLayoutInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameGenresInitiateId + i, ConstraintSet.START, gamePhotoInitiateId + i, ConstraintSet.END);
            constraintSet.connect(gameGenresInitiateId + i, ConstraintSet.TOP, gamePhotoInitiateId + i, ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameGenresInitiateId + i, 0.7f);

            constraintSet.applyTo(constraintLayout);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchGames() {
        FirebaseFirestore.getInstance().collection("games").get().addOnCompleteListener(task -> {
            Log.d("TAG", "fetchGamesFromDataBase: ");
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Game game = doc.toObject(Game.class);
                    gameRepository.add(game);
                    Log.d("TAG", "fetchGamesFromDataBase: ");
                }
            } else {
                Log.d("ERR", "Cannot import games from db");
            }
            initiateGames();
            start();
        });
    }

}