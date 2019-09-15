package com.example.lz.babyperceive.View;

/**
 * Created by lz on 2019/8/19.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lz.babyperceive.R;


/**
 * Created by lz on 2019/7/25.
 */

public class TitleView extends LinearLayout {
    private TextView title_tv;
    private Button more_bt, back_bt;
    private int height, width;
    private FrameLayout layout;
    //private AndroiodScreenProperty androiodScreenProperty;
    private ClickListener clickListener;

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_view, this);
        initView();
    }

    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        more_bt = (Button) findViewById(R.id.mote_bt);
        back_bt = (Button) findViewById(R.id.back_bt);
        more_bt.setOnClickListener(new TitleClistener());
        back_bt.setOnClickListener(new TitleClistener());
        title_tv.setOnClickListener(new TitleClistener());
        layout = (FrameLayout) findViewById(R.id.layout);

    }

    class TitleClistener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(v);
            }
        }
    }
    public void setTitle_tv(String name){
        title_tv.setText(name);
    }

    @SuppressLint("ResourceAsColor")
    public void setTitleView(int tag) {
        if (tag == 1) {
            more_bt.setVisibility(GONE);
            back_bt.setBackgroundResource(android.R.drawable.ic_delete);
            title_tv.setVisibility(GONE);
            //layout.setBackgroundColor(R.color.white);
        } else if (tag == 0) {
            more_bt.setVisibility(VISIBLE);
            back_bt.setBackgroundResource(R.drawable.back);
            title_tv.setVisibility(VISIBLE);
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
    public void setCustomOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
