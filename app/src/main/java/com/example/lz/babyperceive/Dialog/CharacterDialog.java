package com.example.lz.babyperceive.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lz.babyperceive.Adapter.CharaectAdapter;
import com.example.lz.babyperceive.Adapter.MoviesAdapter;
import com.example.lz.babyperceive.Application.MyApplication;
import com.example.lz.babyperceive.Bean.CharacterBean;
import com.example.lz.babyperceive.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lz on 2019/9/22.
 */

public class CharacterDialog extends Dialog {

    private Context mContext;
    private String content;
    private CommomDialog.OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private EditText et1,et2;
    private List<CharacterBean> list =new ArrayList<>();

    private MyApplication myApplication;
    public CharacterDialog(Context context, int dialog, List<CharacterBean> list2, OnCloseListener onCloseListener) {
        super(context);
        this.list=list2;
        this.mContext = context;
    }

    public CharacterDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CharacterDialog(Context context, int themeResId, List<CharacterBean> list, CommomDialog.OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.list = list;
       // this.content = content;
        this.listener = listener;
    }

    protected CharacterDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CharacterDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CharacterDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CharacterDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_character);
        // setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        ListView listView =(ListView) findViewById(R.id.listview);
        CharaectAdapter charaectAdapter  = new CharaectAdapter(mContext, R.layout.character_item, list);
        listView.setAdapter(charaectAdapter);

    }



    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}