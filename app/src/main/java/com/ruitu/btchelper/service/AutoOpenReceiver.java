package com.ruitu.btchelper.service;

import com.ruitu.btchelper.activity.IndexActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoOpenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action="android.intent.action.MAIN";   
        String category="android.intent.category.LAUNCHER";   
        Intent myi=new Intent(context,IndexActivity.class);   
        myi.setAction(action);   
        myi.addCategory(category);   
        myi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        context.startActivity(myi); 
    }

}
