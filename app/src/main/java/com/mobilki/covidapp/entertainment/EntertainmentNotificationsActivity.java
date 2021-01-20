package com.mobilki.covidapp.entertainment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.notification.NotificationHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntertainmentNotificationsActivity extends AppCompatActivity{

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotifications;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableSound;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableVibrations;
    private Button apply;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    Button chooseTimeBtn;
    int hourPicker;
    int minutePicker;
    MaterialTimePicker.Builder pickerBuilder;
    MaterialTimePicker picker;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_notifications);

        pickerBuilder = new MaterialTimePicker.Builder();
        picker = new MaterialTimePicker();


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        collectionReference = mFirestore
                .collection("users").document(mUser.getUid())
                .collection("settings");

        chooseTimeBtn = findViewById(R.id.chooseTimeBtnEnter);
        enableNotifications = findViewById(R.id.enableNotifications);
        enableSound = findViewById(R.id.enableSound);
        enableVibrations = findViewById(R.id.enableVibrs);
        apply = findViewById(R.id.applyEntertainmentNotifications);


        collectionReference.document("entertainmentNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotifications.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotifications.isChecked()) {
                    enableSound.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableSound")).orElse(false));
                    enableVibrations.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableVibrations")).orElse(false));
                    hourPicker = (Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minutePicker = (Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                    chooseTimeBtn.setText(hourPicker + ":" + minutePicker);
                }
            }
        });

        setVisibility(enableNotifications.isChecked());

        start();
    }

    private void start() {
        enableNotifications.setOnCheckedChangeListener((compoundButton, b) -> setVisibility(b));
        chooseTimeBtn.setOnClickListener(view -> pickTime());
        apply.setOnClickListener(x -> {
            apply();
            Intent intent = new Intent(this, EntertainmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    @SuppressLint("SetTextI18n")
    public void pickTime() {
        pickerBuilder
                .setTitleText(R.string.time_picker)
                .setTimeFormat(TimeFormat.CLOCK_24H);
        picker = pickerBuilder.build();
        picker.show(getSupportFragmentManager(), "time picker");
        picker.addOnPositiveButtonClickListener(view -> {
            hourPicker = picker.getHour();
            minutePicker = picker.getMinute();
            chooseTimeBtn.setText(hourPicker + ":" + minutePicker);
        });
    }

    private void setVisibility(boolean visible) {
        chooseTimeBtn.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
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
            notifications.put("hour", hourPicker);
            notifications.put("minute", minutePicker);
            NotificationHelper.setNotification(getApplicationContext(), hourPicker, minutePicker, "Notyfikacja o rozrywce", "Weź coś obejrz", enableSound.isChecked(), enableVibrations.isChecked());
        }
        documentReference.set(notifications).addOnSuccessListener(x -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }
}