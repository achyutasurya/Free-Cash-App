package com.dream.earntwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RecentlyPaidActivity extends AppCompatActivity {

    static String clickedpaytmimage;
    private static CustomAdapter adapter;
    private static RecyclerView recyclerView;
    ArrayList<DataModel> contactList;
    boolean proof = false;
    Toolbar mToolbar;
    Shared shared;
    RelativeLayout relativeLayout;
    private ProgressDialog pDialog;
    private RecyclerView.LayoutManager layoutManager;
    String amount, number, image, paytmImage, name;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if (proof) {
            proof = false;
            mToolbar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_paid);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Recent Payments");

        ADS ads = new ADS(this, false);

        shared = new Shared(this);
        shared.toolbarChanges(mToolbar);

        relativeLayout = findViewById(R.id.relativeLay);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        contactList = new ArrayList<>();

        //new GetContacts().execute();


        pDialog = new ProgressDialog(RecentlyPaidActivity.this);
        pDialog.setMessage("Please wait...");
            /*pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);*/
        if (!isFinishing()) {
            pDialog.show();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.parse_recent_paid));
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList.size() != 0) {
                        for(int i = 0; i<scoreList.size(); i++){
                            name = scoreList.get(i).getString("name");
                            number = scoreList.get(i).getString("number");
                            image = scoreList.get(i).getString("image");
                            paytmImage = scoreList.get(i).getString("paytmImage");
                            amount = scoreList.get(i).getString("amount");
                            contactList.add(new DataModel(name, number, amount, image, paytmImage));
                        }

                        if (pDialog.isShowing() && !isFinishing()) {
                            pDialog.dismiss();
                        }

                        adapter = new CustomAdapter(contactList, RecentlyPaidActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                clickedpaytmimage = contactList.get(position).getPaytmimage();
                                Log.i("ContactsName", contactList.get(position).getName() + "   " + clickedpaytmimage);


                                mToolbar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                proof = true;

                                ImageView imageView = findViewById(R.id.imageProff);

                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                final ProgressBar progressBar = findViewById(R.id.progress);

                                Glide.with(RecentlyPaidActivity.this)
                                        .load(RecentlyPaidActivity.clickedpaytmimage)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(imageView);

                            }
                        });

                    }else{
                        if (pDialog.isShowing() && !isFinishing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(RecentlyPaidActivity.this,"Nothing To Show",Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pDialog.isShowing() && !isFinishing()) {
                        pDialog.dismiss();
                    }
                    Log.d("score", "Error: " + e.getMessage());
                    Toast.makeText(RecentlyPaidActivity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /*private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RecentlyPaidActivity.this);
            pDialog.setMessage("Please wait...");
            *//*pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);*//*
            if (!isFinishing()) {
                pDialog.show();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String mobile = c.getString("mobile");
                        String amt = "RS " + c.getString("amount");
                        String img = c.getString("image");
                        String paytmimage = c.getString("paytmimage");



                        contactList.add(new DataModel(name, mobile, amt, img, paytmimage));
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Error Occured : " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Unable to Receive data from Server!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing() && !isFinishing()) {
                pDialog.dismiss();
            }

            adapter = new CustomAdapter(contactList, RecentlyPaidActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    clickedpaytmimage = contactList.get(position).getPaytmimage();
                    //Toast.makeText(RecentlyPaidActivity.this, Integer.toString(position),Toast.LENGTH_SHORT).show();
                    Log.i("ContactsName", contactList.get(position).getName() + "   " + clickedpaytmimage);


                    mToolbar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    proof = true;

                    ImageView imageView = findViewById(R.id.imageProff);

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    final ProgressBar progressBar = findViewById(R.id.progress);

                    Glide.with(RecentlyPaidActivity.this)
                            .load(RecentlyPaidActivity.clickedpaytmimage)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imageView);

                }
            });

        }

    }*/
}
