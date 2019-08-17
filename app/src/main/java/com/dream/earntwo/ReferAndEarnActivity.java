package com.dream.earntwo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReferAndEarnActivity extends AppCompatActivity {

    String referalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Refer & Earn");

        ADS ads = new ADS(this,false);

        Shared shared = new Shared(this);
        shared.toolbarChanges(mToolbar);
        referalCode = shared.getReferalCode();
        final TextView textView = findViewById(R.id.referal_code_value);
        String a = "  " +referalCode+ "  ";
        textView.setText(a);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", referalCode);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ReferAndEarnActivity.this,"Coupon Copied",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void shareIntent(View view) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.referText,referalCode ));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
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

}
