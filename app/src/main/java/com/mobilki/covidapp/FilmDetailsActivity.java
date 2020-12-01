package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobilki.covidapp.api.model.Film;

public class FilmDetailsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView title;
    private TextView ratingsCount;
    private TextView ratingsCountTxt;
    private TextView description;
    private TextView descriptionTxt;
    private ListView actors;
    private TextView actorsTxt;

    private Film filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        filmId = (Film) getIntent().getSerializableExtra("film");

        ratingBar = findViewById(R.id.ratingBar);
        title = findViewById(R.id.detailedTitle);
        ratingsCount = findViewById(R.id.detailedRatingsCount);
        ratingsCountTxt = findViewById(R.id.detailedRatingsCountTxt);
        description = findViewById(R.id.detailedDescription);
        descriptionTxt = findViewById(R.id.detailedDescriptionTxt);
        actors = findViewById(R.id.detailedActors);
        actorsTxt = findViewById(R.id.detailedActorsTxt);


        ratingBar.setRating((float)filmId.getRatings() / 2);
        title.setText(filmId.getTitle());
        ratingsCount.setText(String.valueOf(filmId.getRatingsCount()));
        description.setText(filmId.getShortDescription());
    }

}