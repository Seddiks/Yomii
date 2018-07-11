package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 23/12/2017.
 */

public class DisplayPhotosPublishedItems {
    private int photo_id;
    private String photo_profil;
    private String photo_published;
    private String full_name;
    private String date;

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto_profil() {
        return photo_profil;
    }

    public void setPhoto_profil(String photo_profil) {
        this.photo_profil = photo_profil;
    }

    public String getPhoto_published() {
        return photo_published;
    }

    public void setPhoto_published(String photo_published) {
        this.photo_published = photo_published;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}