package com.mobilki.covidapp.entertainment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.imageslistview.CustomAdapter;
import com.mobilki.covidapp.api.model.Actor;
import com.mobilki.covidapp.api.model.Film;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilmDetailsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView title;
    private TextView ratingsCount;
    private TextView description;
    private ListView actorsList;

    private CustomAdapter adapter;

    private Film film;
    private ArrayList actors;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        film = (Film) getIntent().getSerializableExtra("film");

        ratingBar = findViewById(R.id.ratingBar);
        title = findViewById(R.id.detailedTitle);
        ratingsCount = findViewById(R.id.detailedRatingsCount);
        description = findViewById(R.id.detailedDescription);
        actorsList = findViewById(R.id.detailedActors);

        actors = (ArrayList) film.getActors();



        ratingBar.setRating((float)film.getRatings() / 2);
        title.setText(film.getTitle());
        ratingsCount.setText(String.valueOf(film.getRatingsCount()));
        description.setText(film.getShortDescription());

        adapter = new CustomAdapter(getApplication(), actors);

        actorsList.setAdapter(adapter);

    }

}