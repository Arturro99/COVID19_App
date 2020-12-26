package com.mobilki.covidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class HealthActivity extends AppCompatActivity {

    Button exerciseSetBtn;
    Button addDataBtn;
    Button notificationsSettingsBtn;
    Button preferencesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_health);

        exerciseSetBtn = findViewById(R.id.exerciseSetBtn);
        addDataBtn = findViewById(R.id.addDataBtn);
        notificationsSettingsBtn = findViewById(R.id.healthNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.healthPreferencesBtn);

        start();
    }

    private void start() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HealthActivity.this, findViewById(R.id.textView3), HealthDataActivity.VIEW_NAME_HEADER_TITLE);
        addDataBtn.setOnClickListener(view -> ActivityCompat.startActivity(HealthActivity.this, new Intent(HealthActivity.this, HealthDataActivity.class), options.toBundle()));
    }


}