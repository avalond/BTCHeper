package com.ruitu.btchelper.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ruitu.btchelper.adapter.MyPagerAdapter;
import com.ruitu.btchelper.adapter.OrdersAdapter;
import com.ruitu.btchelper.adapter.TransactionAdapter;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.domain.Transaction;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DialogUtils;
import com.ruitu.btchelper.util.NetworkUtils;
import com.ruitu.btchelper.weight.RefreshListView;
import com.ruitu.btchelper.weight.RefreshListView.IOnLoadMoreListener;
import com.ruitu.btchelper.weight.RefreshListView.IOnRefreshListener;
import com.ruitu.btchelper.weight.ViewPagerCompat;
import com.ruitu.btchelper.weight.spinner.AbstractSpinerAdapter;
import com.ruitu.btchelper.weight.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.ruitu.btchelper.weight.spinner.NormalSpinerAdapter;
import com.ruitu.btchelper.weight.spinner.SpinerPopWindow;
import com.ruitu.btchelper.R;

public class DepthFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OnPageChangeListener, IRequestContentCallback, IToStringMap, DataHelper {
    public static DepthFragment getInstance(){
        return new DepthFragment();
    }

    private View mView;
    private RadioGroup mRadioGroup;
    private LinearLayout mLinearLayout_fragment2_main;
    private TextView mPlatform_fragment2_main;
    private ImageButton mPlatform_frament2_iv;
    private ViewPager mPager;
  //  private ProgressBar mProgressBar;
    private String platform_name;
    private String platform_name_order;
    private int page = 1;
    private boolean hasNext = false;
    // 第一个pager的元素

    private LinearLayout mLinearLayout_progress_page1;
    private SwipeRefreshLayout mLinearLayout_text_page1;
    private ListView mListView_page1;
    // 第二个pager的元素
    private LinearLayout mLinearLayout_progress_page2;
    private SwipeRefreshLayout mLinearLayout_text_page2;
    private ListView mListView_page2;
    // 第三个pager的元素
    private LinearLayout mLinearLayout_text_page3;
    private WebView mwb_page3;
    private static final String TAG = DepthFragment.class.getSimpleName();
    private List< Orders > m_orders = new ArrayList< Orders >();
    private List< Transaction > m_transactions = new ArrayList< Transaction >();
    private OrdersAdapter mOrdersAdapter;
    private TransactionAdapter mTransactionAdapter;
    // k线图加载成功标志
    private ScanWebViewClient mWeiboWebViewClient;
    private long update_time;
    private Timer mTimer;
    private MyTimerTask orderTimerTask;
    private MyTimerTask transactionTimerTask;
    private MyTimerTask kLineTimerTask;
    private Button goto_teu;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ){
            switch ( msg.what ) {
                case DEPTH_ORDER_ACTION:
                    updateOrderData();
                    break;
                case DEPTH_TRANSACTIONS_ACTION:
                    updateTransactionsData();
                    break;
                case DEPTH_KLINE_ACTION:
                    updateKLineData();
                    break;
            }
        }
    };

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        Log.e( TAG, "onCreateView执行了" );
        mView = inflater.inflate( R.layout.fragment2_main, container, false );
        init();
        return mView;
    }

    @Override
    public void onAttach( Activity activity ){
        Log.e( TAG, "onAttach执行了" );
        super.onAttach( activity );
    }

    @Override
    public void onStart(){
        Log.e( TAG, "onStart执行了" );
        super.onStart();
    }

    @Override
    public void onResume(){
        Log.e( TAG, "onResume执行了" );
        mPager.setCurrentItem( 0 );
        update_time = LocalSharePreference.getDetailInterval( getActivity() );
        mTimer = new Timer();
        orderTimerTask = new MyTimerTask( DEPTH_ORDER_ACTION );
        if ( mTimer == null || orderTimerTask == null )
            return;
        mTimer.schedule( orderTimerTask, 0, update_time );
        super.onResume();
    }

    private class MyTimerTask extends TimerTask {
        private int type;

        public MyTimerTask( int actionType ){
            this.type = actionType;
        }

        @Override
        public void run(){
            Message message = new Message();
            message.what = type;
            mHandler.sendMessage( message );
        }
    }

    @Override
    public void onPause(){
        if ( mTimer != null )
            mTimer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void init(){
        try {
            mRadioGroup = ( RadioGroup ) mView.findViewById( R.id.radiogroup_fragment2 );
            mRadioGroup.setOnCheckedChangeListener( this );
            mLinearLayout_fragment2_main = ( LinearLayout ) mView.findViewById( R.id.ll_fragment2_main );
            mPlatform_fragment2_main = ( TextView ) mView.findViewById( R.id.platform_fragment2_main );
            mPlatform_frament2_iv = ( ImageButton ) mView.findViewById( R.id.platform_iv_fragment2_main );
            mLinearLayout_fragment2_main.setOnClickListener( this );
            mPager = ( ViewPager ) mView.findViewById( R.id.vp_fragment2_main );
            //mProgressBar = ( ProgressBar ) mView.findViewById( R.id.pb_fragment2_main );
            platform_name_order = mPlatform_fragment2_main.getText().toString();
            // 委单信息
            View page1 = View.inflate( getActivity(), R.layout.page1_fragment2, null );
            mLinearLayout_text_page1 = ( SwipeRefreshLayout ) page1.findViewById( R.id.ll1_page1_fragment2 );
            mListView_page1 = ( ListView ) page1.findViewById( R.id.listview_page1_fragment2 );
            mOrdersAdapter = new OrdersAdapter( getActivity(), m_orders );
            mListView_page1.setAdapter( mOrdersAdapter );
            mLinearLayout_progress_page1 = ( LinearLayout ) page1.findViewById( R.id.ll2_page1_fragment2 );
            mLinearLayout_text_page1.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            mLinearLayout_text_page1.setRefreshing( false );
                            updateOrderData();
                        }
                    }, 5000 );
                }
            } );
            mLinearLayout_text_page1.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
            // 成交信息
            View page2 = View.inflate( getActivity(), R.layout.page2_fragment2, null );
            mLinearLayout_progress_page2 = ( LinearLayout ) page2.findViewById( R.id.ll2_page2_fragment2 );
            mLinearLayout_text_page2 = ( SwipeRefreshLayout ) page2.findViewById( R.id.ll1_page2_fragment2 );
            mListView_page2 = ( ListView ) page2.findViewById( R.id.listview_page2_fragment2 );
            mTransactionAdapter = new TransactionAdapter( m_transactions, getActivity() );
            mListView_page2.setAdapter( mTransactionAdapter );
            mLinearLayout_text_page2.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            mLinearLayout_text_page2.setRefreshing( false );
                            updateTransactionsData();
                        }
                    }, 5000 );
                }
            } );
            mLinearLayout_text_page2.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
            // K线图
            View page3 = View.inflate( getActivity(), R.layout.page3_fragment2, null );
            mLinearLayout_text_page3 = ( LinearLayout ) page3.findViewById( R.id.ll1_page3_fragment2 );
            mwb_page3 = ( WebView ) page3.findViewById( R.id.wb_page3_fragmeng2 );
            WebSettings webSettings = mwb_page3.getSettings();
            webSettings.setJavaScriptEnabled( true );
            // 设置可以访问文件
            webSettings.setAllowFileAccess( true );
            // 设置支持缩放
            webSettings.setBuiltInZoomControls( true );
            webSettings.setUseWideViewPort( true );

            // 将导航界面添加到ViewPager中
            List< View > list = new ArrayList< View >();
            list.add( page1 );
            list.add( page2 );
            list.add( page3 );
            MyPagerAdapter adapter = new MyPagerAdapter( list );
            if ( mPager != null ) {
                mPager.setAdapter( adapter );
                mPager.setOnPageChangeListener( this );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged( RadioGroup group, int checkedId ){
        if ( mPager == null )
            return;
        switch ( checkedId ) {
            case R.id.radioBtn1_fragment2_main:// 委单信息
                if ( mPager.getCurrentItem() != 0 )
                    mPager.setCurrentItem( 0 );
                break;
            case R.id.radioBtn2_fragment2_main:// 成交信息
                if ( mPager.getCurrentItem() != 1 )
                    mPager.setCurrentItem( 1 );
                break;
          /*  case R.id.radioBtn3_fragment2_main:// K线图
                if ( mPager.getCurrentItem() != 2 )
                    mPager.setCurrentItem( 2 );
                break;*/

        }
    }

    @Override
    public void onPageScrollStateChanged( int arg0 ){
    }

    @Override
    public void onPageScrolled( int arg0, float arg1, int arg2 ){

    }

    @Override
    public void onPageSelected( int arg0 ){
        if ( mPager == null || mRadioGroup == null )
            return;
        switch ( arg0 ) {
            case 0:// 加载委托信息的数据
                mRadioGroup.check( R.id.radioBtn1_fragment2_main );
                mTimer = new Timer();
                orderTimerTask = new MyTimerTask( DEPTH_ORDER_ACTION );
                if ( mTimer != null || update_time != 0 )
                    mTimer.schedule( orderTimerTask, 0, update_time );
                break;
            case 1:// 加载最近成交的数据
                mRadioGroup.check( R.id.radioBtn2_fragment2_main );
                mTimer = new Timer();
                transactionTimerTask = new MyTimerTask( DEPTH_TRANSACTIONS_ACTION );
                if ( mTimer != null || update_time != 0 )
                    mTimer.schedule( transactionTimerTask, 0, update_time );
                break;
          /*  case 2:// 加载K线图的数据
                mRadioGroup.check( R.id.radioBtn3_fragment2_main );
                updateKLineData();
                break;*/
        }
    }

    /**
     * 更新k线图信息
     */
    private void updateKLineData(){
        if ( NetworkUtils.isNetworkConnected( getActivity() ) ) {
            mWeiboWebViewClient = new ScanWebViewClient();
            if ( mwb_page3 == null )
                return;
            mwb_page3.setWebViewClient( mWeiboWebViewClient );
            if ( platform_name == null ) {
                platform_name = getActivity().getResources().getStringArray( R.array.platform_chart )[ 0 ];
            }
            mwb_page3.loadUrl( DataHelper.KLINE_URL );
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * 更新委单量信息
     */
    private void updateOrderData(){
        if ( NetworkUtils.isNetworkConnected( getActivity() ) ) {
            if ( toStringMap() == null ) {
                return;
            }
            ClientEngine clientEngine = ClientEngine.getInstance( getActivity() );
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = DataHelper.DEPTH_ORDER_ACTION;
            packet.url = DataHelper.ORDER_URL;
            packet.extra = toStringMap();
            packet.context = getActivity();
            packet.object = this;
            clientEngine.httpGetRequest( packet, this );
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * 更新成交信息
     */
    private void updateTransactionsData(){
        if ( NetworkUtils.isNetworkConnected( getActivity() ) ) {
            if ( toStringMap() == null ) {
                return;
            }
            ClientEngine clientEngine = ClientEngine.getInstance( getActivity() );
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = DataHelper.DEPTH_TRANSACTIONS_ACTION;
            packet.url = DataHelper.TRANSACTIONS_URL;
            packet.extra = toStringMap();
            packet.context = getActivity();
            packet.object = this;
            clientEngine.httpGetRequest( packet, this );
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
        }

    }

    @Override
    public Map< String, String > toStringMap(){
        Log.e( TAG, "toStringMap执行了" );
        if ( platform_name_order == null )
            return null;
        Map< String, String > map = new HashMap< String, String >();
        map.put( "platform", platform_name_order );
        map.put( "page", String.valueOf( page ) );
        Log.e( TAG, map.toString() );
        return map;
    }

    @Override
    public void onClick( View v ){
        switch ( v.getId() ) {
            case R.id.ll_fragment2_main:
                showPlatformPopwindow();
                break;

            default:
                break;
        }
    }

    private void showPlatformPopwindow(){
        if ( mPlatform_frament2_iv != null ) {
            mPlatform_frament2_iv.setSelected( true );
        }
        final List< String > list = new ArrayList< String >();
        try {
            String[] platforms = getActivity().getResources().getStringArray( R.array.platform_name );
            for ( int i = 0; i < platforms.length; i++ ) {
                list.add( platforms[ i ] );
            }
            final AbstractSpinerAdapter< String > adapter = new NormalSpinerAdapter( getActivity() );
            adapter.refreshData( list, 0 );
            final SpinerPopWindow spinerPopWindow = new SpinerPopWindow( getActivity() );
            spinerPopWindow.setAdatper( adapter );
            spinerPopWindow.setItemListener( new IOnItemSelectListener() {
                @Override
                public void onItemClick( int pos ){
                    mPlatform_fragment2_main.setText( list.get( pos ) );

                    // 更新数据
                    switch ( mPager.getCurrentItem() ) {
                        case 0:
                            page = 1;
                            platform_name_order = mPlatform_fragment2_main.getText().toString();
                            updateOrderData();
                            break;
                        case 1:
                            page = 1;
                            platform_name_order = mPlatform_fragment2_main.getText().toString();
                            updateTransactionsData();
                            break;
                        case 2:
                            platform_name = getActivity().getResources().getStringArray( R.array.platform_chart )[ pos ];
                            updateKLineData();
                            break;
                        default:
                            break;
                    }
                    mPlatform_frament2_iv.setSelected( false );
                    spinerPopWindow.dismiss();

                }
            } );
            spinerPopWindow.showAsDropDown( mLinearLayout_fragment2_main );
        } catch ( Exception e ) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onResult( int requestAction, Boolean isSuccess, String content, Object extra ){
        if ( isSuccess ) {
            switch ( requestAction ) {
                case DataHelper.DEPTH_ORDER_ACTION:
                    updateOrderData( content );
                    break;
                case DataHelper.DEPTH_TRANSACTIONS_ACTION:
                    updateTransactionData( content );
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
        }
    }

    private void updateOrderData( String content ){
        if ( m_orders.isEmpty() ) {
            mLinearLayout_progress_page1.setVisibility( View.INVISIBLE );
            mLinearLayout_text_page1.setVisibility( View.VISIBLE );
        } else {
        }
        if ( content != null ) {
            // 解析json字符串
            try {
                m_orders = ParseJson.parseOrder( content );
                if ( m_orders != null )
                    mOrdersAdapter.refreshData( m_orders );
            } catch ( Exception e ) {
                e.printStackTrace();
                DialogUtils.showNoDataDialog( getActivity() );
            }
        } else {
            DialogUtils.showNoDataDialog( getActivity() );
        }
    }

    @SuppressWarnings("unchecked")
    private void updateTransactionData( String content ){
        if ( m_transactions.isEmpty() ) {
            mLinearLayout_progress_page2.setVisibility( View.INVISIBLE );
            mLinearLayout_text_page2.setVisibility( View.VISIBLE );
        }
        if ( content != null ) {
            Log.e( TAG, content );
            // 解析json字符串
            try {
                Map< String, Object > map = ParseJson.parseTransaction( content );
                m_transactions = ( List< Transaction > ) map.get( TRANSACTION );
                hasNext = ( Boolean ) map.get( HAS_NEXT );
                if ( m_transactions != null )
                    mTransactionAdapter.refreshData( m_transactions );
            } catch ( Exception e ) {
                e.printStackTrace();
                DialogUtils.showNoDataDialog( getActivity() );
            }
        } else {
            DialogUtils.showNoDataDialog( getActivity() );
        }
    }

    private class ScanWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading( WebView view, String url ){
            view.loadUrl( url );
            return super.shouldOverrideUrlLoading( view, url );
        }

        @Override
        public void onReceivedError( WebView view, int errorCode, String description, String failingUrl ){
            super.onReceivedError( view, errorCode, description, failingUrl );
        }

        @Override
        public void onPageStarted( WebView view, String url, Bitmap favicon ){
            mLinearLayout_text_page3.setVisibility( View.VISIBLE);
            super.onPageStarted( view, url, favicon );
        }

        @Override
        public void onPageFinished( WebView view, String url ){
            Log.e( TAG, "onPageFinished url = " + url );
            mLinearLayout_text_page3.setVisibility( View.VISIBLE );
            super.onPageFinished( view, url );
        }
    }
}
