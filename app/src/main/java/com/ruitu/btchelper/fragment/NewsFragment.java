package com.ruitu.btchelper.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.activity.NewsInfoActivity;
import com.ruitu.btchelper.adapter.MyPagerAdapter;
import com.ruitu.btchelper.adapter.NewsListAdapter;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.util.AsyncImageLoader;
import com.ruitu.btchelper.util.CacheUtil;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DialogUtils;
import com.ruitu.btchelper.util.ImgUtil;
import com.ruitu.btchelper.util.NetworkUtils;
import com.ruitu.btchelper.weight.MyAlertDialog;
import com.ruitu.btchelper.weight.RefreshListView;
import com.ruitu.btchelper.weight.RefreshListView.IOnLoadMoreListener;
import com.ruitu.btchelper.weight.RefreshListView.IOnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("HandlerLeak")
public class NewsFragment extends Fragment implements OnCheckedChangeListener, OnItemClickListener, OnPageChangeListener, DataHelper, IRequestContentCallback, IToStringMap, IOnRefreshListener, OnClickListener {
    public static NewsFragment getInstance(){
        return new NewsFragment();
    }

    private static final String TAG = NewsFragment.class.getSimpleName();
    private View mView;
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
 //   private ProgressBar mProgressBar;
    private ListView lv_index;
    private NewsListAdapter lv_index_adapter;
    private HashMap< Integer, List< News > > newsMap = new HashMap< Integer, List< News > >();
    private ArrayList< News > newsLists = new ArrayList< News >();
    private SwipeRefreshLayout sl_news;
    private ListView lv_news;
    private NewsListAdapter lv_news_adapter;
    private ListView lv_transaction;
    private SwipeRefreshLayout sl_transaction;
    private NewsListAdapter lv_transaction_adapter;
    private ListView lv_something;
    private SwipeRefreshLayout sl_something;
    private NewsListAdapter lv_something_adapter;
    private ListView lv_analyze;
    private SwipeRefreshLayout sl_analyze;
    private NewsListAdapter lv_analyze_adapter;
    private ListView lv_mining;
    private SwipeRefreshLayout sl_mining;
    private NewsListAdapter lv_mining_adapter;
    private int news_type = 1;
    private static final int INDEX = 1;
    private static final int NEWS = 2;
    private static final int TRANSACTION = 3;
    private static final int SOMETHING = 4;
    private static final int ANALYZE = 5;
    private static final int MINING = 6;
    private static final int NOTICE = 7;
    private static final String ACTION = "action_type";
    private static final String PAGE = "page";
    private int page = 1;
    // 广告滚动组件
    private ImageView newsImage;
    private ArrayList< News > noticeList = new ArrayList< News >();// 广告
    private Map< String, Drawable > drawables = new HashMap< String, Drawable >();
    private List< View > noticeViews = new ArrayList< View >();
    private RadioButton btn1, btn2, btn3, btn4, btn5;
    private RadioGroup mRadioGroup2;
    private TextView title_news;
    private int currentItem = 0; // 当前图片的索引号
    private ScheduledExecutorService scheduledExecutorService;
    private MyPagerAdapter noticeAdapter;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage( Message msg ){
            Bundle bundle = msg.getData();
            String url = NEWS_URL;
            int actionType = bundle.getInt( ACTION );
            switch ( msg.what ) {
                case NOTICE_ACTION_NEWS_INDEX:// 请求加载新闻首页的广告
                    try {
                        // 加载数据
                        loadNewsData( actionType, url );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                    break;
                case NOTICE_ACTION_NEWS:// 请求切换广告
                    if ( noticeList != null && ! noticeList.isEmpty() ) {
                        title_news.setText( noticeList.get( currentItem ).getNews_title() );
                        String icon = noticeList.get( currentItem ).getNews_icon();
                        Bitmap drawable = noticeList.get( currentItem ).getNews_icon_drawable();
                        if ( icon != null ) {
                            if ( drawable != null ) {
                                if ( newsImage != null )
                                    newsImage.setImageBitmap( drawable );
                            } else {
                                new AsyncImageLoader().loadNewsDrawable( newsImage, noticeList, currentItem );
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
                            case 4:
                                btn5.setChecked( true );
                                break;
                            default:
                                btn1.setChecked( true );
                                break;
                        }
                        break;
                    }
                    break;
                case NEWS_ACTION_NEWS:
                    try {
                        loadNewsData( actionType, url );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }

        }

    };

    private void updateAdapter( int actionType ){
        switch ( actionType ) {
            case INDEX:
                lv_index_adapter.reflashData( newsLists );
                break;
            case NEWS:
                lv_news_adapter.reflashData( newsLists );
                break;
            case TRANSACTION:
                lv_transaction_adapter.reflashData( newsLists );
                break;
            case SOMETHING:
                lv_something_adapter.reflashData( newsLists );
                break;
            case ANALYZE:
                lv_analyze_adapter.reflashData( newsLists );
                break;
            case MINING:
                lv_mining_adapter.reflashData( newsLists );
                break;
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        mView = inflater.inflate( R.layout.fragment3_main, container, false );
        init();
        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        sendLoadMessage( INDEX, NEWS_ACTION_NEWS );
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate( new ScrollTask(), 0, 2, TimeUnit. SECONDS);//源代码实现方式的问题，改为加快切换速度
    }

    @Override
    public void onAttach( Activity activity ){
        sendLoadMessage( NOTICE, NOTICE_ACTION_NEWS_INDEX );
        super.onAttach( activity );
    }

    @Override
    public void onPause(){
        scheduledExecutorService.shutdownNow();
        super.onPause();

    }

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {
        public void run(){
            synchronized ( newsImage ) {
                currentItem = ( currentItem + 1 ) % 4;
                mHandler.sendEmptyMessage( NOTICE_ACTION_NEWS );// 通过Handler切换图片
            }
        }
    }

    private void init(){
        try {

            mRadioGroup = ( RadioGroup ) mView.findViewById( R.id.radiogroup_fragment3 );
            mViewPager = ( ViewPager ) mView.findViewById( R.id.vp_fragment3_main );
          // mProgressBar = ( ProgressBar ) mView.findViewById( R.id.pb_fragment3_main );
            if ( mRadioGroup != null )
                mRadioGroup.setOnCheckedChangeListener( this );
            // 首页
            View index = View.inflate( getActivity(), R.layout.fragment3_page1, null );
            this.lv_index = ( ListView ) index.findViewById( R.id.lv_fragment3_page1 );
            View head = View.inflate( getActivity(), R.layout.page1_head, null );
            this.newsImage = ( ImageView ) head.findViewById( R.id.iv_fragment3_page1_head );
            title_news = ( TextView ) head.findViewById( R.id.tv_fragment3_page1_head );
            this.btn1 = ( RadioButton ) head.findViewById( R.id.btn1_fragment3_page1_head );
            this.btn2 = ( RadioButton ) head.findViewById( R.id.btn2_fragment3_page1_head );
            this.btn3 = ( RadioButton ) head.findViewById( R.id.btn3_fragment3_page1_head );
            this.btn4 = ( RadioButton ) head.findViewById( R.id.btn4_fragment3_page1_head );
            this.btn5 = ( RadioButton ) head.findViewById( R.id.btn5_fragment3_page1_head );
            mRadioGroup2 = ( RadioGroup ) head.findViewById( R.id.bottm_fragment3_page1_head );
            noticeAdapter = new MyPagerAdapter( noticeViews );
            this.newsImage.setOnClickListener( this );
            head.findViewById( R.id.rl_fragment3_page1_head ).setOnClickListener( this );

            lv_index.addHeaderView( head );
            lv_index_adapter = new NewsListAdapter( newsLists, getActivity() );
            lv_index.setAdapter( lv_index_adapter );
            lv_index.setOnItemClickListener( this );
            // 新闻
            View news = View.inflate( getActivity(), R.layout.fragment3_page2, null );
            this.lv_news = ( ListView ) news.findViewById( R.id.lv_fragment3_page2 );
            this.sl_news = ( SwipeRefreshLayout ) news.findViewById( R.id.ll_page_fragment3 );
            lv_news_adapter = new NewsListAdapter( newsLists, getActivity() );
            lv_news.setAdapter( lv_news_adapter );
            lv_news.setOnItemClickListener( this );
            sl_news.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            sl_news.setRefreshing( false );
                            loadNewsData( NEWS, NEWS_URL );
                        }
                    }, 5000 );
                }
            } );
            sl_news.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
            // 交易
            View transaction = View.inflate( getActivity(), R.layout.fragment3_page2, null );
            this.lv_transaction = ( ListView ) transaction.findViewById( R.id.lv_fragment3_page2 );
            this.sl_transaction = ( SwipeRefreshLayout ) transaction.findViewById( R.id.ll_page_fragment3 );
            lv_transaction_adapter = new NewsListAdapter( newsLists, getActivity() );
            lv_transaction.setAdapter( lv_transaction_adapter );
            lv_transaction.setOnItemClickListener( this );
            sl_transaction.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            sl_transaction.setRefreshing( false );
                            loadNewsData( TRANSACTION, NEWS_URL );
                        }
                    }, 5000 );
                }
            } );
            sl_transaction.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
            // 百科
            View something = View.inflate( getActivity(), R.layout.fragment3_page2, null );
            this.lv_something = ( ListView ) something.findViewById( R.id.lv_fragment3_page2 );
            this.sl_something = ( SwipeRefreshLayout ) something.findViewById( R.id.ll_page_fragment3 );
            lv_something_adapter = new NewsListAdapter( newsLists, getActivity() );
            lv_something.setAdapter( lv_something_adapter );
            lv_something.setOnItemClickListener( this );
            sl_something.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            sl_something.setRefreshing( false );
                            loadNewsData( SOMETHING, NEWS_URL );
                        }
                    }, 5000 );
                }
            } );
            sl_something.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
            // 分析
            View analyze = View.inflate( getActivity(), R.layout.fragment3_page2, null );
            this.lv_analyze = ( ListView ) analyze.findViewById( R.id.lv_fragment3_page2 );
            this.sl_analyze = ( SwipeRefreshLayout ) analyze.findViewById( R.id.ll_page_fragment3 );
            lv_analyze_adapter = new NewsListAdapter( newsLists, getActivity() );
            lv_analyze.setAdapter( lv_analyze_adapter );
            lv_analyze.setOnItemClickListener( this );
            sl_analyze.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            sl_analyze.setRefreshing( false );
                            loadNewsData( ANALYZE, NEWS_URL );
                        }
                    }, 5000 );
                }
            } );
            sl_analyze.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );
            // 挖矿
            View mining = View.inflate( getActivity(), R.layout.fragment3_page2, null );
            this.lv_mining = ( ListView ) mining.findViewById( R.id.lv_fragment3_page2 );
            this.sl_mining = ( SwipeRefreshLayout ) mining.findViewById( R.id.ll_page_fragment3 );
            lv_mining_adapter = new NewsListAdapter( newsLists, getActivity() );
            lv_mining.setAdapter( lv_mining_adapter );
            lv_mining.setOnItemClickListener( this );
            sl_mining.setOnRefreshListener( new OnRefreshListener() {
                @Override
                public void onRefresh(){
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run(){
                            sl_mining.setRefreshing( false );
                            loadNewsData( MINING, NEWS_URL );
                        }
                    }, 5000 );
                }
            } );
            sl_mining.setColorScheme( android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );

            List< View > list = new ArrayList< View >();
            list.add( index );
            list.add( news );
            list.add( transaction );
            list.add( something );
            list.add( analyze );
            list.add( mining );
            MyPagerAdapter myPagerAdapter = new MyPagerAdapter( list );
            mViewPager.setAdapter( myPagerAdapter );
            mViewPager.setOnPageChangeListener( this );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged( RadioGroup group, int checkedId ){
        switch ( checkedId ) {
            case R.id.index_fragment3:// 首页
                if ( mViewPager.getCurrentItem() != 0 ) {
                    mViewPager.setCurrentItem( 0 );
                }
                // 设置缓存
               //  setCache(INDEX);
                break;
            case R.id.news_fragment3:// 新闻
                if ( mViewPager.getCurrentItem() != 1 ) {
                    mViewPager.setCurrentItem( 1 );
                }
                // 设置缓存
                // setCache(NEWS);
                break;
            case R.id.transaction_fragment3:// 交易
                if ( mViewPager.getCurrentItem() != 2 ) {
                    mViewPager.setCurrentItem( 2 );
                }
                // 设置缓存
                // setCache(TRANSACTION);
                break;
            case R.id.something_fragment3:// 百科
                if ( mViewPager.getCurrentItem() != 3 )
                    mViewPager.setCurrentItem( 3 );
                // 设置缓存
                // setCache(SOMETHING);
                break;
            case R.id.analyze_fragment3:// 分析
                if ( mViewPager.getCurrentItem() != 4 )
                    mViewPager.setCurrentItem( 4 );
                // 设置缓存
                // setCache(ANALYZE);
                break;
            case R.id.mining_fragment3:
                if ( mViewPager.getCurrentItem() != 5 )
                    mViewPager.setCurrentItem( 5 );
                // 设置缓存
                // setCache(MINING);
                break;
            default:// 首页
                if ( mViewPager.getCurrentItem() != 0 )
                    mViewPager.setCurrentItem( 0 );
                break;
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
        page = 1;
        switch ( arg0 ) {
            case 0:// 首页
                mRadioGroup.check( R.id.index_fragment3 );
                news_type = INDEX;
                sendLoadMessage( INDEX, NEWS_ACTION_NEWS );
                break;
            case 1:// 新闻
                news_type = NEWS;
                mRadioGroup.check( R.id.news_fragment3 );
                sendLoadMessage( NEWS, NEWS_ACTION_NEWS );
                break;
            case 2:// 交易
                news_type = TRANSACTION;
                mRadioGroup.check( R.id.transaction_fragment3 );
                // 加载交易数据
                sendLoadMessage( TRANSACTION, NEWS_ACTION_NEWS );
                break;
            case 3:// 百科
                news_type = SOMETHING;
                mRadioGroup.check( R.id.something_fragment3 );
                // 加载百科数据
                sendLoadMessage( SOMETHING, NEWS_ACTION_NEWS );
                break;
            case 4:// 分析
                news_type = ANALYZE;
                mRadioGroup.check( R.id.analyze_fragment3 );
                // 加载分析数据
                sendLoadMessage( ANALYZE, NEWS_ACTION_NEWS );
                break;
            case 5:// 挖矿
                news_type = MINING;
                mRadioGroup.check( R.id.mining_fragment3 );
                // 加载挖矿数据
                sendLoadMessage( MINING, NEWS_ACTION_NEWS );
                break;
            default:
                news_type = INDEX;
                mRadioGroup.check( R.id.index_fragment3 );
                // 加载首页数据
                sendLoadMessage( INDEX, NEWS_ACTION_NEWS );
                break;
        }
    }

    private void sendLoadMessage( int actionType, int whatAction ){
        Bundle bundle = new Bundle();
        Message mess = Message.obtain( mHandler );
        mess.what = whatAction;
        bundle.putInt( ACTION, actionType );
        mess.setData( bundle );
        mHandler.sendMessage( mess );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResult( int requestAction, Boolean isSuccess, String content, Object extra ){
        Log.e( TAG, "requestAction=====" + requestAction );
        if ( isSuccess ) {
            if ( requestAction == NOTICE ) {
                updateNoticeData( requestAction, content );
            } else {
                updateNewsList( requestAction, content );
            }

        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_SHORT ).show();
        }
    }

    @SuppressWarnings("unchecked")
    private void updateNoticeData( int requestAction, String content ){
        Map< String, Object > map = ParseJson.parseNewsFromNet( content );
        List< News > list = ( List< News > ) map.get( "news" );
        noticeList.clear();
        if ( list != null && ! list.isEmpty() && list.size() >= 9 ) {
            for ( int i = 4; i < 9; i++ ) {
                noticeList.add( list.get( i ) );
            }
            for ( int postion = 0; postion < noticeList.size(); postion++ ) {
                if ( noticeList.get( postion ).getNews_icon() != null && ! "".equals( noticeList.get( postion ).getNews_icon() ) ) {
                    new AsyncImageLoader().loadNewsDrawable( null, noticeList, postion );
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updateNewsList( int requestAction, String content ){
        Map< String, Object > map = ParseJson.parseNewsFromNet( content );
        if ( map != null && ! map.isEmpty() ) {
            List< News > list = ( List< News > ) map.get( NEWS_LIST );
            if ( list != null && ! list.isEmpty() ) {
                newsLists.clear();
                newsLists.addAll( list );
                newsMap.clear();
                newsMap.put( requestAction, newsLists );
                updateAdapter( requestAction );
            }
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
        }
    }

    private void loadNewsData( int actionType, String url ){
        if ( NetworkUtils.isNetworkConnected( getActivity() ) ) {
            ClientEngine clientEngine = ClientEngine.getInstance( getActivity() );
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = actionType;
            packet.url = url.replace( "{1}", String.valueOf( actionType ) );
            packet.extra = null;
            packet.context = getActivity();
            packet.object = null;
            clientEngine.httpGetRequest( packet, this );
        } else {
            Toast.makeText( getActivity(), "网络连接异常", Toast.LENGTH_LONG ).show();
        }
    }



    @Override
    public Map< String, String > toStringMap(){
        Map< String, String > map = new HashMap< String, String >();
        map.put( PAGE, String.valueOf( page ) );
        map.put( NEWS_TYPE, String.valueOf( news_type ) );
        Log.e( TAG, map.toString() );
        return map;
    }

    @Override
    public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ){
        News newslist = null;
        if ( arg0 == lv_index ) {
            newslist = newsLists.get( arg2 - 1 );
        } else {
            newslist = newsLists.get( arg2 );
        }
        Intent intent = new Intent( getActivity(), NewsInfoActivity.class );
        Bundle bundle = new Bundle();
        bundle.putParcelable( NEWS_SER, newslist );
        intent.putExtras( bundle );
        startActivity( intent );
    }

    @Override
    public void OnRefresh(){
        onPageSelected( mViewPager.getCurrentItem() );
    }

    @Override
    public void onClick( View v ){
        switch ( v.getId() ) {
            case R.id.iv_fragment3_page1_head:// 跳转到新闻页面
                toNewsInfo();
                break;
            case R.id.rl_fragment3_page1_head:// 跳转到新闻页面
                toNewsInfo();
                break;
        }
    }

    private void toNewsInfo(){
        Log.e( TAG, "currentItem===" + currentItem );
        if ( noticeList != null && ! noticeList.isEmpty() ) {
            News newslist = noticeList.get( currentItem );
            Intent intent = new Intent( getActivity(), NewsInfoActivity.class );
            Bundle bundle = new Bundle();
            bundle.putParcelable( NEWS_SER, newslist );
         //   System.out.print( newslist.getNews_info_url() );
            intent.putExtras( bundle );
            startActivity( intent );
        } else {
            DialogUtils.showNoDataDialog( getActivity() );
        }
    }

}
