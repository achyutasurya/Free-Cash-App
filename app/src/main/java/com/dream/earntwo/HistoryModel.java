package com.dream.earntwo;

public class HistoryModel {

    String status;
    int gateWay;
    String headText;

    public HistoryModel(String status, int gateWay, String headText) {
        this.status = status;
        this.gateWay = gateWay;
        this.headText = headText;
    }

    public String getStatus() {
        return status;
    }

    public int getGateWay() {
        return gateWay;
    }

    public String getHeadText() {
        return headText;
    }

}