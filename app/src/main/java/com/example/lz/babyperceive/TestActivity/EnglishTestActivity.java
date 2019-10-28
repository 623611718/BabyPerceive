package com.example.lz.babyperceive.TestActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.SharedPreferencesHelper;
import com.example.lz.babyperceive.Utils.SpeechRecognizerTool;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;
import com.example.lz.babyperceive.asrfinishjson.AsrFinishJsonData;
import com.example.lz.babyperceive.asrpartialjson.AsrPartialJsonData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnglishTestActivity extends BaseActivity implements EventListener {
    private ImageView image_answer;
    private TextView title_tv,namespell_tv,name_tv;
    private static  final String TAG = "ObjectTest";
    private List<Object> objectList = new ArrayList<>();
    private Utils utils;
    private TitleView titleView;
    private int random_number;  //随机数
    private String object, name, introduction, imageId, namespell;
    private Button speaking_bt,next_bt;
    private Speek speek;  //百度语音合成封装类
    private EventManager asr;

    private boolean logTime = true;

    private String final_result;

    private Animation animation;
    private TextView textView;
    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.next_bt:
                image_answer.setVisibility(View.GONE);
                textView.setText(" ");
                setData();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        utils = new Utils(this);
        speek = new Speek(this);
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法
        initData();
        setData();
    }
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
    private boolean setImageView_answer(String s) {
        Log.i("test", "选择的答案。。。" + s);
        Log.i("test", "正确的答案。。。" + object);
        if (object.equals(s)) {
            image_answer.setBackgroundResource(R.drawable.ico_exam_correct);
            image_answer.setVisibility(View.VISIBLE);
            image_answer.startAnimation(animation);
          //  textView.setText(" ");
            return true;
        } else {
            image_answer.setBackgroundResource(R.drawable.ico_exam_error);
            image_answer.setVisibility(View.VISIBLE);
            image_answer.startAnimation(animation);
           // textView.setText(" ");
            return false;
        }
    }
    @SuppressLint("NewApi")
    private void setData() {
        int max = objectList.size() - 1;
        random_number = utils.getRandomNumber(max);

        object = objectList.get(random_number).getObject();
        name = objectList.get(random_number).getName();
        imageId = objectList.get(random_number).getImageId();
        introduction = objectList.get(random_number).getIntroduction();

        Log.i("test","11111111:"+object);
        title_tv.setText(object);
        namespell_tv.setText(object);
        name_tv.setText(name);
    }

    private void initData() {
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        intent.getStringExtra("data");
        //objectList = asrJson.parseJSONobject(utils.getAsstesTxt("english.txt"));


        SharedPreferencesHelper sharedPreferencesHelper =
                new SharedPreferencesHelper(this, "english.txt");  //获取学习记录
        int number = (int) sharedPreferencesHelper.getSharedPreference("number", 0); //获取学习记录的位置
        List<Object> objectList1 = new ArrayList<>();
        objectList1 = asrJson.parseJSONobject(utils.getAsstesTxt("english.txt"));
        if (number > 3) {
            for (int i = 0; i <= number; i++) {
                objectList.add(objectList1.get(i));
            }
        } else {
            objectList = asrJson.parseJSONobject(utils.getAsstesTxt("english.txt"));
        }
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
        return R.layout.activity_english_test;
    }

    @Override
    public void initView(View view) {
        title_tv = $(R.id.tv1);
        name_tv = $(R.id.name_tv);
        namespell_tv = $(R.id.namespell_tv);
        speaking_bt = $(R.id.speaking_bt);
        next_bt = $(R.id.next_bt);
        titleView = $(R.id.titleview);
        image_answer = $(R.id.image_answer);
        animation = AnimationUtils.loadAnimation(this,R.anim.narrow);
        next_bt.setOnClickListener(this);
        textView = $(R.id.textView);
        speaking_bt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                       // speechRecognizerTool.startASR(EnglishTestActivity.this);
                        textView.setText(" ");
                        start();
                        speaking_bt.setBackgroundResource(
                                R.drawable.bg_btn_voice_collecting);
                        break;
                    case MotionEvent.ACTION_UP:
                     //   speechRecognizerTool.stopASR();
                        stop();
                        speaking_bt.setBackgroundResource(
                                R.drawable.bg_btn_voice);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        titleView.setTitleView(1);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

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
            next_bt.setEnabled(true);
            speaking_bt.setEnabled(true);
            speaking_bt.setBackgroundResource(
                    R.drawable.bg_btn_voice);
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
            if (params != null && !params.isEmpty()) {
                result += "params :" + params + "\n";
            }
            Log.d(TAG, "Result Params:"+params);
            parseAsrFinishJsonData(params);
        }
        printResult(result);
    }
    private void printResult(String text) {
     //   tvResult.append(text + "\n");
    }

    private void start() {

        next_bt.setEnabled(false);
        speaking_bt.setEnabled(false);
        Map<String, java.lang.Object> params = new LinkedHashMap<String, java.lang.Object>();
        String event = null;
        event = SpeechConstant.ASR_START;
        params.put(SpeechConstant.PID, 1536); // 默认1536
        params.put(SpeechConstant.DECODER, 2); // 0纯在线(默认) 2离在线,在线优先
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN); // 语音活动检测
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 2000); // 不开启长语音。开启VAD尾点检测，即静音判断的毫秒数。建议设置800ms-3000ms
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);// 是否需要语音音频数据回调
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);// 是否需要语音音量数据回调

        String json = null; //可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        printResult("输入参数：" + json);
    }

    private void stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    private void parseAsrPartialJsonData(String data) {
        Log.d(TAG, "parseAsrPartialJsonData data:"+data);
        Gson gson = new Gson();
        AsrPartialJsonData jsonData = gson.fromJson(data, AsrPartialJsonData.class);
        String resultType = jsonData.getResult_type();
        Log.d(TAG, "resultType:"+resultType);
        if(resultType != null && resultType.equals("final_result")){
            final_result = jsonData.getBest_result();
//            tvParseResult.setText("解析结果：" + final_result);
        }
    }

    private void parseAsrFinishJsonData(String data) {
        Log.d(TAG, "parseAsrFinishJsonData data:"+data);
        Gson gson = new Gson();
        AsrFinishJsonData jsonData = gson.fromJson(data, AsrFinishJsonData.class);
        String desc = jsonData.getDesc();
        if(desc !=null && desc.equals("Speech Recognize success.")){
           // tvParseResult.setText("解析结果:" + final_result);
            Log.i("test","结果:"+final_result);
            textView.setText(final_result);
            setImageView_answer(final_result);
            if (object.equals(final_result)){
                image_answer.setVisibility(View.GONE);
                setData();
            }
        }else{
            String errorCode = "\n错误码:" + jsonData.getError();
            String errorSubCode = "\n错误子码:"+ jsonData.getSub_error();
            String errorResult = errorCode + errorSubCode;
       //     tvParseResult.setText("解析错误,原因是:" + desc + "\n" + errorResult);
        }
    }

    @Override
    protected void onDestroy() {
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        super.onDestroy();
    }
}
