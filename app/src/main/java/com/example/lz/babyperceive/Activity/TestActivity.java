package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.View.TitleView;

public class TestActivity extends BaseActivity {
    private Button bt1, bt2, bt3, bt4, bt5;

    private TitleView titleView;
    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                Intent intent1 = new Intent(this,TestActivity1.class);
                startActivity(intent1);
            break;
            case R.id.bt2:
                Intent intent2 = new Intent(this,EnglishTestActivity.class);
                startActivity(intent2);
                break;
            case R.id.bt3:
                Intent intent3 = new Intent(this,EnglishSpellTestActivity.class);
                startActivity(intent3);
                break;
            case R.id.bt4:
                Intent intent4 = new Intent(this,ObjectTestActivity.class);
                startActivity(intent4);
                break;
            case R.id.bt5:
                Intent intent5 = new Intent(this,IdiomTestActivity.class);
                startActivity(intent5);
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
        return R.layout.activity_test;
    }

    @Override
    public void initView(View view) {
        bt1 = $(R.id.bt1);
        bt2 = $(R.id.bt2);
        bt3 = $(R.id.bt3);
        bt4 = $(R.id.bt4);
        bt5 = $(R.id.bt5);
        titleView = $(R.id.titleview);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
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
        titleView.setTitle_tv("  ");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
