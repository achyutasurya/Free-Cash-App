package com.dream.earntwo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ADS {

    Context context;
    ProgressDialog progressDialog;
    int sec;

    ADS(Context context1, boolean isRewardNeeded) {

        context = context1;

        sec = 25000;

        progressDialog = new ProgressDialog(context);

        if(context1 instanceof ImpressionPage) {
            showImpressions();
        }else{
            showAdSpinner();
        }

        checkInternetConnection();

    }

    public void showImpressions(){
        Log.i("Surya","ShowImpressions");
        final StartAppAd startAppAdOne = new StartAppAd(context);
        startAppAdOne.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener() {

            @Override
            public void onReceiveAd(Ad arg0) {
                startAppAdOne.showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad arg0) {
                Log.e("MainActivity", "Failed to load add: " + arg0.getErrorMessage());
            }
        });
    }

    public void showAdSpinner() {
        if(SplashScreenActivity.noOfInterstitialAdsSeen <= 25) {

            Log.i("Surya","showAdSpiner "+Integer.toString(SplashScreenActivity.noOfInterstitialAdsSeen));
            final StartAppAd startAppAdOne = new StartAppAd(context);
            startAppAdOne.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener() {

                @Override
                public void onReceiveAd(Ad arg0) {
                    startAppAdOne.showAd();
                    SplashScreenActivity.noOfInterstitialAdsSeen++;
                }

                @Override
                public void onFailedToReceiveAd(Ad arg0) {
                    Log.e("MainActivity", "Failed to load add: " + arg0.getErrorMessage());
                }
            });

        }
    }

    public void checkInternetConnection(){
        Log.i("Surya","Starting ");
        if(isInternetOn()) {
            Log.i("Surya","if ");
            if(!((Activity) context).isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
            }else{
                return;
            }
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if(!((Activity) context).isFinishing()) {
                                checkInternetConnection();
                            }
                            sec = sec + 2000;
                        }
                    }, sec);
        }else {
            Log.i("Surya","if ");
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Unable to Connect to Server...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if(!((Activity) context).isFinishing()) {
                progressDialog.show();
            }
            else{
                return;
            }

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            sec = 20000;
                            if((!((Activity) context).isFinishing()) && progressDialog.isShowing()) {
                                checkInternetConnection();
                            }
                        }
                    }, 5500);



        }
    }



    public Boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

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