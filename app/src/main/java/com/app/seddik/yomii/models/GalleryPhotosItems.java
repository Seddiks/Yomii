package com.app.seddik.yomii.models;

/**
 * Created by Seddik on 23/12/2017.
 */

public class GalleryPhotosItems {

    //@SerializedName("photo_id")
    private int photo_id;
    //@SerializedName("photo_path")
    private String photo_path;
    //@SerializedName("parent")
    private int parent;


    public int getPhoto_id() {
        return photo_id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        GalleryPhotosItems user = (GalleryPhotosItems) obj;

        return user.photo_id == this.photo_id;
    }

}