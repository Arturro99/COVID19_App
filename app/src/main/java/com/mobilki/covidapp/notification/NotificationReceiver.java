package com.mobilki.covidapp.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.mobilki.covidapp.R;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String PERSONAL_CHANNEL_ID = "PERSONAL";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        final NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "PERSONAL");
        createCustomNotification(notificationUtils , context, intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createCustomNotification(NotificationUtils notificationUtils, Context context, Intent intent) {
        Intent resultIntent = new Intent(context, NotificationReceiver.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                PERSONAL_CHANNEL_ID)
                // set title
                .setContentTitle(intent.getStringExtra("title"))
                // set body
                .setContentText(intent.getStringExtra("body"))
                // set ticker
                .setTicker("")
                .setSmallIcon(R.mipmap.ic_launcher)
                // set small icon
//                .setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
//                R.mipmap.ic_sova))
//                .setSmallIcon(R.drawable.ic_sova_notifi)
//                .setColor(Color.parseColor("#DCBF3F"))
//                .setColor(Color.rgb(72,209,204))
//                .setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
//                        R.drawable.ic_sova_round))
                // set auto cancel behaviour
                .setAutoCancel(true);
//        builder = convertToBigNotificationBuilder(builder);
        notificationUtils.sendNotificationInChannel(103, resultIntent, builder);
    }
}
