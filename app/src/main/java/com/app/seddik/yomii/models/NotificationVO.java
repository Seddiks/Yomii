package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 20/06/2018.
 */

public class NotificationVO {
    private String title;
    private String message;
    private String iconUrl;
    private String path_big_photo;
    private String action;
    private String actionDestination;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDestination() {
        return actionDestination;
    }

    public String getPath_big_photo() {
        return path_big_photo;
    }

    public void setPath_big_photo(String path_big_photo) {
        this.path_big_photo = path_big_photo;
    }

    public void setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
    }
}