package com.ruitu.btchelper.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmUtils {
    private static final String TAG = AlarmUtils.class.getSimpleName();

    /**
     * 获得闹钟管理器
     * 
     */
    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 设置闹钟
     * 
     * @param context
     * @param timeUpdate
     */
    public static PendingIntent setAlarmTask(Context context, String action) {
        Intent intent = new Intent(action);
        Log.e(TAG, "设置好了闹钟");
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

    }

    /**
     * 启动闹钟
     */
    public static void startAlarm(AlarmManager alarmManager, long updateTime,
            PendingIntent pendingIntent) {
        Log.e(TAG, "启动了闹钟");
        final long time = System.currentTimeMillis();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, updateTime,
                pendingIntent);
    }

    /**
     * 取消闹钟
     */
    public static void cancelAlarmTask(AlarmManager alarmManager,
            PendingIntent pendingIntent) {
        Log.e(TAG, "取消了闹钟");
        alarmManager.cancel(pendingIntent);
        pendingIntent = null;
        alarmManager = null;
    }
}
