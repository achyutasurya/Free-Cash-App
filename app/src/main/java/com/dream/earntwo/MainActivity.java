package com.dream.earntwo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


    static int current;
    Shared shared;
    private Toolbar mToolbar;
    private Boolean exit = false;
    static int calledTimes = 1;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shared.toolbarChanges(mToolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartAppSDK.init(this, "209742663", true);
        StartAppAd.disableSplash();

        setContentView(R.layout.activity_main);
        shared = new Shared(this);

        if (calledTimes != 1) {
            ADS ads = new ADS(this, false);
        }
        calledTimes++;

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.REBOOT, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        AppRate.with(this)
                .setInstallDays(2) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                    }
                })
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);


        shared.toolbarChanges(mToolbar);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);
        if (shared.getRunAlarm() && shared.getIsUserLoggedIn() && shared.getPointsScored() < 6500) {
            alarmScheduling();
            shared.setRunAlarm(false);

        }

        if (SplashScreenActivity.isNotificationThere) {
            SplashScreenActivity.isNotificationThere = false;
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.maxlimit);
            TextView heading = dialog.findViewById(R.id.textView1);
            heading.setText(SplashScreenActivity.notificationHeading);
            TextView conent = dialog.findViewById(R.id.textView2);
            conent.setText(SplashScreenActivity.notificationContnet);
            dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view) {
                    if (dialog.isShowing() && !isFinishing()) {
                        dialog.dismiss();
                    }
                }

            });
            dialog.show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Log.i("ABC","outside IF");
        if (!shared.getOtherReferalCode().isEmpty()) {

            Log.i("ABC","Inside IF");
            updateOtherReferalPoints();
            updateOtherReferalPointsTwo();
        }


    }



    public void updateOtherReferalPoints() {

        if (shared.getPointsScored() >= 500 && !shared.getIsOtherReferalPointsUpdated()) {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
            query.whereEqualTo("countryCode",shared.getCountryCode());
            query.whereEqualTo("ReferalCode", shared.getOtherReferalCode().toUpperCase());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {
                        if (scoreList.size() != 0) {
                            final String objId = scoreList.get(0).getObjectId();
                            scoreList.get(0).put("PointsScored", scoreList.get(0).getInt("PointsScored") + 50);
                            scoreList.get(0).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        shared.setIsOtherReferalPointsUpdated(true);
                                        shared.setHistoryActivity(50, 6, objId,456);
                                        Log.i("ABC","Inside First complete");
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
                                        query.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
                                            public void done(ParseObject object, ParseException e) {
                                                if (e == null) {
                                                    object.put("firstReferalUpdate", true);
                                                    object.saveInBackground();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void updateOtherReferalPointsTwo() {

        if (shared.getPointsScored() >= 2000 && !shared.getIsOtherReferalPointsUpdatedTwo() && shared.getIsOtherReferalPointsUpdated() && shared.getIsItOnServer()) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
            query.whereEqualTo("countryCode",shared.getCountryCode());
            query.whereEqualTo("ReferalCode", shared.getOtherReferalCode().toUpperCase());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {
                        if (scoreList.size() != 0) {
                            final String objId = scoreList.get(0).getObjectId();
                            scoreList.get(0).put("PointsScored", scoreList.get(0).getInt("PointsScored") + 450);
                            scoreList.get(0).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        shared.setIsOtherReferalPointsUpdatedTwo(true);
                                        shared.setHistoryActivity(450, 6, objId, 456);
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
                                        query.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
                                            public void done(ParseObject object, ParseException e) {
                                                if (e == null) {
                                                    object.put("secondReferalUpdate", true);
                                                    object.saveInBackground();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void alarmScheduling() {

        int[] hours = {8, 10, 13, 17, 19, 20, 21};
        int[] minutes = {0, 07, 0, 30, 5, 0, 0};
        String[] message = {"oneSurvey", "oneSpin", "twoSurvey", "oneEarn", "threeSurvey", "twoSpin", "twoEarn"};
        int[] requestCode = {101, 102, 103, 104, 105, 106, 107};
        int[] headings = {R.string.notification_head_one, R.string.notification_head_two, R.string.notification_head_one, R.string.notification_head_three, R.string.notification_head_one, R.string.notification_head_two, R.string.notification_head_three};
        int[] content = {R.string.notification_one, R.string.notification_two, R.string.notification_one, R.string.notification_three, R.string.notification_one, R.string.notification_two, R.string.notification_three};


        for (int i = 0; i <= 6; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, hours[i]);

            calendar.set(Calendar.MINUTE, minutes[i]);
            calendar.set(Calendar.SECOND, 0);

            Intent _myIntent = new Intent(MainActivity.this, AlarmService.class);
            _myIntent.putExtra("messageName", message[i]);
            _myIntent.putExtra("hours", hours[i]);
            _myIntent.putExtra("minutes", minutes[i]);
            _myIntent.putExtra("heading", getString(headings[i]));
            _myIntent.putExtra("content", getString(content[i]));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, requestCode[i], _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);

            Objects.requireNonNull(alarmManager).cancel(pendingIntent);
            Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pendingIntent);
        }
    }

    public void alarmCancel() {

        int[] hours = {8, 10, 13, 17, 19, 20, 21};
        int[] minutes = {0, 0, 0, 30, 0, 0, 0};
        String[] message = {"oneSurvey", "oneSpin", "twoSurvey", "oneEarn", "threeSurvey", "twoSpin", "twoEarn"};
        int[] requestCode = {101, 102, 103, 104, 105, 106, 107};


        for (int i = 0; i <= 6; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, hours[i]);

            calendar.set(Calendar.MINUTE, minutes[i]);
            calendar.set(Calendar.SECOND, 0);

            Intent _myIntent = new Intent(MainActivity.this, AlarmService.class);
            _myIntent.putExtra("messageName", message[i]);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, requestCode[i], _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);

            Objects.requireNonNull(alarmManager).cancel(pendingIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {/*
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                Intent nextActivity7 = new Intent(this, PointsActivity.class);
                startActivity(nextActivity7);
                return;
            case 2:
                Intent nextActivity127 = new Intent(this, AccountActivity.class);
                startActivity(nextActivity127);
                break;
            case 3:
                Intent nextActivity17 = new Intent(this, HistoryActivity.class);
                startActivity(nextActivity17);
                break;
            case 4:
                Intent nextActivityk = new Intent(this, ReferralHistoryActivity.class);
                startActivity(nextActivityk);
                return;
            case 5:
                Intent nextActivitye = new Intent(this, RecentlyPaidActivity.class);
                startActivity(nextActivitye);
                return;
            case 6:
                Intent nextActivity1 = new Intent(this, FaqActivity.class);
                startActivity(nextActivity1);
                return;
            case 7:
                Intent nextActivity4 = new Intent(this, PrivacyPolicyActivity.class);
                startActivity(nextActivity4);
                title = getString(R.string.title_privacy);
                return;
            case 8:
                contactUs();
                break;
            case 9:
                logout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void logout() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout");
        builder.setMessage("If you press YES, next time you open the app you need to login with mobile number and password.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Removing Account From Device...");
                progressDialog.show();
                final Intent nextActivity5 = new Intent(MainActivity.this, SplashScreenActivity.class);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(nextActivity5);
                                progressDialog.dismiss();
                            }
                        }, 3000);
                alarmCancel();
                shared.setSharedpreferences();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public void contactUs() {

        String mailto = "mailto:richardbrianofficial@yandex.com";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Cash Pot ");

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "There is no email app installed.", Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public void onBackPressed() {

        if (current == 0) {
            if (exit) {
                finish();
                StartAppAd.onBackPressed(this);
                /*Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);*/
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2000);
            }
        } else {
            finish();
            Intent a = new Intent(MainActivity.this, MainActivity.class);
            startActivity(a);
        }
    }


}

