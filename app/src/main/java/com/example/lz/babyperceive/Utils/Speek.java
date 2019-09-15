package com.example.lz.babyperceive.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.util.ArrayList;

/**
 * Created by lz on 2019/8/26.
 */

public class Speek {
    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     */
    private String appId = "17230558";

    private String appKey = "mVY5zKIhZpANAm7GipGAGYE5";

    private String secretKey = "nXLCGwBTsUfcRG2ZGcSgAbZx5y1TmyEI";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.MIX;

    protected SpeechSynthesizer mSpeechSynthesizer;

    private static final String TAG = "Speek";

    private Activity activity;

    private static boolean isPermissionRequested = false;

    public Speek(Activity activity){
        this.activity=activity;
        Permission();
        initTTs();
    }

    public void Speeking(String text){
        int result = mSpeechSynthesizer.speak(text);
        checkResult(result, "speak");
    }

    /**
     * 初始化TTS
     */
    private void initTTs() {
        SpeechSynthesizerListener listener=new MessageListener();
        //获取 SpeechSynthesizer 实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        //设置使用环境
        mSpeechSynthesizer.setContext(activity);
        //注册监听其
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
        /**
         * 授权检验接口
         * 测试AppId，AppKey AppSecret填写正确，语音合成服务是否开通。
         * 在线合成模式下，验证权限。首次验证时比较耗时，第一次调用成功，之后可以不必调用。
         */
        mSpeechSynthesizer.auth(TtsMode.MIX);  // 离在线混合
        /**
         * 设置 App Id和 App Key 及 App Secret
         * 用户在语音官网或者百度云网站上申请语音合成的应用后，会有appId appKey及appSecret
         */
        int result =  mSpeechSynthesizer.setAppId(appId);
        checkResult(result, "setAppId");
        result = mSpeechSynthesizer.setApiKey(appKey, secretKey);
        checkResult(result, "setApiKey");

        //设置合成参数
        // 以下setParam 参数选填。不填写则默认值生效
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "4"); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9"); // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "3");// 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "3");// 设置合成的语调，0-9 ，默认 5

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        /**
         * 初始化合成引擎
         * 设置合成的参数后，需要调用此方法初始化
         */
        result = mSpeechSynthesizer.initTts(ttsMode);
        checkResult(result, "initTts");

        //设置音频流类型
        mSpeechSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);


    }

    /**
     * 获取权限(Android6.0 以上需要动态获取权限)
     */
    private void Permission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            String permissions[] = {
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
            };

            ArrayList<String> toApplyList = new ArrayList<String>();

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(perm)) {
                    toApplyList.add(perm);
                    //进入到这里代表没有权限.
                }
            }
            if(toApplyList.size()==0)
            {
                return;
            }
            else
            {
                activity.requestPermissions(toApplyList.toArray(new String[toApplyList.size()]), 0);
            }
        }
    }

    private void print(String message) {
        Log.i(TAG, message);
    }
    private void checkResult(int result, String method) {
        if (result != 0) {
            print("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }
    public void Destory(){
        if (mSpeechSynthesizer != null){
            mSpeechSynthesizer.stop();
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;
            print("释放资源成功");
        }
    }
}
