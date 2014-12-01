package com.ruitu.btchelper.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruitu.btchelper.R;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.setting.UpdateManager;
import com.ruitu.btchelper.util.AppUtil;
import com.ruitu.btchelper.util.DataHelper;

public class AboutActivity extends BaseActivity implements OnClickListener {
    private Button back_btn;
    private TextView version;
    private TextView readme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        init();
        new ReadmeTask().execute();
    }

    private class ReadmeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return readFile();
        }

        private String readFile() {
            InputStream is = null;
            ByteArrayOutputStream bos = null;
            try {
                is = AboutActivity.this.getAssets().open("readme.txt");
                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                return bos.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                readme.setText(result);
            }
        }
    }

    private void init() {
        this.back_btn = (Button) this.findViewById(R.id.btn_about_top);
        this.back_btn.setOnClickListener(this);
        this.version = (TextView) this.findViewById(R.id.version_text_about);
        this.readme = (TextView) this.findViewById(R.id.readme_text_about);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String version = AppUtil.getAppVersionName(this);
        this.version.setText("当前版本：" + version);
        TextView t = (TextView) findViewById(R.id.netaddress_text_about);
        t.setText(Html.fromHtml(

        "<a href=\"" + DataHelper.netword + "\">官方网站：" + DataHelper.netword
                + "</a>"));
        t.setMovementMethod(LinkMovementMethod.getInstance());
        
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_about_top:
            this.finish();
            break;
        default:
            break;
        }
    }
}
