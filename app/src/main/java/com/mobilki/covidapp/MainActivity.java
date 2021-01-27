package com.mobilki.covidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.authentication.Login;
import com.mobilki.covidapp.entertainment.EntertainmentActivity;
import com.mobilki.covidapp.health.HealthActivity;
import com.mobilki.covidapp.health.HealthForm;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    Button healthBtn;
    Button entertainmentBtn;
    Button emergencyNumbersBtn;
    Button mLogoutBtn;
    ImageButton polish;
    ImageButton english;
    SwitchCompat darkMode;

    TextView curiosities, resendVerificationTxt;
    Button resendVerification;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firestore;

    SharedPreferences settings;
    DocumentReference languageDocumentReference;

    Button emergencyNumbers;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            super.onCreate(savedInstanceState);
            finish();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        languageDocumentReference = firestore.collection("users").document(user.getUid()).collection("settings").document("language");
//        modeDocumentReference = firestore.collection("users").document(user.getUid()).collection("settings").document("mode");
        super.onCreate(savedInstanceState);
        languageDocumentReference.get().addOnSuccessListener(documentSnapshotLanguage -> {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Configuration conf = getResources().getConfiguration();
            conf.setLocale(new Locale(Optional.ofNullable(documentSnapshotLanguage.getString("language")).orElse("english").equals("polish") ? "pl" : "en"));
            getResources().updateConfiguration(conf, metrics);

            setContentView(R.layout.activity_main);
            setUp();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
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
        entertainmentBtn.setOnClickListener(view -> {
            if (user.isEmailVerified()) {
                startActivity(new Intent(this, EntertainmentActivity.class));
            }
            else {
                Toast.makeText(this, "E-mail not verified", Toast.LENGTH_SHORT).show();
            }
        });
        darkMode.setOnCheckedChangeListener((compoundButton, b) -> {
            //Map<String, Object> mode = new HashMap<>();
            if (compoundButton.isChecked()) {
                //mode.put("mode", "dark");
                settings.edit().putBoolean("darkModeOn", true).apply();
            }
            else {
                //mode.put("mode", "light");
                settings.edit().putBoolean("darkModeOn", false).apply();
            }
            recreate();
            //modeDocumentReference.set(mode);
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
        emergencyNumbers.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, EmergencyNumber.class)));


        polish.setOnClickListener(view -> setLocale("pl"));
        english.setOnClickListener(view -> setLocale("en"));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setLocale(String localCode) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration conf = getResources().getConfiguration();
        conf.setLocale(new Locale(localCode.toLowerCase()));
        getResources().updateConfiguration(conf, metrics);

        setFlagsVisibility(localCode);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFlagsVisibility(String localCode) {
        Map<String, Object> locale = new HashMap<>();
        if (localCode.equals("pl")) {
            locale.put("language", "polish");
            polish.setVisibility(View.GONE);
            english.setVisibility(View.VISIBLE);
        }
        else if (localCode.equals("en")) {
            locale.put("language", "english");
            polish.setVisibility(View.VISIBLE);
            english.setVisibility(View.GONE);
        }
        languageDocumentReference.set(locale);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUp() {
        healthBtn = findViewById(R.id.healthBtn);
        entertainmentBtn = findViewById(R.id.entertainmentBtn);
        emergencyNumbersBtn = findViewById(R.id.emergencyNumbersBtn);
        mLogoutBtn = findViewById(R.id.logout);
        polish = findViewById(R.id.polish);
        english = findViewById(R.id.english);
        darkMode = findViewById(R.id.darkMode);
        emergencyNumbers = findViewById(R.id.emergencyNumbersBtn);


        curiosities = findViewById(R.id.mainCuriositiesTxt);

        resendVerification = findViewById(R.id.resendVerification);
        resendVerificationTxt = findViewById(R.id.resendVerificationTxt);

        if (user == null) {
            finish();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        languageDocumentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            polish.setVisibility(Optional.ofNullable(documentSnapshot.getString("language")).orElse("english").equals("polish") ? View.GONE : View.VISIBLE);
            english.setVisibility(polish.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        darkMode.setChecked(settings.getBoolean("darkModeOn", false));

        if (user.isEmailVerified()) {
            DocumentReference documentReference = firestore.collection("users").document(user.getUid());
            documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
                if (documentSnapshot != null)
                    curiosities.setText(getResources().getString(R.string.hello) + documentSnapshot.getString("name"));
                else
                    curiosities.setText(getResources().getString(R.string.hello_unknown));
            });
        }

        start();
    }
}