package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.mobilki.covidapp.api.model.Game;

public class GameDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Game game = (Game) getIntent().getSerializableExtra("game");

        TextView link = findViewById(R.id.gameLink);
        TextView description = findViewById(R.id.gameDetailedDescription);
        TextView title = findViewById(R.id.gameDetailedTitle);

        link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        description.setText(game.getDescriptionEn());
        link.setText(game.getLink());
        title.setText(game.getTitleEn());
    }
}