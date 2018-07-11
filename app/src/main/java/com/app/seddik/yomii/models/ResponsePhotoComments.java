package com.app.seddik.yomii.models;

import java.util.ArrayList;

/**
 * Created by Seddik on 09/07/2018.
 */

public class ResponsePhotoComments {
    private ArrayList<CommentItems> data;
    private boolean success;
    private String message;
    private int number_pages;

    public ArrayList<CommentItems> getData() {
        return data;
    }

    public void setData(ArrayList<CommentItems> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumber_pages() {
        return number_pages;
    }

    public void setNumber_pages(int number_pages) {
        this.number_pages = number_pages;
    }
}
