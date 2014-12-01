package com.ruitu.btchelper.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateUtil {
    public static String getShortDateStr(long mill) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(new Date(mill));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getSimpleDateStr(long date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
            return dateFormat.format(new Date(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getMinuteStr(long millSecond) {
        if (millSecond < 60000) {
            return millSecond / 1000 + "秒";
        } else {
            return millSecond / 60000 + "分钟";
        }
    }

    public static long getMillSecond(String minute) {
        long time = 0;
        if (minute == null)
            return time;
        if (minute.endsWith("秒")) {
            String time_str = minute.substring(0, minute.lastIndexOf("秒"));
            time = Long.parseLong(time_str) * 1000;
        } else if (minute.endsWith("分钟")) {
            String time_str = minute.substring(0, minute.lastIndexOf("分钟"));
            time = Long.parseLong(time_str) * 60000;
        }
        Log.e(".........", time + "");
        return time;
    }
}
