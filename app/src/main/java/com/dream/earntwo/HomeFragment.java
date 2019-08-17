package com.dream.earntwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class HomeFragment extends Fragment {

    CardView video1, video2, installTheApp, spinTheWheel, referAndEarn, takeSurvey, rateUs;
    View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        assignToVariables(rootView);
        video1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent nextActivity = new Intent(getActivity(), ImpressionsActivity.class);
                nextActivity.putExtra("isItVideoOne", true);
                startActivity(nextActivity);


            }
        });

        video2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent nextActivity = new Intent(getActivity(), ImpressionsActivity.class);
                nextActivity.putExtra("isItVideoOne", false);
                startActivity(nextActivity);
            }
        });

        takeSurvey.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent nextActivity = new Intent(getActivity(), SurveyActivity.class);
                startActivity(nextActivity);
            }
        });

        referAndEarn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent nextActivity = new Intent(getActivity(), ReferAndEarnActivity.class);
                startActivity(nextActivity);
            }
        });

        installTheApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getActivity(),"Under Construction", Toast.LENGTH_SHORT).show();
                /*Intent nextActivity = new Intent(getActivity(), GameActivity.class);
                startActivity(nextActivity);*/
            }
        });

        rateUs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent nextActivity = new Intent(getActivity(), RateUsActivity.class);
                startActivity(nextActivity);
            }
        });

        spinTheWheel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent nextActivity = new Intent(getActivity(), SpinActivity.class);
                startActivity(nextActivity);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void assignToVariables(View rootView){
        video1=rootView.findViewById(R.id.videoOne);
        video2=rootView.findViewById(R.id.videoTwo);
        installTheApp=rootView.findViewById(R.id.install);
        spinTheWheel=rootView.findViewById(R.id.spinWheel);
        referAndEarn=rootView.findViewById(R.id.invite);
        takeSurvey=rootView.findViewById(R.id.survey);
        rateUs=rootView.findViewById(R.id.rateUs);
    }

}