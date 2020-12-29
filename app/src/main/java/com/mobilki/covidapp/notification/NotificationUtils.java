package com.mobilki.covidapp.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager notificationManager;

    String DEFAULT_CHANNEL_ID = "DEFAULT";

    String DEFAULT_CHANNEL_NAME = "DEFAULT CHANNEL";

    String pkgName = "notification.channel";


    public NotificationUtils(Context context) {
        super(context);
        pkgName = context.getPackageName() != null ? context.getPackageName() : pkgName;
        DEFAULT_CHANNEL_ID = pkgName + "." + DEFAULT_CHANNEL_ID.toUpperCase();
        createChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME);
    }

    public void createChannel(String CHANNEL_ID, String CHANNEL_NAME) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            NotificationChannel newChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            newChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            newChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            newChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            newChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(newChannel);
        }
    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public void sendNotificationInChannel(int notificationId, Intent resultIntent,
                                          NotificationCompat.Builder builder) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        // for notification click action, also required on Gingerbread and below
        builder.setContentIntent(pi);

        getNotificationManager().notify(notificationId, builder.build());
    }
}
