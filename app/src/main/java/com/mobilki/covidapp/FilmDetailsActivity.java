package com.mobilki.covidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobilki.covidapp.api.model.Actor;
import com.mobilki.covidapp.api.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilmDetailsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView title;
    private TextView ratingsCount;
    private TextView ratingsCountTxt;
    private TextView description;
    private TextView descriptionTxt;
    private ListView actors;
    private TextView actorsTxt;

    private Film film;

    List<String> actorNames;
    List<ImageView> actorImgs;
    ArrayAdapter<String> namesAdapter;
    ArrayAdapter<ImageView> imagesAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        film = (Film) getIntent().getSerializableExtra("film");

        ratingBar = findViewById(R.id.ratingBar);
        title = findViewById(R.id.detailedTitle);
        ratingsCount = findViewById(R.id.detailedRatingsCount);
        ratingsCountTxt = findViewById(R.id.detailedRatingsCountTxt);
        description = findViewById(R.id.detailedDescription);
        descriptionTxt = findViewById(R.id.detailedDescriptionTxt);
        actors = findViewById(R.id.detailedActors);
        actorsTxt = findViewById(R.id.detailedActorsTxt);


        ratingBar.setRating((float)film.getRatings() / 2);
        title.setText(film.getTitle());
        ratingsCount.setText(String.valueOf(film.getRatingsCount()));
        description.setText(film.getShortDescription());

        actorNames = new ArrayList<>();
        actorNames.addAll(film.getActors().stream()
                .map(Actor::getName)
                .collect(Collectors.toList())
        );
        namesAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.actorName, actorNames);
        actors.setAdapter(namesAdapter);
        namesAdapter.notifyDataSetChanged();

        actorImgs = new ArrayList<>();
    }

}