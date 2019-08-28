package com.example.lz.babyperceive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.speech.EventManager;
import com.example.lz.babyperceive.Activity.EnglishSpeakingActivity;
import com.example.lz.babyperceive.Activity.IdiomSpeakingActivity;
import com.example.lz.babyperceive.Activity.ObjectActivity;
import com.example.lz.babyperceive.Activity.SpeakingActivity;
import com.example.lz.babyperceive.Activity.TestActivity;
import com.example.lz.babyperceive.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button speaking_bt,english_bt,object_bt,test_bt,idiom_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        changeStatusBarTextColor(true);
        initPermission();  //初始化权限
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
    /**
     * android 6.0 以上需要动态申请权限
     */
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
    private void initView(){
        speaking_bt = (Button) findViewById(R.id.speaking_bt);
        english_bt = (Button) findViewById(R.id.english_bt);
        object_bt = (Button) findViewById(R.id.object_bt);
        test_bt = (Button) findViewById(R.id.test_bt);
        idiom_bt = (Button) findViewById(R.id.idiom_bt);
        speaking_bt.setOnClickListener(this);
        english_bt.setOnClickListener(this);
        object_bt.setOnClickListener(this);
        test_bt.setOnClickListener(this);
        idiom_bt.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speaking_bt:
                Intent intent = new Intent(this, SpeakingActivity.class);
                startActivity(intent);
                break;
            case R.id.english_bt:
                Intent intent_english = new Intent(this, EnglishSpeakingActivity.class);
                startActivity(intent_english);
                break;
            case R.id.object_bt:
                Intent intent_object = new Intent(this, ObjectActivity.class);
                startActivity(intent_object);
                break;
            case R.id.test_bt:
                Intent intent1 = new Intent(this, TestActivity.class);
                startActivity(intent1);
                break;
            case R.id.idiom_bt:
                Intent intent2 = new Intent(this, IdiomSpeakingActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
