package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 27/12/2017.
 */

public class NotificationItems {
    private int image_profile ;
    private String name_profile ;
    private String text;
    private String date;

    public int getImage_profile() {
        return image_profile;
    }

    public void setImage_profile(int image_profile) {
        this.image_profile = image_profile;
    }

    public String getName_profile() {
        return name_profile;
    }

    public void setName_profile(String name_profile) {
        this.name_profile = name_profile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
