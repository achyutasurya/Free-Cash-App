package com.dream.earntwo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Objects;

import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends AppCompatActivity {

    static Boolean isNotificationThere = false;
    static String notificationHeading, notificationContnet;
    Shared shared;
    TextView internetConnectivity;
    ProgressBar progressBar;
    Intent intent;
    boolean isUpdateImportant;
    Boolean isPathKnown = false;
    static int currentVersionCode = 1, latestVersionCode;
    Boolean isUserBlocked = false;
    Boolean isDataCameFromServer = false, showUpdateDialog = false;
    static int noOfInterstitialAdsSeen;
    boolean maintenance = false;

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Fabric.with(this, new Crashlytics());

        shared = new Shared(this);


        Bundle extras = getIntent().getExtras();
        String message = null;



        if (extras != null && shared.getIsUserLoggedIn()) {
            message = extras.getString("name");
            if (message != null) {
                switch (message) {
                    case "survey":
                        intent = new Intent(getApplicationContext(), SurveyActivity.class);
                        isPathKnown = true;
                        break;
                    case "spin":
                        intent = new Intent(getApplicationContext(), SpinActivity.class);
                        isPathKnown = true;
                        break;
                    case "main":
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        isPathKnown = true;
                        break;
                }
            }
        }


        internetConnectivity = findViewById(R.id.internetConnectivity);
        progressBar = findViewById(R.id.progressBarInternet);

        int openedCount = shared.getOpenedCount();

        if (openedCount == 0) {
            shared.setSharedpreferences();

        } else {
            shared.setDate();
            shared.setOpenedCount(1);
            shared.setRunningTime();
        }

        conectionToServer();

        internetCheckingCallBack();

    }


    public void updateDetailsFromServer(){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(getString(R.string.parse_app_details));
        query1.getInBackground(getString(R.string.parse_app_details_id), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    latestVersionCode = object.getInt("latestVersionCode");
                    isUpdateImportant = object.getBoolean("isUpdateImportant");
                    showUpdateDialog = object.getBoolean("showUpdateDialog");
                    maintenance = object.getBoolean("maintenance");
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    isDataCameFromServer = true;
                                }
                            }, 400);
                }
            }
        });
    }

    public void conectionToServer(){


        if (shared.getIsUserLoggedIn() && shared.getIsItOnServer()) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
            query.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        isUserBlocked = object.getBoolean("isUserBlocked");
                        shared.justSetPointsScored(object.getInt("PointsScored"));
                        shared.setTaskOneTemp(object.getInt("TaskOne"));
                        shared.setTaskTwoTemp(object.getInt("TaskTwo"));
                        if (object.getBoolean("isNotificationThere")) {
                            notificationHeading = object.getString("notificationHeading");
                            notificationContnet = object.getString("notificationContent");
                            isNotificationThere = true;
                            object.put("isNotificationThere", false);
                            object.saveInBackground();
                        }
                        if(shared.getUserName().isEmpty()){
                            shared.setUserName(object.getString("Name"));
                        }

                        if (shared.getLastOpenedVersion() != currentVersionCode) {
                            shared.setLastOpenedVersion(currentVersionCode);
                            object.put("versionCode", currentVersionCode);
                            object.saveInBackground();
                        }
                        if (isPathKnown) {
                            object.put("FromNotification", object.getInt("FromNotification") + 1);
                            object.saveInBackground();
                        }
                        updateDetailsFromServer();
                    } else {
                        Log.i("ABC Exception", e.toString());
                    }
                }
            });


            Crashlytics.setUserIdentifier(shared.getObjectId());
            Crashlytics.setUserEmail(shared.getPhoneNumber());
            Crashlytics.setUserName(shared.getUserName());


        } else {
            isDataCameFromServer = true;
            updateDetailsFromServer();
        }
    }

    public void startTast() {

        if (isDataCameFromServer) {

            if(!maintenance) {

                if (latestVersionCode != 0 && currentVersionCode != latestVersionCode && (showUpdateDialog || !(latestVersionCode - currentVersionCode <= 1))) {

                    if (!isFinishing()) {
                        showUpdateDialog();
                    }
                    internetConnectivity.setText("Update the App...");
                } else {
                    if (isEmulator()) {
                        if (!isFinishing()) {
                            showEmulatorDialog(R.string.real_device_heading, R.string.real_device_content);
                        }
                        internetConnectivity.setText("Use Real Device...");
                    } else {
                        if (isUserBlocked) {
                            if (!isFinishing()) {
                                showEmulatorDialog(R.string.blocked_heading, R.string.blocked_content);
                            }
                            internetConnectivity.setText("Account is blocked...");
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            checkingUserLoginStatus();
                        }
                    }
                }
            }else{
                if(!isFinishing()) {
                    showMaintainance();
                }
                internetConnectivity.setText("Under Maintenance...");
            }
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            startTast();
                        }
                    }, 2000);
        }

    }

    public void checkingUserLoginStatus() {
        if (!isPathKnown) {
            boolean isLoggedIn = shared.getIsUserLoggedIn();
            if (isLoggedIn) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), SignupActivity.class);
            }
        }

        startActivity(intent);
        finish();
    }

    private void showEmulatorDialog(int headingText, int contentText) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maxlimit);
        TextView heading = dialog.findViewById(R.id.textView1);
        heading.setText(getString(headingText));
        TextView conent = dialog.findViewById(R.id.textView2);
        conent.setText(getString(contentText));
        dialog.findViewById(R.id.popupOk).setVisibility(View.GONE);

        if(!isFinishing()) {
            dialog.show();
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void showUpdateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maxlimit);
        TextView heading = dialog.findViewById(R.id.textView1);
        heading.setText("Update Available");
        TextView conent = dialog.findViewById(R.id.textView2);
        conent.setText("Latest update is available on play store. The current version is no longer working perfectly, so please update the app to earn money.");
        dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                if (dialog.isShowing() && !isFinishing()){
                    dialog.dismiss();
                }
            }

        });
        if (!isUpdateImportant && latestVersionCode - currentVersionCode <= 1) {
            dialog.findViewById(R.id.popuplater).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.popuplater).setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view) {
                    if (dialog.isShowing() && !isFinishing()) {
                        dialog.dismiss();
                    }
                    checkingUserLoginStatus();
                }

            });
        }

        if(!isFinishing()) {
            dialog.show();
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



    private void showMaintainance() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maxlimit);
        TextView heading = dialog.findViewById(R.id.textView1);
        heading.setText("maintenance");
        TextView conent = dialog.findViewById(R.id.textView2);
        conent.setText("Our servers are under maintenance so please check after a few minutes. If you have any doubt contact us richardbrianofficial@yandex.com");
        dialog.findViewById(R.id.popupOk).setVisibility(View.GONE);
        if(!isFinishing()) {
            dialog.show();
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public void internetCheckingCallBack() {

        progressBar.setVisibility(View.VISIBLE);
        internetConnectivity.setText("Checking Internet Connection...");

        if (isInternetOn()) {

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            internetConnectivity.setText("Connecting to Server...");
                        }
                    }, 1000);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                                startTast();
                        }
                    }, 3000);
        } else {

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            internetConnectivity.setText("No Internet Connection...");

                        }
                    }, 5000);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            internetCheckingCallBack();
                            conectionToServer();
                        }
                    }, 12000);
        }
    }

    public Boolean isInternetOn() {

        getBaseContext();
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }


}
