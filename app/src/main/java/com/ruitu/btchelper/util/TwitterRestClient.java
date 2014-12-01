package com.ruitu.btchelper.util;

import java.util.Map;

import android.util.Log;

import com.loopj.android.http.*;
/**
 * 这个类用来发送异步请求
 * @author Administrator
 */
public class TwitterRestClient {
    private static final String TAG = TwitterRestClient.class.getSimpleName();
    /**
     * 基本的请求URL
     */
//    private static final String BASE_URL = Helper.BASE_URL;  
    /**
     * 创建静态的异步httpclient对象
     */
    private static AsyncHttpClient client = new AsyncHttpClient();  
    /**
     * 发送get请求
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {  
        client.get(url, params, responseHandler);  
    }  
    /**
     * 发送post请求
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {  
        client.post(url, params, responseHandler);  
    }  
    /**
     * @param relativeUrl
     * @return
     */
//    private static String getAbsoluteUrl(String relativeUrl) {  
//        return BASE_URL + relativeUrl;  
//    } 
    /**
     * 组装参数
     * @param params
     * @return
     */
    public static RequestParams getRequestParams(Map<String,String> params){
        RequestParams requestParams = new RequestParams(params);
        return requestParams;
    }
}
