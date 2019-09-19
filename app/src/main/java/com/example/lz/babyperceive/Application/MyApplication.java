package com.example.lz.babyperceive.Application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.R;

import org.w3c.dom.ProcessingInstruction;


/**
 * Created by lz on 2019/8/19.
 */

public class MyApplication extends Application {
    public static int time = 0;  //计时
    public final static int start = 0;  //开始计时
    public final static int end = 1;      //结束计时
    private String TAG = "MyApplication";

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case start:
                    time += 1;
                    removeMessages(start);
                    sendEmptyMessageDelayed(start, 1000); //1000 = 1秒
                    Log.i(TAG, "time  " + time);
            }
        }
    };

    @Override
    public void onCreate() {
        Log.i("test", "MyApplication onCreate");
        handler.sendEmptyMessageDelayed(start, 0);
        super.onCreate();
    }

    public void sendEmptyMessage() {
        handler.sendEmptyMessageDelayed(start, 0);//开始发消息,延时0
    }

    public void startActivity(Context context) {
        Intent intent = new Intent(context, YuleActivity.class);
        startActivity(intent);
    }
}

