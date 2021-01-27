package com.mobilki.covidapp.entertainment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;
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

    private TextView day1;
    private TextView day2;
    private TextView day3;
    private TextView day4;
    private TextView day5;
    private TextView day6;
    private TextView day7;

    ArrayList<Long> days;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    Button chooseTimeBtn;
    int hourPicker;
    int minutePicker;
    MaterialTimePicker.Builder pickerBuilder;
    MaterialTimePicker picker;

    SharedPreferences settings;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);

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

        day1 = findViewById(R.id.buttonEntertainmentDay1);
        day2 = findViewById(R.id.buttonEntertainmentDay2);
        day3 = findViewById(R.id.buttonEntertainmentDay3);
        day4 = findViewById(R.id.buttonEntertainmentDay4);
        day5 = findViewById(R.id.buttonEntertainmentDay5);
        day6 = findViewById(R.id.buttonEntertainmentDay6);
        day7 = findViewById(R.id.buttonEntertainmentDay7);
        days = new ArrayList<>();

        setClickDayBtn(day1, days, 2);
        setClickDayBtn(day2, days, 3);
        setClickDayBtn(day3, days, 4);
        setClickDayBtn(day4, days, 5);
        setClickDayBtn(day5, days, 6);
        setClickDayBtn(day6, days, 7);
        setClickDayBtn(day7, days, 1);


        collectionReference.document("entertainmentNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotifications.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                if (enableNotifications.isChecked()) {
                    enableSound.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableSound")).orElse(false));
                    enableVibrations.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableVibrations")).orElse(false));
                    hourPicker = (Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                    minutePicker = (Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                    chooseTimeBtn.setText(hourPicker + ":" + minutePicker);
                    if (documentSnapshot.contains("list")) {
                        ArrayList<Long> tmp = (ArrayList<Long>) (documentSnapshot.get("list"));
                        days.addAll(tmp);
                        changeAppearanceBtn(days, "water");
                    }
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

    private void setClickDayBtn(TextView dayBtn, ArrayList<Long> list, long day) {
        dayBtn.setOnClickListener(view -> {
            if (list.contains(day)) {
                dayBtn.setTextAppearance(R.style.Widget_MaterialComponents_Button_OutlinedButton);
                dayBtn.setBackgroundResource(R.drawable.button_not_pressed);
                list.remove(day);
            } else {
                dayBtn.setTextAppearance(R.style.Widget_MaterialComponents_Button);
                dayBtn.setBackgroundResource(R.drawable.button_pressed);
                list.add(day);
            }
            int a;
        });
    }

    private void changeAppearanceBtn(ArrayList<Long> days, String str) {
        ArrayList<TextView> entertainments =
                new ArrayList<>(Arrays.asList(day7, day1, day2, day3, day4, day5, day6));
        long i = 1;
        for (TextView text: entertainments) {
            text.setTextAppearance(days.contains(i) ? R.style.Widget_MaterialComponents_Button
                    : R.style.Widget_MaterialComponents_Button_OutlinedButton);
            text.setBackgroundResource(days.contains(i) ? R.drawable.button_pressed : R.drawable.button_not_pressed);
            i++;
        }
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
            notifications.put("list", days);
            for (Long day: days) {
                NotificationHelper.setNotification(getApplicationContext(), hourPicker, minutePicker, getResources().getString(R.string.notification_entertainment_title),
                        getResources().getString(R.string.notification_entertainment_body), enableSound.isChecked(), enableVibrations.isChecked(), day);
            }
        }
        documentReference.set(notifications).addOnSuccessListener(x -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }
}