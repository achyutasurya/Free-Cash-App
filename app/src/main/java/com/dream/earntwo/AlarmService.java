package com.dream.earntwo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

import static com.dream.earntwo.StarterApplication.CHANNEL_1_ID;
import static com.dream.earntwo.StarterApplication.CHANNEL_2_ID;
import static com.dream.earntwo.StarterApplication.CHANNEL_3_ID;

public class AlarmService extends BroadcastReceiver {


    NotificationManagerCompat notificationManagerCompat;
    Shared shared;

    @Override
    public void onReceive(Context context, Intent intent) {

        shared = new Shared(context);
        if (shared.getIsUserLoggedIn()) {
            showNot(context, intent);
        }

    }

    public void showNot(Context context, Intent intent) {

        Bundle a = intent.getExtras();

        if (a != null) {
            boolean isToShowNotification = false;
            String mes, hed, x, xyz;

            int currentHours, currentMinutes;
            Calendar now = Calendar.getInstance();
            currentHours = now.get(Calendar.HOUR_OF_DAY);
            currentMinutes = now.get(Calendar.MINUTE);

            int ids, hours, minutes;
            Intent activityIntent;
            x = a.getString("messageName");
            hours = a.getInt("hours");
            minutes = a.getInt("minutes");
            hed = a.getString("heading");
            mes = a.getString("content");

            if (x != null) {
                if (hours == currentHours && (currentMinutes <= minutes + 10 && currentMinutes >= minutes)) {

                    switch (x) {
                        case "oneSurvey":
                        case "twoSurvey":
                        case "threeSurvey":
                            activityIntent = new Intent(context, SplashScreenActivity.class);
                            isToShowNotification = true;
                            activityIntent.putExtra("name", "survey");

                            ids = 1;
                            xyz = CHANNEL_1_ID;
                            break;
                        case "oneSpin":
                        case "twoSpin":
                            activityIntent = new Intent(context, SplashScreenActivity.class);
                            if (shared.getCurrentTimeSpinsAvailable() != 0) {
                                isToShowNotification = true;
                                activityIntent.putExtra("name", "spin");
                            }
                            ids = 2;
                            xyz = CHANNEL_2_ID;
                            break;
                        default:
                            isToShowNotification = true;
                            activityIntent = new Intent(context, SplashScreenActivity.class);
                            activityIntent.putExtra("name", "main");
                            ids = 3;
                            xyz = CHANNEL_3_ID;
                            break;
                    }


                    if (isToShowNotification && shared.getTodayPointsScored() <= 500) {

                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.notification);
                        mediaPlayer.start();

                        notificationManagerCompat = NotificationManagerCompat.from(context);

                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

                        Notification notification = new NotificationCompat.Builder(context, xyz)
                                .setSmallIcon(R.drawable.ic_profile_small_logo)
                                .setContentTitle(hed)
                                .setContentText(mes)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setContentIntent(contentIntent)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(true)
                                .build();

                        notificationManagerCompat.notify(ids, notification);
                    }

                }
            }

        }

    }


}
