package com.ruitu.btchelper.adapter;

import java.util.Date;
import java.util.List;

import com.ruitu.btchelper.adapter.OrdersAdapter.ViewHolder;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.domain.Transaction;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.DateUtil;
import com.ruitu.btchelper.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TransactionAdapter extends BaseAdapter {
    private static final String TAG = TransactionAdapter.class.getSimpleName();
    private List< Transaction > mTransactions;
    private Context mContext;

    public TransactionAdapter( List< Transaction > tracnsactions, Context context ){
        this.mContext = context;
        refreshData( tracnsactions );
    }

    public void refreshData( List< Transaction > tracnsactions ){
        if ( tracnsactions != null ) {
            this.mTransactions = tracnsactions;
        } else {
            this.mTransactions.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mTransactions.size();
    }

    @Override
    public Object getItem( int position ){
        return mTransactions.get( position );
    }

    @Override
    public long getItemId( int position ){
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        ViewHolder viewHolder = null;
        if ( convertView == null ) {
            convertView = View.inflate( this.mContext, R.layout.listview_page2_fragment2_item, null );
            viewHolder = new ViewHolder();
            viewHolder.m_trans_time = ( TextView ) convertView.findViewById( R.id.tv_trans_time );
            viewHolder.m_trans_price = ( TextView ) convertView.findViewById( R.id.tv_trans_price );
            viewHolder.m_trans_vol = ( TextView ) convertView.findViewById( R.id.tv_trans_amount );
            viewHolder.m_iv_trans = convertView.findViewById( R.id.v_trans_amount );
            viewHolder.m_iv_trans_empty = convertView.findViewById( R.id.v_trans_amount_empty );
            convertView.setTag( viewHolder );
        } else {
            viewHolder = ( ViewHolder ) convertView.getTag();
        }
        if ( mTransactions != null && ! mTransactions.isEmpty() ) {
            Transaction transaction = mTransactions.get( position );
            if ( transaction.getTra_time() != null )
                viewHolder.m_trans_time.setText( ( transaction.getTra_time() ) );
            if ( transaction.getTra_price() != null )

                viewHolder.m_trans_price.setText( transaction.getTra_price() );
            if ( transaction.getTra_volume() != null ) {
                String text = DataUtil.changeToTwoPoint( transaction.getTra_volume() );
                if ( text != null )
                    viewHolder.m_trans_vol.setText( text );
            }
        }
        updateRateView( position, viewHolder );
        return convertView;
    }

    private void updateRateView( int pos, ViewHolder viewHolder ){
        if ( mTransactions != null && ! mTransactions.isEmpty() ) {
            try {

                Transaction transaction = mTransactions.get( pos );
                if ( transaction == null )
                    return;
                double vol = Double.parseDouble( transaction.getTra_volume() );
                double max = transaction.getMax_volume();
                double rateBuy = vol / max;
                double rateBuyEmpty = 1 - rateBuy;
                viewHolder.m_iv_trans.setLayoutParams( getParamByWeight( 1 - rateBuy ) );
                viewHolder.m_iv_trans_empty.setLayoutParams( getParamByWeight( 1 - rateBuyEmpty ) );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

    }

    private LinearLayout.LayoutParams getParamByWeight( double weight ){
        int value = ( int ) ( weight * 100 );
        if ( value < 1 ) {
            value = 1;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, value );
        return params;
    }

    public class ViewHolder {
        public TextView m_trans_time;
        public TextView m_trans_price;
        public TextView m_trans_vol;
        public View m_iv_trans;
        public View m_iv_trans_empty;
    }
}
