package com.ruitu.btchelper.network;

import java.util.List;

import android.util.Log;

import com.loopj.android.http.RequestParams;

public class RequestParamHelper {
    private static final String TAG = RequestParamHelper.class.getSimpleName();

    /**
     * 实时详情界面被选中的平台参数
     * 
     * @param platforms
     * @return
     */
    public static RequestParams getPlatformsParams(List<String> platforms) {
        if (platforms.isEmpty()) {
            return null;
        }
        RequestParams requestParams = new RequestParams();
        StringBuffer sb = new StringBuffer();
        for (String platform : platforms) {
            sb.append(platform + "|");
        }
        sb.deleteCharAt(sb.length() - 1);
        requestParams.put("platforms", sb.toString());
        Log.e(TAG, requestParams.toString());
        return requestParams;
    }

    /**
     * 市场深度界面被选中的平台参数
     */
    public static RequestParams getPlarformParms(String platform) {
        if (platform == null)
            return null;
        RequestParams requestParams = new RequestParams();
        requestParams.put("platform", platform);
        Log.e(TAG, requestParams.toString());
        return requestParams;
    }
}
