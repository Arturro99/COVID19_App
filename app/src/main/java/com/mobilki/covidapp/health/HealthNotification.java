package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;

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

public class HealthNotification extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final String PERSONAL_CHANNEL_ID = "PERSONAL";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotiExercises;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotiWater;

    Button timePickerWater;
    Button timePickerExer;

    private Button setWaterNoti;
    private Button setExerNoti;

    private Group exerGroupNoti;
    private Group waterGroupNoti;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    TimePickerDialog picker;
    Button clickedButton;

    int hourWater;
    int hourExer;
    int minuteWater;
    int minuteExer;

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

        timePickerWater = findViewById(R.id.setTimeWaterBtn);
        timePickerExer = findViewById(R.id.setTimeExercBtn);

        setWaterNoti = findViewById(R.id.setWaterNoti);
        setExerNoti = findViewById(R.id.setNotiExer);

        exerGroupNoti = findViewById(R.id.exerGroupNoti);
        waterGroupNoti = findViewById(R.id.waterGroupNoti);

//        hourWater.setMinValue(0);
//        hourExer.setMinValue(0);
//        hourWater.setMaxValue(23);
//        hourExer.setMaxValue(23);
//        minuteWater.setMinValue(0);
//        minuteExer.setMinValue(0);
//        minuteWater.setMaxValue(59);
//        minuteExer.setMaxValue(59);

        collectionReference.document("waterNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotiWater.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotiWater.isChecked()) {
                    hourWater = (Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minuteWater = (Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                    timePickerWater.setText(hourWater + ":" + minuteWater);
                }
            }
        });

        collectionReference.document("exerciseNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotiExercises.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotiExercises.isChecked()) {
                    hourExer = (Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minuteExer = (Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                    timePickerWater.setText(hourExer + ":" + minuteExer);
                }
            }
        });

        exerGroupNoti.setVisibility(enableNotiWater.isChecked()? View.VISIBLE : View.INVISIBLE);
        waterGroupNoti.setVisibility(enableNotiExercises.isChecked()? View.VISIBLE : View.INVISIBLE);





        timePickerWater.setOnClickListener(view -> pickTime(timePickerWater));
        timePickerExer.setOnClickListener(view -> pickTime(timePickerExer));


        start();
    }

    public void pickTime(Button button) {
        clickedButton = button;
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int h, int m) {
        if (clickedButton == timePickerExer) {
            hourExer = h;
            minuteExer = m;
        } else if (clickedButton == timePickerWater) {
            hourWater = h;
            minuteWater = m;
        }
        clickedButton.setText(h + ":" + m);
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

    private void apply(String path, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch enableNotifications, Integer hour , Integer minute, String title, String body) {
        DocumentReference documentReference = collectionReference.document(path);
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("enableNotifications", enableNotifications.isChecked());
        if (enableNotifications.isChecked()) {
            notifications.put("hour", hour);
            notifications.put("minute", minute);
            NotificationHelper.setNotification(getApplicationContext(), hour, minute, title, body);
        }
        documentReference.set(notifications).addOnSuccessListener(x -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }


}