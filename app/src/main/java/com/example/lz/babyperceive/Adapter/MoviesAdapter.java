package com.example.lz.babyperceive.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.ImageRecognitionBean;
import com.example.lz.babyperceive.Bean.MoviesBean;
import com.example.lz.babyperceive.R;

import java.util.List;

/**
 * Created by lz on 2019/9/15.
 * 电影listview 的adapter
 */

public class MoviesAdapter  extends ArrayAdapter<MoviesBean> {

    private int resourceId;
    private List<MoviesBean> list;
    public MoviesAdapter(@NonNull Context context, int resource, @NonNull List<MoviesBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    /**
     * 刷新数据源
     *
     * @param list
     */
    public void refresh(List<MoviesBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MoviesBean moviesBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name_tv=(TextView) view.findViewById((R.id.name_tv));
      //  ImageView imageView=(ImageView) view.findViewById((R.id.score_tv));
        name_tv.setText(moviesBean.getName());
       // imageView.setText(moviesBean.getScore());
        return view;
    }
}
