package com.example.lz.babyperceive.Bean;

/**
 * Created by Administrator on 2019/8/27.
 */

public class Idiom {
    private int id;
    private String idiom;
    private String story;
    private String paraphrase;

    public void setParaphrase(String paraphrase) {
        this.paraphrase = paraphrase;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getId() {
        return id;
    }

    public String getIdiom() {
        return idiom;
    }

    public String getStory() {
        return story;
    }

    public String getParaphrase() {
        return paraphrase;
    }

    @Override
    public String toString() {
        return "Idiom{" +
                "id=" + id +
                ", idiom='" + idiom + '\'' +
                ", story='" + story + '\'' +
                ", paraphrase='" + paraphrase + '\'' +
                '}';
    }
}
