package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.View.TitleView;

public class WebViewActivity extends BaseActivity {

    private WebView webView;
    private String name;
    private TitleView titleView;

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initParms(Bundle parms) {

        //  Log.i("test","name:", (Throwable) parms.get("data"));

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView(View view) {
        titleView=$(R.id.titleview);

        Intent intent = getIntent();
        name = intent.getStringExtra("data");
        webView = $(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        String url = "https://wapbaike.baidu.com/item/" + name;
        webView.loadUrl(url);
        Log.i("web","百度百科url:"+url);

        titleView.setCustomOnClickListener(new TitleView.ClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mote_bt:
                        break;
                    case R.id.back_bt:
                        finish();
                        break;
                }
            }
        });
        titleView.setTitle_tv("幼儿学知识");

    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
