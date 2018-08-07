package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 23/12/2017.
 */

public class DisplayPhotosPublishedItems {
    private int photo_id;
    private int user_id;
    private String photo_profil;
    private String photo_published;
    private String full_name;
    private String date;
    private int number_comments;
    private boolean isLike;
    private int number_likes;
    private String location;
    private String legende;
    private String created_at;

    public String getLegende() {
        return legende;
    }

    public void setLegende(String legende) {
        this.legende = legende;
    }

    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNumber_comments() {
        return number_comments;
    }

    public void setNumber_comments(int number_comments) {
        this.number_comments = number_comments;
    }

    public int getNumber_likes() {
        return number_likes;
    }

    public void setNumber_likes(int number_likes) {
        this.number_likes = number_likes;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}