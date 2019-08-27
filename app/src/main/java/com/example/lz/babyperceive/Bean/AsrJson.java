package com.example.lz.babyperceive.Bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lz on 2019/8/22.
 */

public class AsrJson {
    public List<English> parseJSONenglish(String jsondata){
        Gson gson = new Gson();
        List<English> applist = gson.fromJson(jsondata, new TypeToken<List<English>>() {
        }.getType());
        for (English english :applist){
            Log.i("test","id is:"+english.getId());
            Log.i("test","english is:"+english.getEnglish());
            Log.i("test","chinese is:"+english.getChinese());
        }
        return applist;
    }
    public List<Idiom> parseJSONidiom(String jsondata){
        Gson gson =new Gson();
        List<Idiom> applist = gson.fromJson(jsondata,new TypeToken<List<Idiom>>(){
        }.getType());
        for (Idiom idiom : applist){
            Log.i("AsrJson","idiom id is:"+idiom.getId());
            Log.i("AsrJson","idiom idiom is:"+idiom.getIdiom());
            Log.i("AsrJson","idiom story is:"+idiom.getStory());
        }
        return applist;
    }
}
