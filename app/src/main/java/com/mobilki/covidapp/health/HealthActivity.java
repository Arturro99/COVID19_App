package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.repository.ExerciseRepository;
import com.mobilki.covidapp.pickers.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Objects;

public class HealthActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, DatePickerDialog.OnDateSetListener {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    public static final int DURATION_MILLIS = 500;
    BarChart barChart;
    BarDataSet barDataSet;
    BarData barData;
    private GestureDetector gestureDetector;
    ArrayList<BarEntry> weightEntries;
    ArrayList<BarEntry> stepsEntries;
    ArrayList<BarEntry> sleepEntries;
    ArrayList<BarEntry> waterEntries;
    ArrayList<ArrayList<BarEntry>> entries;
    TextView healthBarChartTitle;
    int entry;
    String[] labels;
    Hashtable<Integer, Integer> dict = new Hashtable<Integer, Integer>();

    ExerciseRepository exerciseRepository;
    BottomNavigationView bottomNavigationView;

    EditText numberStepsSet;
    EditText numberWeightSet;
    EditText numberSleepSet;
    EditText numberWaterSet;

    Button pickDateStepsBtn;
    Button pickDateWeightBtn;
    Button pickDateSleepBtn;
    Button pickDateWaterBtn;
    Button clickedButton;

    Button setStepsBtn;
    Button setWeightBtn;
    Button setSleepBtn;
    Button setWaterBtn;

    ImageView notificationBtn;
    ImageView settingsBtn;

    ExercisesRepository repo;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;

    TextView exerciseTitle1;
    TextView exerciseTitle2;
    TextView exerciseTitle3;
    TextView exerciseTitle4;
    TextView exerciseTitle5;
    TextView exerciseTitle6;

    ImageView exerciseYT1;
    ImageView exerciseYT2;
    ImageView exerciseYT3;
    ImageView exerciseYT4;
    ImageView exerciseYT5;
    ImageView exerciseYT6;

    TextView exerciseDescription1;
    TextView exerciseDescription2;
    TextView exerciseDescription3;
    TextView exerciseDescription4;
    TextView exerciseDescription5;
    TextView exerciseDescription6;

    ImageView exerciseArrow1;
    ImageView exerciseArrow2;
    ImageView exerciseArrow3;
    ImageView exerciseArrow4;
    ImageView exerciseArrow5;
    ImageView exerciseArrow6;

    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    ImageView dot4;
    ArrayList<ImageView> dots;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_preferences), 0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_health_new);

//        exerciseSetBtn = findViewById(R.id.exerciseSetBtn);
//        addDataBtn = findViewById(R.id.addDataBtn);
//        notificationsSettingsBtn = findViewById(R.id.healthNotificationsSettingsBtn);
//        preferencesBtn = findViewById(R.id.healthPreferencesBtn);



        if (settings.getBoolean("first_time_health", true)) {
            startActivity(new Intent(HealthActivity.this, HealthForm.class));
        }


        barChart = findViewById(R.id.barchart);
        labels = new String[7];
        InitBarChart();
        weightEntries = new ArrayList<>();
        stepsEntries = new ArrayList<>();
        sleepEntries = new ArrayList<>();
        waterEntries = new ArrayList<>();
        entries = new ArrayList<>();
        healthBarChartTitle = findViewById(R.id.healthBarChartTitle);

        dict.put(0, R.string.steps_title_barchart);
        dict.put(1, R.string.weight_title_barchart);
        dict.put(2, R.string.sleep_title_barchart);
        dict.put(3, R.string.water_title_barchart);
        exerciseRepository = new ExerciseRepository();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        gestureDetector = new GestureDetector(this);
        start();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Group page1group = findViewById(R.id.page1group);
            Group page2group = findViewById(R.id.page2group);
            Group page3group = findViewById(R.id.page3group);

            if (itemId == R.id.page_1) {
                page1group.setVisibility(View.VISIBLE);
                setEntries();
                setDots(dots.get(0));
            } else {
                page1group.setVisibility(View.GONE);
            }
            page2group.setVisibility(itemId == R.id.page_2 ? View.VISIBLE : View.GONE);
            page3group.setVisibility(itemId == R.id.page_3 ? View.VISIBLE : View.GONE);

            return true;
        });



        notificationBtn = findViewById(R.id.notificationBtnHealth);

        notificationBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthNotification.class)));

        settingsBtn = findViewById(R.id.settingBtnHealth);

        settingsBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthForm.class)));

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        collectionReference = mFirestore
                .collection("exercises");
        repo = new ExercisesRepository();
        repo.getSettings();
        getExercisesFromDB();

    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getExercisesFromDB() {
        collectionReference.get().addOnSuccessListener(document -> {
            if (document != null) {
                int i = 0;
                for (DocumentSnapshot doc : document.getDocuments()) {
                    Exercise.TypeExercise type = null;
                    switch (doc.getString("type")) {
                        case "UPPER":
                            type = Exercise.TypeExercise.UPPER;
                            break;
                        case "LOWER":
                            type = Exercise.TypeExercise.LOWER;
                            break;
                        case "CONDITION":
                            type = Exercise.TypeExercise.CONDITION;
                            break;
                    }
                    repo.add(new com.mobilki.covidapp.health.Exercise(
                                type,
                                doc.getBoolean("goodShoulders"),
                                doc.getBoolean("goodBack"),
                                doc.getBoolean("goodWrists"),
                                doc.getBoolean("goodKnees"),
                                doc.getBoolean("goodElbows"),
                                doc.getBoolean("goodHip"),
                                doc.getString("name_pl"),
                                doc.getString("name_en"),
                                doc.getLong("minReps").intValue(),
                                doc.getLong("maxReps").intValue(),
                                doc.getString("description_pl"),
                                doc.getString("description_en"),
                                doc.getString("yt")
                            )

                    );
                    i++;

                }
                if (i >= document.size()) {
                    initExercises();
                }
            }
        });
    }

    private void initExercises() {
        exerciseTitle1 = findViewById(R.id.exercise1Title);
        exerciseTitle2 = findViewById(R.id.exercise2Title);
        exerciseTitle3 = findViewById(R.id.exercise3Title);
        exerciseTitle4 = findViewById(R.id.exercise4Title);
        exerciseTitle5 = findViewById(R.id.exercise5Title);
        exerciseTitle6 = findViewById(R.id.exercise6Title);

        exerciseYT1 = findViewById(R.id.exercise1YT);
        exerciseYT2 = findViewById(R.id.exercise2YT);
        exerciseYT3 = findViewById(R.id.exercise3YT);
        exerciseYT4 = findViewById(R.id.exercise4YT);
        exerciseYT5 = findViewById(R.id.exercise5YT);
        exerciseYT6 = findViewById(R.id.exercise6YT);


        exerciseDescription1 = findViewById(R.id.exercise1Description);
        exerciseDescription2 = findViewById(R.id.exercise2Description);
        exerciseDescription3 = findViewById(R.id.exercise3Description);
        exerciseDescription4 = findViewById(R.id.exercise4Description);
        exerciseDescription5 = findViewById(R.id.exercise5Description);
        exerciseDescription6 = findViewById(R.id.exercise6Description);

        exerciseArrow1 = findViewById(R.id.exercise1Arrow);
        exerciseArrow2 = findViewById(R.id.exercise2Arrow);
        exerciseArrow3 = findViewById(R.id.exercise3Arrow);
        exerciseArrow4 = findViewById(R.id.exercise4Arrow);
        exerciseArrow5 = findViewById(R.id.exercise5Arrow);
        exerciseArrow6 = findViewById(R.id.exercise6Arrow);




        showDescription(exerciseTitle1, exerciseDescription1, exerciseArrow1);
        showDescription(exerciseTitle2, exerciseDescription2, exerciseArrow2);
        showDescription(exerciseTitle3, exerciseDescription3, exerciseArrow3);
        showDescription(exerciseTitle4, exerciseDescription4, exerciseArrow4);
        showDescription(exerciseTitle5, exerciseDescription5, exerciseArrow5);
        showDescription(exerciseTitle6, exerciseDescription6, exerciseArrow6);

        setExercise(exerciseTitle1, exerciseDescription1, exerciseYT1, Exercise.TypeExercise.UPPER);
        setExercise(exerciseTitle2, exerciseDescription2, exerciseYT2, Exercise.TypeExercise.LOWER);
        setExercise(exerciseTitle3, exerciseDescription3, exerciseYT3, Exercise.TypeExercise.CONDITION);
        setExercise(exerciseTitle4, exerciseDescription4, exerciseYT4, Exercise.TypeExercise.UPPER);
        setExercise(exerciseTitle5, exerciseDescription5, exerciseYT5, Exercise.TypeExercise.LOWER);
        setExercise(exerciseTitle6, exerciseDescription6, exerciseYT6, Exercise.TypeExercise.CONDITION);
    }

    private void showDescription(TextView title, TextView desc, ImageView arrow) {
        arrow.setOnClickListener(view -> {
            desc.setVisibility(desc.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            arrow.setImageResource(desc.getVisibility() == View.GONE ? R.drawable.arrow_down : R.drawable.arrow_up);
        });
        title.setOnClickListener(view -> {
            desc.setVisibility(desc.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            arrow.setImageResource(desc.getVisibility() == View.GONE ? R.drawable.arrow_down : R.drawable.arrow_up);
        });
    }

    private void setExercise(TextView title, TextView desc, ImageView yt, Exercise.TypeExercise type) {
        ExerciseToApp tmp = repo.get(type);
        title.setText(tmp.repsAndTitle);
        desc.setText(tmp.description);
        yt.setOnClickListener(view -> {
            Intent intent = new Intent(HealthActivity.this, PlayerYT.class);
            intent.putExtra("url", tmp.urlYT);
            startActivity(intent);
        });
    }

    //adding exercises to db
//    private void addExercises() {
//
//
//
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Random rand = new Random();
//        for (int i = 0; i< 50; i++) {
//
//            List<Boolean> good = new ArrayList<>();
//            for (int j =0; j<6; j++) {
//                good.add(false);
//            }
//            for (int j =0; j<2; j++) {
//                int r = rand.nextInt(5);
//                good.set(r, true);
//            }
//            String name = i + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam eget leo vehicula lorem placerat tincidunt. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nunc mattis tellus et vehicula lobortis. Vestibulum malesuada dolor dolor, vel lacinia nisl convallis non. Nunc elit ante, pretium quis scelerisque ac, congue at augue. Suspendisse semper imperdiet aliquet. Praesent bibendum tortor accumsan, interdum nisi dignissim, condimentum diam.";
//            String finalName = name;
//            int finalI = i;
//            db.collection("exercises").document("upper "+ i).set(new HashMap<String, Object>()
//            {
//                {
//                    put("type", "UPPER");
//                    put("goodShoulders", good.get(0));
//                    put("goodBack", good.get(1));
//                    put("goodWrists", good.get(2));
//                    put("goodKnees", good.get(3));
//                    put("goodElbows", good.get(4));
//                    put("goodHip", good.get(5));
//                    put("name_pl", "pl upper" + finalI);
//                    put("name_en", "en upper" + finalI);
//                    put("minReps", rand.nextInt(10));
//                    put("maxReps", rand.nextInt(20)+10);
//                    put("description_pl", "pl_des upper" + finalName);
//                    put("description_en", "en_des upper" + finalName);
//                    put("yt", "uxPdPpi5W4o");
//                }
//            });
//            db.collection("exercises").document("lower "+ i).set(new HashMap<String, Object>()
//            {
//                {
//                    put("type", "LOWER");
//                    put("goodShoulders", good.get(0));
//                    put("goodBack", good.get(1));
//                    put("goodWrists", good.get(2));
//                    put("goodKnees", good.get(3));
//                    put("goodElbows", good.get(4));
//                    put("goodHip", good.get(5));
//                    put("name_pl", "pl lower" + finalI);
//                    put("name_en", "en lower" + finalI);
//                    put("minReps", rand.nextInt(10));
//                    put("maxReps", rand.nextInt(20)+10);
//                    put("description_pl", "pl_des lower" + finalName);
//                    put("description_en", "en_des lower" + finalName);
//                    put("yt", "uxPdPpi5W4o");
//                }
//            });
//            db.collection("exercises").document("condition "+ i).set(new HashMap<String, Object>()
//            {
//                {
//                    put("type", "CONDITION");
//                    put("goodShoulders", good.get(0));
//                    put("goodBack", good.get(1));
//                    put("goodWrists", good.get(2));
//                    put("goodKnees", good.get(3));
//                    put("goodElbows", good.get(4));
//                    put("goodHip", good.get(5));
//                    put("name_pl", "pl condition" + finalI);
//                    put("name_en", "en condition" + finalI);
//                    put("minReps", rand.nextInt(10));
//                    put("maxReps", rand.nextInt(20)+10);
//                    put("description_pl", "pl_des condition" + finalName);
//                    put("description_en", "en_des condition" + finalName);
//                    put("yt", "uxPdPpi5W4o");
//                }
//            });
//
//
//        }
//    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void start() {
        //exerciseSetBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, ExerciseSet.class)));
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HealthActivity.this, findViewById(R.id.textView3), HealthDataActivity.VIEW_NAME_HEADER_TITLE);
        //addDataBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthDataActivity.class)));
        //notificationsSettingsBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthNotification.class)));
        setEntries();
        //fetchExercises();
        initAddData();

    }



    private void initAddData() {
        numberStepsSet = findViewById(R.id.numberStepsSet);
        numberWeightSet = findViewById(R.id.numberWeightSet);
        numberSleepSet = findViewById(R.id.numberSleepSet);
        numberWaterSet = findViewById(R.id.numberWaterSet);

        pickDateStepsBtn = findViewById(R.id.pickDateStepsBtn);
        pickDateWeightBtn = findViewById(R.id.pickDateWeightBtn);
        pickDateSleepBtn = findViewById(R.id.pickDateSleepBtn);
        pickDateWaterBtn = findViewById(R.id.pickDateWaterBtn);

        setStepsBtn = findViewById(R.id.setStepsBtn);
        setWeightBtn = findViewById(R.id.setWeightBtn);
        setSleepBtn = findViewById(R.id.setSleepBtn);
        setWaterBtn = findViewById(R.id.setWaterBtn);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(sdf.parse(currentDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pickDateStepsBtn.setText(currentDate);
        pickDateWeightBtn.setText(currentDate);
        pickDateSleepBtn.setText(currentDate);
        pickDateWaterBtn.setText(currentDate);

        setStepsBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberStepsSet.getText().toString()) > 0)
                addToDb(pickDateStepsBtn.getText().toString(), getString(R.string.firebase_steps), Integer.parseInt(numberStepsSet.getText().toString()));
        });
        setWeightBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberWeightSet.getText().toString()) > 0)
                addToDb(pickDateWeightBtn.getText().toString(), getString(R.string.firebase_weight), Integer.parseInt(numberWeightSet.getText().toString()));
        });
        setSleepBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberSleepSet.getText().toString()) > 0)
                addToDb(pickDateSleepBtn.getText().toString(), getString(R.string.firebase_sleep), Integer.parseInt(numberSleepSet.getText().toString()));
        });
        setWaterBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberWaterSet.getText().toString()) > 0)
                addToDb(pickDateWaterBtn.getText().toString(), getString(R.string.firebase_water), Integer.parseInt(numberWaterSet.getText().toString()));
        });

        pickDateStepsBtn.setOnClickListener(view -> pickDate(pickDateStepsBtn));
        pickDateWeightBtn.setOnClickListener(view -> pickDate(pickDateWeightBtn));
        pickDateSleepBtn.setOnClickListener(view -> pickDate(pickDateSleepBtn));
        pickDateWaterBtn.setOnClickListener(view -> pickDate(pickDateWaterBtn));
    }

    public void pickDate(Button button) {
        clickedButton = button;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        clickedButton.setText(d + "-" + m+1 + "-" + y);
    }

    private void addToDb(String date, String value, Integer intValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        db.collection(getString(R.string.firebase_users)).document(mFirebaseAuth.getCurrentUser()
                .getUid()).collection(getString(R.string.firebase_health_data)).document(date).set(new HashMap<String, Integer>()
        {
            {
                put(value, intValue);
            }
        }, SetOptions.merge())
                .addOnSuccessListener(documentReference -> Log.d("DocSnippets", "DocumentSnapshot added" ))
                .addOnFailureListener(e -> Log.w("DocSnippets", "Error adding document", e));
    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        float diffX = moveEvent.getX() - downEvent.getX();
        //right or left swipe
        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && downEvent.getY() > 160 && downEvent.getY() < 1400) {
            if (diffX > 0) {
                onSwipeRight();
            } else {
                onSwipeLeft();
            }
            return true;
        }
        return false;
    }

    private void onSwipeLeft() {
        if (entry == 3)
            entry = 0;
        else
            entry += 1;
        setBarChart(entries.get(entry), labels);
        setDots(dots.get(entry));
        healthBarChartTitle.setText(dict.get(entry));
    }

    private void onSwipeRight() {
        if (entry == 0)
            entry = 3;
        else
            entry -= 1;
        setBarChart(entries.get(entry), labels);
        setDots(dots.get(entry));
        healthBarChartTitle.setText(dict.get(entry));
    }

    private void setDots(ImageView dot) {
        for (ImageView d: dots) {
            if (d == dot) {
                d.setImageResource(R.drawable.dot_purple);
            } else {
                d.setImageResource(R.drawable.dot_gray);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEntries() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Instant firstDay = Instant.now().minus(6, ChronoUnit.DAYS);
        Date myDate = Date.from(firstDay);
        String formattedDate = sdf.format(myDate);

        for (int i = 0; i < 7; i++) {
            DocumentReference docRef = db.collection(getString(R.string.firebase_users))
                    .document(mFirebaseAuth.getCurrentUser().getUid())
                    .collection(getString(R.string.firebase_health_data)).document(formattedDate);
            int finalI = i;
            weightEntries.clear();
            stepsEntries.clear();
            waterEntries.clear();
            sleepEntries.clear();
            docRef.get().addOnSuccessListener(document -> {
                if (document.exists()) {
                    if (document.contains(getString(R.string.firebase_weight))) {
                        weightEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong(getString(R.string.firebase_weight)).toString())));
                    } else {
                        weightEntries.add(new BarEntry(finalI, 0));
                    }
                    if (document.contains(getString(R.string.firebase_steps))) {
                        stepsEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong(getString(R.string.firebase_steps)).toString())));
                    } else {
                        stepsEntries.add(new BarEntry(finalI, 0));
                    }
                    if (document.contains(getString(R.string.firebase_sleep))) {
                        sleepEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong(getString(R.string.firebase_sleep)).toString())));
                    } else {
                        sleepEntries.add(new BarEntry(finalI, 0));
                    }
                    if (document.contains(getString(R.string.firebase_water))) {
                        waterEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong(getString(R.string.firebase_water)).toString())));
                    } else {
                        waterEntries.add(new BarEntry(finalI, 0));
                    }
                } else {
                        weightEntries.add(new BarEntry(finalI, 0));
                        stepsEntries.add(new BarEntry(finalI, 0));
                        sleepEntries.add(new BarEntry(finalI, 0));
                        waterEntries.add(new BarEntry(finalI, 0));
                }
                if (waterEntries.size() == 7) {
                    entries.add(stepsEntries);
                    entries.add(weightEntries);
                    entries.add(sleepEntries);
                    entries.add(waterEntries);
                    setBarChart(stepsEntries, labels);
                    entry = 0;
                    healthBarChartTitle.setText(dict.get(0));
                }
            }).addOnFailureListener(e -> System.err.println(e.toString()));
            labels[i] = formattedDate.substring(0, 5);
            firstDay = firstDay.plus(1, ChronoUnit.DAYS);
            myDate = Date.from(firstDay);
            formattedDate = sdf.format(myDate);
        }
    }


    public void setBarChart(ArrayList<BarEntry> entries, String[] labels) {
        barDataSet.setValues(entries);
        barData = new BarData(barDataSet);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.setData(barData);
        barChart.animateXY(DURATION_MILLIS, DURATION_MILLIS);
        barChart.invalidate();
    }

    private void InitBarChart() {
        barDataSet = new BarDataSet(new ArrayList<>(),null);
        barDataSet.setValueTextSize(14);


        //wylaczenie legendy
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        //ustawienie opisu
//        Description description = barChart.getDescription();
//        description.setText("Liczba kroków");
//        description.setPosition(0, 0);

        barDataSet.setBarBorderColor(Color.RED);
        barDataSet.setBarBorderWidth(1);
//        barDataSet.setLabel("Liczba kroków");
//        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setFormSize(2);

        barChart.setFitBars(true);
        barChart.getDescription().setText("");
        barChart.setTouchEnabled(false);

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLinesBehindData(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setTextSize(11);
//        barChart.getXAxis().getTextColor(Color.)

        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);


        barChart.animateXY(DURATION_MILLIS, DURATION_MILLIS);

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        dots = new ArrayList<>(Arrays.asList(dot1, dot2, dot3, dot4));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void fetchExercises() {
//
//        FirebaseFirestore.getInstance().collection("exercises").get().addOnCompleteListener(task -> {
//            Log.d("TAG", "fetchExercisesFromDataBase: ");
//            if (task.isSuccessful()) {
//
//                for (QueryDocumentSnapshot doc : task.getResult()) {
//                    CollectionReference collectionReference = doc.getReference().collection("normal");
//                    Task<QuerySnapshot> querySnapshotTask =  collectionReference.get();
//                    querySnapshotTask.addOnSuccessListener(document -> {
//                        for (DocumentSnapshot normalDoc : document.getDocuments()) {
//                            Exercise exercises = normalDoc.toObject(Exercise.class);
//                            ExerciseRepository.add(doc.getId(), "normal", exercises);
//                        }
//                    });
//                    CollectionReference collectionReference2 = doc.getReference().collection("stretching");
//                    Task<QuerySnapshot> querySnapshotTask2 =  collectionReference2.get();
//                    querySnapshotTask2.addOnSuccessListener(document -> {
//                        for (DocumentSnapshot normalDoc : document.getDocuments()) {
//                            Exercise exercises = normalDoc.toObject(Exercise.class);
//                            ExerciseRepository.add(doc.getId(), "stretching", exercises);
//                        }
//                    });
//                    Log.d("TAG", "fetchExercisesFromDataBase:");
//                }
//            } else {
//                Log.d("ERR", "Cannot import exercises from db");
//            }
//
//        });
//    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }


}