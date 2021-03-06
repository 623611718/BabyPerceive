package com.example.lz.babyperceive.TestActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
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
import com.example.lz.babyperceive.Utils.SharedPreferencesHelper;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class IdiomTestActivity extends BaseActivity {
    private static final String TAG = "ObjectTest";
    private List<Object> objectList = new ArrayList<>();
    private Utils utils;
    private TextView imageView;
    private ImageView image_answer;
    private TextView tv1, tv2, tv3, tv4;
    private TitleView titleView;
    private int random_number;  //随机数
    private int previous_number = 0;//上一个随机数
    private String object, name, introduction, imageId, namespell;
    private List<String> allOptionsList = new ArrayList<>();
    private List<String> optionsList = new ArrayList<>();
    private Button next_bt;
    private MyHandler myHandler;
    private Animation animation;

    static class MyHandler extends Handler {
        private final WeakReference<IdiomTestActivity> mActivty;

        public MyHandler(IdiomTestActivity activity) {
            mActivty = new WeakReference<IdiomTestActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                setImageView_answer(tv1.getText().toString());
                break;
            case R.id.tv2:
                setImageView_answer(tv2.getText().toString());
                break;
            case R.id.tv3:
                setImageView_answer(tv3.getText().toString());
                break;
            case R.id.tv4:
                setImageView_answer(tv4.getText().toString());
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
        initData();
        setData();
    }

    private void setImageView_answer(String s) {
        Log.i("test", "选择的答案。。。" + s);
        Log.i("test", "正确的答案。。。" + name);
        if (name.equals(s)) {
            image_answer.setBackgroundResource(R.drawable.ico_exam_correct);
            image_answer.setVisibility(View.VISIBLE);
            image_answer.startAnimation(animation);
        } else {
            image_answer.setBackgroundResource(R.drawable.ico_exam_error);
            image_answer.setVisibility(View.VISIBLE);
            image_answer.startAnimation(animation);
        }
        image_answer.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi")
    private void setData() {
        int max = objectList.size() - 1;
        random_number = utils.getRandomNumber(max);

        object = objectList.get(random_number).getObject();
        name = objectList.get(random_number).getName();
        imageId = objectList.get(random_number).getImageId();
        introduction = objectList.get(random_number).getIntroduction();
        StringBuffer stringBuffer = new StringBuffer();
        if (name.length() > 0) {
            for (int i = 1; i <= name.length(); i++) {
                stringBuffer.append(utils.getChineseSpell(name.substring(i - 1, i)));
                stringBuffer.append(" ");
            }
        }
        namespell = stringBuffer.toString();

        /**
         * 选项表赋值
         */
        allOptionsList.clear();
        optionsList.clear();
        for (Object object : objectList) {
            if (name.equals(object.getName())) {

            }
            allOptionsList.add(object.getName());
        }
        int index = allOptionsList.indexOf(name);   // 获取答案在所有选项中的位置
        int index_option = utils.getRandomNumber(3);  //设置答案在4个显示的选项中的位置
        int index_error = utils.getRandomNumber(max);
        //optionsList.add(index_option,allOptionsList.get(index));
        // for (int i = 0; i < 4; i++) {
           /* if (i == index_option) {
                optionsList.add(index_option,allOptionsList.get(index));
            } else {*/
        //如果没有就加入
        while (optionsList.size() < 4) {
            Log.i("test", " 加载。。。" + optionsList.indexOf(allOptionsList.get(index_error)));
            if (optionsList.indexOf(allOptionsList.get(index_error)) == -1) {
                optionsList.add(allOptionsList.get(index_error));
            }
            index_error = utils.getRandomNumber(max);
        }

        // }
        //  }
        if (optionsList.indexOf(allOptionsList.get(index)) == -1) {
            optionsList.add(index_option, allOptionsList.get(index));
        }
        for (String s : optionsList) {
            Log.i("test", " name is:" + s);
        }
        tv1.setText(optionsList.get(0));
        tv2.setText(optionsList.get(1));
        tv3.setText(optionsList.get(2));
        tv4.setText(optionsList.get(3));
        imageView.setText(introduction);
    }

    private void initData() {
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        intent.getStringExtra("data");
        SharedPreferencesHelper sharedPreferencesHelper =
                new SharedPreferencesHelper(this, "idiom.txt");  //获取学习记录
        int number = (int) sharedPreferencesHelper.getSharedPreference("number", 0); //获取学习记录的位置

        Log.i("test", "number:" + number);
        List<Object> objectList1 = new ArrayList<>();
        objectList1 = asrJson.parseJSONobject(utils.getAsstesTxt("idiom.txt"));
        if (number > 3) {
            for (int i = 0; i <= number; i++) {
                objectList.add(objectList1.get(i));
            }
        } else {
            objectList = asrJson.parseJSONobject(utils.getAsstesTxt("idiom.txt"));
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
        return R.layout.activity_idiom_test;
    }

    @Override
    public void initView(View view) {
        tv1 = $(R.id.tv1);
        tv2 = $(R.id.tv2);
        tv3 = $(R.id.tv3);
        tv4 = $(R.id.tv4);
        imageView = $(R.id.image);
        titleView = $(R.id.titleview);
        image_answer = $(R.id.image_answer);
        image_answer.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.narrow);
        next_bt = $(R.id.next_bt);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setTitleView(1);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        next_bt.setOnClickListener(this);

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
        finish();
    }
}
