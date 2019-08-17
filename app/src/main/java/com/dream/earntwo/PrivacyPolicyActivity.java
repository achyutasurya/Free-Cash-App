package com.dream.earntwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class PrivacyPolicyActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        ADS ads = new ADS(this, false);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Privacy Policy");

        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

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
