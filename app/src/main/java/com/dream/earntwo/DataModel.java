package com.dream.earntwo;

public class DataModel {

    String name;
    String mobile;
    String amount;
    String image;
    private String paytmimage;

    public DataModel(String name, String version, String amount, String image, String paytmimage) {
        this.name = name;
        this.mobile = version;
        this.amount = amount;
        this.image=image;
        this.paytmimage = paytmimage;
    }

    public String getName() {
        return name;
    }

    public String getPaytmimage() {
        return paytmimage;
    }

    public String getMobile() {
        return mobile;
    }

    public String getImage() {
        return image;
    }

    public String getAmount() {
        return amount;
    }
}