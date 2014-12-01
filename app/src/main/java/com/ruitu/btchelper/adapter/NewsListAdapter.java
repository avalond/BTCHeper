package com.ruitu.btchelper.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruitu.btchelper.R;
import com.ruitu.btchelper.adapter.OrdersAdapter.ViewHolder;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.util.AsyncImageLoader;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.DateUtil;
import com.ruitu.btchelper.util.ImgUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {
    private Context mContext;
    private List< News > mNewsLists;
    private ImageLoader mImageLoader;

    public NewsListAdapter( List< News > newsList, Context context ){
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init( ImgUtil.getImageConfig( context ) );
        this.mContext = context;
        reflashData( newsList );
    }

    public void reflashData( List< News > newsList ){
        if ( newsList != null ) {
            this.mNewsLists = newsList;
        } else {
            this.mNewsLists.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mNewsLists.size();
    }

    @Override
    public Object getItem( int position ){
        return mNewsLists.get( position );
    }

    @Override
    public long getItemId( int position ){
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        ViewHolder viewHolder = null;
        if ( convertView == null ) {
            convertView = View.inflate( this.mContext, R.layout.fragment3_listview_item, null );
            viewHolder = new ViewHolder();
            viewHolder.m_iv = ( ImageView ) convertView.findViewById( R.id.iv_fragment3_listview_item );
            viewHolder.m_title = ( TextView ) convertView.findViewById( R.id.title_fragment3_listview_item );
            viewHolder.m_author = ( TextView ) convertView.findViewById( R.id.author_fragment3_listview_item );
            viewHolder.m_time = ( TextView ) convertView.findViewById( R.id.tiem_fragment3_listview_item );
            convertView.setTag( viewHolder );
        } else {
            viewHolder = ( ViewHolder ) convertView.getTag();
        }
        if ( mNewsLists != null && ! mNewsLists.isEmpty() ) {
            News newsList = mNewsLists.get( position );
            if ( newsList.getNews_icon() != null ) {
                mImageLoader.displayImage( newsList.getNews_icon(), viewHolder.m_iv );
            }
            if ( newsList.getNews_title() != null )
                viewHolder.m_title.setText( newsList.getNews_title() );
            if ( newsList.getNews_author() != null )
                viewHolder.m_author.setText( newsList.getNews_author() );
            if ( newsList.getNews_time() != null ) {
                long time = Long.parseLong( newsList.getNews_time() );
                String time_str = DateUtil.getShortDateStr( time );
                viewHolder.m_time.setText( time_str );
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView m_iv;
        public TextView m_title;
        public TextView m_author;
        public TextView m_time;
    }
}
