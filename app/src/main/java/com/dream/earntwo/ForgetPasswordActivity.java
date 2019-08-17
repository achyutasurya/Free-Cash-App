package com.dream.earntwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_oldpassword)
    EditText _oldpasswordText;
    @BindView(R.id.input_newpassword_one)
    EditText _newpasswordTextOne;
    @BindView(R.id.input_newpassword_two)
    EditText _newpasswordTextTwo;
    @BindView(R.id.btn_change_password)
    Button _changePasswordBtn;
    String oldPassword, oneNewPassword, twoNewPassword;
    Shared shared;
    ProgressDialog progressDialog;
    int sec;

    @Override
    public void onResume() {
        super.onResume();
        TextView usernamesigin = findViewById(R.id.textViewLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/beloved.otf");
        usernamesigin.setTypeface(tf);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        TextView usernamesigin = (TextView) findViewById(R.id.textViewLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/beloved.otf");
        usernamesigin.setTypeface(tf);

        shared = new Shared(this);

        _changePasswordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changePass();
            }
        });


        sec = 4000;

        progressDialog = new ProgressDialog(ForgetPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);

        checkInternetConnection();


    }

    public void changePass() {

        if (!validate()) {
            onLoginFailed();
            return;
        }else if(oldPassword.equals(oneNewPassword)){
            Toast.makeText(getBaseContext(), "Old and New Passwords are Same", Toast.LENGTH_LONG).show();
            _changePasswordBtn.setEnabled(true);
            return;
        }

        _changePasswordBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Requesting...");
        progressDialog.show();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(getString(R.string.parse_main_one));
        query1.getInBackground(shared.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (object.getString("Password").equals(oldPassword)) {
                        object.put("Password", oneNewPassword);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                shared.setPassword(oneNewPassword);
                                if(!isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                onLoginSuccess();
                            }
                        });
                    } else {
                        _oldpasswordText.setError("Check your old password");
                        _changePasswordBtn.setEnabled(true);
                        if(!isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(ForgetPasswordActivity.this,"Error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    _changePasswordBtn.setEnabled(false);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        finish();
        Intent a = new Intent(ForgetPasswordActivity.this, AccountActivity.class);
        startActivity(a);

    }

    public void onLoginSuccess() {
        Intent intent;
        Toast.makeText(ForgetPasswordActivity.this, "Successfully Password Changed", Toast.LENGTH_SHORT).show();
        intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Please Check Details", Toast.LENGTH_LONG).show();

        _changePasswordBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        oneNewPassword = _newpasswordTextOne.getText().toString();
        twoNewPassword = _newpasswordTextTwo.getText().toString();
        oldPassword = _oldpasswordText.getText().toString();

        if (oldPassword.isEmpty() || oldPassword.length() < 4 || oldPassword.length() > 10) {
            _oldpasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _oldpasswordText.setError(null);
        }


        if (!(oneNewPassword.isEmpty() || oneNewPassword.length() < 4 || oneNewPassword.length() > 10)) {
            if (!(twoNewPassword.isEmpty() || twoNewPassword.length() < 4 || twoNewPassword.length() > 10)) {
                if (oneNewPassword.equals(twoNewPassword)) {
                    _newpasswordTextTwo.setError(null);
                    _newpasswordTextOne.setError(null);
                } else {
                    _newpasswordTextTwo.setError("Both Passwords are not Same");
                    valid = false;
                }
            } else {
                _newpasswordTextTwo.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            }

        } else {
            _newpasswordTextOne.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }


        return valid;
    }


    public void checkInternetConnection(){
        if(isInternetOn()) {
            if(!isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if(!isFinishing()) {
                                checkInternetConnection();
                            }
                            sec = sec + 500;
                        }
                    }, sec);
        }else {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Unable to Connect to Server...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if(!isFinishing()) {
                progressDialog.show();
            }

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            sec = 4000;
                            if(!isFinishing()) {
                                checkInternetConnection();
                            }
                        }
                    }, 4000);



        }
    }



    public Boolean isInternetOn() {

        //getBaseContext();
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

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
