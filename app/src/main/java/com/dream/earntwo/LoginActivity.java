package com.dream.earntwo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_mobile)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.link_forget_password)
    TextView _forgetPassword;
    String email;
    String password;
    Shared shared;
    Boolean isUserBlocked = false;
    CountryCodePicker ccp;

    @Override
    public void onResume() {
        super.onResume();
        TextView usernamesigin = (TextView) findViewById(R.id.textViewLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/beloved.otf");
        usernamesigin.setTypeface(tf);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        int greenColorValue = Color.parseColor("#FFFFFF");

        TextView usernamesigin = (TextView) findViewById(R.id.textViewLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/beloved.otf");
        usernamesigin.setTypeface(tf);

        shared = new Shared(this);
        ccp=findViewById(R.id.ccp);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });



        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _forgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showForgetDialog();
            }
        });
    }

    public void contactUs() {

        String mailto = "mailto:richardbrianofficial@yandex.com";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ExtraCash Pass Forget");

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(LoginActivity.this, "There is no email app installed.", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("SetTextI18n")
    private void showForgetDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maxlimit);
        TextView heading = dialog.findViewById(R.id.textView1);
        heading.setText(getText(R.string.forget_password));
        TextView conent = dialog.findViewById(R.id.textView2);
        conent.setText(getString(R.string.forget_password_content));
        dialog.findViewById(R.id.popupOk).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                if(!isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                    contactUs();
                }
            }

        });
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_main_one));
        query.whereEqualTo("PhoneNumber", email);
        query.whereEqualTo("countryCode",ccp.getSelectedCountryCode());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if(scoreList.size() != 0) {
                        if (scoreList.get(0).getString("Password").equals(password)) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // On complete call either onLoginSuccess or onLoginFailed
                                            onLoginSuccess();
                                            if(!isFinishing() && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }, 4000);

                            final ParseObject gameScore = new ParseObject(getString(R.string.parse_login_users));
                            gameScore.put("objId", scoreList.get(0).getObjectId());
                            gameScore.put("versionCode", SplashScreenActivity.currentVersionCode);
                            gameScore.saveInBackground();

                            shared.setReferalCode(scoreList.get(0).getString("ReferalCode"));
                            shared.justSetPointsScored(scoreList.get(0).getInt("PointsScored"));
                            shared.setOpenedCount(scoreList.get(0).getInt("OpenedCount"));
                            shared.setIsitOnServer(true);
                            shared.setCountryCode(scoreList.get(0).getString("countryCode"));
                            shared.setOtherReferalCode(scoreList.get(0).getString("OtherReferalCode"));
                            shared.setIsOtherReferalPointsUpdated(scoreList.get(0).getBoolean("firstReferalUpdate"));
                            shared.setIsOtherReferalPointsUpdatedTwo(scoreList.get(0).getBoolean("secondReferalUpdate"));
                            shared.setObjectId(scoreList.get(0).getObjectId());
                            shared.setUserName(scoreList.get(0).getString("Name"));
                            shared.setTaskOneTemp(scoreList.get(0).getInt("TaskOne"));
                            shared.setTaskTwoTemp(scoreList.get(0).getInt("TaskTwo"));
                            isUserBlocked = scoreList.get(0).getBoolean("isUserBlocked");
                            shared.setDate();

                            Log.i("ABC ObjectId",scoreList.get(0).getObjectId());

                        } else {

                            Toast.makeText(LoginActivity.this, "Check the Password", Toast.LENGTH_LONG).show();
                            if(!isFinishing() && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            _loginButton.setEnabled(true);
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Entered Number is Invalid, Please Signup", Toast.LENGTH_LONG).show();
                        if(!isFinishing() && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        _loginButton.setEnabled(true);
                    }

                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    _loginButton.setEnabled(true);
                    if(!isFinishing() && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        finish();
        Intent a = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(a);

    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        shared.setIsUserSignIn(true);
        shared.setRunAlarm(true);
        shared.setPhoneNumber(email);
        shared.setPassword(password);
        Intent intent;
        if(isUserBlocked){
            intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        }else {
            Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        if (email.isEmpty() || email.length() < 6 || email.length() > 13) {
            _emailText.setError("enter a valid Mobile Number");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
