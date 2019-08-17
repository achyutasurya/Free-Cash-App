package com.dream.earntwo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class SpinActivity extends AppCompatActivity {
    java.util.List<LuckyItem> data = new ArrayList<>();
    ADS ads;
    RewardAds rewardAds;
    int index;
    MediaPlayer mp;
    boolean isSpinnerRotating = false;
    Shared shared;
    Toolbar mToolbar;
    int x = 0;

    static int runnedSpingAfterOpening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Spin & Earn");

        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

        ads = new ADS(this, true);


        updateSpins();

        final LuckyWheelView luckyWheelView = findViewById(R.id.luckyWheel);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "0";
        luckyItem1.icon = R.drawable.crying;
        luckyItem1.color = Color.parseColor("#FDE700");
        data.add(luckyItem1);


        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "15";
        luckyItem2.icon = R.drawable.coin3;
        luckyItem2.color = Color.parseColor("#772A91");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "5";
        luckyItem3.icon = R.drawable.coin1;
        luckyItem3.color = Color.parseColor("#F51827");
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "15";
        luckyItem4.icon = R.drawable.coin;
        luckyItem4.color = Color.parseColor("#0671C7");
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "0";
        luckyItem5.icon = R.drawable.crying;
        luckyItem5.color = Color.parseColor("#97CE3D");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "25";
        luckyItem6.icon = R.drawable.coin2;
        luckyItem6.color = Color.parseColor("#FB6622");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "0";
        luckyItem7.icon = R.drawable.crying;
        luckyItem7.color = Color.parseColor("#FDE700");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "Spin";
        luckyItem8.icon = R.drawable.spin;
        luckyItem8.color = Color.parseColor("#F51827");
        data.add(luckyItem8);

        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.text = "35";
        luckyItem9.icon = R.drawable.coin4;
        luckyItem9.color = Color.parseColor("#97CE3D");
        data.add(luckyItem9);


        luckyWheelView.setData(data);
        luckyWheelView.setRound(getRandomRound());


        findViewById(R.id.play).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isSpinnerRotating) {

                    if (shared.getSpinsAvailable() >= 1) {
                        if (shared.getCurrentTimeSpinsAvailable() >= 1) {
                            shared.setCurrentTimeSpinsAvailable(-1);
                        }
                        shared.setSpinsAvailable(-1);
                        runnedSpingAfterOpening ++;
                        if(runnedSpingAfterOpening%2 == 0) {
                            ads.showAdSpinner();
                        }
                        index = getRandomIndex();
                        luckyWheelView.startLuckyWheelWithTargetIndex(index);
                        audio(R.raw.bicycle);
                        isSpinnerRotating = true;
                        updateSpins();
                    } else {
                        diaologPopUpWatchAds(null);
                    }
                }
            }
        });


        luckyWheelView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSpinnerRotating) {

                    if (shared.getSpinsAvailable() >= 1) {
                        if (shared.getCurrentTimeSpinsAvailable() >= 1) {
                            shared.setCurrentTimeSpinsAvailable(-1);
                        }
                        runnedSpingAfterOpening++;
                        if(runnedSpingAfterOpening%3 == 0) {
                            ads.showAdSpinner();
                        }
                        shared.setSpinsAvailable(-1);
                        index = getRandomIndex();
                        luckyWheelView.startLuckyWheelWithTargetIndex(index);
                        audio(R.raw.bicycle);
                        isSpinnerRotating = true;
                        updateSpins();
                    } else {
                        diaologPopUpWatchAds(null);
                    }
                }
            }
        });


        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int a) {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        isSpinnerRotating = false;
                    }
                }, 1000);


                if (a == 1 || a == 5 || a == 7 || a == 0) {
                    diaologPopUpLoser(null);
                } else if (a == 8) {
                    diaologPopUpWinner(0, false);
                } else {
                    int score = 0;
                    if (a == 2 || a == 4) {
                        score = 15;
                    } else if (a == 3) {
                        score = 5;
                    } else if (a == 6) {
                        score = 25;
                    } else if (a == 9) {
                        score = 35;
                    }
                    if(score != 0) {
                        shared.setSpinPointsEarnedToday(score);
                        diaologPopUpWinner(score, true);
                    }
                }
            }
        });
    }

    public void updateSpins() {

        final String deadLineContent;

        int spinsAvailable = shared.getCurrentTimeSpinsAvailable();

        Calendar now = Calendar.getInstance();
        int currentMinutes = 60 - now.get(Calendar.MINUTE);

        if (spinsAvailable >= 1) {
            deadLineContent = getString(R.string.notCompleteddeadLineSpin, spinsAvailable, currentMinutes);
        } else {
            deadLineContent = getString(R.string.completeddeadLineSpin, currentMinutes);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                TextView textView23 = findViewById(R.id.availableSpins);
                textView23.setText(getString(R.string.availableSpins, shared.getSpinsAvailable()));
                TextView textViewCurrent = findViewById(R.id.DeadLineSpins);
                textViewCurrent.setText(deadLineContent);
            }
        });
    }

    public void diaologPopUpWatchAds(View view) {

        rewardAds = new RewardAds(this,false);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);
        dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                rewardAds.btnShowRewardedClick(0, true, mToolbar);
            }

        });
        dialog.findViewById(R.id.popuplater).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                if(!isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent nextActivity7 = new Intent(SpinActivity.this, SurveyActivity.class);
                startActivity(nextActivity7);
            }

        });
        if(!isFinishing()) {
            dialog.show();
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void diaologPopUpWinner(int score, boolean check) {

        if(shared.getTodayPointsScored() <= 400) {
            final Dialog dialog = new Dialog(this);
            audio(R.raw.winning);
            dialog.setContentView(R.layout.winner_layout);
            TextView textView = dialog.findViewById(R.id.pointsScored);
            dialog.findViewById(R.id.winnerbutton).setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view) {
                    if(!isFinishing() && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            });
            if (check) {
                textView.setText(String.valueOf(score));
                shared.setPointsScored(score,1);
                shared.toolbarChanges(mToolbar);
            } else {
                textView.setText("Extra");
                TextView spinExtra = dialog.findViewById(R.id.points);
                shared.setSpinsAvailable(1);
                spinExtra.setText("1 Spin");
            }
            if(!isFinishing()) {
                dialog.show();
            }
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        else{
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.maxlimit);
            dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view) {
                    if(!isFinishing() && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            if(!isFinishing()) {
                dialog.show();
            }
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void diaologPopUpLoser(View view) {
        final Dialog dialog = new Dialog(this);
        audio(R.raw.lossing);
        dialog.setContentView(R.layout.fail_layout);
        dialog.findViewById(R.id.failOkButton).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                if(!isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        });
        if(!isFinishing()) {
            dialog.show();
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private int getRandomIndex() {
        Random rand = new Random();
        int value = rand.nextInt(data.size() - 1);
        if(shared.getSpinPointsEarnedToday() > 60){
            int[] intArray = {1, 5, 7, 0, 3};

            int idx = new Random().nextInt(intArray.length);
            value = intArray[idx];
        }
        return value;
    }

    private int getRandomRound() {
        return 4;
    }

    public void audio(int a) {
        if (x == 1) {
            mp.release();
        } else {
            x++;
        }
        mp = MediaPlayer.create(this, a);
        mp.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!isSpinnerRotating) {
            Intent nextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(nextActivity);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shared.toolbarChanges(mToolbar);
    }
}
