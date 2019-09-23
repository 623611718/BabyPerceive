package com.example.lz.babyperceive.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.R;

/**
 * Created by lz on 2019/9/19.
 */

public class CommomDialog   extends Dialog implements View.OnClickListener{
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private EditText et1,et2;

    private MyApplication myApplication;
    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommomDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommomDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CommomDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CommomDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom2);
       // setCanceledOnTouchOutside(false);
        myApplication=(MyApplication) mContext.getApplicationContext();
        myApplication.getYuleTime_end();
        myApplication.getLearningTime_end();
        positiveName = "确定";
        negativeName = "取消";
        initView();
    }

    private void initView(){

        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et1.setText(  String.valueOf(myApplication.getYuleTime_end() / 60000));
        et2.setText( String.valueOf(myApplication.getLearningTime_end()/ 60000));
        et1.setInputType(InputType.TYPE_CLASS_NUMBER);
        et1.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                if (et1.getText().toString()!=null) {
                    myApplication.setYuleTime_end(Integer.parseInt(et1.getText().toString()) *60000);
                }
                if (et2.getText().toString()!=null) {
                    myApplication.setLearningTime_end(Integer.parseInt(et2.getText().toString()) * 60000);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}