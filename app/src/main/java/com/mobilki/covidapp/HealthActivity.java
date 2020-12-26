package com.mobilki.covidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class HealthActivity extends AppCompatActivity {

    Button exerciseSetBtn;
    Button addDataBtn;
    Button notificationsSettingsBtn;
    Button preferencesBtn;

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

        start();
    }

    private void start() {
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HealthActivity.this, findViewById(R.id.textView3), HealthDataActivity.VIEW_NAME_HEADER_TITLE);
        addDataBtn.setOnClickListener(view -> startActivity(new Intent(HealthActivity.this, HealthDataActivity.class)));

        BarChart barChart = findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 4021));
        entries.add(new BarEntry(1f, 3021));
        entries.add(new BarEntry(2f, 2021));
        entries.add(new BarEntry(3f, 5021));
        entries.add(new BarEntry(4f, 6021));
        entries.add(new BarEntry(5f, 2021));
        entries.add(new BarEntry(6f, 3021));

        BarDataSet barDataSet = new BarDataSet(entries,"");
        BarData barData = new BarData(barDataSet);

        //wylaczenie legendy
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        //
        barChart.setFitBars(true);
        barChart.getDescription().setText("");

        String[] labels = {"11-11", "12-11", "13-11", "14-11", "15-11", "16-11", "17-11"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis left = barChart.getAxisLeft();
        YAxis right = barChart.getAxisRight();

        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        left.setAxisMinimum(0f);

        barChart.setData(barData);





    }


}