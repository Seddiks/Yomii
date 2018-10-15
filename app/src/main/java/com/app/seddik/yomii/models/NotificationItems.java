package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 27/12/2017.
 */

public class NotificationItems {
    private int notification_id;
    private String photo_profil_sender;
    private int photo_id;
    private int action_type;
    private String full_name;
    private String notification;
    private String created_at;
    private boolean is_read;


    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getPhoto_profil_sender() {
        return photo_profil_sender;
    }

    public void setPhoto_profil_sender(String photo_profil_sender) {
        this.photo_profil_sender = photo_profil_sender;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
