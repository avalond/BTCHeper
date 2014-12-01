package com.ruitu.btchelper.activity;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

public abstract class BaseActivity extends Activity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("BaseActivity", "这里执行了。。。。。。。。。。。");
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
