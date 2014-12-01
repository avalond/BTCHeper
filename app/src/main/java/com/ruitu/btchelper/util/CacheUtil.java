package com.ruitu.btchelper.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 缓存工具类
 *
 * @author Administrator
 *         <p/>
 *         下午1:51:27
 */
public class CacheUtil {
    private static final String TAG = CacheUtil.class.getName();
    public static final long B = 1;
    public static final long KB = B * 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;
    private static final int BUFFER = 1024;

    /**
     * 1秒超时时间
     */
    public static final int CONFIG_CACHE_SHORT_TIMEOUT = 1000 * 60 * 5; // 5 分钟

    /**
     * 5分钟超时时间
     */
    public static final int CONFIG_CACHE_MEDIUM_TIMEOUT = 1000 * 3600 * 2; // 2小时

    /**
     * 中长缓存时间
     */
    public static final int CONFIG_CACHE_ML_TIMEOUT = 1000 * 60 * 60 * 24 * 1; // 1天

    /**
     * 最大缓存时间
     */
    public static final int CONFIG_CACHE_MAX_TIMEOUT = 1000 * 60 * 60 * 24 * 7; // 7天

    /**
     * CONFIG_CACHE_MODEL_LONG : 长时间(7天)缓存模式 <br>
     * CONFIG_CACHE_MODEL_ML : 中长时间(12小时)缓存模式<br>
     * CONFIG_CACHE_MODEL_MEDIUM: 中等时间(2小时)缓存模式 <br>
     * CONFIG_CACHE_MODEL_SHORT : 短时间(5分钟)缓存模式
     */
    public enum ConfigCacheModel {
        CONFIG_CACHE_MODEL_SHORT, CONFIG_CACHE_MODEL_MEDIUM, CONFIG_CACHE_MODEL_ML, CONFIG_CACHE_MODEL_LONG;
    }

    /**
     * 获取缓存
     *
     * @param url 访问网络的URL
     *
     * @return 缓存数据
     *
     * @throws Exception
     */
    public static String getNoticeCacheStr() throws Exception{
        String result = null;
        String path = DataHelper.ENVIROMENT_DIR_CACHE + "notice.txt";
        File file = new File( path );
        if ( file.exists() && file.isFile() ) {
            try {
                Log.e( TAG, "getUrlCacheStr的readTextFile执行了" );
                result = FileUtils.readTextFile( file );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object getUrlCacheObject( String url, ConfigCacheModel model ) throws Exception{
        Log.e( TAG, "获取新闻信息缓存" );
        if ( url == null ) {
            return null;
        }

        Object result = null;
        String path = DataHelper.ENVIROMENT_DIR_CACHE + StringUtils.replaceFileName( MD5Utils.getMD5Str( url ) );
        File file = new File( path );
        if ( file.exists() && file.isFile() ) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            Log.d( TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min" );
            // 1。如果系统时间是不正确的
            // 2。当网络是无效的,你只能读缓存
            if ( NetworkUtils.isNetworkConnected( new MyApplication().getBaseContext() ) ) {
                if ( expiredTime < 0 ) {
                    return null;
                }
                if ( model == ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT ) {
                    if ( expiredTime > CONFIG_CACHE_SHORT_TIMEOUT ) {
                        return null;
                    }
                } else if ( model == ConfigCacheModel.CONFIG_CACHE_MODEL_MEDIUM ) {
                    if ( expiredTime > CONFIG_CACHE_MEDIUM_TIMEOUT ) {
                        return null;
                    }
                } else if ( model == ConfigCacheModel.CONFIG_CACHE_MODEL_ML ) {
                    if ( expiredTime > CONFIG_CACHE_ML_TIMEOUT ) {
                        return null;
                    }
                } else if ( model == ConfigCacheModel.CONFIG_CACHE_MODEL_LONG ) {
                    if ( expiredTime > CONFIG_CACHE_MEDIUM_TIMEOUT ) {
                        return null;
                    }
                } else {
                    if ( expiredTime > CONFIG_CACHE_MAX_TIMEOUT ) {
                        return null;
                    }
                }
            }
            result = FileUtils.readObjectFile( file );
        }
        return result;
    }

    /**
     * 设置缓存
     *
     * @param data
     * @param url
     *
     * @throws Exception
     */
    public static void setNoticeStrCache( String data ) throws Exception{
        File dir = new File( DataHelper.ENVIROMENT_DIR_CACHE );
        if ( ! dir.exists() && Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED ) ) {
            dir.mkdirs();
        }
        File file = new File( DataHelper.ENVIROMENT_DIR_CACHE + "notice.txt" );
        try {
            // 创建缓存数据到磁盘，就是创建文件
            FileUtils.writeTextFile( file, data );
        } catch ( IOException e ) {
            Log.d( TAG, "write " + file.getAbsolutePath() + " data failed!" );
            e.printStackTrace();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void setUrlObjectCache( Object data, String url ) throws Exception{
        Log.e( TAG, "设置新闻信息缓存" );
        if ( DataHelper.ENVIROMENT_DIR_CACHE == null ) {
            return;
        }
        File dir = new File( DataHelper.ENVIROMENT_DIR_CACHE );
        if ( ! dir.exists() && Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED ) ) {
            dir.mkdirs();
        }
        File file = new File( DataHelper.ENVIROMENT_DIR_CACHE + StringUtils.replaceFileName( MD5Utils.getMD5Str( url ) ) );
        // 创建缓存数据到磁盘，就是创建文件
        FileUtils.writeObjectToFile( file, data );
    }

    public static Bitmap getBitmapCache( String url ) throws Exception{
        Log.e( TAG, "获取图片信息缓存" );
        if ( DataHelper.ENVIROMENT_DIR_CACHE == null ) {
            return null;
        }
        ByteArrayOutputStream data = null;
        FileInputStream fis = null;
        Bitmap bitmap = null;
        String path = DataHelper.ENVIROMENT_DIR_CACHE + StringUtils.replaceFileNameAndPrex( MD5Utils.getMD5Str( url ) ) + url.substring( url.lastIndexOf( "." ) );
        File file = new File( path );
        if ( file.exists() && file.isFile() ) {
            try {

                fis = new FileInputStream( file );
                data = new ByteArrayOutputStream();
                int len = 0;
                byte[] buffer = new byte[ BUFFER ];
                while ( ( len = fis.read( buffer ) ) != - 1 ) {
                    data.write( buffer, 0, len );
                }
                bitmap = BitmapFactory.decodeByteArray( data.toByteArray(), 0, data.size() );
                return bitmap;
            } catch ( Exception e ) {
            } finally {
                if ( data != null ) {
                    data.close();
                }
                if ( fis != null ) {
                    fis.close();
                }
            }
        }
        return null;
    }

    /**
     * 将Bitmap保存本地JPG图片
     *
     * @param imageUrl
     * @param is
     *
     * @return
     *
     * @throws Exception
     */
    public static String saveBitmap2File( String icon, Bitmap loadedImage ) throws Exception{

        FileOutputStream fos = null;
        File dir = new File( DataHelper.ENVIROMENT_DIR_CACHE );
        if ( ! dir.exists() && Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED ) ) {
            dir.mkdirs();
        }
        File targetFile = new File( DataHelper.ENVIROMENT_DIR_CACHE + StringUtils.replaceFileNameAndPrex( MD5Utils.getMD5Str( icon ) ) + icon.substring( icon.lastIndexOf( "." ) ) );
        if ( targetFile.exists() ) {
            return null;
        }
        try {
            fos = new FileOutputStream( targetFile );
            loadedImage.compress( Bitmap.CompressFormat.PNG, 90, fos );
            fos.flush();
            return targetFile.getPath();
        } catch ( Exception e ) {

        } finally {
            if ( fos != null ) {
                fos.close();
            }
        }
        return targetFile.getPath();
    }

    /**
     * 删除历史缓存文件
     *
     * @param cacheFile
     */
    public static void clearCache( File cacheFile ){
        if ( cacheFile == null ) {
            if ( Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED ) ) {
                try {
                    File cacheDir = new File( Environment.getExternalStorageDirectory().getPath() + "/btchelper/cache/" );
                    if ( cacheDir.exists() ) {
                        clearCache( cacheDir );
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        } else if ( cacheFile.isFile() ) {
            cacheFile.delete();
        } else if ( cacheFile.isDirectory() ) {
            File[] childFiles = cacheFile.listFiles();
            for ( int i = 0; i < childFiles.length; i++ ) {
                clearCache( childFiles[ i ] );
            }
        }
    }
}
