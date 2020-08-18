package com.app.seddik.yomii.test.ui.data;

/**
 * Created by Seddik on 16/08/2018.
 */

public class NetworkState {
    public static NetworkState LOADED = new NetworkState(Status.SUCCESS);
    public static NetworkState LOADING = new NetworkState(Status.RUNNING);
    private Status status;
    private String message;

    private NetworkState(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    private NetworkState(Status status) {
        this.status = status;
    }

    public static NetworkState error(String message) {
        return new NetworkState(Status.FAILED, message == null ? "unknown error" : message);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


}
