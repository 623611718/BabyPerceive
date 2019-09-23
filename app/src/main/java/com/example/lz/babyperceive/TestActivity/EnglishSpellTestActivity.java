package com.example.lz.babyperceive.TestActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class EnglishSpellTestActivity extends BaseActivity {
    private ImageView image_answer;
    private TextView title_tv;
    private EditText answer_et;
    private static final String TAG = "ObjectTest";
    private List<Object> objectList = new ArrayList<>();
    private Utils utils;
    private TitleView titleView;
    private int random_number;  //随机数
    private String object, name, introduction, imageId, namespell;
    private Button confirm_bt,next_bt;
    private Speek speek;  //百度语音合成封装类
    private   MyHandler myHandler;
    private Animation animation;

    static class MyHandler extends Handler {
        private final WeakReference<EnglishSpellTestActivity> mActivty;

        public MyHandler(EnglishSpellTestActivity activity) {
            mActivty = new WeakReference<EnglishSpellTestActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_bt:
                if (setImageView_answer(answer_et.getText().toString())) {
                    //两秒后更换下一题
                    myHandler = new MyHandler(this);
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            image_answer.setVisibility(View.GONE);
                            setData();
                        }
                    }, 2000);
                }
                break;
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

        Log.i("test", "11111111:" + object);
        title_tv.setText(introduction);
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
        return R.layout.activity_english_spell_test;
    }

    @Override
    public void initView(View view) {
        title_tv = $(R.id.tv1);
        confirm_bt = $(R.id.confirm_bt);
        titleView = $(R.id.titleview);
        answer_et = $(R.id.answre_et);
        next_bt = $(R.id.next_bt);
        image_answer = $(R.id.image_answer);
        animation = AnimationUtils.loadAnimation(this,R.anim.narrow);
        confirm_bt.setOnClickListener(this);
        titleView.setTitleView(1);
        next_bt.setOnClickListener(this);
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
    protected void onDestroy() {
        super.onDestroy();
        if(myHandler != null){
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }
    }
}
