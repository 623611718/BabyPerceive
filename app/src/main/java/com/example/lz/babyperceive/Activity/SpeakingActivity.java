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
import com.example.lz.babyperceive.View.TitleView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SpeakingActivity extends AppCompatActivity implements View.OnClickListener {

    private Random random;
    private TextView chinese_tv,chineseSpell_tv;
    private int random_number;
    private String text;    //3500常用汉字
    private String chinses;  //单个汉字
    private String previous_number = "";//上一个汉字
    private TextToSpeech tts;  //文字转语音类
    private Button previous_bt,next_bt,play_bt;
    private String[] spell = new String[0];
    private String chineseSpell = "";
<<<<<<< HEAD
    private TitleView titleView;
=======
>>>>>>> 3741aa1b355fb6f410f8eca6bbca581709f05bcd

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

    private String getChineseSpell(String chinese){
        String[] pyStrs = PinyinHelper.toHanyuPinyinStringArray('重');

        for (String s : pyStrs) {
            System.out.println(s);
        }
//-------------------指定格式转换----------------------------
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

// UPPERCASE：大写  (ZHONG)
// LOWERCASE：小写  (zhong)
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//输出大写

// WITHOUT_TONE：无音标  (zhong)
// WITH_TONE_NUMBER：1-4数字表示音标  (zhong4)
// WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);

// WITH_V：用v表示ü  (nv)
// WITH_U_AND_COLON：用"u:"表示ü  (nu:)
// WITH_U_UNICODE：直接用ü (nü)
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        try {
            char c = chinese.charAt(0);
            spell = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        String s = Arrays.toString(spell); //
        return s;
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
        chinses = text.substring(random_number - 1, random_number);      //随机获取的汉字
        chineseSpell = getChineseSpell(chinses);                          //获取拼音
        Log.i("test", "内容:" + chinses+"拼音:"+chineseSpell);
        chinese_tv.setText(chinses);
        chineseSpell_tv.setText(chineseSpell);
    }

    /**
     * 汉字赋值并显示
     */
    private void initData(String name) {
        chinses = name;
        chinese_tv.setText(chinses);
        chineseSpell_tv.setText(chineseSpell);
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
                switch (v.getId()){
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
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
                tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.previous_bt:
                initData(previous_number);
                tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
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
