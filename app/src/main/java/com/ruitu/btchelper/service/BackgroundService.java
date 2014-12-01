package com.ruitu.btchelper.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.MyApplication;
import com.ruitu.btchelper.util.NetworkUtils;
import com.ruitu.btchelper.util.NotificationUtils;
import com.ruitu.btchelper.weight.MyAlertDialog;

@SuppressLint("HandlerLeak")
public class BackgroundService extends Service implements IToStringMap,
        IRequestContentCallback {
    private static final String TAG = BackgroundService.class.getSimpleName();
    private MyApplication myApplication;
    private boolean isNeedFlush = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer timer;
    private MyBackTimerTask myBackTimerTask;
    private Context mcontext;
    private List<String> platforms;

    @Override
    public void onCreate() {
        Log.e(TAG, "后台运行开启");
        if ( mcontext == null )
            mcontext = getApplicationContext();
        Log.e(TAG, "BackgroundService onCreate...");
        platforms = LocalSharePreference.getDefaultPlatnameInterval(mcontext);
        myApplication = (MyApplication) mcontext.getApplicationContext();
        // 开启定时任务
        if ( timer == null )
            timer = new Timer();
        if ( myBackTimerTask == null )
            myBackTimerTask = new MyBackTimerTask();
        timer.schedule(myBackTimerTask, 0,
                LocalSharePreference.getMarketInterval(mcontext));
        super.onCreate();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch ( msg.what ) {
                case DataHelper.TICKER_ACTION:
                    Log.e(TAG, "请求更新数据");
                    // 请求服务器
                    if ( NetworkUtils.isNetworkConnected(mcontext) ) {
                        try {
                            if ( NetworkUtils.isWIFIConnected(mcontext) ) {
                                myApplication.isNotWifiReflush = false;
                                isNeedFlush = true;
                                requestTickerData();
                            } else {
                                if ( !myApplication.isNotWifiReflush && isNeedFlush ) {
                                    popDialog();
                                } else {
                                    requestTickerData();
                                }
                            }
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(mcontext,mcontext.getResources().getString(R.string.no_net_for_service),Toast.LENGTH_LONG).show();
                    }
                    break;

                default:
                    break;
            }
        }

    };

    private void popDialog() {
        Builder builder = new Builder(this);
        builder.setMessage(R.string.not_wifi_warn_text);
        builder.setNegativeButton(R.string.no_text, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isNeedFlush = false;
                myApplication.isNotWifiReflush = true;
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.yes_text, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isNeedFlush = true;
                myApplication.isNotWifiReflush = true;
                requestTickerData();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setType(
                (WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        new Thread() {
            public void run() {
                SystemClock.sleep(4000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            ;
        }.start();
    }

    private class MyBackTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(DataHelper.TICKER_ACTION);
        }

    }

    private void requestTickerData() {
        if ( isNeedFlush ) {
            isNeedFlush = false;
          //  Log.e(TAG, "执行向服务器请求信息");
            ClientEngine clientEngine = ClientEngine.getInstance(mcontext);
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = DataHelper.TICKER_ACTION;
            packet.url = DataHelper.TICKER_URL;
            packet.extra = toStringMap();
            packet.context = mcontext;
            packet.object = this;
            clientEngine.httpGetRequest(packet, this);
        }
    }

    @Override
    public void onDestroy() {
        myBackTimerTask.cancel();
        timer.cancel();
        timer = null;
        super.onDestroy();
    }

    @Override
    public void onResult(int requestAction, Boolean isSuccess, String content,
                         Object extra) {

        switch ( requestAction ) {
            case DataHelper.TICKER_ACTION:
                if ( isSuccess ) {
                    if ( content != null ) {
                        // 解析json字符串
                        try {
                            List<Ticker> tickers = ParseJson.parseTickers(content);
                            if ( tickers != null && !tickers.isEmpty() ) {
                                boolean isWarn = false;
                                for ( Ticker ticker : tickers ) {
                                    isWarn = checkWarn(ticker, mcontext);
                                }
                                if ( !isWarn ) {
                                    Ticker ticker = tickers.get(0);
                                    if ( ticker != null )
                                        WarnUtils.sendNotice(mcontext, ticker);
                                }
                            }
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }

    }

    private boolean checkWarn(Ticker ticker, Context mcontext2) {

        String name = ticker.getName();
        String high = ticker.getHigh();
        String low = ticker.getLow();
        double high_d = 0.00;
        double low_d = 0.00;
        if ( high != null && !"".equals(high) ) {
            high_d = Double.parseDouble(high);
        }
        if ( low != null && !"".equals(low) ) {
            low_d = Double.parseDouble(low);
        }
        // 获取设置的最低价和最高价
        int high_setting = LocalSharePreference.getHightPrice(mcontext, name);
        int low_setting = LocalSharePreference.getLowPrice(mcontext, name);
        boolean isMon = LocalSharePreference.isMonitorInterval(mcontext,
                ticker.getName());
        boolean isAlarm = LocalSharePreference.isAlarmInterval(mcontext,
                ticker.getName());
        boolean isShake = LocalSharePreference.isShakeInterval(mcontext,
                ticker.getName());
        boolean isBackground = LocalSharePreference.isBackground(mcontext);
        if ( low_d <= low_setting || high_d >= high_setting ) {// 价格报警
            if ( isMon || isAlarm || isShake ) {
                // 发出通知
                WarnUtils.sendNotice(mcontext, ticker);
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, String> toStringMap() {
        Map<String, String> map = new HashMap<String, String>();
        StringBuffer sb = new StringBuffer();
        if ( platforms.isEmpty() ) {
            return null;
        } else {
            for ( String platform : platforms ) {
                sb.append(platform + "|");
            }
            sb.deleteCharAt(sb.length() - 1);
            map.put("platforms", sb.toString());
        }
        return map;
    }
}
