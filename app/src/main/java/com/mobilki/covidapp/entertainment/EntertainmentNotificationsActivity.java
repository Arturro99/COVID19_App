package com.mobilki.covidapp.entertainment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.notification.NotificationHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntertainmentNotificationsActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotifications;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableSound;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableVibrations;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private Button apply;
    private TextView colon;
    private TextView chooseTxt;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_notifications);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        collectionReference = mFirestore
                .collection("users").document(mUser.getUid())
                .collection("settings");

        enableNotifications = findViewById(R.id.enableNotifications);
        enableSound = findViewById(R.id.enableSound);
        enableVibrations = findViewById(R.id.enableVibrs);
        hourPicker = findViewById(R.id.hours);
        minutePicker = findViewById(R.id.minutes);
        apply = findViewById(R.id.applyEntertainmentNotifications);
        colon = findViewById(R.id.colon);
        chooseTxt = findViewById(R.id.chooseTimeTxt);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        collectionReference.document("entertainmentNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotifications.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotifications.isChecked()) {
                    enableSound.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableSound")).orElse(false));
                    enableVibrations.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableVibrations")).orElse(false));
                    hourPicker.setValue(Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minutePicker.setValue(Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                }
            }
        });

        setVisibility(enableNotifications.isChecked());

        start();
    }

    private void start() {
        enableNotifications.setOnCheckedChangeListener((compoundButton, b) -> setVisibility(b));
        apply.setOnClickListener(x -> {
            apply();
            Intent intent = new Intent(this, EntertainmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setVisibility(boolean visible) {
        chooseTxt.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        colon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        hourPicker.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        minutePicker.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        enableVibrations.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        enableSound.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void apply() {
        DocumentReference documentReference = collectionReference.document("entertainmentNotifications");
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("enableNotifications", enableNotifications.isChecked());
        if (enableNotifications.isChecked()) {
            notifications.put("enableVibrations", enableVibrations.isChecked());
            notifications.put("enableSound", enableSound.isChecked());
            notifications.put("hour", hourPicker.getValue());
            notifications.put("minute", minutePicker.getValue());
            NotificationHelper.setNotification(getApplicationContext(), hourPicker.getValue(), minutePicker.getValue(), "Notyfikacja o rozrywce", "Weź coś obejrz", enableSound.isChecked(), enableVibrations.isChecked());
        }
        documentReference.set(notifications).addOnSuccessListener(x -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }
}