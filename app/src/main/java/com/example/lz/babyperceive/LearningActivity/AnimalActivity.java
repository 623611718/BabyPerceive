package com.example.lz.babyperceive.LearningActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.SharedPreferencesHelper;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.ButtonView;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于英语学习 事物学习 成语学习
 */
public class AnimalActivity extends BaseActivity {

    private Utils utils;  //工具类
    private TitleView titleView;  //自定义标题栏
    private ImageView imageView;  //显示图片
    private TextView introduction_tv, namespell_tv, name_tv, tv1; //介绍 ,拼音/翻译, 名称/英文 tv1显示成语的介绍
    private ButtonView buttonView;
    private String object, name, introduction, imageId, namespell;
    private int id;
    private List<Object> objectList = new ArrayList<>();
    private int number = 0;  //计数
    private Speek speek; //百度语音合成
    private int previous_number = 0;//上一个随机数
    private boolean isEnglish = false;
    private boolean isIdiom = false; //判断是否是成语
    private SharedPreferencesHelper sharedPreferencesHelper;
    private MyApplication myApplication;
    private FrameLayout iamage_layout;

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.getStringExtra("data").equals("english.txt")) {
            iamage_layout.setVisibility(View.GONE);
        }
        utils = new Utils(this);
        myApplication = (MyApplication) getApplication();
        if (!myApplication.status) {
            myApplication.sendEmptyMessage();
        }
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        /*if (isEnglish){
            speek.Speeking(object);
        }else {
            speek.Speeking(name);
        }*/
    }

    /**
     * 初始化界面上的数据
     */
    @SuppressLint("NewApi")
    private void setData() {


        object = objectList.get(number).getObject();
        name = objectList.get(number).getName();
        imageId = objectList.get(number).getImageId();
        introduction = objectList.get(number).getIntroduction();
        object = objectList.get(number).getObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (!isEnglish) {
            if (name.length() > 0) {
                for (int i = 1; i <= name.length(); i++) {
                    stringBuffer.append(utils.getChineseSpell(name.substring(i - 1, i)));
                    stringBuffer.append(" ");
                }
            }
            namespell = stringBuffer.toString();
        } else {
            namespell = " ";
        }
        namespell = stringBuffer.toString();

        // 如果传入的是英文文档 就把拼音改成英文,图像隐藏
        if (isEnglish == true) {
            namespell_tv.setText(object);
            iamage_layout.setVisibility(View.GONE);
        } else {
            namespell_tv.setText(namespell);
        }
        if (isIdiom == true) {
            tv1.setText(introduction);
            imageView.setVisibility(View.GONE);
            introduction_tv.setVisibility(View.GONE);
        } else {
            introduction_tv.setText(introduction);
        }
        name_tv.setText(name);
        imageView.setBackground(utils.getAssectImage(imageId));
    }

    @SuppressLint("NewApi")
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.

        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected String doInBackground(String... params) {
            Log.i("test", "doInBackground");
            initData();
            return null;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("test", "onPostExecute");
            setData();
            initView1();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        String fileName;
        fileName = intent.getStringExtra("data");
        if (intent.getStringExtra("data").equals("english.txt")) {
            isEnglish = true;
            isIdiom = false;
        } else {
            if (intent.getStringExtra("data").equals("idiom.txt")) {
                isIdiom = true;
                isEnglish = false;
            }
        }
        sharedPreferencesHelper = new SharedPreferencesHelper(this, fileName);
        number = (int) sharedPreferencesHelper.getSharedPreference("number", 0);
        objectList = asrJson.parseJSONobject(utils.getAsstesTxt(intent.getStringExtra("data")));
        speek = new Speek(this);
        speek.Speeking(name);
    }

    /**
     * 传入一个随机数,设置名称/英文  介绍 拼音/翻译
     */
    @SuppressLint("NewApi")
    private void initData(int number1) {
        number = number1;

        object = objectList.get(number).getObject();
        name = objectList.get(number).getName();
        imageId = objectList.get(number).getImageId();
        introduction = objectList.get(number).getIntroduction();
        object = objectList.get(number).getObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (!isEnglish) {
            if (name.length() > 0) {
                for (int i = 1; i <= name.length(); i++) {
                    stringBuffer.append(utils.getChineseSpell(name.substring(i - 1, i)));
                    stringBuffer.append(" ");
                }
            }
            namespell = stringBuffer.toString();
        } else {
            namespell = " ";
        }
        // 如果传入的是英文文档 就把拼音改成英文,图像隐藏

        if (isEnglish == true) {
            namespell_tv.setText(object);
            iamage_layout.setVisibility(View.GONE);
        } else {
            namespell_tv.setText(namespell);
        }
        if (isIdiom == true) {  //如果是成语
            tv1.setText(introduction);
            imageView.setVisibility(View.GONE);
            introduction_tv.setVisibility(View.GONE);
        } else {
            introduction_tv.setText(introduction);
        }
        name_tv.setText(name);
        imageView.setBackground(utils.getAssectImage(imageId));
    }

    /**
     * 判断是否打开娱乐模式
     */
    private void getStatus() {
        if (myApplication.isStatus()) {
            Intent intent = new Intent(this, YuleActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView1() {
        buttonView.setCustomOnClickListener(new ButtonView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.previous_bt:
                        if (myApplication.isStatus()) {
                            getStatus();
                        } else if (number > 0) {
                            initData(number - 1);
                            if (isEnglish == true) {
                                speek.Speeking(object);
                            } else {
                                speek.Speeking(name);
                            }
                        } else if (number == 0) {
                            showToast("已经是第一个啦");
                        }
                        break;
                    case R.id.play_bt:

                        if (isEnglish == true) {
                            speek.Speeking(object);
                        } else {
                            speek.Speeking(name);
                        }
                        break;
                    case R.id.next_bt:
                        // setData();
                        if (myApplication.isStatus()) {
                            getStatus();
                        } else if (number < objectList.size() - 1) {
                            initData(number + 1);
                            if (isEnglish == true) {
                                speek.Speeking(object);
                            } else {
                                speek.Speeking(name);
                            }
                        } else if (number == objectList.size() - 1) {
                            showToast("已经是最后一个啦");
                        }
                        break;
                }
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
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_animal;
    }

    @Override
    public void initView(View view) {
        titleView = $(R.id.titleview);
        buttonView = $(R.id.buttonview);
        introduction_tv = $(R.id.introduction_tv);
        name_tv = $(R.id.name_tv);
        namespell_tv = $(R.id.namespell_tv);
        imageView = $(R.id.image);
        tv1 = $(R.id.tv1);
        iamage_layout=$(R.id.iamage_layout);
    }

    @Override
    protected void onStop() {
        if (number > (int) sharedPreferencesHelper.getSharedPreference("number", 0)) {
            sharedPreferencesHelper.put("number", number);
        }
        super.onStop();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        myApplication.removeEmptyMessage();
        speek.Destory();
        super.onDestroy();
    }
}

