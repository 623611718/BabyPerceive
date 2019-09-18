package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.Utils.TransApi;
import com.example.lz.babyperceive.Utils.TranslateResult;
import com.example.lz.babyperceive.View.TitleView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class TranslateActivity extends BaseActivity {
    private TitleView titleView;
    private static Handler handler = null;
    private String dst;
    private EditText input_et;
    private Spinner spinner;
    private TextView out_tv;
    private Button button;
    private List<String> nameList = new ArrayList<>();
    private ArrayAdapter<String> adapter1;
    private static final String APP_ID = "20190917000335107";
    private static final String SECURITY_KEY = "N_WaL4ElbrYrjlDzI3tO";
    private String from;
    private String to;
    private TransApi api;
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            Log.i("test", "翻译结果:" + dst);
            out_tv.setText(dst);
        }
    };

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        intSpinner();

    }

    public void Main(String key) {
        api = new TransApi(APP_ID, SECURITY_KEY);
        Gson gson = new Gson();
        TranslateResult translateResult = gson.fromJson(api.getTransResult(key, from, to), TranslateResult.class);
        // 取出translateResult中的译文
        dst = translateResult.getTrans_result().get(0).getDst();
        Log.i("test", "翻译结果:" + dst);
        handler.post(runnableUi);
    }

    private void intSpinner() {
        nameList.add("英语 -> 汉语");
        nameList.add("汉语 -> 英语");
        // 为下拉列表定义一个适配器，使用到上面定义的turtleList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
        // 为适配器设置下拉列表下拉时的菜单样式，有好几种样式，请根据喜好选择
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将适配器添加到下拉列表上
        spinner.setAdapter(adapter);

        // 为下拉框设置事件的响应
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             *
             * @param adapterView
             * @param view   显示的布局
             * @param i      在布局显示的位置id
             * @param l      将要显示的数据
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // adapter1 = (ArrayAdapter<String>) adapterView.getAdapter();
                if (i == 0) {
                    from = "en";
                    to = "zh";
                } else if (i == 1) {
                    from = "zh";
                    to = ("en");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 发起http请求获取返回结果
     *
     * @return
     */
    private void sendRequestWithHttpURLConnection(final String key) {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            public void run() {
                Main(key);

            }
        }).start();
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
        return R.layout.activity_translate;
    }

    @Override
    public void initView(View view) {
        input_et = $(R.id.input_et);
        out_tv = $(R.id.out_tv);
        titleView = $(R.id.titleview);
        button = $(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (input_et.getText().toString().length()>0&&input_et.getText().toString() !=null) {
                    sendRequestWithHttpURLConnection(input_et.getText().toString());
                }
                Log.i("test", "翻译:" + input_et.getText().toString());
            }
        });
        spinner = $(R.id.spinner);
        titleView.setTitle_tv("智能翻译");
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
}
