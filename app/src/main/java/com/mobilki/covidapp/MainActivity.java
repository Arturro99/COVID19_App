package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobilki.covidapp.authentication.Login;
import com.mobilki.covidapp.authentication.Register;

public class MainActivity extends AppCompatActivity {

    Button healthBtn;
    Button entertainmentBtn;
    Button emergencyNumbersBtn;
    Button settingsBtn;
    Button mLogoutBtn;

    TextView curiosities, resendVerificationTxt;
    Button resendVerification;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

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

        firebaseAuth = FirebaseAuth.getInstance();
        resendVerification = findViewById(R.id.resendVerification);
        resendVerificationTxt = findViewById(R.id.resendVerificationTxt);
        user = firebaseAuth.getCurrentUser();

        start();
    }

    private void start() {
        healthBtn.setOnClickListener(view -> {
            if (user.isEmailVerified()) {
                if (settings.getBoolean("first_time_health", true)) {
                    startActivity(new Intent(MainActivity.this, HealthForm.class));
                } else {
                    startActivity(new Intent(MainActivity.this, HealthActivity.class));
                }
            }
            else {
                Toast.makeText(this, "E-mail not verified", Toast.LENGTH_SHORT).show();
            }
        });
//        healthBtn.setOnClickListener(view -> startActivity(new Intent(this, HealthActivity.class)));
        entertainmentBtn.setOnClickListener(view -> {
            if (user.isEmailVerified()) {
                startActivity(new Intent(this, EntertainmentActivity.class));
            }
            else {
                Toast.makeText(this, "E-mail not verified", Toast.LENGTH_SHORT).show();
            }
        });

        if (!user.isEmailVerified()) {
            resendVerificationTxt.setVisibility(View.VISIBLE);
            resendVerification.setVisibility(View.VISIBLE);
            curiosities.setVisibility(View.GONE);

            resendVerification.setOnClickListener(view -> {
                assert user != null;
                user.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "E-mail sent successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(aVoid -> Toast.makeText(getApplicationContext(), "E-mail not sent (" + aVoid.getMessage() +")", Toast.LENGTH_SHORT).show());
            });
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}