package com.dream.earntwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ReferralHistoryActivity extends AppCompatActivity {


    Toolbar mToolbar;
    Shared shared;

    String date, activity;
    int point;
    ProgressDialog progressDialogHistory;

    private static UserHistoryAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<UserHistoryModel> contactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_history);


        ADS ads = new ADS(this, false);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Referrals History");
        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

        recyclerView =  findViewById(R.id.payoutHistoryRecycleView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        contactList = new ArrayList<>();

        progressDialogHistory = new ProgressDialog(ReferralHistoryActivity.this);
        progressDialogHistory.setIndeterminate(true);
        progressDialogHistory.setMessage("getting data...");
        progressDialogHistory.setCancelable(false);
        progressDialogHistory.setCanceledOnTouchOutside(false);
        if(!isFinishing()) {
            progressDialogHistory.show();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
        query.whereEqualTo("OtherReferalCode", shared.getReferalCode());
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList.size() != 0) {
                        int max = scoreList.size();
                        for(int i = 0; i<max; i++){
                            date = scoreList.get(i).getString("Name");
                            point = scoreList.get(i).getInt("PointsScored");
                            boolean x = scoreList.get(i).getBoolean("firstReferalUpdate");
                            boolean y = scoreList.get(i).getBoolean("secondReferalUpdate");
                            int points = 0;
                            if(x){
                                points = points + 50;
                            }
                            if(y){
                                points = points + 450;
                            }

                            activity = "You Earned: " + String.valueOf(points) ;


                            contactList.add(new UserHistoryModel(activity, point, date));
                        }
                        adapter = new UserHistoryAdapter(contactList, ReferralHistoryActivity.this);
                        recyclerView.setAdapter(adapter);
                        if (progressDialogHistory.isShowing() && !isFinishing()) {
                            progressDialogHistory.dismiss();
                        }
                    }else{
                        if (progressDialogHistory.isShowing() && !isFinishing()) {
                            progressDialogHistory.dismiss();
                        }
                        recyclerView.setVisibility(View.GONE);
                        LinearLayout linearLayout =  findViewById(R.id.no_payout_history);
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(ReferralHistoryActivity.this,"Nothing To Show",Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (progressDialogHistory.isShowing() && !isFinishing()) {
                        progressDialogHistory.dismiss();
                    }
                    Log.d("score", "Error: " + e.getMessage());
                    Toast.makeText(ReferralHistoryActivity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });


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
}
