package com.ruitu.btchelper.network;
import com.ruitu.btchelper.domain.IToStringMap;

import android.content.Context;



public  class BaseRequestPacket {
    /**
     * 上下文
     */
	public Context context;
	/**
	 * action类型
	 */
	public int action;
	/**
	 * 请求的url
	 */
	public String url;
	
	public IToStringMap object;
	public Object extra;
	public BaseRequestPacket(){
		
	}
}
