package com.dream.earntwo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AccountActivity extends AppCompatActivity {


    Toolbar mToolbar;
    Shared shared;
    RelativeLayout relativeLayout;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    LinearLayout changePassword, reportError, completeProfile;
    TextView userName;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ADS ads = new ADS(this, false);
        shared = new Shared(this);

        userName = findViewById(R.id.username);


        if(!shared.getUserName().isEmpty()) {
            userName.setText(shared.getUserName());
        }else {
            userName.setText(getText(R.string.app_name));
        }
        changePassword  = findViewById(R.id.changePass);
        completeProfile = findViewById(R.id.complete_profile);
        reportError = findViewById(R.id.report_error);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        completeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();
            }
        });
        reportError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();
            }
        });




        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Profile");


        shared.toolbarChanges(mToolbar);

        if (shared.getIsAccountOnServer() && !shared.getIsImageUploaded() && !shared.getImageUri().isEmpty()) {
            Log.i("ABC ", "upload image");
            uploadImage(Uri.parse(shared.getImageUri()));
        }

        relativeLayout = findViewById(R.id.image_relative);
        imageView = findViewById(R.id.imag);

        String stringUri = shared.getImageUri();

        Log.i("ABC", "Before");

        if (!stringUri.isEmpty()) {
            Log.i("ABC", "inside");
            try {
                Uri imageUri = Uri.parse(stringUri);
                imageView.setImageURI(imageUri);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to set Image " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            imageView.setImageDrawable(this.getResources().getDrawable(R.drawable.main));
        }

        if (!shared.getIsAccountOnServer()) {

            Log.i("ABC ", "!getisAccountOnserver");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_user_mobile));
            query.whereEqualTo("objId", shared.getObjectId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() == 0) {
                            Log.i("ABC ", "upload image == 0");
                            final ParseObject jobApplication = new ParseObject(getString(R.string.parse_user_mobile));
                            jobApplication.put("objId", shared.getObjectId());
                            jobApplication.put("phoneNumber", shared.getPhoneNumber());
                            jobApplication.put("countryCode",shared.getCountryCode());
                            jobApplication.put("name", shared.getUserName());
                            jobApplication.put("referalCode", shared.getReferalCode());
                            jobApplication.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        shared.setAccountObjectId(jobApplication.getObjectId());
                                        shared.setIsAccountOnServer(true);
                                    }
                                }
                            });
                        } else {
                            Log.i("ABC ", "upload image != 0");
                            shared.setIsAccountOnServer(true);
                            shared.setAccountObjectId(objects.get(0).getObjectId());

                            Log.i("info", "image found!");

                            if(objects.get(0).get("images") != null) {
                                ParseFile file = (ParseFile) objects.get(0).get("images");
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                imageView.setImageBitmap(bitmap);


                                                try {
                                                    Log.i("ABC", "try");
                                                    File cachePath = new File(AccountActivity.this.getCacheDir(), "images");
                                                    cachePath.mkdirs(); // don't forget to make the directory
                                                    FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                    stream.close();

                                                } catch (IOException ek) {
                                                    ek.printStackTrace();
                                                    Log.i("ABC", "catch");
                                                }

                                                File imagePath = new File(AccountActivity.this.getCacheDir(), "images");
                                                File newFile = new File(imagePath, "image.png");
                                                Uri contentUri = FileProvider.getUriForFile(AccountActivity.this, "com.dream.earntwo.fileprovider", newFile);

                                                if (contentUri != null) {
                                                    Log.i("ABC", "contentUri != null");
                                                    shared.setImageUri(contentUri.toString());
                                                    shared.setIsImageUploaded(true);
                                                }
                                        } else {
                                            Log.i("info", e.getMessage());
                                        }
                                    }
                                });
                            }


                        }
                    }
                }
            });
        }


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int PERMISSION_ALL = 1;
                String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

                if (!hasPermissions(AccountActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(AccountActivity.this, PERMISSIONS, PERMISSION_ALL);
                }*/
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setBorderLineColor(Color.RED)
                        .setGuidelinesColor(Color.GREEN)
                        .setAspectRatio(1, 1)
                        .setBorderLineThickness(getResources().getDimensionPixelSize(R.dimen.thickness))
                        .start(AccountActivity.this);
            }
        });

    }

    public void uploadImage(Uri resultUri) {
        InputStream iStream;
        byte[] data123;
        try {

            Log.i("ABC ", "try");
            iStream = getContentResolver().openInputStream(resultUri);

            data123 = getBytes(iStream);

            final ParseFile file = new ParseFile("resume.png", data123);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("ABC ", "image == null");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_user_mobile));
                        query.getInBackground(shared.getAccountObjectId(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    object.put("images", file);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.i("ABC ", "Image Added");
                                                shared.setIsImageUploaded(true);
                                            } else {

                                                Log.i("ABC ", "upload failed " + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        Log.i("ABC ", "image == null");
                    }
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                shared.setImageUri(resultUri.toString());
                shared.setIsImageUploaded(false);
                if (shared.getIsAccountOnServer()) {
                    uploadImage(resultUri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error has occured " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
