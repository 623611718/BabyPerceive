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

    public List<Object> parseJSONobject(String jsondata){
        Gson gson =new Gson();
        List<Object> applist = gson.fromJson(jsondata,new TypeToken<List<Object>>(){
        }.getType());
        for (Object object : applist){
            Log.i("AsrJson","object name is:"+object.getName());
            Log.i("AsrJson","object object is:"+object.getObject());
            Log.i("AsrJson","object imageId is:"+object.getImageId());
        }
        return applist;
    }
}
