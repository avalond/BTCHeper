package com.ruitu.btchelper.service;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.activity.MainActivity;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.NotificationUtils;
import com.ruitu.btchelper.util.PlatformNameUtils;

public class WarnUtils {
    private static final String TAG = WarnUtils.class.getSimpleName();

    public static boolean cancelNotice(Context c) {
        List<String> platforms = PlatformNameUtils.getPlatFormList();
        int size = platforms.size();
        Log.e(TAG, size + "<<<<<size");
        for (int i = 10000001; i < 10000001 + size; i++) {
            Log.e(TAG, i + "><<<<<<<<<i");
            NotificationUtils.cancel(c, i);
        }
        return true;
    }

    public static void sendNotice(Context context, Ticker ticker) {
        String name = ticker.getName();
        String last = ticker.getLast();
        List<String> platforms = PlatformNameUtils.getPlatFormList();
        int notification_id = 0;
        if (platforms.contains(name)) {
            notification_id = platforms.indexOf(name) + 10000001;
        }

        if (platforms.get(0).equals(name) || platforms.get(4).equals(name)
                || platforms.get(5).equals(name)) {
            last = "$" + last;
        } else {
            last = "¥" + last;
        }

        String content = name + "最近的成交价:" + last;
        String title = name + "实时详情";
        long time = System.currentTimeMillis();
        int soundId = Notification.DEFAULT_SOUND;
        int shakeId = Notification.DEFAULT_VIBRATE;
        int icon_id = R.drawable.ic_launcher;
        int flag = NotificationUtils.FLAG_AUTO_CANCEL;
        NotificationUtils.notify(context, notification_id, time, icon_id,
                content, soundId, shakeId, title, content, MainActivity.class,
                flag);
    }

    @SuppressLint("NewApi")
    public static void shake(Context context) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            // 设备震动
            vibrator.vibrate(new long[] { 300, 1000, 300, 1000, 300, 1000, 300,
                    1000, 300, 1000 }, -1);
        }
    }
}
