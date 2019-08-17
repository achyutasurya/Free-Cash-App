package com.dream.earntwo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.Objects;

public class RewardAds {

    Context context;
    int i = 1;
    private int pointsAds;
    private boolean spinsAds;
    private View viewAds;
    Button button;
    TextView textView;
    Shared shared;
    private Boolean isViewThere = true;

    private StartAppAd rewardedVideo;

    RewardAds(Context context1, Boolean isViewThereHere) {

        context = context1;

        isViewThere = isViewThereHere;

        shared = new Shared(context);

        loadRewardedVideoAd();


    }

    RewardAds(Context context1, Boolean isViewThereHere, View buttonView, View viewOfText) {


        context = context1;

        isViewThere = isViewThereHere;

        shared = new Shared(context);


        if (isViewThere) {
            button = buttonView.findViewById(R.id.videoOneButton);
            textView = viewOfText.findViewById(R.id.textViewStatus);

            textView.setText("Please wait, your ad is Loading");
            button.setVisibility(View.INVISIBLE);
        }

        loadRewardedVideoAd();
    }


    RewardAds(Context context, int actvity){
        shared = new Shared(context);
        shared.setPointsScored(30, actvity);
        shared.toolbarChanges(viewAds);
    }

    private void loadRewardedVideoAd() {

        if (shared.getTodayWatchedReward() < 8) {
            rewardedVideo = new StartAppAd(context);
            rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {

                @Override
                public void onReceiveAd(Ad arg0) {
                    if (isViewThere) {
                        textView.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailedToReceiveAd(Ad arg0) {
                    if (isViewThere) {
                        textView.setText("Ads Failed to Load");
                    }
                }
            });
        } else {
            if (isViewThere) {
                textView.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        }

    }


    public void btnShowRewardedClick(final int points, final boolean spins, final View view) {

        pointsAds = points;
        spinsAds = spins;
        viewAds = view;

        if (shared.getTodayWatchedReward() < 6) {

            if(rewardedVideo != null) {
                rewardedVideo.showAd(new AdDisplayListener() {
                    @Override
                    public void adHidden(Ad ad) {
                    }

                    @Override
                    public void adDisplayed(Ad ad) {
                        if (isViewThere) {
                            button.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Please wait, your ad is Loading");
                        }
                    }

                    @Override
                    public void adClicked(Ad ad) {
                    }

                    @Override
                    public void adNotDisplayed(Ad ad) {
                    }
                });
                rewardedVideo.setVideoListener(new VideoListener() {

                    @Override
                    public void onVideoCompleted() {

                        int a;

                        if (spinsAds) {
                            shared.setSpinsAvailable(2);
                            TextView txtView = ((Activity) context).findViewById(R.id.availableSpins);
                            txtView.setText(context.getString(R.string.availableSpins, shared.getSpinsAvailable()));
                            Toast.makeText(context, "2 Spins has been rewarded", Toast.LENGTH_SHORT).show();
                        } else {
                            if (context instanceof VideoOneActivity) {
                                a = 2;
                            } else if (context instanceof VideoTwoActivity) {
                                a = 3;
                            } else {
                                a = 8;
                            }
                            shared.setPointsScored(pointsAds, a);
                            shared.setTodayWatchedReward(1);
                            shared.toolbarChanges(viewAds);
                        }
                        loadRewardedVideoAd();

                    }
                });
            }else{
                rewardedVideo = new StartAppAd(context);
                btnShowRewardedClick( points,  spins, view);
            }

        } else {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.rewardmax);
            dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view) {
                    if(!((Activity) context).isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent intent = new Intent(context, SurveyActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            dialog.findViewById(R.id.popuplater).setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view) {
                    if(!((Activity) context).isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            });
            dialog.show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

}