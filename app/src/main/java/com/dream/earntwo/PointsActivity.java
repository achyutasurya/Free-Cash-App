package com.dream.earntwo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PointsActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static HistoryAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView recyclerView;
    TextView payout, history;
    RelativeLayout mainLayout;
    LayoutInflater inflater;
    View layout;
    Toolbar mToolbar;
    Shared shared;
    CardView card1, card2, card3, card4, card5;
    CardView cards[] = {card1, card2, card3, card4, card5};
    int[] cardIds = {R.id.cardView1, R.id.cardView2, R.id.cardView3, R.id.cardView4, R.id.cardView5};
    int[] pointsNeeded = {15000, 16000, 15000, 14000, 13000};
    int[] pointsCash = {30, 30, 30, 30, 30};
    String[] process = {"Paytm", "Google Pay", "PayPal", "WeChat", "BitCoin"};
    String[] processDesc = {"Paytm money, transfered to your paytm registed mobile number",
            "money, transfered to your registed Google Pay Account",
            "PayPal money, transfered to your registed email address",
            "money, transfered to your registed WeChat Account",
            "BitCoin, and transfered to your wallet address"};
    String status, headText;
    int money, gateWay, points;
    ArrayList<HistoryModel> contactList;
    ProgressDialog progressDialogHistory;
    int currentVersionCode;
    Dialog dialog;
    private RecyclerView.LayoutManager layoutManager;
    ADS ads;

    public void pointsTextView(int points){
        TextView pointsBoard = findViewById(R.id.pointsBoard);
        pointsBoard.setText(String.valueOf(points));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        payout = findViewById(R.id.payoutTextView);
        history = findViewById(R.id.historyTextView);

        currentVersionCode = SplashScreenActivity.currentVersionCode;

        payout.setTextColor(getResources().getColor(R.color.white));
        history.setTextColor(getResources().getColor(R.color.colorPrimary));

        ads = new ADS(this, false);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Payout");


        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);
        final int pointsAvailable = shared.getPointsScored();
        pointsTextView(pointsAvailable);

        dialog = new Dialog(this);


        mainLayout = findViewById(R.id.includeLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Context context = this;


        for (int i = 0; i < 5; i++) {

            cards[i] = mainLayout.findViewById(cardIds[i]);
            final int finalI = i;
            cards[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(pointsNeeded[finalI] <= pointsAvailable)) {
                        String text = " You Don't have " + Integer.toString(pointsNeeded[finalI]);
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    } else {
                        if (isInternetOn()) {
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery(getString(R.string.parse_app_details));
                            query1.getInBackground(getString(R.string.parse_app_details_id), new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        if (object.getInt("latestVersionCode") != currentVersionCode) {
                                            showUpdateDialog();
                                        } else {
                                            payoutFirstProcess(finalI);
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(PointsActivity.this, "Please check Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }


        payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payoutClick();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyClick();
            }
        });

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
                if (!isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        });
        if (!isFinishing()) {
            dialog.show();
        }
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public void payoutFirstProcess(final int finalI) {


        dialog.setContentView(R.layout.readypayout);
        TextView textView = dialog.findViewById(R.id.whichPayment);
        textView.setText(process[finalI]);
        TextView textViewContent = (TextView)dialog.findViewById(R.id.textViewDesc);
        textViewContent.setText(getString(R.string.readyPayment, processDesc[finalI], pointsNeeded[finalI], pointsCash[finalI]));
        dialog.findViewById(R.id.readyPayoutCancel).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {

                if (!isFinishing() && dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                    dialog.dismiss();
                }
            }

        });
        dialog.findViewById(R.id.readyPayoutOk).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                if (isInternetOn()) {
                    pressedOkButton(pointsNeeded[finalI], pointsCash[finalI], finalI);
                    dialog.hide();
                } else {
                    Toast.makeText(PointsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        });
        if (!isFinishing()) {
            dialog.show();
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void pressedOkButton(final int points, final int money, final int process) {

        final ProgressDialog progressDialog = new ProgressDialog(PointsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Requesting..");
        if (!isFinishing()) {
            progressDialog.show();
        }

        if (shared.getIsUserLoggedIn() && shared.getIsItOnServer()) {

            final int before = shared.getPointsScored();
            ParseQuery<ParseObject> query1 = ParseQuery.getQuery(getString(R.string.parse_main_one));
            query1.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        int serverPoints = object.getInt("PointsScored");
                        Log.i("ABC" + String.valueOf(serverPoints),"  " +String.valueOf(points));
                        if(serverPoints >= points) {
                            Log.i("ABC","Inside");

                            final ParseObject gameScore = new ParseObject(getString(R.string.parse_payout));
                            gameScore.put("money", money);
                            gameScore.put("GateWay", process);
                            gameScore.put("points", points);
                            gameScore.put("IsPaid", false);
                            gameScore.put("statusNumber", 1);
                            gameScore.put("versionCode", SplashScreenActivity.currentVersionCode);
                            gameScore.put("userObjectId", shared.getReferalCode());
                            gameScore.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
                                        query.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
                                            public void done(ParseObject object, ParseException e) {
                                                if (e == null) {
                                                    object.put("PointsScored", object.getInt("PointsScored") - points);
                                                    object.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {
                                                                new android.os.Handler().postDelayed(
                                                                        new Runnable() {
                                                                            public void run() {
                                                                                Intent nextActivity = new Intent(PointsActivity.this, SplashScreenActivity.class);
                                                                                startActivity(nextActivity);
                                                                                if (!isFinishing() && progressDialog.isShowing()) {
                                                                                    progressDialog.dismiss();
                                                                                }
                                                                            }
                                                                        }, 2000);
                                                                shared.justSetPointsScored(shared.getPointsScored() - points);
                                                                shared.setHistoryActivity(-points, 7, shared.getObjectId(),before);
                                                            }
                                                        }

                                                    });

                                                }
                                            }
                                        });
                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {
                                                        Intent nextActivity = new Intent(PointsActivity.this, SplashScreenActivity.class);
                                                        startActivity(nextActivity);
                                                        if (!isFinishing() && progressDialog.isShowing()) {
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                }, 400000);
                                    } else {
                                        if (!isFinishing() && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Log.i("ABC", "in first parseobject else");
                                        Toast.makeText(PointsActivity.this, "Unable to Process your Reqest be" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }else {
                            if (!isFinishing() && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (!isFinishing() && dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            shared.justSetPointsScored(serverPoints);
                            shared.toolbarChanges(mToolbar);
                            pointsTextView(serverPoints);
                            Toast.makeText(PointsActivity.this, "Unable to Process your Reqest required points not available" , Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });



        }else{

            if (!isFinishing() && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(PointsActivity.this, "Unable to Process your Reqest be" , Toast.LENGTH_LONG).show();
        }
    }


    public void payoutClick() {
        history.setBackgroundColor(getResources().getColor(R.color.white));
        history.setTextColor(getResources().getColor(R.color.colorPrimary));
        payout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        payout.setTextColor(getResources().getColor(R.color.white));
        layout = inflater.inflate(R.layout.payoutlayout, null);
        mainLayout.removeAllViews();
        mainLayout.addView(layout);
        ads.showImpressions();
    }

    public void historyClick() {

        if (isInternetOn()) {

            progressDialogHistory = new ProgressDialog(PointsActivity.this);
            progressDialogHistory.setIndeterminate(true);
            progressDialogHistory.setMessage("getting data...");
            if (!isFinishing()) {
                progressDialogHistory.show();
            }

            payoutHistoryParse();


        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void payoutHistoryParse() {


        contactList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_payout));
        query.whereEqualTo("userObjectId", shared.getReferalCode());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList.size() != 0) {
                        for (int i = 0; i < scoreList.size(); i++) {
                            int x = scoreList.get(i).getInt("statusNumber");
                            if (x == 0) {
                                status = scoreList.get(i).getString("status");
                            } else if (x == 1) {
                                status = getString(R.string.payout_submit);
                            } else if (x == 2) {
                                status = getString(R.string.payout_verification_process);
                            } else if (x == 3) {
                                status = getString(R.string.payout_verification_completed);
                            } else if (x == 4) {
                                status = getString(R.string.payout_payment_processing);
                            } else if (x == 5) {
                                status = getString(R.string.payout_payment_completed);
                            } else if (x == 6) {
                                status = getString(R.string.payout_withdraw_rejected);
                            } else {
                                status = getString(R.string.payout_else);
                            }
                            money = scoreList.get(i).getInt("money");
                            gateWay = scoreList.get(i).getInt("GateWay");

                            headText = " Withdrawn cash " + String.valueOf(money) + "USD";

                            contactList.add(new HistoryModel(status, gateWay, headText));
                        }


                        adapter = new HistoryAdapter(contactList, PointsActivity.this);

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        layout = inflater.inflate(R.layout.history_layout, null);
                                        mainLayout.removeAllViews();
                                        mainLayout.addView(layout);
                                        mainLayout.findViewById(R.id.no_payout_history).setVisibility(View.GONE);

                                        recyclerView = mainLayout.findViewById(R.id.payoutHistoryRecycleView);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        recyclerView.setHasFixedSize(true);

                                        layoutManager = new LinearLayoutManager(PointsActivity.this);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                                        historyMethod();
                                        recyclerView.setAdapter(adapter);
                                        if (!isFinishing() && progressDialogHistory != null && progressDialogHistory.isShowing()) {
                                            progressDialogHistory.dismiss();
                                            ads.showImpressions();
                                        }
                                    }
                                }, 1000);
                    } else {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        historyMethod();
                                        layout = inflater.inflate(R.layout.history_layout, null);
                                        mainLayout.removeAllViews();
                                        mainLayout.addView(layout);
                                        mainLayout.findViewById(R.id.no_payout_history).setVisibility(View.VISIBLE);
                                        mainLayout.findViewById(R.id.payoutHistoryRecycleView).setVisibility(View.GONE);
                                        if (!isFinishing() && progressDialogHistory != null && progressDialogHistory.isShowing()) {
                                            progressDialogHistory.dismiss();
                                            ads.showImpressions();
                                        }
                                    }
                                }, 1000);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                    Toast.makeText(PointsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void historyMethod() {

        history.setTextColor(getResources().getColor(R.color.white));
        payout.setTextColor(getResources().getColor(R.color.colorPrimary));
        payout.setBackgroundColor(getResources().getColor(R.color.white));
        history.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(nextActivity);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shared.toolbarChanges(mToolbar);
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
