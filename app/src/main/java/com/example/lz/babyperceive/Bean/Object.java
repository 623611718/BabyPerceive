package com.example.lz.babyperceive.Bean;

/**
 * Created by lz on 2019/8/28.
 */

public class Object {
    private int id;
    private String object;
    private String name;
    private String imageId;
    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Object{" +
                "id=" + id +
                ", object='" + object + '\'' +
                ", name='" + name + '\'' +
                ", iamgeId=" + imageId +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
