package com.app.seddik.yomii.models;

import java.util.ArrayList;

/**
 * Created by Seddik on 07/08/2018.
 */

public class ResponseNotificationItems {
    private ArrayList<NotificationItems> data;
    private boolean success;
    private String message;
    private int number_pages;

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

    public ArrayList<NotificationItems> getData() {
        return data;
    }

    public void setData(ArrayList<NotificationItems> data) {
        this.data = data;
    }

    public int getNumber_pages() {
        return number_pages;
    }

    public void setNumber_pages(int number_pages) {
        this.number_pages = number_pages;
    }
}
