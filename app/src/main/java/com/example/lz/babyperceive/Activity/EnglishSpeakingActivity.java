package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.English;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.AudioUtils;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.ButtonView;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnglishSpeakingActivity extends BaseActivity  {

    private List<English> englishList = new ArrayList<>();
    private Utils utils;
    private int random_number;
    private TitleView titleView;
    private TextView chinese_tv, chineseSpell_tv;
    private String english, chinese;
    private int previous_number = 0;//上一个随机数
    private Speek speek; //百度语音合成
    private ButtonView buttonView;

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_english_speaking);
        initEnglishData();   //初始化英语单词数据
        setData();
        initView();
        initData(0);
        speek = new Speek(this);
        speek.Speeking(english);
    }

    private void initView() {
        chinese_tv.setOnClickListener(this);
        titleView.setTitleView(1);   //1代表标题栏 显示 X
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
        buttonView.setCustomOnClickListener(new ButtonView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.chinese_tv:
                        speek.Speeking(english);
                        Log.i("test","!!!!!1");
                        break;
                    case R.id.next_bt:
                        Log.i("test","!!!!!1");
                        previous_number = random_number;
                        setData();
                        initData(random_number);
                        speek.Speeking(english);
                        break;
                    case R.id.previous_bt:
                        Log.i("test","!!!!!1");
                        initData(previous_number);
                        speek.Speeking(english);
                        break;
                    case R.id.play_bt:
                        Log.i("test","!!!!!1");
                        speek.Speeking(english);
                        break;
                }
            }
        });
    }


    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_english_speaking;
    }

    @Override
    public void initView(View view) {
        chinese_tv = $(R.id.chinese_tv);
        chineseSpell_tv = $(R.id.chineseSpell_tv);
        titleView = $(R.id.titleview);
        buttonView = $(R.id.buttonview);
    }

    @Override
    public void setListener() {

    }

    private void setData() {
        random_number = utils.getRandomNumber(5);
        chinese = englishList.get(random_number).getChinese();
        english = englishList.get(random_number).getEnglish();
    }

    private void initEnglishData() {
        utils = new Utils();
        String jsondata = utils.getTextEnglish();
        AsrJson englishAsrJson = new AsrJson();
        englishList = englishAsrJson.parseJSONenglish(jsondata);
    }


    /**
     * \
     */
    private void initData(int random_number) {
        //  chinses = text.substring(random_number - 1, random_number);      //随机获取的汉字
        //   chineseSpell = getChineseSpell(chinses);                          //获取拼音
        english = englishList.get(random_number).getEnglish();
        chinese = englishList.get(random_number).getChinese();
        Log.i("test", "英语:" + english + "中文:" + chinese);
        chinese_tv.setText(english);
        chineseSpell_tv.setText(chinese);
    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
