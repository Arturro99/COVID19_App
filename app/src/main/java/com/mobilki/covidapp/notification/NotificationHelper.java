package com.mobilki.covidapp.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.mobilki.covidapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class NotificationHelper {
    private static final String PERSONAL_CHANNEL_ID = "PERSONAL";

    public static void setNotification(Context context ,int hour, int minute, String title, String body) {
        final NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "CovidApp");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
//
        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour*100+minute+Calendar.DAY_OF_MONTH, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);


        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void setNotification(Context context ,int hour, int minute, String title, String body, Long day) {
        final NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "CovidApp");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.DAY_OF_WEEK, day.intValue());
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
//
        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 7);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour*100+minute+Calendar.DAY_OF_MONTH, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);


        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Ustawiony na " + date_format.format(calendar.getTime()));
    }

    public static void setNotification(Context context ,int hour, int minute, String title, String body, boolean enableSound, boolean enableVibrations) {
        final NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "CovidApp", enableVibrations, enableSound);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
//
        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour*100+minute+Calendar.DAY_OF_MONTH, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);


        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createCustomNotification(NotificationUtils notificationUtils, Context context) {
        Intent resultIntent = new Intent(context, NotificationReceiver.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                PERSONAL_CHANNEL_ID)
                // set title
                .setContentTitle("TEST!")
                // set body
                .setContentText("TEST!")
                // set ticker
                .setTicker("")
                .setSmallIcon(R.mipmap.ic_launcher)
                // set small icon
//                .setSmallIcon(R.drawable.ic_sova_notifi)
////                .setColor(Color.parseColor("#DCBF3F"))
//                .setColor(Color.rgb(72,209,204))
////                .setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
//                        R.drawable.ic_sova_round))
                // set auto cancel behaviour
                .setAutoCancel(true);

        notificationUtils.sendNotificationInChannel(103, resultIntent, builder);
    }
}
