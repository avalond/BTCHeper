package com.ruitu.btchelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.util.AppUtil;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DialogUtils;
import com.ruitu.btchelper.util.NetworkUtils;

@SuppressLint("SetJavaScriptEnabled")
public class NewsInfoActivity extends BaseActivity implements DataHelper, OnClickListener {
    private static final String TAG = NewsInfoActivity.class.getSimpleName();
    private News mNewsList;
    private TextView title_text;
    private TextView title_content;
    private WebView mWebView;
    private Button back_btn;
    private Button share_btn;
    private ScanWebViewClient mScanWebViewClient;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.newsinfo );
        Bundle bundle = getIntent().getExtras();
        if ( bundle.containsKey( NEWS_SER ) ) {
            Log.e( TAG, "这里执行了" );
            mNewsList = ( News ) bundle.getParcelable( NEWS_SER );
        }
        init();
    }

    @SuppressLint("NewApi")
    private void init(){
        this.back_btn = ( Button ) this.findViewById( R.id.btn_newsinfo_top );
        this.title_text = ( TextView ) this.findViewById( R.id.title_newsinfo_main );
        this.title_content = ( TextView ) this.findViewById( R.id.news_title_newsinfo );
        this.share_btn = ( Button ) this.findViewById( R.id.share_btn_top );
        this.mWebView = ( WebView ) this.findViewById( R.id.wv_newsinfo );
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled( true );
        // 设置可以访问文件
        webSettings.setAllowFileAccess( true );
        // 设置支持缩放
        webSettings.setBuiltInZoomControls( true );
        this.back_btn.setOnClickListener( this );
        this.share_btn.setOnClickListener( this );
    }

    @Override
    protected void onResume(){

        if ( mNewsList != null ) {
            this.title_text.setText( mNewsList.getNews_title() );
            this.title_content.setText( mNewsList.getNews_title() );
            // 加载新闻
            if ( NetworkUtils.isNetworkConnected( this ) ) {
                mScanWebViewClient = new ScanWebViewClient();
                mWebView.setWebViewClient( mScanWebViewClient );
                mWebView.loadUrl( mNewsList.getNews_info_url() );
            } else {
                Toast.makeText( this, "网络连接异常！！", Toast.LENGTH_LONG ).show();
            }
        }
        super.onResume();
    }

    @Override
    public void onClick( View v ){
        switch ( v.getId() ) {
            case R.id.btn_newsinfo_top:
                finish();
                break;
            case R.id.share_btn_top:
                //分享
                AppUtil.share( this,  mNewsList.getNews_info_url());
                break;
            default:
                break;
        }
    }

    private class ScanWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading( WebView view, String url ){
            view.loadUrl( url );
            return true;
        }

        @Override
        public void onReceivedError( WebView view, int errorCode, String description, String failingUrl ){
            super.onReceivedError( view, errorCode, description, failingUrl );
        }

        @Override
        public void onPageStarted( WebView view, String url, Bitmap favicon ){
            super.onPageStarted( view, url, favicon );

        }

        @Override
        public void onPageFinished( WebView view, String url ){
            super.onPageFinished( view, url );
        }

    }
}
