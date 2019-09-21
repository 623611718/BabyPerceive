package com.example.lz.babyperceive.TestActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.SpeechRecognizerTool;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

public class EnglishTestActivity extends BaseActivity implements SpeechRecognizerTool.ResultsCallback {
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
    private SpeechRecognizerTool speechRecognizerTool;//百度语音识别封装类
    private Animation animation;
    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.next_bt:
                image_answer.setVisibility(View.GONE);
                setData();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(this);
        speek = new Speek(this);
        speechRecognizerTool= new SpeechRecognizerTool(this);
        initData();
        setData();
    }
    private boolean setImageView_answer(String s) {
        Log.i("test", "选择的答案。。。" + s);
        Log.i("test", "正确的答案。。。" + object);
        if (object.equals(s)) {
            image_answer.setBackgroundResource(R.drawable.ico_exam_correct);
            image_answer.setVisibility(View.VISIBLE);
            image_answer.startAnimation(animation);
            return true;
        } else {
            image_answer.setBackgroundResource(R.drawable.ico_exam_error);
            image_answer.setVisibility(View.VISIBLE);
            image_answer.startAnimation(animation);
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
       // speek.Speeking(object);

    }

    private void initData() {
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        intent.getStringExtra("data");
        objectList = asrJson.parseJSONobject(utils.getAsstesTxt("english.txt"));
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
        speaking_bt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizerTool.startASR(EnglishTestActivity.this);
                        speaking_bt.setBackgroundResource(
                                R.drawable.bg_btn_voice_collecting);
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizerTool.stopASR();
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

    @Override
    protected void onStart() {
        super.onStart();
        speechRecognizerTool.createTool();
    }

    @Override
    protected void onStop() {
        super.onStop();
        speechRecognizerTool.destroyTool();
    }

    @Override
    public void onResults(String result) {
        Log.i("test","结果:"+result);
        setImageView_answer(result);
        if (object.equals(result)){
            image_answer.setVisibility(View.GONE);
            setData();
        }
    }
}
