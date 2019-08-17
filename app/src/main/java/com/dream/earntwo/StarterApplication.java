package com.dream.earntwo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.Objects;


public class StarterApplication extends Application {


    public static final String CHANNEL_1_ID = "surveys";
    public static final String CHANNEL_2_ID = "spins";
    public static final String CHANNEL_3_ID = "SpotCash";


    static {
        System.loadLibrary("native-lib");
    }


    public native String getMyStrings(String iKeyName);



    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);


        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(getMyStrings("appId"))
                .clientKey(getMyStrings("clientKey"))
                .server(getMyStrings("server"))
                .build()
        );



        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Surveys",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Still Surveys are Available, earn money by doing surveys");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Spins",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("Spins is goig to expire soon, spin the wheel and earn money.");

            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "SpotCash",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel3.setDescription("Earn by watching Videos.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
}