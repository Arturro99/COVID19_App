package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mobilki.covidapp.authentication.Login;
import com.mobilki.covidapp.authentication.Register;

public class MainActivity extends AppCompatActivity {

    Button healthBtn;
    Button entertainmentBtn;
    Button emergencyNumbersBtn;
    Button settingsBtn;
    Button mLogoutBtn;

    TextView curiosities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        healthBtn = findViewById(R.id.healthBtn);
        entertainmentBtn = findViewById(R.id.entertainmentBtn);
        emergencyNumbersBtn = findViewById(R.id.emergencyNumbersBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        mLogoutBtn = findViewById(R.id.logout);

        curiosities = findViewById(R.id.mainCuriositiesTxt);

        start();
    }

    private void start() {
        healthBtn.setOnClickListener(view -> startActivity(new Intent(this, Register.class)));
        entertainmentBtn.setOnClickListener(view -> startActivity(new Intent(this, EntertainmentActivity.class)));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}