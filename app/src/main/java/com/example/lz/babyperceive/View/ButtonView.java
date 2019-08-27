package com.example.lz.babyperceive.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.lz.babyperceive.R;

/**
 * Created by Administrator on 2019/8/27.
 */

public class ButtonView extends LinearLayout {
    private Button previous_bt,play_bt,next_bt;
    private ClickListener clickListener;
    public ButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_view, this);
        initView();

    }


    private void initView() {
        previous_bt = (Button) findViewById(R.id.previous_bt);
        play_bt = (Button) findViewById(R.id.play_bt);
        next_bt = (Button) findViewById(R.id.next_bt);
        previous_bt.setOnClickListener(new ButtonViewClistener());
        play_bt.setOnClickListener(new ButtonViewClistener());
        next_bt.setOnClickListener(new ButtonViewClistener());
    }

    class ButtonViewClistener implements OnClickListener{

        @Override
        public void onClick(View v) {
            if (clickListener !=null){
                clickListener.onClick(v);
            }
        }
    }
    /**
     * 设置一个接口
     */
    public interface ClickListener {
        void onClick(View v);
    }

    /**
     * 这个方法等同于setOnClickListener
     *
     * @param clickListener 这个接口就是OnClickListener
     */
    public void setCustomOnClickListener(ClickListener clickListener){
        this.clickListener =clickListener;
    }
}
