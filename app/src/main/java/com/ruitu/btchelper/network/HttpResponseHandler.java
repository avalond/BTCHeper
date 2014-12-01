package com.ruitu.btchelper.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.util.Log;

public class HttpResponseHandler extends AsyncHttpResponseHandler {

    private static final String TAG = HttpResponseHandler.class.getSimpleName();
    private int mAction = 0;
    private IRequestContentCallback mContentCallback;

    private Object mExtra;

    public HttpResponseHandler(int action, IRequestContentCallback callback2,
            Object extra) {
        mAction = action;
        mContentCallback = callback2;
        mExtra = extra;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "mAction = " + mAction + ", onStart!");
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Log.e(TAG, "mAction = " + mAction + ", onFinish!");
    }

    @Override
    public void onFailure(Throwable error, String content) {
        Log.e(TAG,
                "mAction = " + mAction + ", onFailure! error = "
                        + error.getMessage() + "\ncontent = " + content);
        if (mContentCallback == null) {
            return;
        }
        mContentCallback.onResult(mAction, false, content, mExtra);
    }

    // @Override
    // public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable
    // arg3) {
    // if (mContentCallback == null) {
    // return;
    // }
    // String content = null;
    // if (arg2 != null) {
    // try {
    // content = new String(arg2, "UTF-8");
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // }
    // }
    // mContentCallback.onResult(mAction, false, content, mExtra);
    // }

    // @Override
    // public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
    // if (mContentCallback == null) {
    // return;
    // }
    // String content = null;
    // if (arg2 != null) {
    // try {
    // content = new String(arg2, "UTF-8");
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // }
    // }
    // mContentCallback.onResult(mAction, true, content, mExtra);
    // }
    @Override
    public void onSuccess(int statusCode, String content) {
        if (mContentCallback == null) {
            return;
        }
        mContentCallback.onResult(mAction, true, content, mExtra);
    }

}
