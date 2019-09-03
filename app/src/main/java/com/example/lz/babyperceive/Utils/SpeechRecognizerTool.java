package com.example.lz.babyperceive.Utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;

/**
 * Created by Administrator on 2019/8/19.
 */

public class SpeechRecognizerTool implements RecognitionListener {

        public interface ResultsCallback {
            void onResults(String result);
        }

        private Context mContext;

        private SpeechRecognizer mSpeechRecognizer;

        private ResultsCallback mResultsCallback;

  public SpeechRecognizerTool(Context context) {
            mContext = context;
        }

    public synchronized void createTool() {
        if (null == mSpeechRecognizer) {

            // 创建识别器
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext,
                    new ComponentName(mContext, VoiceRecognitionService.class));

            // 注册监听器
            mSpeechRecognizer.setRecognitionListener(this);
        }
    }

    public synchronized void destroyTool() {
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.destroy();
        mSpeechRecognizer = null;
    }

    // 开始识别
    public void startASR(ResultsCallback callback) {
        mResultsCallback = callback;

        Intent intent = new Intent();
        bindParams(intent);
        mSpeechRecognizer.startListening(intent);
    }

    //停止识别
    public void stopASR() {
        mSpeechRecognizer.stopListening();
    }

    private void bindParams(Intent intent) {
        // 设置识别参数
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        // 准备就绪
        Toast.makeText(mContext, "请开始说话", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBeginningOfSpeech() {
        // 开始说话处理
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // 音量变化处理
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // 录音数据传出处理
    }

    @Override
    public void onEndOfSpeech() {
        // 说话结束处理
    }

    @Override
    public void onError(int error) {
    }

    @Override
    public void onResults(Bundle results) {

        // 最终结果处理
        if (mResultsCallback != null) {
            String text = results.get(SpeechRecognizer.RESULTS_RECOGNITION)
                    .toString().replace("]", "").replace("[", "");
            mResultsCallback.onResults(text);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // 临时结果处理
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}
