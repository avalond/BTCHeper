package com.ruitu.btchelper.service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.util.CacheUtil;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.ImgUtil;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class ImageService extends Service implements IRequestContentCallback,
        DataHelper {
    private static final String TAG = ImageService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadNewsData();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 请求广告数据
     */
    private void loadNewsData() {
        ClientEngine clientEngine = ClientEngine.getInstance(this);
        BaseRequestPacket packet = new BaseRequestPacket();
        packet.action = NOTICE_ACTION_INDEX;
        packet.url = NEWS_URL.replace("{1}", String.valueOf(7));
        packet.context = this;
        clientEngine.httpGetRequest(packet, this);
    }


    private void updateNewsListData(final String content) {
        Log.e(TAG, content);
        if (content != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CacheUtil.setNoticeStrCache(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Map<String, Object> map = ParseJson.parseNewsFromNet(content);
            List<News> list = (List<News>) map.get(NEWS_LIST);
            if (list != null && !list.isEmpty() && list.size() >= 9) {
                for (News news : list) {
                    if (news == null)
                        return;
                    final String icon = news.getNews_icon();
                    if (icon != null && !"".equals(icon)) {
                        mImageLoader.loadImage(icon,
                                ImgUtil.getDisplayOptions(),
                                new ImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(
                                            String imageUri, View view) {

                                    }

                                    @Override
                                    public void onLoadingFailed(
                                            String imageUri, View view,
                                            FailReason failReason) {

                                    }

                                    @Override
                                    public void onLoadingComplete(
                                            String imageUri, View view,
                                            final Bitmap loadedImage) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    CacheUtil.saveBitmap2File(
                                                            icon, loadedImage);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }

                                    @Override
                                    public void onLoadingCancelled(
                                            String imageUri, View view) {

                                    }
                                });
                    }
                }
            }
        }
    }

    // }

    public static Bitmap getBitmap(String icon) {

        return null;
    }

    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImgUtil.getImageConfig(this));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResult(int requestAction, Boolean isSuccess, String content,
            Object extra) {
        switch (requestAction) {
        case NOTICE_ACTION_INDEX:
            if (isSuccess) {
                updateNewsListData(content);
            }
            break;

        default:
            break;
        }

    }
}
