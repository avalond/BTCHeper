package com.ruitu.btchelper.util;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName: AppUtil
 * @Description: 为程序使用提供公共方法类
 * @author hbq
 * @date 2013-5-6 下午1:57:53
 * 
 */
public class AppUtil {


    /**
     * 
     * @Methods: getAppVersionName
     * @Description: 返回当前程序版本名
     * @param context
     * @return
     * @throws
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 返回当前程序渠道号
     *
     * @param context
     * @return
     */
    public static String getAppChannelCode(Context context) {
        String code = getMetaData(context, "CHANNEL");
        if (code != null) {
            return code;
        }
        return "A1001";
    }

    /**
     * 获取Manifest文件中自定义Meta-Data标签的值
     * 
     * @param context
     * @param key
     *            标签名
     * @return
     */
    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 方法名: getVersionCode
     * <p>
     * 功能说明： 返回当前应用的版本号
     * </p>
     * 
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        }catch (Exception e) {
        }
        return verCode;

    }

    /**
     * 
     * @Methods: openGPSEnable
     * @Description: 打开GPS定位服务
     * @param context
     * @throws
     */
    public static void openGPSEnable(Context context) {
        // true 打开 false 关闭
        boolean isOpen = isGPSEnable(context);
        if (!isOpen) {
            toggleGPS(context);
        }
    }

    /**
     * 
     * @Methods: closeGPSEnable
     * @Description: 关闭GPS定位服务
     * @param context
     * @throws
     */
    public static void closeGPSEnable(Context context) {
        // true 打开 false 关闭
        boolean isOpen = isGPSEnable(context);
        if (isOpen) {
            toggleGPS(context);
        }
    }

    /**
     * 
     * @Methods: isGPSEnable
     * @Description: 检测GPRS是否打开
     * @param context
     * @return
     * @throws
     */
    private static boolean isGPSEnable(Context context) {
        String str = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (str != null) {
            return str.contains("gps");
        } else {
            return false;
        }
    }

    /**
     * 
     * @Methods: toggleGPS
     * @Description: 开启/关闭GPRS
     * @param context
     * @throws
     */
    private static void toggleGPS(Context context) {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Methods: getDisplayMetrics
     * @Description: 获取屏幕信息对象
     * @param activity
     * @return
     * @throws
     */
    public static DisplayMetrics getDisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        SharedPreferences preferences = context.getSharedPreferences(
                DataHelper.SYSTEM_SETTING, Context.MODE_PRIVATE);
        preferences.edit().putInt(DataHelper.SCREEN_W, dm.widthPixels)
                .putInt(DataHelper.SCREEN_H, dm.heightPixels)
                .putFloat(DataHelper.DENSITY, dm.density)
                .putInt(DataHelper.DENSITYDPI, dm.densityDpi).commit();
        return dm;
    }

    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        return imsi;

    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;

    }

    /**
     * 
     * @Methods: isSdCardExist
     * @Description: 检测SD卡是否存在
     * @return true 存在、false 不存在
     * @throws
     */
    public static boolean isSdCardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * sd卡可用空间
     * 
     * @return sd卡总空间
     */
    public static long getAvailaleSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * sd卡总空间
     * 
     * @return sd卡总空间
     */
    public static long getAllSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    /**
     * 得到SD卡的目录
     */
    public static String getFileUtils() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 获取应用根目录
     * 
     * @param con
     *            上下文
     * @return /data/data/com.greenpoint.joycity/
     */
    public static String getPath(Context con) {
        String path = con.getFilesDir().getParent();
        return path + "/";
    }

    /**
     * 获取应用缓存目录
     * 
     * @param con
     *            上下文
     * @return /data/data/com.greenpoint.joycity/JCache/
     */
    public static String getCachePath(Context con) {
        String path = con.getFilesDir().getParent();
        return path + "/Cache/";
    }

    /**
     * 得到SD卡的目录
     * 
     * @return /mnt/sdcard/JOY_CITY/
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory() + File.separator;
    }

    /**
     * 获得圆角图片的方法
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 正则表达式验证
     * 
     * @param regEx
     * @param str
     * @return
     */
    public static boolean regExCheck(String regEx, String str) {
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 分享
     * 
     * @param con
     * @param shareMsg
     */
    public static void share(Context con, String shareMsg) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, shareMsg); // 附带的说明信息
        intent.putExtra(Intent.EXTRA_SUBJECT, "标题");
        intent.setType("text/plain"); // 分享发送到数据类型
        con.startActivity(Intent.createChooser(intent, "分享"));
    }

}
