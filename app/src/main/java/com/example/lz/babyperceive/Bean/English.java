package com.example.lz.babyperceive.Bean;

/**
 * Created by lz on 2019/8/22.
 */

public class English {
    private int id;
    private String english;
    private String chinese;

    public void setId(int id) {
        this.id = id;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public String getEnglish() {
        return english;
    }

    public String getChinese() {
        return chinese;
    }

    @Override
    public String toString() {
        return "English{" +
                "id=" + id +
                ", english='" + english + '\'' +
                ", chinese='" + chinese + '\'' +
                '}';
    }
}
