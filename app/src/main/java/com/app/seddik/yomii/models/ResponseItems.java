package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 17/12/2017.
 */

public class ResponseItems {
    private boolean success;
    private String message;
    private int number_comments_per_post;
    private int number_likes_per_post;

    public ResponseItems(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

    public int getNumber_comments_per_post() {
        return number_comments_per_post;
    }

    public void setNumber_comments_per_post(int number_comments_per_post) {
        this.number_comments_per_post = number_comments_per_post;
    }

    public int getNumber_likes_per_post() {
        return number_likes_per_post;
    }

    public void setNumber_likes_per_post(int number_likes_per_post) {
        this.number_likes_per_post = number_likes_per_post;
    }
}
