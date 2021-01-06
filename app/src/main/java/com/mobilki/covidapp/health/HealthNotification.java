package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

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

public class HealthNotification extends AppCompatActivity {

    public static final String PERSONAL_CHANNEL_ID = "PERSONAL";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotiExercises;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotiWater;

    private NumberPicker hourWater;
    private NumberPicker hourExer;
    private NumberPicker minuteWater;
    private NumberPicker minuteExer;
    private Button setWaterNoti;
    private Button setExerNoti;

    private Group exerGroupNoti;
    private Group waterGroupNoti;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_health);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        collectionReference = mFirestore
                .collection("users").document(mUser.getUid())
                .collection("settings");

        enableNotiExercises = findViewById(R.id.enableNotiExercises);
        enableNotiWater = findViewById(R.id.enableNotiWater);

        hourWater = findViewById(R.id.hoursWater);
        hourExer = findViewById(R.id.hoursExer);
        minuteWater = findViewById(R.id.minutesWater);
        minuteExer = findViewById(R.id.minutesExer);

        setWaterNoti = findViewById(R.id.setWaterNoti);
        setExerNoti = findViewById(R.id.setNotiExer);

        exerGroupNoti = findViewById(R.id.exerGroupNoti);
        waterGroupNoti = findViewById(R.id.waterGroupNoti);

        hourWater.setMinValue(0);
        hourExer.setMinValue(0);
        hourWater.setMaxValue(23);
        hourExer.setMaxValue(23);
        minuteWater.setMinValue(0);
        minuteExer.setMinValue(0);
        minuteWater.setMaxValue(59);
        minuteExer.setMaxValue(59);

        collectionReference.document("waterNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotiWater.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotiWater.isChecked()) {
                    hourWater.setValue(Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minuteWater.setValue(Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                }
            }
        });

        collectionReference.document("exerciseNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotiExercises.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotiExercises.isChecked()) {
                    hourExer.setValue(Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minuteExer.setValue(Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                }
            }
        });

        exerGroupNoti.setVisibility(enableNotiWater.isChecked()? View.VISIBLE : View.INVISIBLE);
        waterGroupNoti.setVisibility(enableNotiExercises.isChecked()? View.VISIBLE : View.INVISIBLE);

        start();
    }

    private void start() {
        enableNotiWater.setOnCheckedChangeListener((compoundButton, b) -> waterGroupNoti.setVisibility(b? View.VISIBLE : View.INVISIBLE));
        enableNotiExercises.setOnCheckedChangeListener((compoundButton, b) -> exerGroupNoti.setVisibility(b? View.VISIBLE : View.INVISIBLE));
        setWaterNoti.setOnClickListener(view -> {
            apply("waterNotifications", enableNotiWater, hourWater, minuteWater, "Napij się wody", "Woda jest zdrowa");
        });
        setExerNoti.setOnClickListener(view -> {
            apply("exerciseNotifications", enableNotiExercises, hourExer, minuteExer, "Pora na ćwiczenia", "Co powiesz na 20 pompek?");
        });
    }

    private void apply(String path, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch enableNotifications, NumberPicker hourPicker, NumberPicker minutePicker, String title, String body) {
        DocumentReference documentReference = collectionReference.document(path);
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("enableNotifications", enableNotifications.isChecked());
        if (enableNotifications.isChecked()) {
            notifications.put("hour", hourPicker.getValue());
            notifications.put("minute", minutePicker.getValue());
            NotificationHelper.setNotification(getApplicationContext(), hourPicker.getValue(), minutePicker.getValue(), title, body);
        }
        documentReference.set(notifications).addOnSuccessListener(x -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }
}