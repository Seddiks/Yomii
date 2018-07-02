package com.app.seddik.yomii.models;

import java.io.ByteArrayOutputStream;

/**
 * Created by Seddik on 02/01/2018.
 */

public class PopulairCategoriesHorizontalItems {
    private int image;


    private String title;
    private ByteArrayOutputStream stream;
    private int  markFilter;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ByteArrayOutputStream getStream() {
        return stream;
    }

    public void setStream(ByteArrayOutputStream stream) {
        this.stream = stream;
    }

    public int getMarkFilter() {
        return markFilter;
    }

    public void setMarkFilter(int markFilter) {
        this.markFilter = markFilter;
    }
}
