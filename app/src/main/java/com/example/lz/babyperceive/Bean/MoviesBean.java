package com.example.lz.babyperceive.Bean;

import android.widget.ImageView;

/**
 * Created by lz on 2019/9/15.
 */

public class MoviesBean {
    private int imageId;
    private String name;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MoviesBean{" +
                "imageId=" + imageId +
                ", name='" + name + '\'' +
                '}';
    }
}
