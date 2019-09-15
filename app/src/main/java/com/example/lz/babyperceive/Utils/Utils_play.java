package com.example.lz.babyperceive.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.TimeZone;

/**
 * Created by lz on 2019/9/15.
 */

public class Utils_play {
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public static String formatTime(int ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(Integer.valueOf(ms));
        System.out.println(hms);
        return hms;
    }

    public String getDada() {
        //获取系统的日期
//年
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH) + 1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);

        String data = year + "-" + month + "-" + day + "  "+hour+":"+minute+":"+second;
        Log.i("utils", "日期+时间:  " + data);
        return data;
    }

    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        if (hours > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    public boolean isNetUri(String uri) {
        if (uri == null) {
            return false;
        }
        if (uri.toLowerCase().startsWith("http") || uri.toLowerCase().startsWith("rtsp") || uri.toLowerCase().startsWith("mms")) {
            return true;
        }
        return false;
    }
}
