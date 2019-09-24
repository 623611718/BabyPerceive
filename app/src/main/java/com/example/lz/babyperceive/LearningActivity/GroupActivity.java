package com.example.lz.babyperceive.LearningActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.Adapter.GroupAdapter;
import com.example.lz.babyperceive.Adapter.MoviesAdapter;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.AsrJson;
import com.example.lz.babyperceive.Bean.GroupBean;
import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.TestActivity.EnglishTestActivity;
import com.example.lz.babyperceive.Utils.Utils;
import com.example.lz.babyperceive.Utils.UtilsGetUrl;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity {
    private TitleView titleView;
    private Button confirm_bt;
    private EditText edit_query;
    private List<Object> objectList = new ArrayList<>();
    private List<String> groupList = new ArrayList<>();
    private Utils utils;
    private String query = " ";
    private String answer = " ";
    private TextView textView;
    private MyApplication myApplication;
    private UtilsGetUrl utilsGetUrl;
    private MyAsyncTaskQuery myAsyncTaskQuery;
    private ListView listView;
    private List<GroupBean> groupBeanList = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_bt:
                Log.i("test", "confirm_bt");
                textView.setText("正在查询...");
                myAsyncTaskQuery = new MyAsyncTaskQuery();
                myAsyncTaskQuery.execute();
                getStatus();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        myApplication.sendEmptyMessage();
        utils = new Utils(this);
        confirm_bt.setEnabled(false);
        textView.setText("加载词库中...");
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        //  initData();
    }

    @SuppressLint("NewApi")
    private void getStatus() {
        if (myApplication.isStatus()) {
            //  myApplication.setStatus(false);
            Intent intent = new Intent(this, YuleActivity.class);
            startActivity(intent);
            myAsyncTaskQuery.cancel(true);
        }
    }

    @SuppressLint("NewApi")
    class MyAsyncTaskQuery extends AsyncTask<String, Void, String> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.

        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected String doInBackground(String... params) {
            Log.i("test", "doInBackground");
            groupBeanList.clear();//每次查询前，结果列表置空
            answer = " ";
            query = edit_query.getText().toString();
            textView.setText("正在查询...");
            StringBuffer stringBuffer = new StringBuffer();
            for (String s : groupList) {
                //如果包含查询的字词,与 最终结果里没有 则加入
                if (s.contains(query) && !answer.contains(s)) {
                    stringBuffer.delete(0, stringBuffer.length());
                    GroupBean groupBean = new GroupBean();
                    stringBuffer.append("[");
                    stringBuffer.append(s);
                    stringBuffer.append("]");
                    stringBuffer.append(" ");
                    groupBean.setGroup(stringBuffer.toString());
                    groupBeanList.add(groupBean);
                }
                answer = stringBuffer.toString();
                if (answer.equals(" ")) {
                    answer = "没有查询到结果";
                }

            }
            return null;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("test", "onPostExecute");
            Adapter adapter = new GroupAdapter(GroupActivity.this, R.layout.group_item, groupBeanList);
            listView.setAdapter((ListAdapter) adapter);
            if (answer.equals("没有查询到结果")) {
                textView.setText(answer);
                textView.setVisibility(View.VISIBLE);
            }else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("NewApi")
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.

        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected String doInBackground(String... params) {
            Log.i("test", "doInBackground");
            initData();
            return null;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("test", "onPostExecute");
            confirm_bt.setEnabled(true);
            textView.setText("加载完成");
        }
    }

    private void initData() {
        /**
         * 获取配置文件URL 名称
         */
        utilsGetUrl = new UtilsGetUrl(this, "group.txt");
        groupList = utilsGetUrl.getUrls();
        AsrJson asrJson = new AsrJson();
        Intent intent = getIntent();
        intent.getStringExtra("data");

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
        return R.layout.activity_group;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView(View view) {
        edit_query = $(R.id.edit_query);
        titleView = $(R.id.titleview);
        confirm_bt = $(R.id.confirm_bt);
        titleView.setTitle_tv("词组练习");
        textView = $(R.id.image);
        listView = $(R.id.listview);
        confirm_bt.setOnClickListener(this);
        confirm_bt.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("NewApi")
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("test", "ACTION_DOWN");
                        confirm_bt.setBackgroundResource(R.color.skyblue);
                        textView.setText("正在查询...");
                        myAsyncTaskQuery = new MyAsyncTaskQuery();
                        myAsyncTaskQuery.execute();
                        getStatus();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("test", "ACTION_UP");
                        confirm_bt.setBackgroundResource(R.color.white);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        myApplication.removeEmptyMessage();
        super.onDestroy();
    }
}
