package com.mobilki.covidapp.health;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.model.Exercise;
import com.mobilki.covidapp.api.repository.ExerciseRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    Button exerciseSetBtn;
    Button addDataBtn;
    Button notificationsSettingsBtn;
    Button preferencesBtn;
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

    Button test;
    TextView test1;
    TextView test2;

    ImageView notificationtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_health_new);

//        exerciseSetBtn = findViewById(R.id.exerciseSetBtn);
//        addDataBtn = findViewById(R.id.addDataBtn);
//        notificationsSettingsBtn = findViewById(R.id.healthNotificationsSettingsBtn);
//        preferencesBtn = findViewById(R.id.healthPreferencesBtn);
        barChart = findViewById(R.id.barchart);
        labels = new String[7];
        InitBarChart();
        weightEntries = new ArrayList<>();
        stepsEntries = new ArrayList<>();
        sleepEntries = new ArrayList<>();
        waterEntries = new ArrayList<>();
        entries = new ArrayList<>();
        healthBarChartTitle = findViewById(R.id.healthBarChartTitle);

        dict.put(0, R.string.weight_title_barchart);
        dict.put(1, R.string.steps_title_barchart);
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

            page1group.setVisibility(itemId == R.id.page_1 ? View.VISIBLE : View.GONE);
            page2group.setVisibility(itemId == R.id.page_2 ? View.VISIBLE : View.GONE);
            page3group.setVisibility(itemId == R.id.page_3 ? View.VISIBLE : View.GONE);

            return true;
        });

        test2 = findViewById(R.id.exercise1Description);
        test1 = findViewById(R.id.exercise1Title);
        test1.setOnClickListener(view -> {
            if(test2.getVisibility() == View.GONE)
                test2.setVisibility(View.VISIBLE);
            else
                test2.setVisibility(View.GONE);
        });

        notificationtn = findViewById(R.id.notificationBtnHealth);

        notificationtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthNotification.class)));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void start() {
        //exerciseSetBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, ExerciseSet.class)));
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HealthActivity.this, findViewById(R.id.textView3), HealthDataActivity.VIEW_NAME_HEADER_TITLE);
        //addDataBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthDataActivity.class)));
        //notificationsSettingsBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthNotification.class)));
        setEntries();
        //fetchExercises();
        initAddData();
        initExercises();
    }

    private void initExercises() {
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
                addToDb(pickDateStepsBtn.getText().toString(), "steps", Integer.parseInt(numberStepsSet.getText().toString()));
        });
        setWeightBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberWeightSet.getText().toString()) > 0)
                addToDb(pickDateWeightBtn.getText().toString(), "weight", Integer.parseInt(numberWeightSet.getText().toString()));
        });
        setSleepBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberSleepSet.getText().toString()) > 0)
                addToDb(pickDateSleepBtn.getText().toString(), "sleep", Integer.parseInt(numberSleepSet.getText().toString()));
        });
        setWaterBtn.setOnClickListener(view -> {
            if (Integer.parseInt(numberWaterSet.getText().toString()) > 0)
                addToDb(pickDateWaterBtn.getText().toString(), "water", Integer.parseInt(numberWaterSet.getText().toString()));
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

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        clickedButton.setText(d + "-" + m+1 + "-" + y);
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

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        float diffX = moveEvent.getX() - downEvent.getX();
        //right or left swipe
        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && downEvent.getY() > 160 && downEvent.getY() < 800) {
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
        healthBarChartTitle.setText(dict.get(entry));
    }

    private void onSwipeRight() {
        if (entry == 0)
            entry = 3;
        else
            entry -= 1;
        setBarChart(entries.get(entry), labels);
        healthBarChartTitle.setText(dict.get(entry));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEntries() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Instant firstDay = Instant.now().minus(6, ChronoUnit.DAYS);
        Date myDate = Date.from(firstDay);
        String formattedDate = sdf.format(myDate);
        Source source = Source.SERVER;

        for (int i = 0; i < 7; i++) {
            DocumentReference docRef = db.collection("users").document(mFirebaseAuth.getCurrentUser().getUid()).collection("health_data").document(formattedDate);
            int finalI = i;

            docRef.get().addOnSuccessListener(document -> {
                if (document.exists()) {
                    if (document.contains("weight")) {
                        weightEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong("weight").toString())));
                    } else {
                        weightEntries.add(new BarEntry(finalI, 0));
                    }
                    if (document.contains("steps")) {
                        stepsEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong("steps").toString())));
                    } else {
                        stepsEntries.add(new BarEntry(finalI, 0));
                    }
                    if (document.contains("sleep")) {
                        sleepEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong("sleep").toString())));
                    } else {
                        sleepEntries.add(new BarEntry(finalI, 0));
                    }
                    if (document.contains("water")) {
                        waterEntries.add(new BarEntry(finalI, Integer.parseInt(document.getLong("water").toString())));
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
                    entries.add(weightEntries);
                    entries.add(stepsEntries);
                    entries.add(sleepEntries);
                    entries.add(waterEntries);
                    setBarChart(stepsEntries, labels);
                    entry = 1;
                    healthBarChartTitle.setText(dict.get(1));
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

        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);


        barChart.animateXY(DURATION_MILLIS, DURATION_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchExercises() {

        FirebaseFirestore.getInstance().collection("exercises").get().addOnCompleteListener(task -> {
            Log.d("TAG", "fetchExercisesFromDataBase: ");
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CollectionReference collectionReference = doc.getReference().collection("normal");
                    Task<QuerySnapshot> querySnapshotTask =  collectionReference.get();
                    querySnapshotTask.addOnSuccessListener(document -> {
                        for (DocumentSnapshot normalDoc : document.getDocuments()) {
                            Exercise exercises = normalDoc.toObject(Exercise.class);
                            ExerciseRepository.add(doc.getId(), "normal", exercises);
                        }
                    });
                    CollectionReference collectionReference2 = doc.getReference().collection("stretching");
                    Task<QuerySnapshot> querySnapshotTask2 =  collectionReference2.get();
                    querySnapshotTask2.addOnSuccessListener(document -> {
                        for (DocumentSnapshot normalDoc : document.getDocuments()) {
                            Exercise exercises = normalDoc.toObject(Exercise.class);
                            ExerciseRepository.add(doc.getId(), "stretching", exercises);
                        }
                    });
                    Log.d("TAG", "fetchExercisesFromDataBase:");
                }
            } else {
                Log.d("ERR", "Cannot import exercises from db");
            }

        });
    }


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