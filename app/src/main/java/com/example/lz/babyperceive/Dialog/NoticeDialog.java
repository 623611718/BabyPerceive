package com.example.lz.babyperceive.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.Adapter.CharaectAdapter;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.CharacterBean;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/1/3.
 */

public class NoticeDialog extends Dialog implements View.OnClickListener{
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private NoticeDialog.OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private TextView et2;

    private MyApplication myApplication;
    public NoticeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NoticeDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public NoticeDialog(Context context, int themeResId, String content, NoticeDialog.OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected NoticeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public NoticeDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public NoticeDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public NoticeDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_layout);
        // setCanceledOnTouchOutside(false);
        myApplication=(MyApplication) mContext.getApplicationContext();
        myApplication.getYuleTime_end();
        myApplication.getLearningTime_end();
        positiveName = "现在就去";
        negativeName = "等会再去";
        initView();
    }

    private void initView(){

        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        et2 = (TextView) findViewById(R.id.et2);
        et2.setText(  String.valueOf(myApplication.getYuleTime_end() / 60));
        // if(!TextUtils.isEmpty(positiveName)){
        submitTxt.setText(positiveName);
        //  }

        //   if(!TextUtils.isEmpty(negativeName)){
        cancelTxt.setText(negativeName);
        //   }

        //  if(!TextUtils.isEmpty(title)){
        titleTxt.setText(title);
        //   }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                Intent intent = new Intent(mContext, YuleActivity.class);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}