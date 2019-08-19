package com.example.lz.babyperceive.Activity;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Utils;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SpeakingActivity extends AppCompatActivity implements View.OnClickListener {

    private Random random;
    private TextView chinese_tv;
    private int random_number;
    private String text;    //3500常用汉字
    private String chinses;  //单个汉字
    private String previous_number = "";//上一个汉字
    private TextToSpeech tts;  //文字转语音类
    private Button previous_bt,next_bt,play_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        changeStatusBarTextColor(true);
        initView();
        initData();
        TextToSpeech(); //文字转语音
        //   List


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
     * 获取随机数
     */
    private void getRandomNumber() {
        //random = new Random(3500);//指定种子数字
        //  random_number = random.nextInt();
        int max = 3500;
        int min = 1;
        random = new Random();
        random_number = random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * \
     * 设定随机数
     */
    private void initData() {
        getRandomNumber();
        Utils utils = new Utils();
        text = utils.getTextHanzi();
        Log.i("test", "位置:" + random_number);
        Log.i("test", "长度:" + text.length());
        chinses = text.substring(random_number - 1, random_number);
        Log.i("test", "内容:" + chinses);
        chinese_tv.setText(chinses);
    }

    /**
     * 汉字赋值并显示
     */
    private void initData(String name) {
        chinses = name;
        chinese_tv.setText(chinses);
    }

    private void initView() {
        chinese_tv = (TextView) findViewById(R.id.chinese_tv);
        chinese_tv.setOnClickListener(this);
        previous_bt = (Button) findViewById(R.id.previous_bt);
        previous_bt.setOnClickListener(this);
        next_bt = (Button) findViewById(R.id.next_bt);
        next_bt.setOnClickListener(this);
        play_bt = (Button) findViewById(R.id.play_bt);
        play_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chinese_tv:
                tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.next_bt:
                previous_number = chinses;
                initData();
                break;
            case R.id.previous_bt:
                initData(previous_number);
                break;
            case R.id.play_bt:
                tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
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

}



