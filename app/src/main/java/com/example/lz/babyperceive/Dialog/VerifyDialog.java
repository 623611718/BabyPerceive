package com.example.lz.babyperceive.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.SharedPreferencesHelper;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.View.ButtonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lz on 2019/8/21.
 */

public class VerifyDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;


    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private int answer_number = 0;
    private SharedPreferencesHelper sharedPreferencesHelper;
    public VerifyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public VerifyDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public VerifyDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected VerifyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public VerifyDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    private int random_number;  //随机数
    private List<Object> objectList = new ArrayList<>();
    private Utils utils;
    private String name1, name2, imageId1, imageId2;
    private List<String> allOptionsList = new ArrayList<>();
    private List<String> optionsList = new ArrayList<>();
    private ImageView iv1, iv2, iv3, iv4, iv5, iv0;
    private View v1, v2, v3, v4, v5, v0;
    private String answer_iv0, answer_iv1;
    private Button button;
    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(false);   //dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        setCancelable(false);//dialog弹出后点击物理返回键dialog不消失
        myApplication=(MyApplication) mContext.getApplicationContext();
        sharedPreferencesHelper = new SharedPreferencesHelper(mContext,"MyApplication");
        utils = new Utils(mContext);
        initView(); //初始化view
        initData();  //初始化数据
        setData();   //设置数据
        myApplication.setShow(true); //将验证框显示状态设置为true
    }

    @SuppressLint("NewApi")
    private void setData() {
        answer_number = 0; //答案计数归零
        //获取一个随机数
        int max = objectList.size() - 1;
        random_number = utils.getRandomNumber(max);

        //获取信息
        name1 = objectList.get(random_number).getName();
        imageId1 = objectList.get(random_number).getImageId();
        int random_number2 = utils.getRandomNumber(max);
        while (random_number == random_number2) {   //如果第二个随机数等于第一个随机数
            random_number2 = utils.getRandomNumber(max);
        }
        name2 = objectList.get(random_number2).getName();
        imageId2 = objectList.get(random_number2).getImageId();
        /**
         * 选项表赋值
         */
        allOptionsList.clear();
        optionsList.clear();
        for (Object object : objectList) {
          /*  if (name.equals(object.getName())) {

            }*/
            allOptionsList.add(object.getImageId());
        }
        int index1 = allOptionsList.indexOf(imageId1);   // 获取答案在所有选项中的位置
        int index2 = allOptionsList.indexOf(imageId2);   // 获取答案在所有选项中的位置
        int index_option1 = utils.getRandomNumber(5);  //设置答案在6个显示的选项中的位置
        int index_option2 = utils.getRandomNumber(5);  //设置答案在6个显示的选项中的位置
        while (index_option1 == index_option2) {   //如果第二个随机数等于第一个随机数
            index_option2 = utils.getRandomNumber(5);
        }

        int index_error = utils.getRandomNumber(max);
        answer_iv0 = "iv" + index_option1;
        answer_iv1 = "iv" + index_option2;
        Log.i("test", "index1:" + index1);
        Log.i("test", "index2:" + index2);
        Log.i("test", "index_option1:" + index_option1);
        Log.i("test", "index_option2:" + index_option2);
        Log.i("test", "index_error:" + index_error);
        //如果没有就加入
        while (optionsList.size() < 6) {
            Log.i("test", " 加载。。。" + optionsList.indexOf(allOptionsList.get(index_error)));
            if (optionsList.indexOf(allOptionsList.get(index_error)) == -1) {
                optionsList.add(allOptionsList.get(index_error));
            }
            index_error = utils.getRandomNumber(max);
        }
        if (optionsList.indexOf(allOptionsList.get(index1)) == -1) {
            optionsList.remove(index_option1);
            optionsList.add(index_option1, allOptionsList.get(index1));
        }
        if (optionsList.indexOf(allOptionsList.get(index2)) == -1) {
            optionsList.remove(index_option2);
            optionsList.add(index_option2, allOptionsList.get(index2));
        }

        for (String s : optionsList) {
            Log.i("test", " name is:" + s);
        }
        iv0.setBackground(utils.getAssectImage(optionsList.get(0)));
        iv1.setBackground(utils.getAssectImage(optionsList.get(1)));
        iv2.setBackground(utils.getAssectImage(optionsList.get(2)));
        iv3.setBackground(utils.getAssectImage(optionsList.get(3)));
        iv4.setBackground(utils.getAssectImage(optionsList.get(4)));
        iv5.setBackground(utils.getAssectImage(optionsList.get(5)));
        Log.i("test", "iv0.getDrawable();" + utils.getAssectImage(optionsList.get(0)));
        Log.i("test", "iv1.getDrawable();" + utils.getAssectImage(optionsList.get(1)));
        titleTxt.setText("选出下图中的 " + name1 + " 和 " + name2);
       /* tv1.setText(optionsList.get(0));
        tv2.setText(optionsList.get(1));
        tv3.setText(optionsList.get(2));
        tv4.setText(optionsList.get(3));
        imageView.setBackground(utils.getAssectImage(imageId));*/
    }

    private void initData() {
        AsrJson asrJson = new AsrJson();
        objectList = asrJson.parseJSONobject(utils.getAsstesTxt("animal.txt"));
        objectList.addAll(asrJson.parseJSONobject(utils.getAsstesTxt("fruit.txt")));
    }

    private void initView() {
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);
        iv0 = (ImageView) findViewById(R.id.iv0);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        iv0.setOnClickListener(this);
        v1 = (View) findViewById(R.id.v1);
        v2 = (View) findViewById(R.id.v2);
        v3 = (View) findViewById(R.id.v3);
        v4 = (View) findViewById(R.id.v4);
        v5 = (View) findViewById(R.id.v5);
        v0 = (View) findViewById(R.id.v0);
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
        v5.setOnClickListener(this);
        v0.setOnClickListener(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new ButtonViewClistener());

        contentTxt = (TextView) findViewById(R.id.content);
        titleTxt = (TextView) findViewById(R.id.title);
        contentTxt.setText(content);


    }
    class ButtonViewClistener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (listener !=null){
                listener.onClick(v);
            }
        }
    }
    private void GoneView(){
        v4.setVisibility(View.GONE);
        v0.setVisibility(View.GONE);
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v5.setVisibility(View.GONE);
        setData();
    }
    private void correct(){
        myApplication.setStatus(true);
       // myApplication.setYueleStatus(true);
        myApplication.setTime(0);
        myApplication.setYuletime(0);
        myApplication.setShow(false);
        myApplication.sendYuleEmptyMessage();
        dismiss();
     //   getOwnerActivity().finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv0:
                if (answer_iv0.equals("iv0") || answer_iv1.equals("iv0")) {
                    answer_number += 1;
                    v0.setVisibility(View.VISIBLE);
                    if (answer_number == 2) {
                        correct();
                    }
                } else {
                    GoneView();
                }
                break;
            case R.id.iv1:
                if (answer_iv0.equals("iv1") || answer_iv1.equals("iv1")) {
                    answer_number += 1;
                    v1.setVisibility(View.VISIBLE);
                    if (answer_number == 2) {
                        correct();
                    }
                } else {
                    GoneView();
                }
                break;
            case R.id.iv2:
                if (answer_iv0.equals("iv2") || answer_iv1.equals("iv2")) {
                    answer_number += 1;
                    v2.setVisibility(View.VISIBLE);
                    if (answer_number == 2) {
                        correct();
                    }
                } else {
                    GoneView();
                }
                break;
            case R.id.iv3:
                if (answer_iv0.equals("iv3") || answer_iv1.equals("iv3")) {
                    answer_number += 1;
                    v3.setVisibility(View.VISIBLE);
                    if (answer_number == 2) {
                        correct();
                    }
                } else {
                    GoneView();
                }
                break;
            case R.id.iv4:
                if (answer_iv0.equals("iv4") || answer_iv1.equals("iv4")) {
                    answer_number += 1;
                    v4.setVisibility(View.VISIBLE);
                    if (answer_number == 2) {
                        correct();
                    }
                } else {
                    GoneView();
                }
                break;
            case R.id.iv5:
                if (answer_iv0.equals("iv5") || answer_iv1.equals("iv5")) {
                    answer_number += 1;
                    v5.setVisibility(View.VISIBLE);
                    if (answer_number == 2) {
                        correct();
                    }
                } else {
                    GoneView();
                }
                break;
            case R.id.button:
                 myApplication.setStatus(false);
                sharedPreferencesHelper.put("status",false);
                //dismiss();
             //   myApplication.setYueleStatus(false);
             //   android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.v0:
                break;
            case R.id.v1:
                break;
            case R.id.v2:
                break;

            case R.id.v3:
                break;
            case R.id.v4:
                break;
            case R.id.v5:
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(View view);
    }
    /**
     * 这个方法等同于setOnClickListener
     *
     * @param clickListener 这个接口就是OnClickListener
     */
    public void setCustomOnClickListener(OnCloseListener clickListener){
        this.listener =clickListener;
    }
}
