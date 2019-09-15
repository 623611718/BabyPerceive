package com.example.lz.babyperceive.View;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lz.babyperceive.R;


/**
 * Created by lz on 2019/6/13.
 */

public class Play_title extends LinearLayout implements View.OnClickListener {


    public Button titleBack, lock;
    public TextView title_name, title_time, title_speed;

    public Play_title(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.play_title, this);
        titleBack = (Button) findViewById(R.id.play_back);
        lock = (Button) findViewById(R.id.paly_lock);
        title_name = (TextView) findViewById(R.id.play_title_name);
        title_time = (TextView) findViewById(R.id.now_time);
        title_speed = (TextView) findViewById(R.id.now_speed);

        titleBack.setOnClickListener(this);
    }

    public void setTitle_name(String name) {
        title_name.setText(name);
    }

    public void setTitle_time(String time) {
        title_time.setText(time);
    }

    public void setTitle_speed(String speed) {
        title_speed.setText(speed);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_back:
                ((Activity) getContext()).finish();
                break;
        }
    }

}
