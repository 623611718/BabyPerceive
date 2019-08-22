package com.example.lz.babyperceive.View;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lz.babyperceive.R;

/**
 * Created by lz on 2019/8/21.
 */

public class SpeakingView extends LinearLayout {
    private ImageView signal_iv;
    public SpeakingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.speaking_view, this);
        initView();
    }

    private void initView() {
        signal_iv = (ImageView) findViewById(R.id.signal_iv);
        signal_iv.setImageResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable =(AnimationDrawable) signal_iv.getDrawable();
        animationDrawable.start();

    }
}
