package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HealthActivity extends AppCompatActivity {

    Button exerciseSetBtn;
    Button addDataBtn;
    Button notificationsSettingsBtn;
    Button preferencesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        exerciseSetBtn = findViewById(R.id.exerciseSetBtn);
        addDataBtn = findViewById(R.id.addDataBtn);
        notificationsSettingsBtn = findViewById(R.id.healthNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.healthPreferencesBtn);

        start();
    }

    private void start() {

        addDataBtn.setOnClickListener(view -> startActivity(new Intent(this, HealthDataActivity.class)));
    }
}