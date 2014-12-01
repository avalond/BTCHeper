package com.ruitu.btchelper.setting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class LocalSharePreference {
    private static final String TAG = LocalSharePreference.class
            .getSimpleName();
    /**
     * 本地设置文件名
     */
    public static final String preference_name = "LocalSharePreference";
    /**
     * 实时详情刷新时间
     */
    public static final String KEY_MARKET = "KEY_MARKET";
    /**
     * 市场深度刷新时间
     */
    public static final String KEY_DETAIL = "KEY_DETAIL";
    public static final String KEY_WARNPLATIFORM = "KEY_WARNPLATIFORM";
    /**
     * 最低价
     */
    public static final String KEY_LOWRRICE = "KEY_LOWRRICE";
    /**
     * 最高价
     */
    public static final String KEY_HIGHTPRICE = "KEY_HIGHTPRICE";
    /**
     * 是否监控
     */
    public static final String KEY_MONITOR = "KEY_MONITOR";
    /**
     * 是否已经提示过了
     */
    public static final String KEY_HASWARN = "KEY_HASWARN";
    /**
     * 是否响铃
     */
    public static final String KEY_ALARM = "KEY_ALARM";
    /**
     * 是否震动
     */
    public static final String KEY_SHAKE = "KEY_SHAKE";
    /**
     * 选择的铃声
     */
    public static final String KEY_RINGTONEURI = "KEY_RINGTONEURI";
    /**
     * 实时详情关注的平台
     */
    public static final String KEY_PLATNAME_DEFAULT = "KEY_PLATNAME_DEFAULT";
    /**
     * 用户是否登陆
     */
    public static final String KEY_ISLOGIN = "KEY_ISLOGIN";
    public static final String KEY_USERNAME = "KEY_USERNAME";
    public static final String KEY_COMPAREPF1 = "KEY_COMPAREPF1";
    public static final String KEY_COMPAREPF2 = "KEY_COMPAREPF2";
    public static final String KEY_COMPAREPRICE = "KEY_COMPAREPRICE";
    public static final String KEY_NOTIFYPLATIFORM = "KEY_NOTIFYPLATIFORM";
    /**
     * 是否后台监控
     */
    public static final String KEY_BACKGROUND = "KEY_BACKGROUND";

    /**
     * 版本号
     */
    public static final String KEY_VERSION = "KEY_VERSION";
    /**
     * 第一次登录
     */
    public static final String KEY_FIRSET = "KEY_FIRST";
    /**
     * 是否使用流量更新
     */
    public static final String KEY_ONT_WIFI_REFULSH = "KEY_ONT_WIFI_REFULSH";

    public static boolean commitNotWifiRelush(Context context, Boolean isrelush) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ONT_WIFI_REFULSH, true);
        return editor.commit();
    }

    public static boolean isNotWifiRelush(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        return sharedPreferences.getBoolean(KEY_ONT_WIFI_REFULSH, false);
    }

    public static boolean clearWifiRelush(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ONT_WIFI_REFULSH);
        editor.commit();
        return true;
    }

    public static boolean commitBackground(Context context, Boolean isBackground) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_BACKGROUND, true);
        return editor.commit();
    }

    public static boolean isBackground(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        return sharedPreferences.getBoolean(KEY_BACKGROUND, false);
    }

    public static void commitFirstLogin(Context context, Boolean isFirst) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRSET, false);
        editor.commit();
    }

    public static boolean isFirstLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        return sharedPreferences.getBoolean(KEY_FIRSET, true);
    }

    public static boolean commitDefaultPlatform(Context context,
            List<String> platnames) {
        if (platnames.isEmpty()) {
            return false;
        }
        StringBuffer buff = new StringBuffer();
        for (String platname : platnames) {
            buff.append(platname + "|");
        }
        buff.deleteCharAt(buff.length() - 1);
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PLATNAME_DEFAULT, buff.toString());
        editor.commit();
        return true;
    }

    public static List<String> getDefaultPlatnameInterval(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        String value_str = sharedPreferences
                .getString(KEY_PLATNAME_DEFAULT, "");
        List<String> value = new ArrayList<String>();
        String[] str_arr = value_str.split("\\|");
        for (String str : str_arr) {
            value.add(str);
        }
        return value;
    }

    public static boolean commitHasWarnInterval(Context context,
            String platname, boolean haswarn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(platname + "_" + KEY_HASWARN, haswarn);
        editor.commit();
        return true;
    }

    public static boolean isHasWarn(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        boolean value = sharedPreferences.getBoolean(platname + "_"
                + KEY_HASWARN, false);
        return value;
    }

    public static boolean clearWarn(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.remove(platname + "_" + KEY_HASWARN);
        editor.commit();
        return true;
    }

    public static boolean commitMonitorInterval(Context context,
            String platname, boolean isMonitor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(platname + "_" + KEY_MONITOR, isMonitor);
        editor.commit();
        return true;
    }

    public static boolean isMonitorInterval(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        boolean value = sharedPreferences.getBoolean(platname + "_"
                + KEY_MONITOR, false);
        return value;
    }

    public static boolean clearMonitorInterval(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.remove(platname + "_" + KEY_MONITOR);
        editor.commit();
        return true;
    }

    public static boolean commitAlarmInterval(Context context, String platname,
            boolean isMonitor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(platname + "_" + KEY_ALARM, isMonitor);
        editor.commit();
        return true;
    }

    public static boolean isAlarmInterval(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        boolean value = sharedPreferences.getBoolean(
                platname + "_" + KEY_ALARM, false);
        return value;
    }

    public static boolean clearAlarmInterval(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.remove(platname + "_" + KEY_ALARM);
        editor.commit();
        return true;
    }

    public static boolean commitShakeInterval(Context context, String platname,
            boolean isMonitor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(platname + "_" + KEY_SHAKE, isMonitor);
        editor.commit();
        return true;
    }

    public static boolean isShakeInterval(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        boolean value = sharedPreferences.getBoolean(
                platname + "_" + KEY_SHAKE, false);
        return value;
    }

    public static boolean clearShakeInterval(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.remove(platname + "_" + KEY_SHAKE);
        editor.commit();
        return true;
    }

    public static boolean commintMarketInterval(Context context, long timeSec) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_MARKET, timeSec);
        editor.commit();
        return true;
    }

    public static long getMarketInterval(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        long value = sharedPreferences.getLong(KEY_MARKET, 6000);
        return value;
    }

    public static boolean commintDetailInterval(Context context, long timeSec) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_DETAIL, timeSec);
        editor.commit();
        return true;
    }

    public static long getDetailInterval(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        long value = sharedPreferences.getLong(KEY_DETAIL, 6000);
        return value;
    }

    public static boolean commitPriceNoticeType(Context context, int types) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_NOTIFYPLATIFORM, types);
        editor.commit();
        return true;
    }

    public static int getPriceNoticeType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        int value = sharedPreferences.getInt(KEY_NOTIFYPLATIFORM, -1);
        return value;
    }

    public static boolean commitPriceWarningType(Context context, int types) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_WARNPLATIFORM, types);
        editor.commit();
        return true;
    }

    public static boolean commitRingtoneUri(Context context, Uri uri) {
        Log.e("uri==============", uri.toString());
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RINGTONEURI, uri.toString());
        editor.commit();
        return true;
    }

    public static Uri getRingtoneUri(Context context) {
        RingtoneUtils ringtoneUtils = new RingtoneUtils(context);
        Uri defaultUri = ringtoneUtils
                .getDefaultRingtoneUri(RingtoneManager.TYPE_NOTIFICATION);
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        String value = sharedPreferences.getString(KEY_RINGTONEURI,
                defaultUri.toString());
        Log.e(TAG, value);
        return Uri.parse(value);
    }

    public static boolean commitLowPrice(Context context, int price,
            String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(platname + "_" + KEY_LOWRRICE, price);
        return editor.commit();
    }

    public static int getLowPrice(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        int value = sharedPreferences.getInt(platname + "_" + KEY_LOWRRICE, 0);
        return value;
    }

    public static boolean commitHightPrice(Context context, int price,
            String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(platname + "_" + KEY_HIGHTPRICE, price);
        return editor.commit();
    }

    public static int getHightPrice(Context context, String platname) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        int value = sharedPreferences
                .getInt(platname + "_" + KEY_HIGHTPRICE, 0);
        return value;
    }

    public static boolean commitComparePF1(Context context, int type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COMPAREPF1, type);
        return editor.commit();
    }

    public static int getComparePF1(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        int value = sharedPreferences.getInt(KEY_COMPAREPF1, 0);
        return value;
    }

    public static boolean commitComparePF2(Context context, int type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COMPAREPF2, type);
        return editor.commit();
    }

    public static int getComparePF2(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        int value = sharedPreferences.getInt(KEY_COMPAREPF2, 0);
        return value;
    }

    public static boolean commitPriceInterval(Context context, int priceInterval) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COMPAREPRICE, priceInterval);
        return editor.commit();
    }

    public static int getPriceInterval(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        int value = sharedPreferences.getInt(KEY_COMPAREPRICE, 0);
        return value;
    }

    public static void clearData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    // public static boolean cancel(Context context, String username) {
    // SharedPreferences sharedPreferences = context.getSharedPreferences(
    // preference_name, 0);
    // Editor editor = sharedPreferences.edit();
    // editor.putBoolean(KEY_ISLOGIN + username, false);
    // return editor.commit();
    // }
    //
    // public static boolean login(Context context, String username) {
    // SharedPreferences sharedPreferences = context.getSharedPreferences(
    // preference_name, 0);
    // Editor editor = sharedPreferences.edit();
    // editor.putBoolean(KEY_ISLOGIN + username, true);
    // return editor.commit();
    // }
    //
    // public static boolean isLogin(Context context, String username) {
    // SharedPreferences sharedPreferences = context.getSharedPreferences(
    // preference_name, 0);
    // boolean value = sharedPreferences.getBoolean(KEY_ISLOGIN + username,
    // false);
    // return value;
    // }

    public static boolean setUserName(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        return editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public static boolean clearUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        return editor.commit();
    }
}
