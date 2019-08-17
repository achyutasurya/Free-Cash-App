package com.dream.earntwo;

public class UserHistoryModel {

    String date;
    int point;
    String activity;

    public UserHistoryModel(String date, int point, String activity) {
        this.date = date;
        this.point = point;
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public int getPoint() {
        return point;
    }

    public String getActivity() {
        return activity;
    }

}