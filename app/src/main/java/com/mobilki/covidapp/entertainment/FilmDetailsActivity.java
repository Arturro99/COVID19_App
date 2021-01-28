package com.mobilki.covidapp.entertainment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.imageslistview.CustomAdapter;
import com.mobilki.covidapp.api.model.Film;

import java.util.ArrayList;

public class FilmDetailsActivity extends AppCompatActivity {

    SharedPreferences settings;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        Film film = (Film) getIntent().getSerializableExtra("film");

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView title = findViewById(R.id.detailedTitle);
        TextView ratingsCount = findViewById(R.id.detailedRatingsCount);
        TextView description = findViewById(R.id.detailedDescription);
        ListView actorsList = findViewById(R.id.detailedActors);

        ArrayList actors = (ArrayList) film.getActors();



        ratingBar.setRating((float) film.getRatings() / 2);
        title.setText(film.getTitle());
        ratingsCount.setText(String.valueOf(film.getRatingsCount()));
        description.setText(film.getShortDescription());

        CustomAdapter adapter = new CustomAdapter(getApplication(), actors);

        actorsList.setAdapter(adapter);

    }

}