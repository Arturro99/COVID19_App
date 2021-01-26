package com.mobilki.covidapp.entertainment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.*;
import com.mobilki.covidapp.api.customThreads.FilmByGenresSorter;
import com.mobilki.covidapp.api.customThreads.FilmByValuesSorter;
import com.mobilki.covidapp.api.customThreads.GenresSetter;
import com.mobilki.covidapp.api.model.Game;
import com.mobilki.covidapp.api.repository.GameRepository;
import com.mobilki.covidapp.entertainment.fragments.BookFragment;
import com.mobilki.covidapp.entertainment.fragments.FilmFragment;
import com.mobilki.covidapp.entertainment.fragments.GameFragment;

import java.util.Optional;

import lombok.SneakyThrows;


public class EntertainmentActivity extends AppCompatActivity {

    FirebaseFirestore mFirestore;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    LinearLayout filmsLayout;
    LinearLayout booksLayout;
    LinearLayout gamesLayout;
    TextView filmTextView;
    TextView bookTextView;
    TextView gameTextView;
    HorizontalScrollView filmScrollView;
    HorizontalScrollView bookScrollView;
    HorizontalScrollView gameScrollView;

    ProgressBar mProgressBar;
    BottomNavigationView bottomNavigationView;


    private ImdbApi imdbApi;
    private GoogleBooksApi googleApi;

    private int bookDigit;
    private int filmDigit;
    private String filmSortingMethod;
    private String filmSortingByValuesType;
    private String filmGenre;
    private String bookGenre;

    private GameRepository gameRepository;

    private FilmFragment filmFragment;
    private BookFragment bookFragment;
    private GameFragment gameFragment;

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            mFirestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            DocumentReference filters = mFirestore.collection("users").document(mUser.getUid()).collection("settings").document("filters");

            gameRepository = new GameRepository();
            imdbApi = new ImdbApi();
            googleApi = new GoogleBooksApi();


            Toolbar mToolbar = findViewById(R.id.finalToolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            bottomNavigationView = findViewById(R.id.bottomNavigation);

            filmsLayout = findViewById(R.id.filmsLinearLayout);
            booksLayout = findViewById(R.id.booksLinearLayout);
            gamesLayout = findViewById(R.id.gamesLinearLayout);
            filmTextView = findViewById(R.id.filmTextView);
            bookTextView = findViewById(R.id.bookTextView);
            gameTextView = findViewById(R.id.gameTextView);
            filmScrollView = findViewById(R.id.filmScrollView);
            bookScrollView = findViewById(R.id.bookScrollView);
            gameScrollView = findViewById(R.id.gameScrollView);

            mProgressBar = findViewById(R.id.loading);
            mProgressBar.setVisibility(View.VISIBLE);

            filters.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null) {
                    filmDigit = Optional.ofNullable(documentSnapshot.getLong("filmDigit")).orElse(10L).intValue();
                    bookDigit = Optional.ofNullable(documentSnapshot.getLong("bookDigit")).orElse(10L).intValue();
                    filmSortingMethod = Optional.ofNullable(documentSnapshot.getString("filmSortingMethod")).orElse("sortByValues");
                    filmSortingByValuesType = Optional.ofNullable(documentSnapshot.getString("filmSortingByValuesType")).orElse("Most popular");
                    filmGenre = Optional.ofNullable(documentSnapshot.getString("filmGenre")).orElse("War");
                    bookGenre = Optional.ofNullable(documentSnapshot.getString("bookGenre")).orElse("drama");
                    try {
                        setUp();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    Log.d("TAG", "NULL in setters");
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            filmTextView.setVisibility(id == R.id.filmPage ? View.VISIBLE : View.GONE);
            filmScrollView.setVisibility(id == R.id.filmPage ? View.VISIBLE : View.GONE);
            bookTextView.setVisibility(id == R.id.bookPage ? View.VISIBLE : View.GONE);
            bookScrollView.setVisibility(id == R.id.bookPage ? View.VISIBLE : View.GONE);
            gameTextView.setVisibility(id == R.id.gamePage ? View.VISIBLE : View.GONE);
            gameScrollView.setVisibility(id == R.id.gamePage ? View.VISIBLE : View.GONE);

            switch (id) {
                case (R.id.filmPage): {
                    switchFragment(filmFragment, R.id.filmsLinearLayout);
                    Log.d("TAG", "onNavigationItemSelected: FILM");
                    setPhotosClickable(R.id.filmPage);
                    return true;
                }
                case (R.id.bookPage): {
                    switchFragment(bookFragment, R.id.booksLinearLayout);
                    Log.d("TAG", "onNavigationItemSelected: BOOK");
                    setPhotosClickable(R.id.bookPage);
                    return true;
                }
                case (R.id.gamePage): {
                    switchFragment(gameFragment, R.id.gamesLinearLayout);
                    Log.d("TAG", "onNavigationItemSelected: GAME");
                    setPhotosClickable(R.id.gamePage);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entertainment, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SneakyThrows
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case (R.id.menuEntertainmentSettings):
                startActivity(new Intent(this, EntertainmentSettingsActivity.class));
                return true;
            case (R.id.menuEntertainmentNotifications):
                startActivity(new Intent(this, EntertainmentNotificationsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUp() throws InterruptedException {
        Thread genresSetter = new Thread(new GenresSetter(imdbApi), "genresSetter");
        genresSetter.start();
        genresSetter.join();

        String locale = getResources().getConfiguration().locale.getLanguage();

        if (filmSortingMethod.equals("sortByValues")) {
            Thread filmByValuesSorter = new Thread(new FilmByValuesSorter(imdbApi, getSortingValue(filmSortingByValuesType), filmDigit, locale), "filmValuesSorter");
            filmByValuesSorter.start();
            filmByValuesSorter.join(3000L);
        } else {
            Thread filmByGenresSorter = new Thread(new FilmByGenresSorter(imdbApi, filmGenre, filmDigit, locale), "filmGenresSorter");
            filmByGenresSorter.start();
            filmByGenresSorter.join(3000L);
        }
        if (locale.equals("en")) {
            googleApi.getByGenre(bookGenre, bookDigit, "en");
        }
        else {
            googleApi.getByGenre(bookGenre, bookDigit, "pl");
        }


        filmFragment = new FilmFragment(filmsLayout, filmDigit, imdbApi);
        bookFragment = new BookFragment(booksLayout, bookDigit, googleApi);
        gameFragment = new GameFragment(gamesLayout, gameRepository);

        fetchGames();
        bottomNavigationView.setSelectedItemId(R.id.filmPage);
        switchFragment(filmFragment, R.id.filmsLinearLayout);
//        Log.d("TAG", "onNavigationItemSelected: FILM");
        setPhotosClickable(R.id.filmPage);
        mProgressBar.setVisibility(View.GONE);
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
            start();
        });
    }

    private void setPhotosClickable(int resource) {
        switch(resource) {
            case (R.id.filmPage): {
                for (int i = 0; i < filmDigit; i++) {
                    int finalFilmI = i;
                    filmFragment.getPhoto(i).setOnClickListener(view -> {
                        Intent intent = new Intent(this, FilmDetailsActivity.class);

                        intent.putExtra("film", imdbApi.getAll().get(finalFilmI));
                        startActivity(intent);
                    });
                }
                break;
            }
            case (R.id.bookPage): {
                for (int i = 0; i < bookDigit; i++) {
                    int finalBookI = i;
                    bookFragment.getPhoto(i).setOnClickListener(view -> {
                        Intent intent = new Intent(this, BookDetailsActivity.class);

                        intent.putExtra("book", googleApi.getAll().get(finalBookI));
                        startActivity(intent);
                    });
                }
                break;
            }
            case (R.id.gamePage): {
                for (int i = 0; i < 8; i++) {
                    int finalGameI = i;
                    gameFragment.getPhoto(i).setOnClickListener(view -> {
                        Intent intent = new Intent(this, GameDetailsActivity.class);

                        intent.putExtra("game", gameRepository.getAll().get(finalGameI));
                        startActivity(intent);
                    });
                }
            }
        }
    }

    private void switchFragment(Fragment fragment, int resource) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(resource, fragment)
                    .commitNow();
        }
        catch (IllegalStateException ex) { }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}