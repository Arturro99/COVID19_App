package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.notification.NotificationHelper;
import com.mobilki.covidapp.pickers.TimePickerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HealthNotification extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotiExercises;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enableNotiWater;

    Button timePickerWater;
    Button timePickerExer;

    private Button setWaterNoti;
    private Button setExerNoti;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    Button clickedButton;

    int hourWater;
    int hourExer;
    int minuteWater;
    int minuteExer;

    TextView buttonWaterDay1;
    TextView buttonWaterDay2;
    TextView buttonWaterDay3;
    TextView buttonWaterDay4;
    TextView buttonWaterDay5;
    TextView buttonWaterDay6;
    TextView buttonWaterDay7;

    ArrayList<Long> waterDays = new ArrayList<>();

    LinearLayout linearLayoutWater;


    TextView buttonExerciseDay1;
    TextView buttonExerciseDay2;
    TextView buttonExerciseDay3;
    TextView buttonExerciseDay4;
    TextView buttonExerciseDay5;
    TextView buttonExerciseDay6;
    TextView buttonExerciseDay7;

    ArrayList<Long> exerciseDays = new ArrayList<>();

    LinearLayout linearLayoutExercise;

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


        collectionReference.document("waterNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotiWater.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                hourWater = (Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                minuteWater = (Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                if (documentSnapshot.contains("list")) {
                    ArrayList<Long> tmp = (ArrayList<Long>) (documentSnapshot.get("list"));
                    waterDays.addAll(tmp);
                    changeAppearanceBtn(waterDays, "water");
                }
                timePickerWater.setText(hourWater + ":" + minuteWater);
            }
        });

        collectionReference.document("exerciseNotifications").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                enableNotiExercises.setChecked(Optional.ofNullable(documentSnapshot.getBoolean("enableNotifications")).orElse(false));
                hourExer = (Optional.ofNullable(documentSnapshot.getLong("hour")).orElse(19L).intValue());
                minuteExer = (Optional.ofNullable(documentSnapshot.getLong("minute")).orElse(30L).intValue());
                if (documentSnapshot.contains("list")) {
                    ArrayList<Long> tmp2 = (ArrayList<Long>)(documentSnapshot.get("list"));
                    exerciseDays.addAll(tmp2);
                    changeAppearanceBtn(exerciseDays, "exercise");
                }
                timePickerExer.setText(hourExer + ":" + minuteExer);
            }
        });

        linearLayoutWater = findViewById(R.id.linearLayoutWater);
        linearLayoutWater.setVisibility(enableNotiWater.isChecked()? View.VISIBLE : View.INVISIBLE);
        setWaterNoti.setVisibility(enableNotiWater.isChecked()? View.VISIBLE : View.INVISIBLE);
        timePickerWater.setVisibility(enableNotiWater.isChecked()? View.VISIBLE : View.INVISIBLE);

        linearLayoutExercise = findViewById(R.id.linearLayoutExercise);
        linearLayoutExercise.setVisibility(enableNotiExercises.isChecked()? View.VISIBLE : View.INVISIBLE);
        setExerNoti.setVisibility(enableNotiExercises.isChecked()? View.VISIBLE : View.INVISIBLE);
        timePickerExer.setVisibility(enableNotiExercises.isChecked()? View.VISIBLE : View.INVISIBLE);

        timePickerWater.setOnClickListener(view -> pickTime(timePickerWater));
        timePickerExer.setOnClickListener(view -> pickTime(timePickerExer));

        buttonWaterDay1 = findViewById(R.id.buttonWaterDay1);
        buttonWaterDay2 = findViewById(R.id.buttonWaterDay2);
        buttonWaterDay3 = findViewById(R.id.buttonWaterDay3);
        buttonWaterDay4 = findViewById(R.id.buttonWaterDay4);
        buttonWaterDay5 = findViewById(R.id.buttonWaterDay5);
        buttonWaterDay6 = findViewById(R.id.buttonWaterDay6);
        buttonWaterDay7 = findViewById(R.id.buttonWaterDay7);

        setClickDayBtn(buttonWaterDay1, waterDays, 2);
        setClickDayBtn(buttonWaterDay2, waterDays, 3);
        setClickDayBtn(buttonWaterDay3, waterDays, 4);
        setClickDayBtn(buttonWaterDay4, waterDays, 5);
        setClickDayBtn(buttonWaterDay5, waterDays, 6);
        setClickDayBtn(buttonWaterDay6, waterDays, 7);
        setClickDayBtn(buttonWaterDay7, waterDays, 1);

        buttonExerciseDay1 = findViewById(R.id.buttonExerDay1);
        buttonExerciseDay2 = findViewById(R.id.buttonExerDay2);
        buttonExerciseDay3 = findViewById(R.id.buttonExerDay3);
        buttonExerciseDay4 = findViewById(R.id.buttonExerDay4);
        buttonExerciseDay5 = findViewById(R.id.buttonExerDay5);
        buttonExerciseDay6 = findViewById(R.id.buttonExerDay6);
        buttonExerciseDay7 = findViewById(R.id.buttonExerDay7);

        setClickDayBtn(buttonExerciseDay1, exerciseDays, 2);
        setClickDayBtn(buttonExerciseDay2, exerciseDays, 3);
        setClickDayBtn(buttonExerciseDay3, exerciseDays, 4);
        setClickDayBtn(buttonExerciseDay4, exerciseDays, 5);
        setClickDayBtn(buttonExerciseDay5, exerciseDays, 6);
        setClickDayBtn(buttonExerciseDay6, exerciseDays, 7);
        setClickDayBtn(buttonExerciseDay7, exerciseDays, 1);

        start();
    }

    private void setClickDayBtn(TextView dayBtn, ArrayList<Long> list, long day) {
        dayBtn.setOnClickListener(view -> {
            if (list.contains(day)) {
                dayBtn.setTextAppearance(R.style.Widget_MaterialComponents_Button_OutlinedButton);
                dayBtn.setBackgroundResource(R.drawable.button_not_pressed);
                list.remove((Long) day);
            } else {
                dayBtn.setTextAppearance(R.style.Widget_MaterialComponents_Button);
                dayBtn.setBackgroundResource(R.drawable.button_pressed);
                list.add(day);
            }
            int a;
        });
    }

    private void changeAppearanceBtn(ArrayList<Long> days, String str) {
        ArrayList<TextView> water = new ArrayList<>(Arrays.asList(buttonWaterDay7, buttonWaterDay1, buttonWaterDay2, buttonWaterDay3,
                buttonWaterDay4, buttonWaterDay5, buttonWaterDay6));
        ArrayList<TextView> exercise = new ArrayList<>(Arrays.asList(buttonExerciseDay7, buttonExerciseDay1, buttonExerciseDay2, buttonExerciseDay3,
                buttonExerciseDay4, buttonExerciseDay5, buttonExerciseDay6));
        if (str.equals("water")) {
            long i = 1;
            for (TextView text: water) {
                text.setTextAppearance(days.contains(i) ? R.style.Widget_MaterialComponents_Button
                        : R.style.Widget_MaterialComponents_Button_OutlinedButton);
                text.setBackgroundResource(days.contains(i) ? R.drawable.button_pressed : R.drawable.button_not_pressed);
                i++;
            }
        }
        if (str.equals("exercise")) {
            long i = 1;
            for (TextView text: exercise) {
                text.setTextAppearance(days.contains(i) ? R.style.Widget_MaterialComponents_Button
                        : R.style.Widget_MaterialComponents_Button_OutlinedButton);
                text.setBackgroundResource(days.contains(i) ? R.drawable.button_pressed : R.drawable.button_not_pressed);
                i++;
            }
        }
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
        enableNotiWater.setOnCheckedChangeListener((compoundButton, b) -> {
            linearLayoutWater.setVisibility(b? View.VISIBLE : View.INVISIBLE);
            setWaterNoti.setVisibility(b? View.VISIBLE : View.INVISIBLE);
            timePickerWater.setVisibility(b? View.VISIBLE : View.INVISIBLE);
            if (!enableNotiWater.isChecked()) {
                DocumentReference documentReference = collectionReference.document("waterNotifications");
                Map<String, Object> notifications = new HashMap<>();
                notifications.put("enableNotifications", false);
                documentReference.set(notifications);
            }
        });
        enableNotiExercises.setOnCheckedChangeListener((compoundButton, b) -> {
            linearLayoutExercise.setVisibility(b? View.VISIBLE : View.INVISIBLE);
            setExerNoti.setVisibility(b? View.VISIBLE : View.INVISIBLE);
            timePickerExer.setVisibility(b? View.VISIBLE : View.INVISIBLE);
            if (!enableNotiExercises.isChecked()) {
                DocumentReference documentReference = collectionReference.document("exerciseNotifications");
                Map<String, Object> notifications = new HashMap<>();
                notifications.put("enableNotifications", false);
                documentReference.set(notifications, SetOptions.merge());
            }

        });
        setWaterNoti.setOnClickListener(view -> {
            apply("waterNotifications", enableNotiWater, hourWater, minuteWater, "Napij się wody", "Woda jest zdrowa", waterDays);
        });
        setExerNoti.setOnClickListener(view -> {
            apply("exerciseNotifications", enableNotiExercises, hourExer, minuteExer, "Pora na ćwiczenia", "Co powiesz na 20 pompek?", exerciseDays);
        });
    }

    private void apply(String path, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch enableNotifications, Integer hour , Integer minute, String title, String body, ArrayList<Long> list) {
        DocumentReference documentReference = collectionReference.document(path);
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("enableNotifications", enableNotifications.isChecked());
        if (enableNotifications.isChecked()) {
            notifications.put("hour", hour);
            notifications.put("minute", minute);
            notifications.put("list", list);
            for (Long day: list) {
                NotificationHelper.setNotification(getApplicationContext(), hour, minute, title, body, day);
            }
        }
        documentReference.set(notifications, SetOptions.merge()).addOnSuccessListener(x -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }
}