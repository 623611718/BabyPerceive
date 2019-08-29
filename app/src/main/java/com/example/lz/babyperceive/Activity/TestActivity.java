package com.example.lz.babyperceive.Activity;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.lz.babyperceive.Dialog.SpeakingDialog;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.Utils.Voiceparsing;
import com.example.lz.babyperceive.View.TitleView;
import com.example.lz.babyperceive.asrfinishjson.AsrFinishJsonData;
import com.example.lz.babyperceive.asrpartialjson.AsrPartialJsonData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "TestActivity";
    private String final_result;  //语音解析的最终结果
    private String chinses;  //单个汉字
    private Utils utils;
    private TextView chinese_tv, chineseSpell_tv;
    private Button previous_bt, next_bt, play_bt;
    private TitleView titleView;
    private int score;   //用于测试的分数计算
    private ImageView right_iv;  //显示正确或者错误的Imageview
    private Voiceparsing voiceparsing;
    private Observable<String> observable;
    private Observer<String> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        changeStatusBarTextColor(true);
        utils = new Utils(this);
        chinses = utils.getChinese();
        voiceparsing = new Voiceparsing(this);
        initView();
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

    private void initView() {
        right_iv = (ImageView) findViewById(R.id.right_iv);
        chinese_tv = (TextView) findViewById(R.id.chinese_tv);
        chinese_tv.setOnClickListener(this);
        chinese_tv.setText(chinses);
        chineseSpell_tv = (TextView) findViewById(R.id.chineseSpell_tv);
        previous_bt = (Button) findViewById(R.id.previous_bt);
        previous_bt.setOnClickListener(this);
        next_bt = (Button) findViewById(R.id.next_bt);
        next_bt.setOnClickListener(this);
        play_bt = (Button) findViewById(R.id.play_bt);
        play_bt.setOnClickListener(this);
        play_bt.setOnTouchListener(new onTouchListener());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        voiceparsing.EventCancel();
    }


    /**
     * 传入参数,判断是否正确
     *
     * @param data
     */
    private void parseAsrFinishJsonData1(String data) {

        if (utils.getChineseSpell(chinses).equals(utils.getChineseSpell(final_result))) {
            setRight_iv(true);
        } else if (!utils.getChineseSpell(chinses).equals(utils.getChineseSpell(final_result))) {
            setRight_iv(false);
        }
    }

    private String previous_number = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chinese_tv:
                // tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.next_bt:
                right_iv.setVisibility(View.GONE);
                previous_number = chinses;
                chinses = utils.getChinese();
                initChinese_tv(chinses);
                //  tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.previous_bt:
                initChinese_tv(previous_number);
                right_iv.setVisibility(View.GONE);
                //  tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.play_bt:
                //   tts.speak(chinses, TextToSpeech.QUEUE_FLUSH, null);
                right_iv.setVisibility(View.GONE);
                //start();
                break;
        }
    }

    class onTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    voiceparsing.start();
                    break;
                case MotionEvent.ACTION_UP:
                    voiceparsing.stop();
                    final_result = voiceparsing.getResult();
                    parseAsrFinishJsonData1(final_result);
                    break;
            }
            return true;
        }
    }

    private void setRight_iv(boolean bool) {
        right_iv.setVisibility(View.VISIBLE);
        if (bool == true) {
            right_iv.setBackgroundResource(R.drawable.right);
        } else {
            right_iv.setBackgroundResource(R.drawable.wrong);
        }
    }

    private void initChinese_tv(String chinses) {
        chinese_tv.setText(chinses);
    }
}
