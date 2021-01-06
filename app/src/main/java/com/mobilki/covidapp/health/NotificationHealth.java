package com.mobilki.covidapp.health;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.notification.NotificationHelper;
import com.mobilki.covidapp.notification.NotificationUtils;

public class NotificationHealth extends AppCompatActivity {
    Button button;
    public static final String PERSONAL_CHANNEL_ID = "PERSONAL";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_health);

        final NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "PERSONAL");

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener((View.OnClickListener) view -> {
            EditText tx = (EditText) findViewById(R.id.czas);
            String[] t = tx.getText().toString().split(":");
            NotificationHelper.setNotification(getApplicationContext(),Integer.parseInt(t[0]), Integer.parseInt(t[1]), "title to jest", "bodddy");
        });
    }
}