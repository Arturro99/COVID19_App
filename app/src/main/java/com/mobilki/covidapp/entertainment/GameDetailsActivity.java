package com.mobilki.covidapp.entertainment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.model.Game;

public class GameDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Game game = (Game) getIntent().getSerializableExtra("game");
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();

        TextView link = findViewById(R.id.gameLink);
        TextView description = findViewById(R.id.gameDetailedDescription);
        TextView title = findViewById(R.id.gameDetailedTitle);

        link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (currentLanguage.equals("en")) {
            description.setText(game.getDescriptionEn());
            title.setText(game.getTitleEn());
        }
        else {
            description.setText(game.getDescriptionPl());
            title.setText(game.getTitlePl());
        }
        link.setText(game.getLink());
    }
}