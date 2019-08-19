package com.example.lz.babyperceive;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lz.babyperceive.Activity.SpeakingActivity;
import com.example.lz.babyperceive.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String text;
    private Button speaking_bt,english_bt,object_bt,test_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        changeStatusBarTextColor(true);
        initData();//初始化常用汉子  3500字
        initView();//初始化View
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

    private void initView(){
        speaking_bt = (Button) findViewById(R.id.speaking_bt);
        english_bt = (Button) findViewById(R.id.english_bt);
        object_bt = (Button) findViewById(R.id.object_bt);
        test_bt = (Button) findViewById(R.id.test_bt);
        speaking_bt.setOnClickListener(this);
        english_bt.setOnClickListener(this);
        object_bt.setOnClickListener(this);
        test_bt.setOnClickListener(this);
    }
    /**
     * 初始化数据 获取常用汉字
     */
    private void initData(){
        Utils utils = new Utils();
        text = utils.getTextHanzi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speaking_bt:
                Intent intent = new Intent(this, SpeakingActivity.class);
                startActivity(intent);
                break;
            case R.id.english_bt:
                break;
            case R.id.object_bt:
                break;
            case R.id.test_bt:
                break;
        }
    }
}
