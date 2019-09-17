package com.example.lz.babyperceive.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.lz.babyperceive.Bean.Object;
import com.example.lz.babyperceive.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2019/9/17.
 */

public class BaiduTranslateService {
    private Handler handler = null;
    private static String dst;
    private static String content;





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

}