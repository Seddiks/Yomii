package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 17/12/2017.
 */

public class ResponseItems {
    private boolean success;
    private String message;

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

}
