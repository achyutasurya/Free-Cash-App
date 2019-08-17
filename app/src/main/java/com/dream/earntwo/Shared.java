package com.dream.earntwo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Shared {

    String currentDate, currentHourandDate;
    int currentHours, currentHoursCount, currentMinutes;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private String stringTaskOne, stringTaskTwo, stringCountryCode, stringAccountObjectId, stringImageUri, stringIsAccountOnServer, stringImageUploaded, stringSpinPointsEarnedToday, stringLastOpenedVersion, stringIsOtherReferalPointsUpdatedTwo, stringRunAlarm, stringTodayWatchedReward, stringObjectId, stringIsOtherReferalPointsUpdated, stringIsItOnServer, stringHour, stringEmail, stringTodayEarnedPoints, stringdate, stringReferalCode, stringOtherReferalCode, stringName, stringPointsScored, stringPhoneNumber, stringPassword, stringSpinsAvailable, stringCurretTimeSpinsAvailable, stringRunningTime, stringOpenedCount, stringIsLoggedIn;

    Shared(Context context1) {
        context = context1;
        sharedpreferences = context.getSharedPreferences("drunkendog.free", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sdf.format(date);

        Calendar now = Calendar.getInstance();
        currentHours = now.get(Calendar.HOUR_OF_DAY);
        currentMinutes = now.get(Calendar.MINUTE);

        currentHourandDate = currentDate + Integer.toString(currentHours);


        stringName = "name";
        stringTaskOne = "taskOne";
        stringTaskTwo = "taskTwo";
        stringTodayWatchedReward = "todayRewardsWatched";
        stringCountryCode = "countryCode";
        stringObjectId = "objectId";
        stringTodayEarnedPoints = "todayEarnings";
        stringPointsScored = "pointsScored";
        stringPhoneNumber = "phoneNumber";
        stringPassword = "password";
        stringEmail = "Email";
        stringOtherReferalCode = "otherReferalCode";
        stringSpinsAvailable = "spinsAvailable";
        stringCurretTimeSpinsAvailable = "curretTimeSpinsAvailable";
        stringRunningTime = "runningTime";
        stringOpenedCount = "openedCount";
        stringIsLoggedIn = "isLoggedIn";
        stringReferalCode = "referalCode";
        stringdate = "lastDate";
        stringHour = "lastHour";
        stringRunAlarm = "runAlarm";
        stringImageUri = "imageUri";
        stringImageUploaded = "imageUploaded";
        stringIsItOnServer = "isItOnServer";
        stringSpinPointsEarnedToday = "spinPointsEarnedToday";
        stringIsOtherReferalPointsUpdatedTwo = "IsOtherReferalPointsUpdatedTwo";
        stringIsOtherReferalPointsUpdated = "IsOtherReferalPointsUpdated";
        stringLastOpenedVersion = "lastOpenedVersion";
        stringIsAccountOnServer = "isAccountOnServer";
        stringAccountObjectId = "accountObjectId";
    }

    public void setSharedpreferences() {
        editor.putString(stringName, "");
        editor.putString(stringReferalCode, referalCodeGen());
        editor.putString(stringOtherReferalCode, "");
        editor.putString(stringPhoneNumber, "");
        editor.putString(stringPassword, "");
        editor.putString(stringEmail, "");
        editor.putString(stringObjectId, "");
        editor.putString(stringImageUri, "");
        editor.putInt(stringSpinsAvailable, 5);
        editor.putInt(stringCurretTimeSpinsAvailable, 5);
        editor.putString(stringRunningTime, currentHourandDate);
        editor.putString(stringAccountObjectId, "");
        editor.putString(stringCountryCode,"");
        editor.putInt(stringOpenedCount, 1);
        editor.putBoolean(stringIsLoggedIn, false);
        editor.putInt(stringPointsScored, 200);
        editor.putString(stringdate, currentDate);
        editor.putInt(stringTodayEarnedPoints, 0);
        editor.putInt(stringLastOpenedVersion, 4);
        editor.putInt(stringTodayWatchedReward, 0);
        editor.putInt(stringSpinPointsEarnedToday, 0);
        editor.putBoolean(stringIsItOnServer, false);
        editor.putBoolean(stringRunAlarm, true);
        editor.putBoolean(stringIsOtherReferalPointsUpdatedTwo, false);
        editor.putBoolean(stringImageUploaded, false);
        editor.putBoolean(stringIsAccountOnServer, false);
        editor.putBoolean(stringIsOtherReferalPointsUpdated, false);
        editor.putInt(stringTaskOne, 15);
        editor.putInt(stringTaskTwo, 15);
        editor.apply();
    }

    public void setTaskOneTemp(int value){
        editor.putInt(stringTaskOne, value);
        editor.apply();
    }

    public void setTaskTwoTemp(int value){
        editor.putInt(stringTaskTwo, value);
        editor.apply();
    }

    public void setTaskOne(final int value){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("TaskOne", value);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            editor.putInt(stringTaskOne, value);
                            editor.apply();
                        }
                    });
                } else {
                    Log.i("ABC Exception", e.toString());
                }
            }
        });
    }

    public int getTaskOne() {
        return sharedpreferences.getInt(stringTaskOne, 10);
    }

    public void setTaskTwo(final int value){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("TaskTwo", value);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            editor.putInt(stringTaskTwo, value);
                            editor.apply();
                        }
                    });
                } else {
                    Log.i("ABC Exception", e.toString());
                }
            }
        });
    }

    public int getTaskTwo() {
        return sharedpreferences.getInt(stringTaskTwo, 10);
    }

    public String getCountryCode() {
        return sharedpreferences.getString(stringAccountObjectId, "");
    }

    public void setCountryCode(String value) {
        editor.putString(stringAccountObjectId, value);
        editor.apply();
    }

    public String getAccountObjectId() {
        return sharedpreferences.getString(stringAccountObjectId, "");
    }

    public void setAccountObjectId(String value) {
        editor.putString(stringAccountObjectId, value);
        editor.apply();
    }

    public boolean getIsAccountOnServer() {
        return sharedpreferences.getBoolean(stringIsAccountOnServer, false);
    }

    public void setIsAccountOnServer(Boolean value) {
        editor.putBoolean(stringIsAccountOnServer, value);
        editor.apply();
    }

    public boolean getIsImageUploaded() {
        return sharedpreferences.getBoolean(stringImageUploaded, false);
    }

    public void setIsImageUploaded(Boolean value) {
        editor.putBoolean(stringImageUploaded, value);
        editor.apply();
    }

    public String getImageUri() {
        return sharedpreferences.getString(stringImageUri, "");
    }

    public void setImageUri(String value) {
        editor.putString(stringImageUri, value);
        editor.apply();
    }

    public int getSpinPointsEarnedToday() {
        return sharedpreferences.getInt(stringSpinPointsEarnedToday, 0);
    }

    public void setSpinPointsEarnedToday(int value) {
        editor.putInt(stringSpinPointsEarnedToday, getSpinPointsEarnedToday() + value);
        editor.apply();
    }

    public int getLastOpenedVersion() {
        return sharedpreferences.getInt(stringLastOpenedVersion, 4);
    }

    public void setLastOpenedVersion(int value) {
        editor.putInt(stringLastOpenedVersion, value);
        editor.apply();
    }

    public String getObjectId() {
        return sharedpreferences.getString(stringObjectId, "");
    }

    public void setObjectId(String value) {
        editor.putString(stringObjectId, value);
        editor.apply();
    }

    public boolean getIsOtherReferalPointsUpdated() {
        return sharedpreferences.getBoolean(stringIsOtherReferalPointsUpdated, false);
    }

    public void setIsOtherReferalPointsUpdated(Boolean value) {
        editor.putBoolean(stringIsOtherReferalPointsUpdated, value);
        editor.apply();
    }

    public boolean getIsOtherReferalPointsUpdatedTwo() {
        return sharedpreferences.getBoolean(stringIsOtherReferalPointsUpdatedTwo, false);
    }

    public void setIsOtherReferalPointsUpdatedTwo(Boolean value) {
        editor.putBoolean(stringIsOtherReferalPointsUpdatedTwo, value);
        editor.apply();
    }

    public boolean getIsItOnServer() {
        return sharedpreferences.getBoolean(stringIsItOnServer, false);
    }

    public void setIsitOnServer(Boolean value) {
        editor.putBoolean(stringIsItOnServer, value);
        editor.apply();
    }

    public void setDate() {

        ParseQuery<ParseObject> queryMain = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        queryMain.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    editor.putString(stringdate, object.getString("todayPointsDate"));
                    editor.putInt(stringTodayEarnedPoints, object.getInt("todayPoints"));
                    editor.putInt(stringSpinPointsEarnedToday, object.getInt("todaySpinPoints"));
                    editor.apply();

                    if (!(getDate().equals(currentDate))) {
                        editor.putInt(stringTodayEarnedPoints, 0);
                        editor.putInt(stringTodayWatchedReward, 0);
                        editor.putInt(stringSpinPointsEarnedToday, 0);
                        editor.putString(stringdate, currentDate);
                        editor.putInt(stringTaskOne, 15);
                        editor.putInt(stringTaskTwo, 15);
                        editor.apply();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
                        query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    object.put("todayPoints", 0);
                                    object.put("todaySpinPoints", 0);
                                    object.put("todayPointsDate", currentDate);
                                    object.put("TaskOne",15);
                                    object.put("TaskTwo",15);
                                    object.saveInBackground();
                                } else {
                                    Log.i("ABC Exception", e.toString());
                                }
                            }
                        });


                        setRunAlarm(true);
                    }


                } else {
                    Log.i("ABC Exception", e.toString());
                }
            }
        });


    }

    public boolean getRunAlarm() {
        return sharedpreferences.getBoolean(stringRunAlarm, false);
    }

    public void setRunAlarm(Boolean value) {
        editor.putBoolean(stringRunAlarm, value);
        editor.apply();
    }

    public void setRunningTime() {

        if (!getRunningTime().equals(currentHourandDate)) {

            int co = getCurrentTimeSpinsAvailable();
            setCurrentTimeSpinsAvailable(3 - co);
            setSpinsAvailable(3 - co);

            editor.putString(stringRunningTime, currentHourandDate);
            editor.apply();
        }
    }

    public String getOtherReferalCode() {
        return sharedpreferences.getString(stringOtherReferalCode, "");
    }

    public void setOtherReferalCode(String referalCodeSetting) {
        editor.putString(stringOtherReferalCode, referalCodeSetting);
        editor.apply();
    }

    public String getEmail() {
        return sharedpreferences.getString(stringEmail, "");
    }

    public void setEmail(String email) {
        editor.putString(stringOtherReferalCode, email);
        editor.apply();
    }

    public String getDate() {
        return sharedpreferences.getString(stringdate, currentDate);
    }

    public String getReferalCode() {
        return sharedpreferences.getString(stringReferalCode, referalCodeGen());
    }

    public void setReferalCode(String value) {
        editor.putString(stringReferalCode, value);
        editor.apply();
    }

    public int getTodayWatchedReward() {
        return sharedpreferences.getInt(stringTodayWatchedReward, 0);
    }

    public void setTodayWatchedReward(int valueNeedtobeAdded) {
        editor.putInt(stringTodayWatchedReward, getTodayWatchedReward() + valueNeedtobeAdded);
        editor.apply();

    }

    public int getTodayPointsScored() {
        return sharedpreferences.getInt(stringTodayEarnedPoints, 0);
    }

    public void setTodayEarnedPoints(int valueNeedtobeAdded) {
        editor.putInt(stringTodayEarnedPoints, getTodayPointsScored() + valueNeedtobeAdded);
        editor.apply();

    }


    public int getPointsScored() {
        return sharedpreferences.getInt(stringPointsScored, 200);
    }

    public void justSetPointsScored(int value) {
        editor.putInt(stringPointsScored, value);
        editor.apply();
    }

    public void setPayoutPointsScored(final int value, final boolean positive) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int before = getPointsScored();
                    if (positive) {
                        editor.putInt(stringPointsScored, getPointsScored() + value);
                    } else {
                        editor.putInt(stringPointsScored, getPointsScored() - value);
                    }
                    editor.apply();


                    object.put("PointsScored", getPointsScored());
                    object.saveInBackground();

                    setHistoryActivity(-value, 7, getObjectId(), before);


                }
            }
        });

    }

    public void setHistoryActivity(int points, int act, String objId, int before) {

        int hours;
        String a, min;
        if (currentHours > 12) {
            hours = currentHours - 12;
            a = " PM,";
        } else {
            hours = currentHours;
            a = " AM,";
        }

        if (currentMinutes < 10) {
            min = "0" + String.valueOf(currentMinutes);
        } else {
            min = String.valueOf(currentMinutes);
        }
        String date = String.valueOf(hours) + ":" + min + a + " " + currentDate;

        final ParseObject gameScore = new ParseObject(context.getString(R.string.parse_history));
        gameScore.put("date", date);
        gameScore.put("activityNumber", act);
        gameScore.put("points", points);
        if(act != 6) {
            gameScore.put("before", before);
            gameScore.put("after", getPointsScored());
        }
        gameScore.put("objId", objId);
        gameScore.put("version", SplashScreenActivity.currentVersionCode);
        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("ABC", "Success");
                } else {
                    Log.i("ABC", e.getMessage());
                }
            }
        });
    }

    public void setGamePointScored(final int valueNeedtobeAdded) {

        final int before = getPointsScored();
        editor.putInt(stringPointsScored, getPointsScored() + valueNeedtobeAdded);
        editor.apply();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("PointsScored", getPointsScored());
                    object.saveInBackground();
                    setHistoryActivity(valueNeedtobeAdded, 9, getObjectId(), before);
                } else {
                    Log.i("ABC Exception", e.toString());
                }
            }
        });

    }

    public void setPointsScored(final int valueNeedtobeAdded, final int act) {

        if (getTodayPointsScored() <= 400) {
            final int before = getPointsScored();
            Toast.makeText(context, Integer.toString(valueNeedtobeAdded) + " points has been added", Toast.LENGTH_SHORT).show();
            editor.putInt(stringPointsScored, getPointsScored() + valueNeedtobeAdded);
            setTodayEarnedPoints(valueNeedtobeAdded);
            editor.apply();


            ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
            query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {

                        object.put("PointsScored", getPointsScored());
                        object.put("todayPoints", getTodayPointsScored());
                        object.put("todayPointsDate", getDate());
                        if (act == 1) {
                            object.put("todaySpinPoints", getSpinPointsEarnedToday());
                        }
                        object.saveInBackground();
                        setHistoryActivity(valueNeedtobeAdded, act, getObjectId(), before);
                    } else {
                        Log.i("ABC Exception", e.toString());
                    }
                }
            });

        } else {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.maxlimit);
            dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view) {
                    if (!((Activity) context).isFinishing() && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            });
            dialog.show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Log.i("ABC after pointsScored" + String.valueOf(valueNeedtobeAdded), "getTodaypoints " + String.valueOf(getTodayPointsScored()));

    }

    public String getUserName() {
        return sharedpreferences.getString(stringName, "");
    }

    public void setUserName(String value) {

        editor.putString(stringName, value);
        editor.apply();
    }

    public String getPhoneNumber() {
        return sharedpreferences.getString(stringPhoneNumber, "");
    }

    public void setPhoneNumber(String value) {

        editor.putString(stringPhoneNumber, value);
        editor.apply();

    }

    public String getPassword() {
        return sharedpreferences.getString(stringPassword, "");
    }

    public void setPassword(String value) {

        editor.putString(stringPassword, value);
        editor.apply();

    }

    public int getSpinsAvailable() {
        return sharedpreferences.getInt(stringSpinsAvailable, 3);
    }

    @SuppressLint("LongLogTag")
    public void setSpinsAvailable(int valueNeedtobeAdded) {

        editor.putInt(stringSpinsAvailable, getSpinsAvailable() + valueNeedtobeAdded);
        editor.apply();

    }

    public String getRunningTime() {
        return sharedpreferences.getString(stringRunningTime, currentHourandDate);
    }


    public int getCurrentTimeSpinsAvailable() {
        return sharedpreferences.getInt(stringCurretTimeSpinsAvailable, 5);
    }

    @SuppressLint("LongLogTag")
    public void setCurrentTimeSpinsAvailable(int valueNeedtobeAdded) {
        int totalValue;
        totalValue = getCurrentTimeSpinsAvailable() + valueNeedtobeAdded;

        editor.putInt(stringCurretTimeSpinsAvailable, totalValue);
        editor.apply();
    }

    public boolean getIsUserLoggedIn() {
        return sharedpreferences.getBoolean(stringIsLoggedIn, false);
    }

    public int getOpenedCount() {
        return sharedpreferences.getInt(stringOpenedCount, 0);
    }

    public void setOpenedCount(int value) {
        editor.putInt(stringOpenedCount, getOpenedCount() + value);
        editor.apply();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        query.getInBackground(getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("OpenedCount", getOpenedCount());
                    object.saveInBackground();
                } else {
                    Log.i("ABC Exception", e.toString());
                }
            }
        });

    }

    public void setIsUserSignIn(Boolean value) {

        editor.putBoolean(stringIsLoggedIn, value);
        editor.apply();

    }


    public void toolbarChanges(final View view) {


        if (view != null) {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = view.findViewById(R.id.pointsToolBarTextView);
                    textView.setText(context.getString(R.string.toolbarpoints, getPointsScored()));

                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(context instanceof PointsActivity)) {
                        Intent intent = new Intent(context, PointsActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public String referalCodeGen() {
        final String code = gen();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.parse_main_one));
        query.whereEqualTo("ReferalCode", code);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList.size() != 0) {
                        referalCodeGen();
                    }
                }
            }
        });

        return code;
    }

    private String gen() {
        int passwordSize = 6;
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < passwordSize; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }


}