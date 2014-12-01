package com.ruitu.btchelper.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.xml.sax.InputSource;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.activity.HelperActivity;
import com.ruitu.btchelper.activity.LoginActivity;
import com.ruitu.btchelper.util.AppUtil;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.MyApplication;
import com.ruitu.btchelper.weight.MyAlertDialog;

public class UpdateManager implements DataHelper {
    private static final String TAG = UpdateManager.class.getSimpleName();
    private Context mContext;
    private String pastVersion;
    private String nowVersion;
    private String updateMsg = "未发现新版本{1}，请更新！";
    private String isnewMsg = "已经是最新版本\r\n现在的版本是：{1}";
    private MyAlertDialog lastIsNewDialog;
    private MyAlertDialog noticeDialog;
    private Dialog downloadDialog;
    private static final String savePath = Environment
            .getExternalStorageDirectory() + File.separator + "update";// 保存apk的文件夹
    private static final String SaveFileName = savePath + File.separator
            + "BTCHelperUpdate.apk";
    private ProgressBar mProgressBar;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int SDCARD_NOT_EXIST = 3;
    private static final int SDCARD_MEMORY_SMALL = 4;
    private static final int DOWN_CANCEL = 5;
    private static final int DOWN_ERROR = 6;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    // 通知处理刷新界面的handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATE:
                mProgressBar.setProgress(progress);
                break;
            case DOWN_OVER:
                installApk();
                break;
            case SDCARD_NOT_EXIST:
                Toast.makeText(mContext, "手机没有SD卡", Toast.LENGTH_LONG).show();
                break;
            case SDCARD_MEMORY_SMALL:
                Toast.makeText(mContext, "sd卡内容不足", Toast.LENGTH_LONG).show();
                break;
            case DOWN_CANCEL:
                break;
            case DOWN_ERROR:
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }

    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    public boolean checkUpdate() {
        String pastVersion = loadPastVersion();
        nowVersion = getVersionXml(versionUrl);
        if (pastVersion != null && nowVersion != null
                && !"".equals(pastVersion) && !pastVersion.equals(nowVersion)) {
            return true;
        } else {
            return false;
        }
    }

    public String loadPastVersion() {
        return AppUtil.getAppVersionName(mContext);
    }

    public boolean hasInstall() {
        if (loadPastVersion() != null)
            return true;
        else
            return false;
    }

    public boolean deleteUpdateFile() {
        try {
            File file = new File(SaveFileName);
            if (file.exists()) {
                file.delete();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private String getVersionXml(String versionUrl2) {
        String db = null;
        URL url = null;
        InputSource is = null;
        try {
            if (versionUrl2 == null || "".equals(versionUrl2)) {
                return null;
            }
            url = new URL(versionUrl2);
            is = new InputSource(url.openStream());
            is.setEncoding("UTF-8");
            db = SAXVersionService.readRssXml(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return db;
    }

    // 显示更新程序对话框，供主程序调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    public void showLastIsNewDilog() {
        lastIsNewDialog = MyAlertDialog.getInstance(mContext);
        lastIsNewDialog.setMessage(isnewMsg.replace("{1}", nowVersion));
        lastIsNewDialog.setNegativeButton(
                mContext.getResources().getString(R.string.ok_text),
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastIsNewDialog.dismiss();
                    }
                });
    }

    private void showNoticeDialog() {
        noticeDialog = MyAlertDialog.getInstance(mContext);
        noticeDialog.setTitle(R.string.update_text1);
        if (nowVersion != null) {
            noticeDialog.setMessage(updateMsg.replace("{1}", nowVersion));
        }
        noticeDialog.setPositiveButton(
                mContext.getResources().getString(R.string.download_text),
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                        showDownloadDialog();
                    }
                });
        noticeDialog.setNegativeButton(
                mContext.getResources().getString(R.string.cancel_text),
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                    }
                });
    }

    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.update_text1);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_progress, null);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
        builder.setNegativeButton(R.string.cancel_text,
                new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final MyAlertDialog my = MyAlertDialog
                                .getInstance(mContext);
                        my.setTitle(R.string.not_text);
                        my.setMessage(R.string.cancel_download_text);
                        my.setPositiveButton(
                                mContext.getResources().getString(
                                        R.string.ok_text),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        mHandler.sendEmptyMessage(DOWN_CANCEL);
                                        my.dismiss();
                                        downloadDialog.dismiss();
                                        interceptFlag = true;
                                    }
                                });
                        my.setNegativeButton(
                                mContext.getResources().getString(
                                        R.string.cancel_text),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        my.dismiss();
                                    }
                                });
                    }
                });
        downloadDialog = builder.create();
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadApk();
    }

    private void downloadApk() {
        if (AppUtil.isSdCardExist()) {
            interceptFlag = false;
            downLoadThread = new Thread(mdownApkRunnable);
            downLoadThread.start();
        } else {
            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    private void installApk() {
        // GetBroadcast.registerReceiver(mContext.getApplicationContext());
        File apkFile = new File(SaveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    public static class GetBroadcast extends BroadcastReceiver {
        private static GetBroadcast mReceiver = new GetBroadcast();
        private static IntentFilter mIntentFilter;

        public static void registerReceiver(Context context) {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addDataScheme("package");
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
            context.registerReceiver(mReceiver, mIntentFilter);
        }

        public static void unregisterReceiver(Context context) {
            context.unregisterReceiver(mReceiver);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String packageName = intent.getData().getSchemeSpecificPart();
            if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
                PackageManager pm = context.getPackageManager();
                Intent intent1 = new Intent();
                intent1 = pm.getLaunchIntentForPackage(packageName);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                PackageManager pm = context.getPackageManager();
                Intent intent1 = new Intent();
                intent1 = pm.getLaunchIntentForPackage(packageName);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
                PackageManager pm = context.getPackageManager();
                Intent intent1 = new Intent();
                intent1 = pm.getLaunchIntentForPackage(packageName);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else if (Intent.ACTION_PACKAGE_INSTALL.equals(action)) {
                PackageManager pm = context.getPackageManager();
                Intent intent1 = new Intent();
                intent1 = pm.getLaunchIntentForPackage(packageName);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        }
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url = null;
            FileOutputStream outsStream = null;
            InputStream ins = null;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                if (AppUtil.getAvailaleSize() <= length) {
                    mHandler.sendEmptyMessage(SDCARD_MEMORY_SMALL);
                } else {
                    ins = conn.getInputStream();
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    Log.e(TAG, file.toString());
                    String apkFile = SaveFileName;
                    File ApkFile = new File(apkFile);
                    Log.e(TAG, "ApkFile" + apkFile);
                    outsStream = new FileOutputStream(ApkFile);
                    int count = 0;
                    byte[] buffer = new byte[1024 * 8];
                    do {
                        int numread = ins.read(buffer);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        outsStream.write(buffer, 0, numread);
                    } while (!interceptFlag);

                    if (interceptFlag) {
                        mHandler.sendEmptyMessage(DOWN_CANCEL);
                    }
                }
            } catch (Exception e) {
                mHandler.sendMessage(mHandler.obtainMessage(DOWN_ERROR,
                        e.getMessage()));
            } finally {
                if (outsStream != null) {
                    try {
                        outsStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ins != null) {
                    try {
                        ins.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };
}
