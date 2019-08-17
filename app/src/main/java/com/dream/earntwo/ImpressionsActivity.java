package com.dream.earntwo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.startapp.android.publish.ads.banner.Banner;

public class ImpressionsActivity extends AppCompatActivity {

    TextView textView, headintText;
    Button button;
    CheckBox checkBox;
    Banner banner;
    Handler handler;
    Runnable runnable;
    ProgressBar progressBar;
    Shared shared;
    static Boolean isItVideoOne;
    static Boolean isAutomated = false;
    RewardAds rewardAds;
    int ads;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressions);

        textView = findViewById(R.id.textViewRemain);
        checkBox = findViewById(R.id.automatic_check_box);
        button  = findViewById(R.id.okImpressions);
        banner = findViewById(R.id.startAppBanner);
        progressBar = findViewById(R.id.progress_rount);
        shared = new Shared(this);
        headintText = findViewById(R.id.task_number);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isItVideoOne = bundle.getBoolean("isItVideoOne");
        }

        if(isItVideoOne){
            ads = shared.getTaskOne();
        }else{
            ads = shared.getTaskTwo();
            headintText.setText("Task-2");
        }

        textView.setText(Integer.toString(ads));

        if(ads == 0){
            button.setText("Completed");
            isAutomated=false;
        }


        if(isAutomated){

            button.setText("Stop");
            banner.hideBanner();
            progressBar.setVisibility(View.VISIBLE);
            checkBox.setChecked(true);
            handler = new Handler();
            runnable = new Runnable() {
                @SuppressLint("SetTextI18n")
                public void run() {
                    goToImpressionsPage();
                }
            };
            handler.postDelayed(runnable, 4000);
            ads();

        }else{
            ads();

        }


    }



    public void start(View view){

        if(isAutomated){
            isAutomated = false;
            handler.removeCallbacks(runnable);
            button.setText("Start");
            progressBar.setVisibility(View.GONE);
            banner.showBanner();
            ads();

        }else {

            if (!checkBox.isChecked()) {

                isAutomated = false;


            } else {

                button.setText("Stop");
                isAutomated = true;

            }

            goToImpressionsPage();
        }


    }


    public void goToImpressionsPage(){
        if(isItVideoOne) {
            int x = shared.getTaskOne();
            if(x != 0) {
                x=x-1;
                shared.setTaskOne(x);
                if(x==0){
                    rewardAds = new RewardAds(this, 2);
                }
            }
        }else{
            int x = shared.getTaskTwo();
            if(x != 0) {
                x = x-1;
                shared.setTaskTwo(x);
                if(x==0){
                    rewardAds = new RewardAds(this, 3);
                }
            }
        }
        Intent nextActivity = new Intent(this, ImpressionPage.class);
        startActivity(nextActivity);
        finish();
    }


    public void ads(){
        RelativeLayout mainLayout = findViewById(R.id.relative_imp);
        Banner startAppBanner = new Banner(this);
        RelativeLayout.LayoutParams bannerParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mainLayout.addView(startAppBanner, bannerParameters);

        Banner startAppBanner1 = new Banner(this);
        RelativeLayout.LayoutParams bannerParameters1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bannerParameters1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mainLayout.addView(startAppBanner1, bannerParameters1);
    }

    @Override
    public void onBackPressed() {
        isAutomated = false;
        Log.i("Surya","Activity onbackpressed");
        Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(nextActivity);
        finish();
        Log.i("Surya","Activity onbackpressed end");
    }
}
