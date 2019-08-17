package com.dream.earntwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VideoOneActivity extends AppCompatActivity {

    ADS ads;
    Toolbar mToolbar;
    Shared shared;
    TextView textView;
    Button button;
    RewardAds rewardAds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_one);


        button = findViewById(R.id.videoOneButton);
        textView = findViewById(R.id.textViewStatus);

        ads = new ADS(this,true);
        rewardAds = new RewardAds(this, true, button, textView);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Watch and Earn");

        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

    }

    public void butt(View view) {
        rewardAds.btnShowRewardedClick(30,false, mToolbar);

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
