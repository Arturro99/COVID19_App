package com.mobilki.covidapp.health;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.notification.NotificationHelper;

public class ExerciseSet extends Activity {
    Button button;
    public static final String PERSONAL_CHANNEL_ID = "PERSONAL";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_set);

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                EditText tx = (EditText) findViewById(R.id.czas);
                String[] t = tx.getText().toString().split(":");
                NotificationHelper.setNotification(getApplicationContext(),Integer.parseInt(t[0]), Integer.parseInt(t[1]), "title to jest", "bodddy");
//                final NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "CovidApp");
//
//                notificationHelper.createCustomNotification(notificationUtils, getApplicationContext());
            }
        });
    }
}
