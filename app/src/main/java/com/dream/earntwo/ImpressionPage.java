package com.dream.earntwo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.startapp.android.publish.ads.banner.Banner;

public class ImpressionPage extends AppCompatActivity {

    int x=10;
    TextView textView;
    ADS ads;
    Boolean abc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impression_page);

        textView = findViewById(R.id.countDown);

        runEverySec();
        ads = new ADS(this,false);
        adsOne();



    }

    public void goToImpressionsActivity(){
        if(abc) {
            Intent nextActivity = new Intent(this, ImpressionsActivity.class);
            startActivity(nextActivity);
        }
    }

    public void runEverySec(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                if(x!=0){
                    runEverySec();
                    x--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.countDown)).setText(Integer.toString(x));
                        }
                    });
                }else {
                    goToImpressionsActivity();
                }
            }
        }, 1000);
    }

    public void adsOne(){
        RelativeLayout mainLayout = findViewById(R.id.relative_impressions_page);
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
        Log.i("Surya","Page onbackpressed Start");
        Intent nextActivity = new Intent(getApplicationContext(), ImpressionsActivity.class);
        startActivity(nextActivity);
        abc = false;
        finish();
        Log.i("Surya","Page onbackpressed end");
    }
}
