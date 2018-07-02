package com.app.seddik.yomii.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Seddik on 03/06/2018.
 */

public class TravelStoryItems implements Serializable{
    private  ArrayList<Data> data;
    private String  message;
    private boolean success;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public static class Data implements Serializable{
        private int travel_story_id;
        private String photo_path, title, location, full_story, pathPhoto_from_uri;

        public int getTravel_story_id() {
            return travel_story_id;
        }

        public void setTravel_story_id(int travel_story_id) {
            this.travel_story_id = travel_story_id;
        }

        public String getPhoto_path() {
            return photo_path;
        }

        public void setPhoto_path(String photo_path) {
            this.photo_path = photo_path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getFull_story() {
            return full_story;
        }

        public void setFull_story(String full_story) {
            this.full_story = full_story;
        }

        public String getPathPhoto_from_uri() {
            return pathPhoto_from_uri;
        }

        public void setPathPhoto_from_uri(String pathPhoto_from_uri) {
            this.pathPhoto_from_uri = pathPhoto_from_uri;
        }
    }
}
