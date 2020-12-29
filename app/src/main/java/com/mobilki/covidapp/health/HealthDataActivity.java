package com.mobilki.covidapp.health;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.ViewCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mobilki.covidapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

    Switch stepSwitch;
    Switch sleepSwitch;
    Switch waterSwitch;

    Group weightGroup;
    Group stepGroup;
    Group sleepGroup;
    Group waterGroup;

    EditText weightValue;
    EditText stepsValue;
    EditText sleepValue;
    EditText waterValue;

    Button addBtn;
    TextView title;

    SimpleDateFormat sdf;
    Calendar calendar;
    public static final String VIEW_NAME_HEADER_TITLE = "activity:header:title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setExitTransition(new Explode());
//        getWindow().setAllowEnterTransitionOverlap(true);
//        getWindow().setAllowReturnTransitionOverlap(true);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        stepSwitch = findViewById(R.id.stepCounterSwitch);
        sleepSwitch = findViewById(R.id.sleepMeasuringSwitch);
        waterSwitch = findViewById(R.id.waterDrunkSwitch);

        weightGroup = findViewById(R.id.weightGroup);
        stepGroup = findViewById(R.id.stepGroup);
        sleepGroup = findViewById(R.id.sleepGroup);
        waterGroup = findViewById(R.id.waterGroup);


        addBtn = findViewById(R.id.button);

        weightValue = findViewById(R.id.currentWeightValue);
        stepsValue = findViewById(R.id.currentStepsValue);
        sleepValue = findViewById(R.id.currentSleepValue);
        waterValue = findViewById(R.id.currentWaterValue);

        title = findViewById(R.id.titleaaaa);
        ViewCompat.setTransitionName(title, VIEW_NAME_HEADER_TITLE);
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
//        addTransitionListener();
        start();

        addBtn.setOnClickListener(view -> datab());
    }


    private void datab() {
        if (Integer.parseInt(weightValue.getText().toString()) > 0)
            addToDb(weightDate.getText().toString(), "weight", Integer.parseInt(weightValue.getText().toString()));
        if (Integer.parseInt(stepsValue.getText().toString()) > 0)
            addToDb(stepDate.getText().toString(), "steps", Integer.parseInt(stepsValue.getText().toString()));
        if (Integer.parseInt(sleepValue.getText().toString()) > 0)
            addToDb(sleepDate.getText().toString(), "sleep", Integer.parseInt(sleepValue.getText().toString()));
        if (Integer.parseInt(waterValue.getText().toString()) > 0)
            addToDb(waterDate.getText().toString(), "water", Integer.parseInt(waterValue.getText().toString()));
    }

    private void addToDb(String date, String value, Integer intValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        db.collection("users").document(mFirebaseAuth.getCurrentUser().getUid()).collection("health_data").document(date).set(new HashMap<String, Integer>()
        {
            {
                put(value, intValue);
            }
        }, SetOptions.merge())
                .addOnSuccessListener(documentReference -> Log.d("DocSnippets", "DocumentSnapshot added" ))
                .addOnFailureListener(e -> Log.w("DocSnippets", "Error adding document", e));
    }

    private void start() {

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

//    /**
//     * Try and add a {@link Transition.TransitionListener} to the entering shared element
//     * {@link Transition}. We do this so that we can load the full-size image after the transition
//     * has completed.
//     *
//     * @return true if we were successful in adding a listener to the enter transition
//     */
//    @RequiresApi(21)
//    private boolean addTransitionListener() {
//        final Transition transition = getWindow().getSharedElementEnterTransition();
//
//        if (transition != null) {
//            // There is an entering shared element transition so add a listener to it
//            transition.addListener(new Transition.TransitionListener() {
//                @Override
//                public void onTransitionEnd(Transition transition) {
//                    // As the transition has ended, we can now load the full-size image
//                    // Make sure we remove ourselves as a listener
//                    transition.removeListener(this);
//                }
//
//                @Override
//                public void onTransitionStart(Transition transition) {
//                    // No-op
//                }
//
//                @Override
//                public void onTransitionCancel(Transition transition) {
//                    // Make sure we remove ourselves as a listener
//                    transition.removeListener(this);
//                }
//
//                @Override
//                public void onTransitionPause(Transition transition) {
//                    // No-op
//                }
//
//                @Override
//                public void onTransitionResume(Transition transition) {
//                    // No-op
//                }
//            });
//            return true;
//        }
//
//        // If we reach here then we have not added a listener
//        return false;
//    }
}