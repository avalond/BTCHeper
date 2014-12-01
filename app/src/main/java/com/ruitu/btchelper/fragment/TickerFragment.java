package com.ruitu.btchelper.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.activity.MainActivity;
import com.ruitu.btchelper.activity.NewsInfoActivity;
import com.ruitu.btchelper.adapter.MyPagerAdapter;
import com.ruitu.btchelper.adapter.TickerApdater;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.AsyncImageLoader;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DateUtil;
import com.ruitu.btchelper.util.DialogUtils;
import com.ruitu.btchelper.util.NetworkUtils;
import com.ruitu.btchelper.util.PlatformNameUtils;
import com.ruitu.btchelper.weight.MyImgScroll;
import com.ruitu.btchelper.weight.MyListView;
import com.ruitu.btchelper.weight.RefreshListView;

@SuppressLint( { "NewApi", "HandlerLeak" } )
public class TickerFragment extends Fragment implements OnItemClickListener, OnClickListener, IRequestContentCallback, IToStringMap, OnRefreshListener, OnDismissListener, DataHelper {
    public static TickerFragment getInstance(){
        return new TickerFragment();
    }

    private static final String TAG = TickerFragment.class.getSimpleName();
    private View mView;
    private Button mButton;
    private MyListView mListView;
    // 广告滚动组件
    // private ViewPager mViewPager;
    private ImageView mImageView;
    private ArrayList< News > newsList = new ArrayList< News >();
    private RadioButton btn1, btn2, btn3, btn4;
    private TextView title_news;
    private int currentItem = 0; // 当前图片的索引号
    private ScheduledExecutorService scheduledExecutorService;
    private LinearLayout newsLinearLayout;
    private MyPagerAdapter newsPagerAdapter;
    private MyImgScroll myImgScroll;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout my_btns;
    private RadioGroup newsRadioGroup;
    private List< View > newsViewList = new ArrayList< View >();
  // private ProgressBar mBar;
    private TextView mUpdateTime;
    private Timer mTimer;
    private MyTimerTask myTimerTask;
    private List< Ticker > mTickers = new ArrayList< Ticker >();
    private long updateTime;
    private Map< String, Ticker > mTickersCache = new HashMap< String, Ticker >();
    private List< String > defaultforms = new ArrayList< String >();
    private List< String > platforms = new ArrayList< String >();
    private Map< String, Drawable > drawables = new HashMap< String, Drawable >();
    private TickerApdater mTickerApdater;
    private boolean isCurrent = false;
    private boolean isLoadingTicker = false;
    private Dialog mDialog = null;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ){
            Log.e( TAG, msg.what + "<<<<<<<<<<<<" );
            switch ( msg.what ) {
                case TICKER_ACTION:
                    updateData();
                    break;
                case NOTICE_ACTION:
                    loadNewsData();
                    break;
                case NOTICE_ACTION_TICKER:
                    if ( newsList != null && ! newsList.isEmpty() ) {
                        News news = newsList.get( currentItem );
                        if ( news == null || title_news == null )
                            return;
                        title_news.setText( news.getNews_title() );
                        String icon = news.getNews_icon();
                        Bitmap drawable = news.getNews_icon_drawable();
                        if ( icon != null ) {
                            if ( drawable != null ) {
                                Log.e( TAG, "设置图片执行了" );
                                mImageView.setImageBitmap( drawable );
                            } else {
                                Log.e( TAG, "加载图片执行了" );
                                new AsyncImageLoader().loadNewsDrawable( mImageView, newsList, currentItem );
                            }
                        }
                        switch ( currentItem ) {
                            case 0:
                                btn1.setChecked( true );
                                break;
                            case 1:
                                btn2.setChecked( true );
                                break;
                            case 2:
                                btn3.setChecked( true );
                                break;
                            case 3:
                                btn4.setChecked( true );
                                break;
                            default:
                                btn1.setChecked( true );
                                break;
                        }
                        break;
                    }
            }
        }

    };

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        Log.e( TAG, "onCreateView" );
        initViews( inflater, container );
        return mView;
    }

    @Override
    public void onAttach( Activity activity ){
        Log.e( TAG, "onAttach执行了" );
        mHandler.sendEmptyMessage( NOTICE_ACTION );
        super.onAttach( activity );
    }

    @Override
    public void onStart(){
        Log.e( TAG, "onStart" );
        super.onStart();
    }

    private void initViews( LayoutInflater inflater, ViewGroup container ){

        mView = inflater.inflate( R.layout.fragment1_main, container, false );
       // mBar = ( ProgressBar ) mView.findViewById( R.id.pb_fragment1_main );
        mButton = ( Button ) mView.findViewById( R.id.setting_btn_top );
        swipeLayout = ( SwipeRefreshLayout ) mView.findViewById( R.id.swipe_container );
        swipeLayout.setOnRefreshListener( ( OnRefreshListener ) this );
        swipeLayout.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
        mListView = ( MyListView ) mView.findViewById( R.id.ticker_lv );
        mImageView = ( ImageView ) mView.findViewById( R.id.vp_fragment1_main );
        newsPagerAdapter = new MyPagerAdapter( newsViewList );
        // mViewPager.setAdapter(newsPagerAdapter);
        this.btn1 = ( RadioButton ) mView.findViewById( R.id.btn1_fragment1_main );
        this.btn2 = ( RadioButton ) mView.findViewById( R.id.btn2_fragment1_main );
        this.btn3 = ( RadioButton ) mView.findViewById( R.id.btn3_fragment1_main );
        this.btn4 = ( RadioButton ) mView.findViewById( R.id.btn4_fragment1_main );
        newsLinearLayout = ( LinearLayout ) mView.findViewById( R.id.rl_fragment1_main_news );
        title_news = ( TextView ) mView.findViewById( R.id.tv_fragment1_vp_page );
        newsRadioGroup = ( RadioGroup ) mView.findViewById( R.id.bottm_news_main );
        mUpdateTime = ( TextView ) mView.findViewById( R.id.updatetime_tv_top );
    }

    /**
     * 初始化图片
     */
    private void InitViewPager(){
        if ( newsList != null && ! newsList.isEmpty() ) {

            for ( int i = 0; i < newsList.size(); i++ ) {
                ImageView imageView = new ImageView( getActivity() );
                imageView.setOnClickListener( new OnClickListener() {
                    public void onClick( View v ){// 设置图片点击事件
                      //  Toast.makeText( getActivity(), "点击了:" + myImgScroll.getCurIndex(), Toast.LENGTH_SHORT ).show();
                    }
                } );
                imageView.setImageBitmap( newsList.get( i ).getNews_icon_drawable() );
                imageView.setScaleType( ScaleType.CENTER_CROP );
                newsViewList.add( imageView );
            }
        }
    }

    @Override
    public void onResume(){
        try {
            Log.e( TAG, "onResume" );
            // isCurrent = true;
            isLoadingTicker = false;
            mTimer = new Timer();
            myTimerTask = new MyTimerTask( TICKER_ACTION );
            updateTime = LocalSharePreference.getMarketInterval( getActivity() );
            defaultforms = LocalSharePreference.getDefaultPlatnameInterval( getActivity() );
            platforms = PlatformNameUtils.getPlatFormList();
            mTickerApdater = new TickerApdater( getActivity(), mTickers, mTickersCache, platforms );
            mListView.setAdapter( mTickerApdater );
            mListView.setOnItemClickListener( this );
            mButton.setOnClickListener( this );
            newsLinearLayout.setOnClickListener( this );
            mImageView.setOnClickListener( this );
            mTimer.schedule( myTimerTask, 0, updateTime );
            // new Thread(mRunnable).start();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        if ( scheduledExecutorService == null )
            return;
        scheduledExecutorService.scheduleAtFixedRate( new ScrollTask(), 0, 5, TimeUnit.SECONDS );
        scheduledExecutorService.schedule( new ScrollTask(), 5, TimeUnit.SECONDS );
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

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {
        public void run(){
            synchronized ( mImageView ) {
                currentItem = ( currentItem + 1 ) % 4;
                mHandler.sendEmptyMessage( NOTICE_ACTION_TICKER );// 通过Handler切换图片
            }
        }

    }

    @Override
    public void onPause(){
        Log.e( TAG, "onPause" );
        if ( scheduledExecutorService != null )
            scheduledExecutorService.shutdownNow();
        isCurrent = false;
        if ( myTimerTask != null )
            myTimerTask.cancel();
        if ( mTimer != null )
            mTimer.cancel();
        ClientEngine.getInstance( getActivity() ).cancelTask( getActivity() );
        super.onPause();

    }

    @Override
    public void onStop(){
        Log.e( TAG, "onStop" );
        super.onStop();

    }

    @Override
    public void onDestroy(){
        Log.e( TAG, "onDestroy" );
        super.onDestroy();
    }

    private void updateData(){
        // if (!isLoadingTicker) {
        if ( NetworkUtils.isNetworkConnected( getActivity() ) ) {
            Log.e( TAG, "开始加载" );
            //mBar.setVisibility( View.VISIBLE );
            mTickersCache.clear();
            if ( mTickers != null && ! mTickers.isEmpty() ) {
                for ( Ticker ticker : mTickers ) {
                    if ( ticker != null ) {
                        mTickersCache.put( ticker.getName(), ticker );
                    }
                }
            }
            if ( toStringMap() == null ) {
                DialogUtils.showNotSelectorPlat( getActivity() );
                mTickers.clear();
                mTickersCache.clear();
                mTickerApdater.refreshData( mTickers, mTickersCache );
                return;
            }
            // isLoadingTicker = true;
            ClientEngine clientEngine = ClientEngine.getInstance( getActivity() );
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = DataHelper.TICKER_ACTION;
            packet.url = DataHelper.TICKER_URL;
            packet.extra = toStringMap();
            packet.context = getActivity();
            packet.object = this;
            clientEngine.httpGetRequest( packet, this );
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();

        }

        // }
    }

    /**
     * 请求广告数据
     */
    private void loadNewsData(){
        ClientEngine clientEngine = ClientEngine.getInstance( getActivity() );
        BaseRequestPacket packet = new BaseRequestPacket();
        packet.action = DataHelper.NOTICE_ACTION;
        packet.url = DataHelper.NEWS_URL.replace( "{1}", String.valueOf( 7 ) );
        packet.context = getActivity();
        clientEngine.httpGetRequest( packet, this );
    }

    @Override
    public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ){
        if ( mTickers == null || mTickers.isEmpty() )
            return;
        Ticker ticker = mTickers.get( arg2 );
        showMenuDialog( arg0, ticker );
    }

    /**
     * 创建列表对话框
     */
    protected void showMenuDialog( AdapterView< ? > arg0, final Ticker ticker ){
        if ( ticker == null )
            return;
        String[] items = getActivity().getResources().getStringArray( R.array.ticker_menu_arr );
        LayoutInflater layoutInflater = ( LayoutInflater ) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mDialogView = layoutInflater.inflate( R.layout.dialog_menu, null );
        TextView tv = ( TextView ) mDialogView.findViewById( R.id.title_dialog_menu );
        tv.setText( ticker.getName() );
        ListView mListView = ( ListView ) mDialogView.findViewById( R.id.listView_dialog_menu );
        ArrayAdapter< String > adapter = new ArrayAdapter< String >( getActivity(), R.layout.dialog_menu_item, items );
        mListView.setAdapter( adapter );
        mListView.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ){
                if ( mDialog == null )
                    return;
                switch ( arg2 ) {
                    case 0:
                     //   System.out.println( "------> 价格预警" );
                        showWarnSettingWindow( ticker );
                        mDialog.dismiss();
                        break;
                    case 1:
                      //  System.out.println( "------> 价格详情" );
                        showPriceInfoWindow( ticker );
                        mDialog.dismiss();
                        break;
                    case 2:
                     //   System.out.println( "------> 打开网站" );
                        String net_address = null;
                        if ( ticker.getNet_name() == null ) {
                            net_address = "http://www.btcte.com/";
                        } else {
                            net_address = ticker.getNet_name();
                        }
                        Uri uri = Uri.parse( net_address );
                        Intent it = new Intent( Intent.ACTION_VIEW, uri );
                        startActivity( it );
                        mDialog.dismiss();
                        break;
                    default:
                        break;
                }
            }

        } );
        mDialog = new Dialog( getActivity(), R.style.dialog );
        if ( mDialog == null )
            return;
        mDialog.setContentView( mDialogView );
        mDialog.setCanceledOnTouchOutside( false );
        mDialog.show();
    }

    /**
     * 显示详情对话框
     */
    @SuppressWarnings( "deprecation" )
    private void showPriceInfoWindow( Ticker ticker ){
        final Dialog dialog = new Dialog( getActivity(), R.style.dialog );
        View view = View.inflate( getActivity(), R.layout.fragment1_pop_price, null );
        TextView platformname = ( TextView ) view.findViewById( R.id.name_pop_price );
        TextView last_price = ( TextView ) view.findViewById( R.id.last_text_pop_price );
        TextView buy_one_price = ( TextView ) view.findViewById( R.id.buy_text_pop_price );
        TextView sell_one_price = ( TextView ) view.findViewById( R.id.sell_text_pop_price );
        TextView max_price = ( TextView ) view.findViewById( R.id.high_text_pop_price );
        TextView min_price = ( TextView ) view.findViewById( R.id.low_text_pop_price );
        TextView volume = ( TextView ) view.findViewById( R.id.volume_text_pop_price );
        TextView updateTime = ( TextView ) view.findViewById( R.id.update_text_pop_price );
        Button btn = ( Button ) view.findViewById( R.id.close_btn_pop_price );
        platformname.setText( ticker.getName() );
        last_price.setText( getActivity().getResources().getString( R.string._last_text ) + ticker.getLast() );
        buy_one_price.setText( getActivity().getResources().getString( R.string._buy_one_text ) + ticker.getBuy() );
        sell_one_price.setText( getActivity().getResources().getString( R.string._sell_one_text ) + ticker.getSell() );
        min_price.setText( getActivity().getResources().getString( R.string._low_text ) + ticker.getLow() );
        max_price.setText( getActivity().getResources().getString( R.string._high_text ) + ticker.getHigh() );
        volume.setText( getActivity().getResources().getString( R.string._volume_one_text ) + ticker.getVolume() );
        updateTime.setText( getActivity().getResources().getString( R.string._update_text ) +ticker.getUpdateTime()  );
        btn.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ){
                dialog.dismiss();
            }
        } );
        dialog.setContentView( view );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity( Gravity.CENTER );
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        lp.height = ( int ) ( d.getHeight() * 0.5 ); // 高度设置为屏幕的0.6
        lp.width = ( int ) ( d.getWidth() * 0.6 ); // 宽度设置为屏幕的0.95
        dialogWindow.setAttributes( lp );
        dialog.setCanceledOnTouchOutside( false );
        dialog.show();
    }

    /**
     * 价格预警设置对话框
     */
    @SuppressWarnings( "deprecation" )
    private void showWarnSettingWindow( final Ticker ticker ){
        final Dialog dialog = new Dialog( getActivity(), R.style.dialog );
        View view = View.inflate( getActivity(), R.layout.fragment1_pop_warn_setting, null );
        TextView name_pop_warn = ( TextView ) view.findViewById( R.id.name_pop_warn );
        name_pop_warn.setText( ticker.getName() );
        final CheckBox warn_cbx = ( CheckBox ) view.findViewById( R.id.monitor_cb_pop_warn );
        final CheckBox alarm_cbx = ( CheckBox ) view.findViewById( R.id.alarm_cb_pop_warn );
        final CheckBox cha_cbx = ( CheckBox ) view.findViewById( R.id.shake_cb_pop_warn );
        final EditText low_price = ( EditText ) view.findViewById( R.id.low_text_pop_warn );
        final EditText high_price = ( EditText ) view.findViewById( R.id.high_text_pop_warn );
        Button close_btn = ( Button ) view.findViewById( R.id.close_btn_pop_warn );
        Button save_btn = ( Button ) view.findViewById( R.id.save_btn_pop_warn );
        low_price.setText( String.valueOf( LocalSharePreference.getLowPrice( getActivity(), ticker.getName() ) ) );
        high_price.setText( String.valueOf( LocalSharePreference.getHightPrice( getActivity(), ticker.getName() ) ) );
        if ( LocalSharePreference.isMonitorInterval( getActivity(), ticker.getName() ) ) {
            warn_cbx.setChecked( true );
        } else {
            warn_cbx.setChecked( false );
        }
        if ( LocalSharePreference.isAlarmInterval( getActivity(), ticker.getName() ) ) {
            alarm_cbx.setChecked( true );
        } else {
            alarm_cbx.setChecked( false );
        }
        if ( LocalSharePreference.isShakeInterval( getActivity(), ticker.getName() ) ) {
            cha_cbx.setChecked( true );
        } else {
            cha_cbx.setChecked( false );
        }

        close_btn.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ){
                dialog.dismiss();
            }
        } );
        /**
         * 保存设置信息
         */
        save_btn.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ){
                if ( warn_cbx.isChecked() ) {
                  //  Log.e( TAG, "监控设置" );
                    LocalSharePreference.commitMonitorInterval( getActivity(), ticker.getName(), true );
                } else {
                    LocalSharePreference.commitMonitorInterval( getActivity(), ticker.getName(), false );
                }
                if ( alarm_cbx.isChecked() ) {
                  //  Log.e( TAG, "响铃设置" );
                    LocalSharePreference.commitAlarmInterval( getActivity(), ticker.getName(), true );
                } else {
                    LocalSharePreference.commitAlarmInterval( getActivity(), ticker.getName(), false );
                }
                if ( cha_cbx.isChecked() ) {
                  //  Log.e( TAG, "震动设置" );
                    LocalSharePreference.commitShakeInterval( getActivity(), ticker.getName(), true );
                } else {
                    LocalSharePreference.commitShakeInterval( getActivity(), ticker.getName(), false );
                }
                String min = low_price.getText().toString().trim();
                String max = high_price.getText().toString().trim();
                Log.e( TAG, min + "===" + max );
                // 将设置信息写入到文件中
                if ( min == null || "".equals( min ) ) {
                    Toast.makeText( getActivity(), "最低价只能输入数字", Toast.LENGTH_LONG ).show();
                    return;
                } else if ( max == null || "".equals( max ) ) {
                    Toast.makeText( getActivity(), "最高价只能输入数字", Toast.LENGTH_LONG ).show();
                    return;
                } else {
                    LocalSharePreference.commitLowPrice( getActivity(), Integer.parseInt( min ), ticker.getName() );
                    LocalSharePreference.commitHightPrice( getActivity(), Integer.parseInt( max ), ticker.getName() );
                }
                dialog.dismiss();
            }
        } );

        dialog.setContentView( view );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity( Gravity.CENTER );
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        lp.height = ( int ) ( d.getHeight() * 0.6 ); // 高度设置为屏幕的0.6
        lp.width = ( int ) ( d.getWidth() * 0.8 ); // 宽度设置为屏幕的0.95
        dialogWindow.setAttributes( lp );
        dialog.setCanceledOnTouchOutside( false );
        dialog.show();
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public void onClick( View v ){
        int viewId = v.getId();
        switch ( viewId ) {
            case R.id.setting_btn_top:// 设置显示的平台
                List< String > list = PlatformNameUtils.getPlatFormList();
                View view = View.inflate( getActivity(), R.layout.platform_layout, null );
                ListView lv = ( ListView ) view.findViewById( R.id.lv_platform_layout );
                Button btn1 = ( Button ) view.findViewById( R.id.btn1_platform_layout );
                Button btn2 = ( Button ) view.findViewById( R.id.btn2_platform_layout );
                PlatformAdapter platformAdapter = new PlatformAdapter( list );
                lv.setAdapter( platformAdapter );
                final PopupWindow popupWindow = new PopupWindow( view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
                popupWindow.setFocusable( true );
                popupWindow.setOutsideTouchable( true );
                popupWindow.setBackgroundDrawable( new BitmapDrawable() );
                popupWindow.setOnDismissListener( this );
                btn1.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick( View v ){
                        LocalSharePreference.commitDefaultPlatform( getActivity(), defaultforms );
                        updateData();
                        popupWindow.dismiss();
                    }
                } );
                btn2.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick( View v ){
                        popupWindow.dismiss();
                    }
                } );

                popupWindow.showAsDropDown( v, 0, 20 );
                break;
            case R.id.vp_fragment1_main:// 跳转到新闻页面
                toNewsInfo();
                break;
            case R.id.rl_fragment1_main_news:// 跳转到新闻页面
                toNewsInfo();
                break;
            default:
                break;
        }
    }

    private void toNewsInfo(){
      //  Log.e( TAG, "currentItem===" + currentItem );
        if ( newsList != null && ! newsList.isEmpty() ) {
            News newslist = newsList.get( currentItem );
            Intent intent = new Intent( getActivity(), NewsInfoActivity.class );
            Bundle bundle = new Bundle();
            bundle.putParcelable( NEWS_SER, newslist );
            intent.putExtras( bundle );
            startActivity( intent );
        } else {
            DialogUtils.showNoDataDialog( getActivity() );
        }
    }

    private class PlatformAdapter extends BaseAdapter {
        private List< String > mPlatforms;

        public PlatformAdapter( List< String > platforms ){
            mPlatforms = platforms;
        }

        @Override
        public int getCount(){
            return mPlatforms.size();
        }

        @Override
        public Object getItem( int position ){
            return mPlatforms.get( position );
        }

        @Override
        public long getItemId( int position ){
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent ){

            if ( convertView == null ) {
                convertView = View.inflate( getActivity(), R.layout.lv_item_platform_layout, null );
                TextView tv = ( TextView ) convertView.findViewById( R.id.name_lv_item_platform );
                CheckBox cb = ( CheckBox ) convertView.findViewById( R.id.cb_lv_item_platform );
                final String text = mPlatforms.get( position );
                tv.setText( text );
                for ( String te : defaultforms ) {
                    if ( text.equals( te ) ) {
                        cb.setChecked( true );
                    }
                }
                cb.setOnCheckedChangeListener( new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ){
                        if ( isChecked ) {
                            defaultforms.add( text );
                        } else {
                            defaultforms.remove( text );
                            mTickersCache.remove( mTickersCache.get( text ) );
                        }
                    }
                } );
            }
            return convertView;
        }

    }

    @Override
    public void onDismiss(){

    }

    @Override
    public void onResult( int requestAction, Boolean isSuccess, String content, Object extra ){
     //   Log.e( TAG, "action===" + requestAction + "=========" + isSuccess + "========" + content + "====" + extra );
        switch ( requestAction ) {
            case DataHelper.TICKER_ACTION:
                isLoadingTicker = false;
                if ( mListView == null )
                    return;
                // mListView.onRefreshComplete();
                if ( isSuccess ) {
                    updateTickerData( content );
                } else {
                    Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
                    // mListView.onRefreshComplete();
                }
                break;
            case NOTICE_ACTION:
                if ( isSuccess ) {
                    updateNewsListData( content );
                } else {
                    this.title_news.setText( getActivity().getResources().getString( R.string.no_data ) );
                }
                break;
            default:
                break;
        }
    }

    @SuppressWarnings( "unchecked" )
    private void updateNewsListData( String content ){
        if ( content != null ) {
            Map< String, Object > map = ParseJson.parseNewsFromNet( content );
            if ( ! map.containsKey( NEWS_LIST ) )
                return;
            List< News > list = ( List< News > ) map.get( NEWS_LIST );
            if ( newsList == null )
                return;
            newsList.clear();
            if ( list != null && ! list.isEmpty() && list.size() >= 4 ) {
                for ( int i = 0; i < 4; i++ ) {
                    newsList.add( list.get( i ) );
                }
                for ( int postion = 0; postion < newsList.size(); postion++ ) {
                    if ( newsList.get( postion ).getNews_icon() != null && ! "".equals( newsList.get( postion ).getNews_icon() ) ) {
                        new AsyncImageLoader().loadNewsDrawable( null, newsList, postion );
                    }
                }
            }
        }
    }

    private void updateTickerData( String content ){
        isLoadingTicker = false;
        if ( mUpdateTime != null ) {
            mUpdateTime.setText( "更新于 " + DateUtil.getShortDateStr( System.currentTimeMillis() ) );
        }
        if ( content != null ) {
            // 解析json字符串
            try {
              //  Log.e( TAG, content );
                mTickers = ParseJson.parseTickers( content );
                if ( mTickers != null && ! mTickers.isEmpty() ) {
                    for ( Ticker ticker : mTickers ) {
                        if ( ticker == null )
                            return;
                        if ( ( ( MainActivity ) getActivity() ).warnService == null )
                            return;
                        ( ( MainActivity ) getActivity() ).warnService.checkWarn( ticker, getActivity() );
                    }
                    if ( mTickerApdater == null )
                        return;
                    mTickerApdater.refreshData( mTickers, mTickersCache );
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map< String, String > toStringMap(){
        Map< String, String > map = new HashMap< String, String >();
        StringBuffer sb = new StringBuffer();
        if ( defaultforms == null || defaultforms.isEmpty() ) {
            return null;
        } else {
            for ( String platform : defaultforms ) {
                sb.append( platform.trim() + "|" );
            }
            sb.deleteCharAt( sb.length() - 1 );
            map.put( "platforms", sb.toString() );
        }
        return map;
    }

    @Override
    public void onRefresh(){
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run(){
                swipeLayout.setRefreshing( false );
                updateData();
            }
        }, 5000 );
    }

}
