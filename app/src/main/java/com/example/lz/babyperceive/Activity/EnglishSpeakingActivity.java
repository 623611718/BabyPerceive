package com.example.lz.babyperceive.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lz.babyperceive.Bean.English;
import com.example.lz.babyperceive.Bean.EnglishAsrJson;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EnglishSpeakingActivity extends AppCompatActivity {

    private List<English> englishList = new ArrayList<>();
    private Utils utils;
    private int random_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_speaking);
        initEnglishData();   //初始化英语单词数据
        initRandom();
    }

    private void initRandom() {
        random_number = utils.getRandomNumber(5);
    }

    private  void initEnglishData(){
        utils = new Utils();
        String jsondata= utils.getTextEnglish();
        EnglishAsrJson englishAsrJson = new EnglishAsrJson();
        englishList = englishAsrJson.parseJSONenglish(jsondata);
    }
}
