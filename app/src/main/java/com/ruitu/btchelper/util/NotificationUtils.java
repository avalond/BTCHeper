package com.ruitu.btchelper.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

public class NotificationUtils {
    final static public int FLAG_ONGOING_EVENT_AUTO_CANCEL = Notification.FLAG_AUTO_CANCEL
            | Notification.FLAG_ONGOING_EVENT;
    final static public int FLAG_ONGOING_EVENT = Notification.FLAG_ONGOING_EVENT;
    final static public int FLAG_NO_CLEAR = Notification.FLAG_NO_CLEAR;
    final static public int FLAG_AUTO_CANCEL = Notification.FLAG_AUTO_CANCEL;

    static public Notification notify(Context c, int notifyId, long time,
            int iconResId, int textResId, int soundResId, int shakeId,
            int titleResId, int contentResId, Class<?> cls, int flag) {
        final Resources res = ((Activity) c).getResources();
        return notify(c, notifyId, time, iconResId, res.getString(textResId),
                soundResId, shakeId, res.getString(titleResId),
                res.getString(contentResId), cls, flag);
    }

    static public Notification notify(Context c, int notifyId, long time,
            int iconResId, String notifyShowText, int soundResId, int shakeId,
            String titleText, String contentText, Class<?> cls, int flag) {
        Log.e("通知", notifyId + "notifyId");
        Notification n = genNotification(c, notifyId, time, iconResId,
                notifyShowText, soundResId, shakeId, titleText, contentText,
                cls, flag);
        notify(c, notifyId, n);
        return n;
    }

    static public void notify(Context c, int notifyId, Notification n) {
        final NotificationManager nm = (NotificationManager) c
                .getSystemService(Context.NOTIFICATION_SERVICE);
       // nm.notify(notifyId, n);
    }

    static public Notification genNotification(Context c, int notifyId,
            long time, int iconResId, int textResId, int soundResId,
            int shakeId, int titleResId, int contentResId, Class<?> cls,
            int flag) {
        final Resources res = ((Activity) c).getResources();
        return genNotification(c, notifyId, time, iconResId,
                res.getString(textResId), soundResId, shakeId,
                res.getString(titleResId), res.getString(contentResId), cls,
                flag);
    }

    public static void cancel(Context c, int notifyId) {
        ((NotificationManager) ((Activity) c)
                .getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(notifyId);
    }

    static public Notification genNotification(Context c, int notifyId,
            long time, int iconResId, String notifyShowText, int soundResId,
            int shakeId, String titleText, String contentText, Class<?> cls,
            int flags) {
        final Notification n = new Notification();

        n.when = time;
        n.flags = flags;
        n.icon = iconResId;
        n.tickerText = notifyShowText;
        // 添加声音效果
        n.defaults |= soundResId;
        // 添加震动
        n.defaults |= shakeId;
        Intent intent = null;
        if (cls != null)
            intent = new Intent(c, cls);
        final PendingIntent ip = PendingIntent.getActivity(c, 0, intent, 0);
        n.setLatestEventInfo(c, titleText, contentText, ip);

        return n;
    }
}
