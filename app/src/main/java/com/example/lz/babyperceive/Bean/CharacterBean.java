package com.example.lz.babyperceive.Bean;

/**
 * Created by lz on 2019/9/10.
 */

public class CharacterBean {
    private String words;
    private String wordsSpell;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getWordsSpell() {
        return wordsSpell;
    }

    public void setWordsSpell(String wordsSpell) {
        this.wordsSpell = wordsSpell;
    }

    @Override
    public String toString() {
        return "CharacterBean{" +
                "words='" + words + '\'' +
                ", wordsSpell='" + wordsSpell + '\'' +
                '}';
    }
}
