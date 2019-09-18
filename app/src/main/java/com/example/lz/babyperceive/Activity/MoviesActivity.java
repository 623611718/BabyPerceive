package com.example.lz.babyperceive.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lz.babyperceive.Adapter.ImageRecognitionAdapter;
import com.example.lz.babyperceive.Adapter.MoviesAdapter;
import com.example.lz.babyperceive.Bean.ImageRecognitionBean;
import com.example.lz.babyperceive.Bean.MoviesBean;
import com.example.lz.babyperceive.R;
import com.example.lz.babyperceive.View.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviesActivity extends BaseActivity {
    private TitleView titleView;
    private ListView listViewl;
    private List<MoviesBean> list = new ArrayList<>();
    private MoviesAdapter adapter;
    private MoviesBean moviesBean;
    private Map<String,String> m = new HashMap<String,String>();
    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] s = {"行尸走肉.mp4","test.mp4"};
        m.put("行尸走肉.mp4","http://lz.free.idcfengye.com/行尸走肉.mp4");
        m.put("test.mp4","http://s.swao.cn/o_1c3n5oq2s1gcai5d1vm917hv1cps7.mp4");
        initList(s);
    }

    private void initList(String[] s) {
        for (int i=0;i<s.length;i++){
            MoviesBean moviesBean = new MoviesBean();
            moviesBean.setName(s[i]);
            list.add(moviesBean);
        }
    }

    @Override
    public void initParms(Bundle parms) {
      /*  moviesBean = new MoviesBean();
        moviesBean.setName("行尸走肉.mp4");
        list.add(moviesBean);*/
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_movies;
    }

    @Override
    public void initView(View view) {
        titleView = $(R.id.titleview);
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
        titleView.setTitle_tv("视频列表");
        adapter = new MoviesAdapter(this, R.layout.movies_item, list);
        listViewl = $(R.id.listview);
        listViewl.setAdapter(adapter);
        listViewl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoviesActivity.this, PlayerActivity.class);
                intent.putExtra("data", list.get(position).getName());
                m.get(list.get(position).getName());
                intent.putExtra("url", m.get(list.get(position).getName()));
                startActivity(intent);
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
