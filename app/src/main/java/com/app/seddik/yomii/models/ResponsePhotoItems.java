package com.app.seddik.yomii.models;

import java.util.ArrayList;

/**
 * Created by Seddik on 17/12/2017.
 */

public class ResponsePhotoItems {
    private boolean success;
    private String message;
    private ArrayList<Paths> data;


    public ResponsePhotoItems(boolean success, String message, ArrayList<Paths> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResponsePhotoItems(ArrayList<Paths> data) {
        this.data = data;
    }

    public ResponsePhotoItems() {

    }

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

    public ArrayList<Paths> getData() {
        return data;
    }

    public void setData(ArrayList<Paths> data) {
        this.data = data;
    }

    public class Paths  {
        private int photo_id;
        private String photo_path;
        private String city;
        private int parent;

        private int user_id;
        private String photo_profil_path;
        private String full_name;


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
    }
}
