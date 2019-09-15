package com.example.lz.babyperceive.Bean;

/**
 * Created by lz on 2019/9/15.
 */

public class ImageRecognitionBean {
    private String name;
    private String score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ImageRecognitionBean{" +
                "name='" + name + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
