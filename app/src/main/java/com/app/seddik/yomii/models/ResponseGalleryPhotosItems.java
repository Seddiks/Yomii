package com.app.seddik.yomii.models;

import java.util.ArrayList;

/**
 * Created by Seddik on 13/08/2018.
 */

public class ResponseGalleryPhotosItems {
    private boolean success;
    private String message;
    private ArrayList<GalleryPhotosItems> data;
    private int number_pages;

    public int getNumber_pages() {
        return number_pages;
    }

    public void setNumber_pages(int number_pages) {
        this.number_pages = number_pages;
    }

    public ArrayList<GalleryPhotosItems> getData() {
        return data;
    }

    public void setData(ArrayList<GalleryPhotosItems> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
