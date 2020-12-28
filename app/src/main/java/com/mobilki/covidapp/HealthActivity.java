package com.mobilki.covidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class HealthActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_health);

        exerciseSetBtn = findViewById(R.id.exerciseSetBtn);
        addDataBtn = findViewById(R.id.addDataBtn);
        notificationsSettingsBtn = findViewById(R.id.healthNotificationsSettingsBtn);
        preferencesBtn = findViewById(R.id.healthPreferencesBtn);
        barChart = findViewById(R.id.barchart);

        InitBarChart();

        exerciseSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        gestureDetector = new GestureDetector(this);
        start();
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

    private void start() {
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HealthActivity.this, findViewById(R.id.textView3), HealthDataActivity.VIEW_NAME_HEADER_TITLE);
        addDataBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthDataActivity.class)));

        onSwipeLeft();
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
            System.out.println("aa" + moveEvent.getY() + "  " + downEvent.getY());
            return true;
        }




        return false;
    }

    private void onSwipeLeft() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 4021));
        entries.add(new BarEntry(1f, 3021));
        entries.add(new BarEntry(2f, 2021));
        entries.add(new BarEntry(3f, 5021));
        entries.add(new BarEntry(4f, 6021));
        entries.add(new BarEntry(5f, 2021));
        entries.add(new BarEntry(6f, 3021));

        String[] labels = {"11-11", "12-11", "13-11", "14-11", "15-11", "16-11", "17-11"};
        setBarChart(entries, labels);
    }

    private void onSwipeRight() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 121));
        entries.add(new BarEntry(1f, 121));
        entries.add(new BarEntry(2f, 221));
        entries.add(new BarEntry(3f, 321));
        entries.add(new BarEntry(4f, 100));
        entries.add(new BarEntry(5f, 121));
        entries.add(new BarEntry(6f, 221));

        String[] labels = {"21-11", "22-11", "23-11", "24-11", "25-11", "26-11", "27-11"};
        setBarChart(entries, labels);
    }

    public void setBarChart(ArrayList<BarEntry> entries, String[] labels) {
        barDataSet.setValues(entries);
        barData = new BarData(barDataSet);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.setData(barData);
        barChart.animateXY(DURATION_MILLIS, DURATION_MILLIS);
        barChart.invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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