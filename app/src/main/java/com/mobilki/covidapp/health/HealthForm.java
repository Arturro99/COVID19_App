package com.mobilki.covidapp.health;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mobilki.covidapp.R;

public class HealthForm extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_form);
        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_preferences), 0);
//        DisplayMetrics dm =  new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width * .9), (int)(height * .9));
        settings.edit().putBoolean("first_time_health", false).apply();
    }
}
