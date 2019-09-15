package com.example.lz.babyperceive.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.ImageRecognitionBean;
import com.example.lz.babyperceive.R;

import java.util.List;

/**
 * Created by lz on 2019/9/15.
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
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name_tv=(TextView) view.findViewById((R.id.name_tv));
        TextView score_tv=(TextView) view.findViewById((R.id.score_tv));
        name_tv.setText(imageRecognitionBean.getName());
        score_tv.setText(imageRecognitionBean.getScore());
        return view;
    }
}
