package com.ruitu.btchelper.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class ClientEngine {

    private static final String TAG = ClientEngine.class.getSimpleName();
    private static ClientEngine mInstance;

    private Context mContext;
    private AsyncHttpClient client = new AsyncHttpClient();

    public static synchronized ClientEngine getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ClientEngine(context);
        }
        return mInstance;
    }

    private ClientEngine(Context context) {
        mContext = context;
        client.setTimeout(20 * 1000);
    }

    public void cancelTask(Context context) {
        client.cancelRequests(context, true);
    }

    public synchronized boolean httpGetRequest(BaseRequestPacket packet,
            IRequestContentCallback callback) {
        String url = packet.url;
        if (url.equals("")) {
            Log.e(TAG, "can't get serverURL by action : " + packet.action);
            if (callback != null) {
                callback.onResult(packet.action, false, null, null);
            }
            return false;
        }

        Log.e(TAG, "httpGetRequest url = " + url);
        RequestParams param = null;
        if (packet.object != null) {
            if (packet.object.toStringMap() != null
                    && !packet.object.toStringMap().isEmpty()) {
                param = new RequestParams(packet.object.toStringMap());
            }
        }

        try {
            HttpResponseHandler handler = new HttpResponseHandler(
                    packet.action, callback, packet.extra);
            client.get(packet.context, url, param, handler);
        } catch (Exception e) {
            if (callback != null) {
                callback.onResult(packet.action, false, null, null);
            }
            return false;
        }
        return true;
    }

    public synchronized boolean httpPostRequest(BaseRequestPacket packet,
            IRequestContentCallback callback) {
        String url = packet.url;
        if (url.equals("")) {
            Log.e(TAG, "can't get serverURL by action : " + packet.action);
            return false;
        }

        Log.e(TAG, "httpPostRequestEx url = " + url);
        RequestParams param = null;
        if (packet.object != null) {
            if (packet.object.toStringMap() != null
                    && !packet.object.toStringMap().isEmpty()) {
                param = new RequestParams(packet.object.toStringMap());
            }
        }

        try {
            HttpResponseHandler handler = new HttpResponseHandler(
                    packet.action, callback, packet.extra);
            client.post(packet.context, url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onResult(packet.action, false, null, null);
            }
            return false;
        }

        return true;
    }

}
