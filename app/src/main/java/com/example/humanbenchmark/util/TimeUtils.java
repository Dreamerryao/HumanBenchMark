package com.example.humanbenchmark.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public  static String getCurrentTime()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time= simpleDateFormat.format(date);
        Log.i("LOGCAT","Current Time: "+ time);

        return time;
    }
    /*time1 new ,  time2  old */
    public  static long getMinuteDiff(String time1, String time2) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1= simpleDateFormat.parse(time1);
        Date d2 = simpleDateFormat.parse(time2);
        long diff = d1.getTime()- d2.getTime();//微秒级别
        long minutediff= diff/(1000*60);
        Log.i("LOGCAT","Minute Diff: "+ minutediff);

        return minutediff+1;

    }
}
