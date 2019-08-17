package com.dream.earntwo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishSurveyCompletedListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishSurveyReceivedListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;
import com.pollfish.main.PollFish;

import java.util.Objects;

public class SurveyActivity extends AppCompatActivity implements
        PollfishSurveyCompletedListener, PollfishOpenedListener,
        PollfishClosedListener, PollfishSurveyReceivedListener,
        PollfishSurveyNotAvailableListener, PollfishUserNotEligibleListener {

    Shared shared;
    Toolbar mToolbar;
    private Button coinsBtn;
    private TextView loggingTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_survey);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Take Survey");

        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

        new ADS(this, false);

        loggingTxt = findViewById(R.id.logText);
        coinsBtn = findViewById(R.id.coins_btn);

        coinsBtn.setVisibility(View.INVISIBLE);

        coinsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PollFish.show();
            }
        });


    }


    @Override
    public void onPollfishClosed() {
        Log.d("Pollfish1", "onPollfishClosed");
    }

    @Override
    public void onPollfishOpened() {
        Log.d("Pollfish1", "onPollfishOpened");

    }

    @Override
    public void onPollfishSurveyNotAvailable() {
        Log.d("Pollfish1", "onPollfishSurveyNotAvailable");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loggingTxt.setText(getString(R.string.not_available));
            }
        });
    }

    @Override
    public void onUserNotEligible() {
        Log.d("Pollfish1", "onUserNotEligible");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loggingTxt.setText(getString(R.string.user_not_eligible));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        PollFish.ParamsBuilder paramsBuilder = new PollFish.ParamsBuilder("5fa4f6f4-55d5-4f8c-88b1-95b32854747d")
                .indicatorPadding(5)
                .customMode(true)
                .build();

        PollFish.initWith(this, paramsBuilder);

        loggingTxt.setText(getString(R.string.logging));

        coinsBtn.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        PollFish.ParamsBuilder paramsBuilder = new PollFish.ParamsBuilder("5fa4f6f4-55d5-4f8c-88b1-95b32854747d")
                .indicatorPadding(5)
                .customMode(true)
                .build();

        PollFish.initWith(this, paramsBuilder);

        loggingTxt.setText(getString(R.string.logging));

        coinsBtn.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onPollfishSurveyReceived(boolean playfulSurvey, int surveyPrice) {
        Log.d("Pollfish1", "onPollfishSurveyReceived(" + playfulSurvey + " , " + surveyPrice + ")");

        //PollFish.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                coinsBtn.setVisibility(View.VISIBLE);
                loggingTxt.setText("Pollfish Survey Received");
            }
        });
    }

    @Override
    public void onPollfishSurveyCompleted(boolean playfulSurvey, final int surveyPrice) {
        Log.d("Pollfish1", "onPollfishSurveyCompleted(" + playfulSurvey + " , " + surveyPrice + ")");
        shared.setPointsScored(100, 4);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
        query.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("surveysCompleted", object.getInt("surveysCompleted") + 1);
                    object.saveInBackground();
                } else {
                    Log.i("ABC Exception", e.toString());
                }
            }
        });
        shared.toolbarChanges(mToolbar);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                coinsBtn.setVisibility(View.INVISIBLE);
                loggingTxt.setText(getString(R.string.survey_completed));

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
