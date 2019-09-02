package com.example.lz.babyperceive.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;

public class ObjectActivity extends AppCompatActivity implements View.OnClickListener {
    private Button animal_bt,fruit_bt,vegetables_bt;
    private TitleView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
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
        animal_bt = (Button) findViewById(R.id.animal_bt);
        fruit_bt = (Button) findViewById(R.id.fruit_bt);
        vegetables_bt = (Button) findViewById(R.id.vegetables_bt);
        titleView = (TitleView) findViewById(R.id.titleview);
        animal_bt.setOnClickListener(this);
        fruit_bt.setOnClickListener(this);
        fruit_bt.setOnClickListener(this);
        titleView.setTitleView(1);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.animal_bt:
                Intent intent = new Intent(this, AnimalActivity.class);
                intent.putExtra("data","animal.txt");
                startActivity(intent);
                break;
            case R.id.fruit_bt:
                Intent intent_fruit = new Intent(this, AnimalActivity.class);
                intent_fruit.putExtra("data","fruit.txt");
                startActivity(intent_fruit);
                break;
            case R.id.vegetables_bt:
                break;

        }
    }
}