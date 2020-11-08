package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button healthBtn;
    Button entertainmentBtn;
    Button emergencyNumbersBtn;
    Button settingsBtn;

    TextView curiosities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        healthBtn = findViewById(R.id.healthBtn);
        entertainmentBtn = findViewById(R.id.entertainmentBtn);
        emergencyNumbersBtn = findViewById(R.id.emergencyNumbersBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        curiosities = findViewById(R.id.mainCuriositiesTxt);

        start();
    }

    private void start() {
        healthBtn.setOnClickListener(view -> startActivity(new Intent(this, HealthActivity.class)));
        entertainmentBtn.setOnClickListener(view -> startActivity(new Intent(this, EntertainmentActivity.class)));
    }
}