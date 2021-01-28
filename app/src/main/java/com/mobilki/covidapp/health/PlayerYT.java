package com.mobilki.covidapp.health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mobilki.covidapp.R;

public class PlayerYT extends YouTubeBaseActivity  {
    Intent intent;
    private YouTubePlayerView youTubePlayerView;
    private String KEY;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_preferences), 0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);
        setContentView(R.layout.youtube_activity);
        KEY = getString(R.string.ytKey);
        intent = getIntent();
        youTubePlayerView = findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(KEY, onInitializedListener);
    }


    private final YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(intent.getStringExtra("url"));
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            Toast.makeText(getApplicationContext(),"Video loading failure", Toast.LENGTH_LONG).show();
        }
    };

}
