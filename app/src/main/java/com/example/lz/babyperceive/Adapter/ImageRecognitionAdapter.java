package com.example.lz.babyperceive.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.ImageRecognitionBean;
import com.example.lz.babyperceive.R;

import java.util.List;

/**
 * Created by lz on 2019/9/15.
 */


/**
 * 图像识别listview 的adapter
 */
public class ImageRecognitionAdapter extends ArrayAdapter<ImageRecognitionBean> {

    private int resourceId;
    private List<ImageRecognitionBean> list;

    public ImageRecognitionAdapter(@NonNull Context context, int resource, @NonNull List<ImageRecognitionBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    /**
     * 刷新数据源
     *
     * @param list
     */
    public void refresh(List<ImageRecognitionBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageRecognitionBean imageRecognitionBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView name_tv = (TextView) view.findViewById((R.id.name_tv));
        TextView score_tv = (TextView) view.findViewById((R.id.score_tv));
        name_tv.setText(imageRecognitionBean.getName());
        score_tv.setText(imageRecognitionBean.getScore());
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        String url = "https://wapbaike.baidu.com/item/" + imageRecognitionBean.getName();
        webView.loadUrl(url);
        Log.i("web", "百度百科url:" + url);
        return view;
    }
}
