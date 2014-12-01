package com.ruitu.btchelper.util;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ruitu.btchelper.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImgUtil {
    public static final String TAG = ImgUtil.class.getSimpleName();

    public static final int BASE_SIZE = 160;

    public static Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只计算几何尺寸，不返回bitmap，不占内存。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int w = options.outWidth;
        int h = options.outHeight;
        //  Log.i(TAG, "--img src,w:" + w + " h:" + h);
        int min = w < h ? w : h;

        int rate = min / BASE_SIZE;
        if (rate <= 0) {
            rate = 1;
        }
        options.inSampleSize = rate;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
      //  Log.i(TAG,"--img dst,w:" + bitmap.getWidth() + " h:" + bitmap.getHeight());
        return bitmap;
    }

    public static String getAlbumImagePath(Context context, Uri uri) {
        String path = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public static ImageLoaderConfiguration getImageConfig(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                        // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        return config;
    }

    public static Bitmap showImage(ImageLoader mImageLoader, String uri,
                                   final ProgressBar pb, final ImageView store_img) {
        Bitmap bit = null;
        mImageLoader.displayImage(uri, store_img, ImgUtil.getDisplayOptions(),
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        store_img.setImageResource(R.drawable.progress_1);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        store_img.setImageResource(R.drawable.progress_2);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        store_img.setImageBitmap(loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        store_img.setImageResource(R.drawable.progress_3);
                    }
                });
        return bit;
    }

    public static DisplayImageOptions getDisplayOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoading(R.drawable.progress_2).build();
        return options;
    }
}
