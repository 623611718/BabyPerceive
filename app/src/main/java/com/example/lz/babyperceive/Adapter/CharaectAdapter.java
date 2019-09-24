package com.example.lz.babyperceive.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lz.babyperceive.Bean.CharacterBean;
import com.example.lz.babyperceive.Bean.MoviesBean;
import com.example.lz.babyperceive.R;

import java.util.List;

/**
 * Created by lz on 2019/9/22.
 * 文字识别listview 的adapter
 */

public class CharaectAdapter extends ArrayAdapter<CharacterBean> {

    private int resourceId;
    private List<CharacterBean> list;
    public CharaectAdapter(@NonNull Context context, int resource, @NonNull List<CharacterBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    /**
     * 刷新数据源
     *
     * @param list
     */
    public void refresh(List<CharacterBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CharacterBean characterBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView spell=(TextView) view.findViewById((R.id.spell_tv));
        TextView chinese_tv=(TextView) view.findViewById((R.id.chinese_tv));
        //  ImageView imageView=(ImageView) view.findViewById((R.id.score_tv));
        spell.setText(characterBean.getWordsSpell());
        chinese_tv.setText(characterBean.getWords());
        // imageView.setText(moviesBean.getScore());
        return view;
    }
}

