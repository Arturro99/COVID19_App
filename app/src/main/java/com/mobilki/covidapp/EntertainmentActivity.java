package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class EntertainmentActivity extends AppCompatActivity {

    Button recommendedMoviesBtn;
    Button recommendedBooksBtn;
    Button familialGamesBtn;
    Button notificationsSettingsBtn;
    Button preferencesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);

        notificationsSettingsBtn = findViewById(R.id.entertainmentNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.entertainmentPreferencesBtn);
    }
}