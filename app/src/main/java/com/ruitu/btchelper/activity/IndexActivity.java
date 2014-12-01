package com.ruitu.btchelper.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.service.BackgroundService;
import com.ruitu.btchelper.service.ImageService;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.setting.UpdateManager;
import com.ruitu.btchelper.util.AppUtil;
import com.ruitu.btchelper.util.DataHelper;
/**
 * 此为欢迎界面的activity
 */
public class IndexActivity extends Activity implements DataHelper {
    private static final String TAG = IndexActivity.class.getSimpleName();
    private boolean isNetWork;
    private boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_index);
        // 初始化控件
        init();
        // 判断登录是否为第一次
        isFirst();
        // 写入默认的系统设置
        writeSystemSetting();
    }

    /**
	 * 
	 */
    private void init() {
        if (new UpdateManager(this).deleteUpdateFile()) {
            Log.e(TAG, "更新文件删除成功！！");
        }
    }

    /**
     * 初始化一些默认的系统设置
     */
    private void writeSystemSetting() {
        if (isFirstStart) {
            Log.e(TAG, "write执行了");
            Intent intent = new Intent(this, ImageService.class);
            startService(intent);
            // 数据刷新时间
            LocalSharePreference.commintMarketInterval(this, 60000);
            LocalSharePreference.commintDetailInterval(this, 60000);
            List<String> platnameList = new ArrayList<String>();
            //添加平台的，第一次
            platnameList.add(BITSTAMP);//Bitstamp
            platnameList.add(BTCTRADE);//btcTrade
            platnameList.add(BTC_CHINA);//比特币中国
            platnameList.add(BITER); //比特儿
            platnameList.add(HUOBI);//火币网
            platnameList.add( OKCOIN );//OKCoin
            platnameList.add( FERA796 );//796期货
            /*比特币时代---->>会出现没有数据的情况*/
            platnameList.add( BTC_38 );//比特币时代
            LocalSharePreference.commitDefaultPlatform(this, platnameList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 检测网络
        new StartTask().execute();
    }

    /**
     * 判断登录是否为第一次
     */
    private void isFirst() {
        isFirstStart = LocalSharePreference.isFirstLogin(this);
    }

    /**
     * 检测网络的异步任务
     */
    private class StartTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // 获取网络连接管理器
                ConnectivityManager connectivityManager = (ConnectivityManager) IndexActivity.this
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (null == connectivityManager) {
                    isNetWork = false;
                } else {
                    // 获取所有的网络连接
                    NetworkInfo[] networks = connectivityManager
                            .getAllNetworkInfo();
                    // 遍历所有的网络连接，只要一个打开即可
                    for (NetworkInfo network : networks) {
                        if (network.getState() == NetworkInfo.State.CONNECTED) {
                            isNetWork = true;
                        }
                    }
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isNetWork;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
            startActivity(intent);
            IndexActivity.this.finish();
        }
    }

    @Override
    protected void onPause() {
        LocalSharePreference.commitFirstLogin(this, false);
        super.onPause();
    }
}
