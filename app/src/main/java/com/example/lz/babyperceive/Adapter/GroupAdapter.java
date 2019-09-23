package com.example.lz.babyperceive.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.GroupBean;
import com.example.lz.babyperceive.Bean.MoviesBean;
import com.example.lz.babyperceive.LearningActivity.GroupActivity;
import com.example.lz.babyperceive.R;

import java.util.List;

/**
 * Created by lz on 2019/9/23.
 */

public class GroupAdapter extends ArrayAdapter<GroupBean> {

    private int resourceId;
    private List<GroupBean> list;
    public GroupAdapter(@NonNull Context context, int resource, @NonNull List<GroupBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }



    /**
     * 刷新数据源
     *
     * @param list
     */
    public void refresh(List<GroupBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GroupBean groupBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name_tv=(TextView) view.findViewById((R.id.name_tv));
        //  ImageView imageView=(ImageView) view.findViewById((R.id.score_tv));
        name_tv.setText(groupBean.getGroup());
        // imageView.setText(moviesBean.getScore());
        return view;
    }
}
