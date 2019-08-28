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
import com.example.lz.babyperceive.View.TitleView;
import com.example.lz.babyperceive.asrfinishjson.AsrFinishJsonData;
import com.example.lz.babyperceive.asrpartialjson.AsrPartialJsonData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class TestActivity extends AppCompatActivity implements EventListener, View.OnClickListener {
    private EventManager asr;   //语音识别管理类
    private String TAG = "TestActivity";
    private String final_result;  //语音解析的最终结果
    private String chinses;  //单个汉字
    private Utils utils;
    private TextView chinese_tv, chineseSpell_tv;
    private Button previous_bt, next_bt, play_bt;
    private TitleView titleView;
    private int score;   //用于测试的分数计算
    private ImageView right_iv;  //显示正确或者错误的Imageview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        changeStatusBarTextColor(true);
        initEventManager();
        utils = new Utils(this);
        chinses = utils.getChinese();
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

    private void initEventManager() {
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法

    }

    /*
     * EventListener  回调方法
     * name:回调事件
     * params: JSON数据，其格式如下：
     *
     * */
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String result = "";

        if (length > 0 && data.length > 0) {
            result += ", 语义解析结果：" + new String(data, offset, length);
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            // 引擎准备就绪，可以开始说话
            result += "引擎准备就绪，可以开始说话";

        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)) {
            // 检测到用户的已经开始说话
            result += "检测到用户的已经开始说话";

        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)) {
            // 检测到用户的已经停止说话
            result += "检测到用户的已经停止说话";
            if (params != null && !params.isEmpty()) {
                result += "params :" + params + "\n";
            }
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            // 临时识别结果, 长语音模式需要从此消息中取出结果
            result += "识别临时识别结果";
            if (params != null && !params.isEmpty()) {
                result += "params :" + params + "\n";
            }
//            Log.d(TAG, "Temp Params:"+params);
            parseAsrPartialJsonData(params);
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            // 识别结束， 最终识别结果或可能的错误
            result += "识别结束";
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
            if (params != null && !params.isEmpty()) {
                result += "params :" + params + "\n";
            }
            Log.d(TAG, "Result Params:" + params);
            parseAsrFinishJsonData(params);
        }
    }

    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START;
        params.put(SpeechConstant.PID, 1536); // 默认1536
        params.put(SpeechConstant.DECODER, 0); // 纯在线(默认)
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN); // 语音活动检测
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 2000); // 不开启长语音。开启VAD尾点检测，即静音判断的毫秒数。建议设置800ms-3000ms
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);// 是否需要语音音频数据回调
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);// 是否需要语音音量数据回调

        String json = null; //可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
    }

    private void stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }

    private void parseAsrPartialJsonData(String data) {
        Log.d(TAG, "parseAsrPartialJsonData data:" + data);
        Gson gson = new Gson();
        AsrPartialJsonData jsonData = gson.fromJson(data, AsrPartialJsonData.class);
        String resultType = jsonData.getResult_type();
        Log.d(TAG, "resultType:" + resultType);
        if (resultType != null && resultType.equals("final_result")) {
            final_result = jsonData.getBest_result();
//            tvParseResult.setText("解析结果：" + final_result);
        }
    }

    private void parseAsrFinishJsonData(String data) {
        Log.d(TAG, "parseAsrFinishJsonData data:" + data);
        Gson gson = new Gson();
        AsrFinishJsonData jsonData = gson.fromJson(data, AsrFinishJsonData.class);
        String desc = jsonData.getDesc();
        if (desc != null && desc.equals("Speech Recognize success.")) {
            Log.i(TAG, "解析结果:" + final_result);
        } else {
            String errorCode = "\n错误码:" + jsonData.getError();
            String errorSubCode = "\n错误子码:" + jsonData.getSub_error();
            String errorResult = errorCode + errorSubCode;
            Log.i(TAG, "解析错误,原因是:" + desc + "\n" + errorResult);
        }
        Log.i(TAG,"chinses:"+chinses);
        Log.i(TAG,"final_result:"+final_result);
        if (utils.getChineseSpell(chinses).equals(utils.getChineseSpell(final_result))){
            setRight_iv(true);
        }else if (!utils.getChineseSpell(chinses).equals(utils.getChineseSpell(final_result))){
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
                start();
                break;
        }
    }

    class onTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    start();
                    break;
                case MotionEvent.ACTION_UP:
                    stop();
                    break;
            }
            return true;
        }
    }
    private void setRight_iv(boolean bool){
        right_iv.setVisibility(View.VISIBLE);
        if (bool==true){
            right_iv.setBackgroundResource(R.drawable.right);
        }else {
            right_iv.setBackgroundResource(R.drawable.wrong);
        }
    }

    private void initChinese_tv(String chinses) {
        chinese_tv.setText(chinses);
    }
}
