package com.mobilki.covidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mobilki.covidapp.authentication.Login;
import com.mobilki.covidapp.health.HealthActivity;
import com.mobilki.covidapp.health.HealthForm;

public class MainActivity extends AppCompatActivity {

    Button healthBtn;
    Button entertainmentBtn;
    Button emergencyNumbersBtn;
    Button settingsBtn;
    Button mLogoutBtn;

    TextView curiosities;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        healthBtn = findViewById(R.id.healthBtn);
        entertainmentBtn = findViewById(R.id.entertainmentBtn);
        emergencyNumbersBtn = findViewById(R.id.emergencyNumbersBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        mLogoutBtn = findViewById(R.id.logout);

        curiosities = findViewById(R.id.mainCuriositiesTxt);
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        start();
    }

    private void start() {
        healthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settings.getBoolean("first_time_health", true)) {
                    startActivity(new Intent(MainActivity.this, HealthForm.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this, HealthActivity.class));
                }
            }
        });
//        healthBtn.setOnClickListener(view -> startActivity(new Intent(this, HealthActivity.class)));
        entertainmentBtn.setOnClickListener(view -> startActivity(new Intent(this, EntertainmentActivity.class)));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}