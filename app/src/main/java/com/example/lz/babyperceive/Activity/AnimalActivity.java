package com.example.lz.babyperceive.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Idiom;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.Speek;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.ButtonView;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

public class AnimalActivity extends BaseActivity {

    private Utils utils;
    private TitleView titleView;
    private ImageView imageView;
    private TextView introduction_tv, namespell_tv, name_tv;
    private ButtonView buttonView;
    private String object, name,introduction,imageId,namespell;
    private int id;
    private List<Object> objectList = new ArrayList<>();
    private int random_number;  //随机数
    private Speek speek; //百度语音合成
    private int previous_number = 0;//上一个随机数

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(this);
        initData();
        setData();
        initView1();
        speek.Speeking(name);
    }

    @SuppressLint("NewApi")
    private void setData() {
        int max = objectList.size()-1;
        random_number = utils.getRandomNumber(max);

        object = objectList.get(random_number).getObject();
        name = objectList.get(random_number).getName();
        imageId = objectList.get(random_number).getImageId();
        introduction = objectList.get(random_number).getIntroduction();
        StringBuffer stringBuffer = new StringBuffer();
        if (name.length()>0) {
            for (int i = 1; i <= name.length(); i++) {
                stringBuffer.append(utils.getChineseSpell(name.substring(i-1,i)));
                stringBuffer.append(" ");
            }
        }
        namespell = stringBuffer.toString();

        namespell_tv.setText(namespell);
        introduction_tv.setText(introduction);
        name_tv.setText(name);
        imageView.setBackground(utils.getAssectImage(imageId));
    }

    private void initData() {
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        intent.getStringExtra("data");
        objectList = asrJson.parseJSONobject(utils.getAsstesTxt( intent.getStringExtra("data")));
        speek = new Speek(this);
        speek.Speeking(name);
    }
    /**
     * \
     */
    @SuppressLint("NewApi")
    private void initData(int random_number) {
        object = objectList.get(random_number).getObject();
        name = objectList.get(random_number).getName();
        imageId = objectList.get(random_number).getImageId();
        introduction = objectList.get(random_number).getIntroduction();
        StringBuffer stringBuffer = new StringBuffer();
        if (name.length()>0) {
            for (int i = 1; i <= name.length(); i++) {
                stringBuffer.append(utils.getChineseSpell(name.substring(i-1,i)));
                stringBuffer.append(" ");
            }
        }
        namespell = stringBuffer.toString();

        namespell_tv.setText(namespell);
        introduction_tv.setText(introduction);
        name_tv.setText(name);
        imageView.setBackground(utils.getAssectImage(imageId));
    }
    private void initView1() {
        buttonView.setCustomOnClickListener(new ButtonView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.previous_bt:
                        initData(previous_number);
                        speek.Speeking(name);
                        break;
                    case R.id.play_bt:

                        speek.Speeking(name);
                        break;
                    case R.id.next_bt:
                        previous_number = random_number;
                        setData();
                        initData(random_number);
                        speek.Speeking(name);
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
        namespell_tv= $(R.id.namespell_tv);
        imageView = $(R.id.image);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}

