package com.mobilki.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HealthDataActivity extends AppCompatActivity {

    ImageButton nextDayWeightBtn, previousDayWeightBtn;
    ImageButton nextDayStepBtn, previousDayStepBtn;
    ImageButton nextDaySleepBtn, previousDaySleepBtn;
    ImageButton nextDayWaterBtn, previousDayWaterBtn;

    TextView weightDate;
    TextView stepDate;
    TextView sleepDate;
    TextView waterDate;

    Switch weightSwitch;
    Switch stepSwitch;
    Switch sleepSwitch;
    Switch waterSwitch;

    Group weightGroup;
    Group stepGroup;
    Group sleepGroup;
    Group waterGroup;

    SimpleDateFormat sdf;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_data);

        nextDayWeightBtn = findViewById(R.id.nextDayWeightBtn);
        nextDayStepBtn = findViewById(R.id.nextDayStepBtn);
        nextDaySleepBtn = findViewById(R.id.nextDaySleepBtn);
        nextDayWaterBtn = findViewById(R.id.nextDayWaterBtn);
        previousDayWeightBtn = findViewById(R.id.previousDayWeightBtn);
        previousDayStepBtn = findViewById(R.id.previousDayStepBtn);
        previousDaySleepBtn = findViewById(R.id.previousDaySleepBtn);
        previousDayWaterBtn = findViewById(R.id.previousDayWaterBtn);

        weightDate = findViewById(R.id.currentWeightDate);
        stepDate = findViewById(R.id.stepCounterDate);
        sleepDate = findViewById(R.id.sleepMeasuringDate);
        waterDate = findViewById(R.id.waterDrunkDate);

        weightSwitch = findViewById(R.id.currentWeightSwitch);
        stepSwitch = findViewById(R.id.stepCounterSwitch);
        sleepSwitch = findViewById(R.id.sleepMeasuringSwitch);
        waterSwitch = findViewById(R.id.waterDrunkSwitch);

        weightGroup = findViewById(R.id.weightGroup);
        stepGroup = findViewById(R.id.stepGroup);
        sleepGroup = findViewById(R.id.sleepGroup);
        waterGroup = findViewById(R.id.waterGroup);

        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(sdf.parse(currentDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weightDate.setText(currentDate);
        stepDate.setText(currentDate);
        sleepDate.setText(currentDate);
        waterDate.setText(currentDate);

        start();
    }

    private void start() {

        weightSwitch.setOnCheckedChangeListener((compoundButton, b) -> changeVisibility(weightGroup, compoundButton));
        stepSwitch.setOnCheckedChangeListener((compoundButton, b) -> changeVisibility(stepGroup, compoundButton));
        sleepSwitch.setOnCheckedChangeListener((compoundButton, b) -> changeVisibility(sleepGroup, compoundButton));
        waterSwitch.setOnCheckedChangeListener((compoundButton, b) -> changeVisibility(waterGroup, compoundButton));


        nextDayWeightBtn.setOnClickListener(view -> addDays(1, weightDate));
        previousDayWeightBtn.setOnClickListener(view -> addDays(-1, weightDate));

        nextDayStepBtn.setOnClickListener(view -> addDays(1, stepDate));
        previousDayStepBtn.setOnClickListener(view -> addDays(-1, stepDate));

        nextDaySleepBtn.setOnClickListener(view -> addDays(1, sleepDate));
        previousDaySleepBtn.setOnClickListener(view -> addDays(-1, sleepDate));

        nextDayWaterBtn.setOnClickListener(view -> addDays(1, waterDate));
        previousDayWaterBtn.setOnClickListener(view -> addDays(-1, waterDate));

    }

    //Metoda dodaje/odejmuje dni w kalendarzu, pobierając jako argument ich ilość oraz TextView, na którym data
    //będzie wyświetlona
    private void addDays(int amount, TextView textView) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(sdf.parse(textView.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, amount);
        String addedDay = sdf.format(calendar.getTime());
        textView.setText(addedDay);
    }

    //Metoda zmieniająca widoczność grup zawierających przyciski do zmieniania daty i TextView z datą
    private void changeVisibility(Group group, CompoundButton compoundButton) {
        if (compoundButton.isChecked()) {
            group.setVisibility(View.VISIBLE);
        } else {
            group.setVisibility(View.INVISIBLE);
        }
    }

}