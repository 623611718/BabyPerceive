package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lz.babyperceive.MainActivity;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.View.TitleView;

public class IdentifyActivity extends BaseActivity {
    private Button bt1, bt2, bt3, bt4, bt5,bt6; //1:文字识别 2:物体识别 3:动物识别 4:植物识别 5蔬果识别 6地标识别

    private TitleView titleView;
    @Override
    public void widgetClick(View v) {//1:文字识别 2:物体识别 3:动物识别 4:植物识别 5蔬果识别 6地标识别
        switch (v.getId()){
            case R.id.bt1:
                Intent intent1 = new Intent(this, CharacterRecognitionActivity.class);
                startActivity(intent1);
                break;
            case R.id.bt2:
                Intent intent2 = new Intent(this, ImageRecognitionActivity.class);
                intent2.putExtra("type","advanced_general");
                intent2.putExtra("title","物体识别");
                startActivity(intent2);
                break;
            case R.id.bt3:
                Intent intent3 = new Intent(this, ImageRecognitionActivity.class);
                intent3.putExtra("type","animal");
                intent3.putExtra("title","动物识别");
                startActivity(intent3);
                break;
            case R.id.bt4:
                Intent intent4 = new Intent(this, ImageRecognitionActivity.class);
                intent4.putExtra("type","plant");
                intent4.putExtra("title","植物识别");
                startActivity(intent4);
                break;
            case R.id.bt5:
                Intent intent5 = new Intent(this, ImageRecognitionActivity.class);
                intent5.putExtra("type","ingredient");
                intent5.putExtra("title","果蔬识别");
                startActivity(intent5);
                break;
            case R.id.bt6:
                Intent intent6 = new Intent(this, ImageRecognitionActivity.class);
                intent6.putExtra("type","landmark");
                intent6.putExtra("title","地标识别");
                startActivity(intent6);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return R.layout.activity_identify;
    }

    @Override
    public void initView(View view) {
        bt1 = $(R.id.bt1);
        bt2 = $(R.id.bt2);
        bt3 = $(R.id.bt3);
        bt4 = $(R.id.bt4);
        bt5 = $(R.id.bt5);
        bt6 = $(R.id.bt6);
        titleView = $(R.id.titleview);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
        titleView.setTitle_tv("智能识别");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}

