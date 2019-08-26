package com.example.lz.babyperceive.Activity;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.English;
import com.example.lz.babyperceive.Bean.EnglishAsrJson;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnglishSpeakingActivity extends AppCompatActivity implements View.OnClickListener {

    private List<English> englishList = new ArrayList<>();
    private Utils utils;
    private int random_number;
    private TextToSpeech tts;   //文字转语音
    private TitleView titleView;
    private TextView chinese_tv, chineseSpell_tv;
    private Button previous_bt, next_bt, play_bt;
    private String english, chinese;
    private int previous_number = 0;//上一个随机数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_speaking);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        changeStatusBarTextColor(true);
        initEnglishData();   //初始化英语单词数据
        initRandom();
        initView();
        TextToSpeech();
    }

    private void initView() {
        chinese_tv = (TextView) findViewById(R.id.chinese_tv);
        chinese_tv.setOnClickListener(this);
        chineseSpell_tv = (TextView) findViewById(R.id.chineseSpell_tv);
        previous_bt = (Button) findViewById(R.id.previous_bt);
        previous_bt.setOnClickListener(this);
        next_bt = (Button) findViewById(R.id.next_bt);
        next_bt.setOnClickListener(this);
        play_bt = (Button) findViewById(R.id.play_bt);
        play_bt.setOnClickListener(this);
        titleView = (TitleView) findViewById(R.id.titleview);
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
    }

    //改变状态栏字体颜色
    private void changeStatusBarTextColor(boolean isBlack) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
    }

    private void initRandom() {
        random_number = utils.getRandomNumber(5);
        chinese = englishList.get(random_number).getChinese();
        english = englishList.get(random_number).getEnglish();
    }

    private void initEnglishData() {
        utils = new Utils();
        String jsondata = utils.getTextEnglish();
        EnglishAsrJson englishAsrJson = new EnglishAsrJson();
        englishList = englishAsrJson.parseJSONenglish(jsondata);
    }

    private void TextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
// 判断是否转化成功
                if (status == TextToSpeech.SUCCESS) {
                    //默认设定语言为中文，原生的android貌似不支持中文。
                    int result = tts.setLanguage(Locale.CHINESE);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Toast.makeText(this, R.string.notAvailable, Toast.LENGTH_SHORT).show();
                    } else {
                        //不支持中文就将语言设置为英文
                        tts.setLanguage(Locale.US);
                    }
                }
            }
        });
        tts.setSpeechRate(0.7f);
    }

    /**
     * \
     *
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chinese_tv:
                tts.speak(english, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.next_bt:
                previous_number = random_number;
                initRandom();
                initData(random_number);
                tts.speak(english, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.previous_bt:
                initData(previous_number);
                tts.speak(english, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.play_bt:
                tts.speak(english, TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }
}
