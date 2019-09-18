package com.example.lz.babyperceive;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lz.babyperceive.Activity.AnimalActivity;
import com.example.lz.babyperceive.Activity.BaseActivity;
import com.example.lz.babyperceive.Activity.CharacterRecognitionActivity;
import com.example.lz.babyperceive.Activity.IdentifyActivity;
import com.example.lz.babyperceive.Activity.ImageRecognitionActivity;
import com.example.lz.babyperceive.Activity.LearningActivity;
import com.example.lz.babyperceive.Activity.MoviesActivity;
import com.example.lz.babyperceive.Activity.MusicActivity;
import com.example.lz.babyperceive.Activity.ObjectActivity;
import com.example.lz.babyperceive.Activity.PlayerActivity;
import com.example.lz.babyperceive.Activity.SpeakingActivity;
import com.example.lz.babyperceive.Activity.TestActivity;
import com.example.lz.babyperceive.Activity.TranslateActivity;
import com.example.lz.babyperceive.Activity.YuleActivity;
import com.example.lz.babyperceive.Utils.BaiduTranslateService;
import com.example.lz.babyperceive.Utils.TranslateResult;
import com.example.lz.babyperceive.View.TitleView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button bt1, bt2, bt3, bt4; //1:学一学 2:考一考 3:智能识别 4:休闲娱乐
    private TitleView titleView;
    private static  Handler handler = null;
    private static String dst;

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
      //  getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
       // changeStatusBarTextColor(true);
        initPermission();  //初始化权限
        initView();//初始化View
        handler = new Handler();
        translate("apple");

    }
    static Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
          Log.i("test","翻译结果:"+dst);
        }
    };
    /**
     * 发起http请求获取返回结果
     *
     * @param requestUrl 请求地址
     * @return
     */
    private static final String sendRequestWithHttpURLConnection(final String requestUrl) {
        //开启线程来发起网络请求
        final StringBuffer buffer1 = new StringBuffer();
        final String[] c = new String[1];
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection connection = null;
                StringBuffer buffer = new StringBuffer();
                try {

                    URL url = new URL(requestUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);

                    }
                    c[0] = response.toString();
                    Log.i("test", "c[0]:" + c[0]);
                    Gson gson = new Gson();
                    TranslateResult translateResult = gson.fromJson(c[0], TranslateResult.class);
                    Log.i("test", "translateResult:" + translateResult);
                    // 取出translateResult中的译文
                    dst = translateResult.getTrans_result().get(0).getDst();
                    Log.i("test", "result:" + dst);
                    handler.post(runnableUi);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
        return c[0];
    }

    /**
     * utf编码
     *
     * @param source
     * @return
     */

    public static String urlEncodeUTF8(String source) {

        String result = source;

        try {

            result = java.net.URLEncoder.encode(source, "utf-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return result;

    }


    /**
     * 翻译（中->英 英->中 日->中 ）
     *
     * @param source
     * @return
     */


    public static void translate(String source) {
        String dst = null;
        // 组装查询地址
//APP ID：20190917000335107

        // 密钥：N_WaL4ElbrYrjlDzI3tO
        String requestUrl = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=apple&from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4";
        //http://openapi.baidu.com/public/2.0/bmt/translate?client_id=20190917000335107&q=apple&from=auto&to=zh
        //http://api.fanyi.baidu.com/api/trans/vip/translate?q={keyWord}&from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4
        // 对参数q的值进行urlEncode utf-8编码
        //requestUrl = requestUrl.replace("{keyWord}", urlEncodeUTF8(source));
        // 查询并解析结果
        // 查询并获取返回结果
        sendRequestWithHttpURLConnection(requestUrl);


    }


    //改变状态栏字体颜色
    private void changeStatusBarTextColor(boolean isBlack) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
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
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_PHONE_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void initView() {
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        titleView = (TitleView) findViewById(R.id.titleview);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mote_bt:
                        showPopupMenu(v);
                        break;
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
        titleView.setTitle_tv(" ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                Intent intent1 = new Intent(this, LearningActivity.class);
                intent1.putExtra("title","学一学");
                startActivity(intent1);
                break;
            case R.id.bt2:
                Intent intent2 = new Intent(this, TestActivity.class);
                intent2.putExtra("title","考一考");
                startActivity(intent2);
                break;
            case R.id.bt3:
                Intent intent3 = new Intent(this, IdentifyActivity.class);
                intent3.putExtra("title","智能识别");
                startActivity(intent3);
                break;
            case R.id.bt4:
                Intent intent4 = new Intent(this, YuleActivity.class);
                intent4.putExtra("title","休闲娱乐");
                startActivity(intent4);
                break;
           /* case R.id.idiom_bt:
                Intent intent_idiom = new Intent(this, AnimalActivity.class);
                intent_idiom.putExtra("data", "idiom.txt");
                startActivity(intent_idiom);
                break;*/
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }


    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.info, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.quit1:
                        Intent intent1 = new Intent(MainActivity.this, TranslateActivity.class);
                        startActivity(intent1);
                        break;


                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}
