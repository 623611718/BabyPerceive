package com.example.lz.babyperceive.Application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.SharedPreferencesHelper;

import org.w3c.dom.ProcessingInstruction;


/**
 * Created by lz on 2019/8/19.
 */

public class MyApplication extends Application {
    public  boolean status =false;
    public boolean yueleStatus =true;
    public  int time = 0;  //计时
    public  int learningTime_end=18; //学习时间
    public  int yuletime = 0;  //计时
    public  int yuleTime_end=18;   //娱乐时间
    public final static int yuleStart = 0;  //娱乐计时
    public final static int learnStart = 1;      //学习计时
    private String TAG = "MyApplication";
    private boolean isShow =false;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case learnStart:
                    time += 1;
                    removeMessages(learnStart);
                    if (time == learningTime_end){
                        status =true;
                        yueleStatus =true;
                        time =0;
                    }else {
                        sendEmptyMessageDelayed(learnStart, 1000); //1000 = 1秒
                    }
                    Log.i(TAG, "time  " + time);
                    break;
                case yuleStart:
                    yuletime +=1;
                    removeMessages(yuleStart);
                    if (yuletime == yuleTime_end){
                        yueleStatus =false;
                        yuletime =0;
                    }//else {
                        sendEmptyMessageDelayed(yuleStart, 1000); //1000 = 1秒
                  //  }
                    Log.i(TAG, "time  " + time);
            }
        }
    };

    @Override
    public void onCreate() {
        Log.i("test", "MyApplication onCreate");
        //handler.sendEmptyMessageDelayed(start, 0);
        super.onCreate();
        sharedPreferencesHelper = new SharedPreferencesHelper(this,"MyApplication");
        yuleTime_end = (int)sharedPreferencesHelper.getSharedPreference("yuleTime",18);
        learningTime_end=(int)sharedPreferencesHelper.getSharedPreference("learnTime",18);
    }

    public void sendEmptyMessage() {
        handler.sendEmptyMessageDelayed(learnStart, 0);//开始发消息,延时0
    }
    public void removeEmptyMessage(){
        handler.removeMessages(learnStart);
    }
    public void sendYuleEmptyMessage() {
        handler.sendEmptyMessageDelayed(yuleStart, 0);//开始发消息,延时0
    }
    public void removeYuleEmptyMessage(){
        handler.removeMessages(yuleStart);
    }

    public void startActivity(Context context) {
        Intent intent = new Intent(context, YuleActivity.class);
        startActivity(intent);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isYueleStatus() {
        return yueleStatus;
    }

    public void setYueleStatus(boolean yueleStatus) {
        this.yueleStatus = yueleStatus;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLearningTime_end() {
        return learningTime_end;
    }

    public void setLearningTime_end(int learningTime_end) {
        this.learningTime_end = learningTime_end;
    }

    public int getYuletime() {
        return yuletime;
    }

    public void setYuletime(int yuletime) {
        this.yuletime = yuletime;
    }

    public int getYuleTime_end() {
        return yuleTime_end;
    }

    public void setYuleTime_end(int yuleTime_end) {
        this.yuleTime_end = yuleTime_end;
    }

    public static int getYuleStart() {
        return yuleStart;
    }

    public static int getLearnStart() {
        return learnStart;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {

        sharedPreferencesHelper.put("yuleTime",yuleTime_end);
        sharedPreferencesHelper.put("learnTime",learningTime_end);
        super.onTrimMemory(level);
    }
}

