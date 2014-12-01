package com.ruitu.btchelper.util;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.ruitu.btchelper.domain.News;

public class AsyncImageLoader {
    private static final String TAG = AsyncImageLoader.class.getSimpleName();
    AsyncHttpClient client;

    public AsyncImageLoader() {
        client = new AsyncHttpClient();
        client.setTimeout(20 * 2000);
    }

    public void loadDrawable(final String imageUrl, final ImageView v) {
        if (imageUrl == null || "".equals(imageUrl)) {
            return;
        }
        try {
            client.get(imageUrl, new BinaryHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.d("onStart", "onStart");
                }

                @Override
                public void onFinish() {
                    Log.d("finish", "finish");
                }

                @Override
                public void onFailure(Throwable arg0, byte[] arg2) {
                    Log.d("failure", "failure");
                    return;
                }

                @Override
                public void onSuccess(int arg0, byte[] arg2) {
                    try {
                        if (arg2 != null) {
                            final ByteArrayInputStream is = new ByteArrayInputStream(
                                    arg2);
                            if (is != null) {
                                Drawable img = Drawable.createFromStream(is,
                                        imageUrl);
                                if (v != null)
                                    v.setImageDrawable(img);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void loadNewsDrawable(final ImageView v, final List<News> newsList,
            final int position) {
        try {
            final String imageUrl = newsList.get(position).getNews_icon();
            Log.e(TAG, imageUrl);
            client.get(imageUrl, new BinaryHttpResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onFailure(Throwable arg0, byte[] arg2) {
                    return;
                }

                @Override
                public void onSuccess(int arg0, byte[] arg2) {
                    Log.d("finish", "onSuccess");
                    if (arg2 != null) {
                        final ByteArrayInputStream is = new ByteArrayInputStream(
                                arg2);
                        if (is != null) {
                            Log.e(TAG, "这里执行了");
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            // Drawable img = Drawable.createFromStream(is,
                            // imageUrl);
                            newsList.get(position)
                                    .setNews_icon_drawable(bitmap);
                            if (v != null)
                                v.setImageBitmap(bitmap);
                        }
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
