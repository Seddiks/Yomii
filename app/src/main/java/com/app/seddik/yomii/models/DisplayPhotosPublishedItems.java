package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 23/12/2017.
 */

public class DisplayPhotosPublishedItems {
    private int photo_id;
    private String photo_path;
    private String city;
    private int parent;

    private int user_id;
    private String photo_profil_path;
    private String full_name;
    private int number_comments;
    private boolean isLike;
    private int number_likes;
    private String location;
    private String legende;
    private String created_at;


    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_profil_path() {
        return photo_profil_path;
    }

    public void setPhoto_profil_path(String photo_profil_path) {
        this.photo_profil_path = photo_profil_path;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLegende() {
        return legende;
    }

    public void setLegende(String legende) {
        this.legende = legende;
    }
}