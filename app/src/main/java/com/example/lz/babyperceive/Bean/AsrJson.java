package com.example.lz.babyperceive.Bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lz on 2019/8/22.
 * 事物的JSON解析
 */

public class AsrJson {

    public List<Object> parseJSONobject(String jsondata) {
        Gson gson = new Gson();
        List<Object> applist = gson.fromJson(jsondata, new TypeToken<List<Object>>() {
        }.getType());
        for (Object object : applist) {
           /* Log.i("AsrJson","object name is:"+object.getName());
            Log.i("AsrJson","object object is:"+object.getObject());
            Log.i("AsrJson","object imageId is:"+object.getImageId());*/
        }
        return applist;
    }

    public List<Object> parseJSONobject(String jsondata, int position) {
        Gson gson = new Gson();
        List<Object> applist = gson.fromJson(jsondata, new TypeToken<List<Object>>() {
        }.getType());
        List<Object> applist2 = new ArrayList<>();
        Log.i("pas", "position:" + position);
        for (int i = 0; i <=position; i++) {
           applist2.add(applist.get(i));
           Log.i("pas","加入的:"+applist2.get(i).getName());
        }
        Log.i("pas", "length:" + applist2.size());
        return applist2;
    }
}
