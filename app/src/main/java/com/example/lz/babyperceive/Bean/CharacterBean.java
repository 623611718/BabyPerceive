package com.example.lz.babyperceive.Bean;

/**
 * Created by lz on 2019/9/10.
 */

public class CharacterBean {
    private String words;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "Character{" +
                "words='" + words + '\'' +
                '}';
    }
}
