package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 18/12/2017.
 */

public class UserItems {
    private boolean success;
    private int user_id;
    private String email;
    private int isConfirmed;
    private String first_name;
    private String last_name;
    private String message;
    private String token;
    private String full_name,username,photo_profil_path,photo_cover_path,bio;


    public UserItems() {
    }

    public UserItems(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }



    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getPhoto_profil_path() {
        return photo_profil_path;
    }

    public void setPhoto_profil_path(String photo_profil_path) {
        this.photo_profil_path = photo_profil_path;
    }

    public String getPhoto_cover_path() {
        return photo_cover_path;
    }

    public void setPhoto_cover_path(String photo_cover_path) {
        this.photo_cover_path = photo_cover_path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }



    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }


}
