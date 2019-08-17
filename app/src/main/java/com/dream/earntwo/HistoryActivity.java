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

public class HistoryActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_history);

        ADS ads = new ADS(this, false);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Users History");
        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

        recyclerView =  findViewById(R.id.payoutHistoryRecycleView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        contactList = new ArrayList<>();

        progressDialogHistory = new ProgressDialog(HistoryActivity.this);
        progressDialogHistory.setIndeterminate(true);
        progressDialogHistory.setMessage("getting data...");
        progressDialogHistory.setCancelable(false);
        progressDialogHistory.setCanceledOnTouchOutside(false);
        if(!isFinishing()) {
            progressDialogHistory.show();
        }

        Log.i("ABC", shared.getReferalCode());

        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_history));
        query.whereEqualTo("objId", shared.getObjectId());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList.size() != 0) {
                        int max = scoreList.size() > 20 ? 20 : scoreList.size();
                        for(int i = 0; i<max; i++){
                            date = scoreList.get(i).getString("date");
                            point = scoreList.get(i).getInt("points");
                            int x = scoreList.get(i).getInt("activityNumber");
                            if(x == 0){
                                activity = scoreList.get(i).getString("activity");
                            }else if(x == 1){
                                activity = getString(R.string.activity_spin);
                            }else if(x == 2){
                                activity = getString(R.string.activity_reward_one);
                            }else if(x == 3){
                                activity = getString(R.string.activity_reward_two);
                            }else if(x == 4){
                                activity = getString(R.string.activity_survey);
                            }else if(x == 5){
                                activity = getString(R.string.activity_first_time);
                            }else if(x == 6){
                                activity = getString(R.string.activity_refer);
                            }else if(x == 7){
                                activity = getString(R.string.activity_with_drawn);
                            }else if(x == 8){
                                activity = getString(R.string.activity_rate);
                            }else if(x == 9){
                                activity = getString(R.string.activity_game);
                            }
                            else{
                                activity = getString(R.string.activity_else);
                            }

                            contactList.add(new UserHistoryModel(date, point, activity));
                        }
                        adapter = new UserHistoryAdapter(contactList, HistoryActivity.this);
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
                        Toast.makeText(HistoryActivity.this,"Nothing To Show",Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (progressDialogHistory.isShowing() && !isFinishing()) {
                        progressDialogHistory.dismiss();
                    }
                    Log.d("score", "Error: " + e.getMessage());
                    Toast.makeText(HistoryActivity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
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
