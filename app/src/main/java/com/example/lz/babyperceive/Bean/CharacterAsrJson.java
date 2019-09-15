package com.example.lz.babyperceive.Bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by lz on 2019/9/10.
 * 语音识别解析
 */

public class CharacterAsrJson {
    public List<CharacterBean> parseJSONobject(String jsondata){
        Gson gson =new Gson();
        List<CharacterBean> applist = gson.fromJson(jsondata,new TypeToken<List<CharacterBean>>(){
        }.getType());
        for (CharacterBean object : applist){
            Log.i("AsrJson","object name is:"+object.getWords());
        }
        return applist;
    }
}
