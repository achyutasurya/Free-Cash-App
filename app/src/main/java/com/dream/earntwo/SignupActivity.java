package com.dream.earntwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.referal_code) EditText _referalText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    CountryCodePicker ccp;

    String name ;
    String mobile ;
    String password ;
    String referal;

    Shared shared;

    @Override
    public void onResume(){
        super.onResume();
        TextView usernamesigin = (TextView)findViewById(R.id.textViewLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/beloved.otf");
        usernamesigin.setTypeface(tf);

    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        TextView usernamesigin = (TextView)findViewById(R.id.textViewLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/beloved.otf");
        usernamesigin.setTypeface(tf);

        shared = new Shared(this);
        ccp=findViewById(R.id.ccp);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying Details...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
        query.whereEqualTo("PhoneNumber", mobile);
        query.whereEqualTo("countryCode",ccp.getSelectedCountryCode());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if(scoreList.size() == 0){




                        final ParseObject gameScore = new ParseObject(getString(R.string.parse_main_one));
                        gameScore.put("PhoneNumber", mobile);
                        gameScore.put("Password", password);
                        gameScore.put("Name", name);
                        gameScore.put("OtherReferalCode", referal.toUpperCase());
                        gameScore.put("ReferalCode", shared.getReferalCode());
                        gameScore.put("PointsScored", 200);
                        gameScore.put("TaskOne",15);
                        gameScore.put("TaskTwo",15);
                        gameScore.put("IsFacebook", false);
                        gameScore.put("AppName", "Cash Pot");
                        gameScore.put("countryCode", ccp.getSelectedCountryCode());
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Log.i("Success","full");
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    onSignupSuccess();
                                                    shared.setObjectId(gameScore.getObjectId());
                                                    shared.setHistoryActivity(200,5,gameScore.getObjectId(),0);
                                                    if(!isFinishing() && progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            }, 3000);
                                }
                                else {
                                    Log.i("Error","came "+ e.toString());
                                    Toast.makeText(SignupActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                                    if(!isFinishing() && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    _signupButton.setEnabled(true);
                                }
                            }
                        });

                    }else {

                        Toast.makeText(SignupActivity.this, "User Already Exists" , Toast.LENGTH_SHORT).show();
                        if(!isFinishing() && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        _signupButton.setEnabled(true);
                    }
                } else {
                    Toast.makeText(SignupActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                    _signupButton.setEnabled(true);
                }
            }
        });
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);

        shared.setIsUserSignIn(true);
        shared.setRunAlarm(true);
        shared.setUserName(name);
        shared.setPassword(password);;
        shared.setPhoneNumber(mobile);
        shared.setOtherReferalCode(referal);
        shared.setIsitOnServer(true);
        shared.setCountryCode(ccp.getSelectedCountryCode());

        Toast.makeText(SignupActivity.this, "Acount Has Been Created Success Now Start Earning", Toast.LENGTH_LONG).show();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        referal = _referalText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() < 6 || mobile.length() > 13) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if(!referal.isEmpty()) {
            if (referal.length() != 6) {
                _referalText.setError("must be 6 characters");
                valid = false;
            } else {
                _referalText.setError(null);
            }
        }


        return valid;
    }

    static int current;
    private Boolean exit = false;

    @Override
    public void onBackPressed() {

        if (current == 0) {
            if (exit) {
                finish();
                StartAppAd.onBackPressed(this);
                /*Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);*/
            } else {
                Toast.makeText(this, "Press Back again to go Login Page.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        exit=false;
                    }
                }, 2000);
            }
        }
        else {
            finish();
            Intent a = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(a);
        }
    }
}