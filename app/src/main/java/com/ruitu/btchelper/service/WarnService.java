package com.ruitu.btchelper.service;

import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.MediaPlayerUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("NewApi")
public class WarnService extends Service implements DataHelper {
    private static final String TAG = WarnService.class.getSimpleName();
    private MyBinder myBinder;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        if (mediaPlayer == null)
            this.mediaPlayer = new MediaPlayer();
        if (myBinder == null)
            this.myBinder = new MyBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public WarnService getInstance() {
            return WarnService.this;
        }
    }

    public void checkWarn(Ticker ticker, Context context) {
        String name = ticker.getName();
        String high = ticker.getHigh();
        String low = ticker.getLow();
        double high_d = 0.00;
        double low_d = 0.00;
        if (high != null && !"".equals(high)) {
            high_d = Double.parseDouble(high);
        }
        if (low != null && !"".equals(low)) {
            low_d = Double.parseDouble(low);
        }
        // 获取设置的最低价和最高价
        int high_setting = LocalSharePreference.getHightPrice(context, name);
        int low_setting = LocalSharePreference.getLowPrice(context, name);
        boolean isMon = LocalSharePreference.isMonitorInterval(context,
                ticker.getName());
        boolean isAlarm = LocalSharePreference.isAlarmInterval(context, name);
        boolean isShake = LocalSharePreference.isShakeInterval(context, name);
        if (low_d <= low_setting || high_d >= high_setting) {// 价格报警
            if (isMon) {
                Log.e(TAG, ticker.getName() + ".........................."
                        + ticker.getLast());
                // 发送广播到ui更新ui
                sendBroadcast(ticker);
                // 发出通知
                WarnUtils.sendNotice(context, ticker);
                // 响铃
                if (isAlarm)
                    startAlarm(context);
                // 震动
                if (isShake)
                    WarnUtils.shake(context);
            }
        }
    }

    private void startAlarm(Context context) {
        Log.e(TAG, mediaPlayer.isPlaying() + "<<<<isPlaying");
        if (mediaPlayer.isPlaying()) {
            MediaPlayerUtils.backToBeginMediaPlayer(mediaPlayer);
        } else
            MediaPlayerUtils.resetMediaPlayer(mediaPlayer,
                    LocalSharePreference.getRingtoneUri(context), context);
    }

    public void cancelAlarm(Context context) {
        MediaPlayerUtils.cancelMediaplayer(mediaPlayer);
    }

    private void sendBroadcast(Ticker ticker) {
        Intent intent = new Intent();
        intent.putExtra(WARN_TICKER, ticker);
        intent.setAction(WARN_ACTION);
        sendBroadcast(intent);
    }

}
