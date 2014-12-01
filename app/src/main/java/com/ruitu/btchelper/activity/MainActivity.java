package com.ruitu.btchelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.adapter.FragmentTabAdapter;
import com.ruitu.btchelper.adapter.MyPopAdapter;
import com.ruitu.btchelper.adapter.SystemAdapter;
import com.ruitu.btchelper.domain.SysSetting;
import com.ruitu.btchelper.domain.SysSettingData;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.fragment.DepthFragment;
import com.ruitu.btchelper.fragment.NewsFragment;
import com.ruitu.btchelper.fragment.SystemFragment;
import com.ruitu.btchelper.fragment.TeuFragment;
import com.ruitu.btchelper.fragment.TickerFragment;
import com.ruitu.btchelper.service.BackgroundService;
import com.ruitu.btchelper.service.WarnService;
import com.ruitu.btchelper.service.WarnUtils;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.setting.RingtoneUtils;
import com.ruitu.btchelper.setting.UpdateManager;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DateUtil;
import com.ruitu.btchelper.util.MediaPlayerUtils;
import com.ruitu.btchelper.util.PlatformNameUtils;
import com.ruitu.btchelper.weight.MenuPopWindow;
import com.ruitu.btchelper.weight.MyListView;
import com.ruitu.btchelper.weight.MyRelativeLayout;
import com.ruitu.btchelper.weight.SilidingLayout;
import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity implements DataHelper, AdapterView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RadioGroup mRadioGroup;
    public List< Fragment > fragments = new ArrayList< Fragment >();
    private FragmentTabAdapter tabAdapter;
    public WarnService warnService;
    public WarnReceiver warnReceiver;
    private PopupWindow mPopupWindow;
    private Intent service;
    private List< String > platnames = new ArrayList< String >();
    /**
     * 主内容的布局。
     */
    private RelativeLayout content;
    //
    private SilidingLayout mSilidingLayout;

    private MyListView mListView;
    private List< SysSetting > mList = new ArrayList< SysSetting >();
    private SystemAdapter mSystemAdapter;
    private String mRingtone;
    private RingtoneUtils mRingtoneUtils;
    private List< String > mStrings = new ArrayList< String >();
    private List< Uri > mUris = new ArrayList< Uri >();
    private Uri mRingtoneUri;
    private Map< String, Uri > map = new HashMap< String, Uri >();
    private UpdateManager mUpdateManager;
    private MediaPlayer mediaPlayer;

    @Override
    public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ){
        Log.e( TAG, arg2 + ">><<<" );
        Intent intent = null;
        switch ( arg2 ) {
            case 0:// 设置铃声
                setAlarm();
                break;
            case 1:// 实时详情刷新间隔
                setTickerTime( arg1, arg2 );
                break;
            case 2:// 市场深度刷新间隔
                setDepthTime( arg1, arg2 );
                break;
            case 3:// 使用帮助
                intent = new Intent( this, HelperActivity.class );
                startActivity( intent );
                break;
            case 4:// 检查更新
                checkUpdate();
                break;
            case 5://个人中心
                break;
            case 6:// 关于比特币助手
                intent = new Intent( this, AboutActivity.class );
                startActivity( intent );
                break;
            default:
                break;
        }
    }
    //

    /**
     * 检查更新
     */
    private void checkUpdate(){
        new UpdateAysncTask().execute();
    }

    //
    private class UpdateAysncTask extends AsyncTask< Void, Void, Boolean > {

        @Override
        protected Boolean doInBackground( Void... params ){
            return mUpdateManager.checkUpdate();
        }

        @Override
        protected void onPostExecute( Boolean result ){
            if ( result ) {
                mUpdateManager.checkUpdateInfo();
            } else {
                mUpdateManager.showLastIsNewDilog();
            }
        }

    }

    //
    @SuppressWarnings( "deprecation" )
    private void setDepthTime( View v, final int pos ){
        LayoutInflater localinflater = ( LayoutInflater ) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = localinflater.inflate( R.layout.fragment4_pop, null );
        ListView listview = ( ListView ) view.findViewById( R.id.lv_fragment4_pop );
        try {
            final String[] arr = this.getResources().getStringArray( R.array.time_arr );
            MyPopAdapter adapter = new MyPopAdapter( this, arr );
            listview.setAdapter( adapter );
            listview.setItemsCanFocus( false );
            listview.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
            final PopupWindow pop = new PopupWindow( view, 150, WindowManager.LayoutParams.WRAP_CONTENT );
            listview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ){
                    if ( arr.length >= ( arg2 + 1 ) ) {
                        String timeSelected = arr[ arg2 ];
                        if ( mList != null && mList.size() >= pos )
                            mList.get( pos ).setSys_setting_text( timeSelected );
                        if ( mSystemAdapter != null )
                            mSystemAdapter.reflashData( mList );
                        LocalSharePreference.commintDetailInterval( MainActivity.this, DateUtil.getMillSecond( timeSelected ) );
                        pop.dismiss();
                    }
                }
            } );
            pop.setContentView( view );
            pop.setOutsideTouchable( true );
            pop.setFocusable( true );
            pop.setBackgroundDrawable( new BitmapDrawable() );
            pop.update();
            int[] location = new int[ 2 ];
            v.getLocationOnScreen( location );
            pop.showAtLocation( v, Gravity.NO_GRAVITY, location[ 0 ] + v.getWidth(), location[ 1 ] + v.getHeight() );
        } catch ( Exception e ) {
            e.printStackTrace();
            return;
        }
    }

    @SuppressWarnings( "deprecation" )
    private void setTickerTime( View v, final int pos ){
        View view = View.inflate( MainActivity.this, R.layout.fragment4_pop, null );
        ListView listview = ( ListView ) view.findViewById( R.id.lv_fragment4_pop );
        final String[] arr = MainActivity.this.getResources().getStringArray( R.array.time_arr );
        Log.e( TAG, arr.length + "......." );
        MyPopAdapter adapter = new MyPopAdapter( MainActivity.this, arr );
        listview.setAdapter( adapter );
        listview.setItemsCanFocus( false );
        listview.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        final PopupWindow pop = new PopupWindow( view, 150, WindowManager.LayoutParams.WRAP_CONTENT );
        listview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ){
                String timeSelected = arr[ arg2 ];
                mList.get( pos ).setSys_setting_text( timeSelected );
                mSystemAdapter.reflashData( mList );
                Log.e( TAG, timeSelected );
                LocalSharePreference.commintMarketInterval( MainActivity.this, DateUtil.getMillSecond( timeSelected ) );
                pop.dismiss();
            }
        } );
        pop.setContentView( view );
        pop.setOutsideTouchable( true );
        pop.setFocusable( true );
        pop.setBackgroundDrawable( new BitmapDrawable() );
        pop.update();
        int[] location = new int[ 2 ];
        v.getLocationOnScreen( location );
        pop.showAtLocation( v, Gravity.NO_GRAVITY, location[ 0 ] + v.getWidth(), location[ 1 ] + v.getHeight() );

    }

    private void setAlarm(){
        mediaPlayer = new MediaPlayer();
        Uri uri = LocalSharePreference.getRingtoneUri( MainActivity.this );
        int pos = mUris.indexOf( uri );
        mRingtoneUri = uri;
        // 被选择的铃声
        ArrayAdapter< String > adapter = new ArrayAdapter< String >( MainActivity.this, android.R.layout.select_dialog_singlechoice, mStrings );
        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
        builder.setTitle( "选择铃声" );
        builder.setSingleChoiceItems( adapter, pos, new AlertDialog.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialog, int which ){
                mRingtone = mStrings.get( which );
                Log.e( TAG, mRingtone );
                mRingtoneUri = map.get( mRingtone );
                MediaPlayerUtils.resetMediaPlayer( mediaPlayer, mRingtoneUri, MainActivity.this );
            }
        } );
        builder.setPositiveButton( "确定", new AlertDialog.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which ){
                // 保存设置好的铃声
                LocalSharePreference.commitRingtoneUri( MainActivity.this, mRingtoneUri );
                MediaPlayerUtils.cancelMediaplayer( mediaPlayer );
                dialog.dismiss();
            }
        } );
        builder.setNegativeButton( "取消", new AlertDialog.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which ){
                MediaPlayerUtils.cancelMediaplayer( mediaPlayer );
                dialog.dismiss();
            }
        } );

        Dialog dialog = builder.create();
        dialog.show();

    }


    private ServiceConnection warnServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected( ComponentName name, IBinder service ){
            Log.e( TAG, "监控服务绑定成功!" );
            WarnService.MyBinder binder = ( WarnService.MyBinder ) service;
            warnService = binder.getInstance();
        }

        @Override
        public void onServiceDisconnected( ComponentName name ){
            warnService = null;
        }
    };


    private class WarnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive( Context context, Intent intent ){
            String action = intent.getAction();
            if ( WARN_ACTION.equals( action ) ) {
                if ( intent.hasExtra( WARN_TICKER ) ) {
                    final Ticker ticker = intent.getParcelableExtra( WARN_TICKER );
                    if ( ticker == null )
                        return;
                    String last = ticker.getLast();
                    String name = ticker.getName();
                    if ( platnames == null || platnames.isEmpty() ) {
                        return;
                    }
                    if ( platnames.get( 0 ).equals( name ) || platnames.get( 4 ).equals( name ) || platnames.get( 5 ).equals( name ) ) {
                        last = "$" + last;
                    } else {
                        last = "¥" + last;
                    }
                    if ( mPopupWindow != null ) {
                        mPopupWindow.dismiss();
                    }

                    mPopupWindow = new PopupWindow();

                    View view = View.inflate( MainActivity.this, R.layout.warn_dialog, null );
                    TextView title = ( TextView ) view.findViewById( R.id.title_tv_warn_dialog );
                    title.setText( ticker.getName() + "_最近成交价:" );
                    TextView price = ( TextView ) view.findViewById( R.id.price_tv_warn_dialog );
                    price.setText( last );
                    mPopupWindow.setContentView( view );
                    mPopupWindow.setWindowLayoutMode( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
                    view.findViewById( R.id.close_this_warn ).setOnClickListener( new OnClickListener() {
                        @Override
                        public void onClick( View v ){
                            if ( warnService != null ) {
                                warnService.cancelAlarm( MainActivity.this );
                            }
                            LocalSharePreference.commitHasWarnInterval( MainActivity.this, ticker.getName(), false );
                            if ( mPopupWindow != null )
                                mPopupWindow.dismiss();
                        }
                    } );
                    view.findViewById( R.id.clear_this_warn ).setOnClickListener( new OnClickListener() {
                        @Override
                        public void onClick( View v ){
                            LocalSharePreference.commitHightPrice( MainActivity.this, 0, ticker.getName() );
                            LocalSharePreference.commitLowPrice( MainActivity.this, 0, ticker.getName() );
                            LocalSharePreference.clearMonitorInterval( MainActivity.this, ticker.getName() );
                            LocalSharePreference.clearAlarmInterval( MainActivity.this, ticker.getName() );
                            LocalSharePreference.clearShakeInterval( MainActivity.this, ticker.getName() );
                            LocalSharePreference.commitHasWarnInterval( MainActivity.this, ticker.getName(), false );
                            if ( warnService != null ) {
                                warnService.cancelAlarm( MainActivity.this );
                            }
                            if ( mPopupWindow != null ) {
                                mPopupWindow.dismiss();
                            }
                        }
                    } );
                    mPopupWindow.setFocusable( true );
                    mPopupWindow.setBackgroundDrawable( new BitmapDrawable() );
                    mPopupWindow.setOutsideTouchable( false );
                    mPopupWindow.update();
                    mPopupWindow.showAtLocation( MainActivity.this.getCurrentFocus(), Gravity.CENTER, 0, 0 );
                }
            }
        }
    }
    @Override
    protected void onCreate( Bundle bundle ){
        Log.e( TAG, "create执行了" );
        super.onCreate( bundle );
        setContentView( R.layout.layout_main );
        if ( fragments.isEmpty() ) {
            init();
        }
        service = new Intent( this, WarnService.class );
        if ( service != null && warnServiceConnection != null ) {
            bindService( service, warnServiceConnection, Context.BIND_AUTO_CREATE );
        }
        warnReceiver = new WarnReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction( DataHelper.WARN_ACTION );
        if ( warnReceiver != null ) {
            this.registerReceiver( warnReceiver, filter );
        }
    }
    @Override
    protected void onResume(){
        Log.e( TAG, "onResume执行了" );
        platnames = PlatformNameUtils.getPlatFormList();

        if ( mRingtoneUtils != null ) {
            mStrings = mRingtoneUtils.getRingtoneTitleList( RingtoneManager.TYPE_NOTIFICATION );
            mUris = mRingtoneUtils.getRingtoneUriList( RingtoneManager.TYPE_NOTIFICATION );
        }
        if ( mStrings != null && mUris != null & ! mStrings.isEmpty() && ! mUris.isEmpty() ) {
            for ( int i = 0; i < mStrings.size(); i++ ) {
                map.put( mStrings.get( i ), mUris.get( i ) );
            }
        }
        Intent intent = new Intent( this, BackgroundService.class );
        this.stopService( intent );
        super.onResume();
    }

    /**
     * 初始化试图
     */
    private void init(){
        try {
            // configure the SlidingMenu
            SlidingMenu menu = new SlidingMenu(this);
            menu.setMode(SlidingMenu.LEFT);
            // 设置触摸屏幕的模式
            menu.setTouchModeAbove( SlidingMenu.TOUCHMODE_FULLSCREEN );
            menu.setShadowWidthRes( R.dimen.dp_0 );
            menu.setShadowDrawable( R.color.argb_ffffff );
            // 设置滑动菜单视图的宽度
            menu.setBehindOffsetRes(R.dimen.dp_50);
            // 设置渐入渐出效果的值
            menu.setFadeDegree(0.50f);
            /**
             * SLIDING_WINDOW will include the Title/ActionBar in the content
             * section of the SlidingMenu, while SLIDING_CONTENT does not.
             */
            menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
            //this is left layout
            menu.setMenu(R.layout.left_menu_layout);
            //
            mRingtoneUtils = new RingtoneUtils( this );
            mUpdateManager = new UpdateManager( this );
            //
            this.mListView = ( MyListView ) findViewById( R.id.lv_left_menu );
            mList = SysSettingData.getLeftMenuList( this );
            long depth_time = LocalSharePreference.getDetailInterval( this );
            long ticker_time = LocalSharePreference.getMarketInterval( this );
            String depth_time_str = DateUtil.getMinuteStr( depth_time );
            String ticker_time_str = DateUtil.getMinuteStr( ticker_time );
            mList.get( 1 ).setSys_setting_text( ticker_time_str );
            mList.get( 2 ).setSys_setting_text( depth_time_str );
            mSystemAdapter = new SystemAdapter( this, mList );
            if ( mListView != null ) {
                mListView.setAdapter( mSystemAdapter );
                mListView.setOnItemClickListener( this );
            }
            fragments.add( TickerFragment.getInstance() );
            fragments.add( DepthFragment.getInstance() );
            fragments.add( NewsFragment.getInstance() );
            fragments.add( TeuFragment.getInstance() );
            mRadioGroup = ( RadioGroup ) this.findViewById( R.id.radioGroup_main );
            if ( mRadioGroup != null ) {
                ( ( RadioButton ) mRadioGroup.getChildAt( 0 ) ).setChecked( true );
                tabAdapter = new FragmentTabAdapter( this, fragments, R.id.frame_main, mRadioGroup );
                if ( tabAdapter != null ) {
                    tabAdapter.setOnRgsExtraCheckedChangedListener( new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
                        @Override
                        public void OnRgsExtraCheckedChanged( RadioGroup radioGroup, int checkedId, int index ){
                        }
                    } );
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        if ( warnServiceConnection != null ) {
            unbindService( warnServiceConnection );
        }
        if ( warnReceiver != null ) {
            this.unregisterReceiver( warnReceiver );
        }
        super.onDestroy();
    }

    private MenuPopWindow menuPopWindow;

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ){
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            showMenuDialog();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    private void showMenuDialog(){
        if ( itemsOnClick != null ) {
            menuPopWindow = new MenuPopWindow( this, itemsOnClick );
        }
        // 显示窗口
        if ( menuPopWindow != null ) {
            menuPopWindow.showAtLocation( this.findViewById( R.id.radioGroup_main ), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0 ); // 设置layout在PopupWindow中显示的位置
        }
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        public void onClick( View v ){

            switch ( v.getId() ) {
                case R.id.exit_menu_pop:
                    exitProcess();
                    break;
                case R.id.runinbackground_menu_pop:
                    runInBackground();
                    break;
                default:
                    break;
            }
            if ( menuPopWindow != null )
                menuPopWindow.dismiss();
        }

    };

    private void runInBackground(){
        startBackgroundService();
    }

    public void exitProcess(){
        Intent intent = new Intent();
        intent.setClass( this, BackgroundService.class );
        stopService( intent );
        // 清除通知
        if ( WarnUtils.cancelNotice( this ) ) {
            System.exit( 0 );
        }
        this.finish();
    }

    private void startBackgroundService(){
        if ( LocalSharePreference.commitBackground( this, true ) ) {
            Intent intent = new Intent();
            intent.setClass( this, BackgroundService.class );
            startService( intent );
            this.finish();
        }
    }

}
