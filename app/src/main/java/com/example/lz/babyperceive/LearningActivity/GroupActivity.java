package com.example.lz.babyperceive.LearningActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.Utils.UtilsGetUrl;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity {
    private TitleView titleView;
    private Button confirm_bt;
    private EditText edit_query;
    private List<Object> objectList = new ArrayList<>();
    private List<String> groupList = new ArrayList<>();
    private Utils utils;
    private  String query =" ";
    private String answer=" ";
    private TextView textView;
    private MyApplication myApplication;
    private UtilsGetUrl utilsGetUrl;
    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.confirm_bt:
                answer = " ";
                query = edit_query.getText().toString();
                textView.setText("正在查询...");
                StringBuffer stringBuffer = new StringBuffer();
                for (String s :groupList){
                    //如果包含查询的字词,与 最终结果里没有 则加入
                    if (s.contains(query) && !answer.contains(query)) {
                        stringBuffer.append("[");
                        stringBuffer.append(s);
                        stringBuffer.append("]");
                        stringBuffer.append(" ");

                    }
                    answer = stringBuffer.toString();
                    if (answer.equals(" ")){
                        answer="没有查询到结果";
                    }
                    textView.setText(answer);
                }
                getStatus();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        myApplication.sendEmptyMessage();
        utils = new Utils(this);
        initData();
    }

    private void getStatus(){
        if (myApplication.isStatus()){
          //  myApplication.setStatus(false);
            Intent intent=new Intent(this,YuleActivity.class);
            startActivity(intent);
        }
    }
    private void initData() {
        /**
         * 获取配置文件URL 名称
         */

        utilsGetUrl = new UtilsGetUrl(this, "group.txt");
        groupList = utilsGetUrl.getUrls();
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        intent.getStringExtra("data");
      /*  objectList = asrJson.parseJSONobject(utils.getAsstesTxt("animal.txt"));
        objectList.addAll(asrJson.parseJSONobject(utils.getAsstesTxt("fruit.txt")));
        objectList.addAll(asrJson.parseJSONobject(utils.getAsstesTxt("idiom.txt")));
        for (Object objectList : objectList){
            groupList.add(objectList.getName());
        }*/
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
        return R.layout.activity_group;
    }

    @Override
    public void initView(View view) {
        edit_query = $(R.id.edit_query);
        titleView = $(R.id.titleview);
        confirm_bt = $(R.id.confirm_bt);
        titleView.setTitle_tv("词组练习");
        textView = $(R.id.image);
        confirm_bt.setOnClickListener(this);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                   case  R.id.back_bt:
                    finish();
                    break;
                }
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
        myApplication.removeEmptyMessage();
        super.onDestroy();
    }
}
