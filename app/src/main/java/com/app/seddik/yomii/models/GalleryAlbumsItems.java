package com.app.seddik.yomii.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Seddik on 23/12/2017.
 */

public class GalleryAlbumsItems implements Serializable {

    private Integer image_id;
    private String title;

    private ArrayList<GalleryAlbumsItems.Paths> data;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getImage_ID() {
        return image_id;
    }

    public void setImage_ID(Integer android_image_url) {
        this.image_id = android_image_url;
    }

    public ArrayList<Paths> getData() {
        return data;
    }

    public void setData(ArrayList<Paths> data) {
        this.data = data;
    }



    public static class Paths implements Serializable{
        String photo_path;

        public String getPhoto_path() {
            return photo_path;
        }

        public void setPhoto_path(String photo_path) {
            this.photo_path = photo_path;
        }



    }
}