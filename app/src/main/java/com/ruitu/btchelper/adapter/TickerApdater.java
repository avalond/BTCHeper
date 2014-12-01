package com.ruitu.btchelper.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.MediaPlayerUtils;
import com.ruitu.btchelper.util.PlatformNameUtils;

/**
 * 绑定市场行情数据的adapter
 */
@SuppressLint("NewApi")
public class TickerApdater extends BaseAdapter {
    private static final String TAG = TickerApdater.class.getSimpleName();
    private List<Ticker> mTickers = new ArrayList<Ticker>();
    private List<String> platnames;
    private Context mContext;
    private Map<String, Ticker> mCaTickers;

    public TickerApdater(Context context, List<Ticker> tickers,
            Map<String, Ticker> cacheTickers, List<String> platforms) {
        this.mContext = context;
        this.platnames = platforms;
        refreshData(tickers, cacheTickers);
    }

    public void refreshData(List<Ticker> tickers,
            Map<String, Ticker> cacheTickers) {
        if (tickers != null) {
            this.mTickers = tickers;
        } else {
            this.mTickers.clear();
        }
        if (cacheTickers != null) {
            this.mCaTickers = cacheTickers;
        } else {
            this.mCaTickers.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTickers.size();
    }

    @Override
    public Object getItem(int position) {
        return mTickers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.lv_item_fragment1_main, null);
            viewHolder = new ViewHolder();
            viewHolder.m_name = (TextView) convertView
                    .findViewById(R.id.tv1_listview_itme);
            viewHolder.m_last = (TextView) convertView
                    .findViewById(R.id.tv2_listview_itme);
            viewHolder.m_buy = (TextView) convertView
                    .findViewById(R.id.tv3_listview_itme);
            viewHolder.m_sell = (TextView) convertView
                    .findViewById(R.id.tv4_listview_itme);
            viewHolder.m_volume = (TextView) convertView
                    .findViewById(R.id.tv5_listview_itme);
            viewHolder.m_buy_iv = (ImageView) convertView
                    .findViewById(R.id.buy_iv);
            viewHolder.m_sell_iv = (ImageView) convertView
                    .findViewById(R.id.sell_iv);
            viewHolder.m_last_iv = (ImageView) convertView
                    .findViewById(R.id.last_iv);
            viewHolder.m_volume_iv = (ImageView) convertView
                    .findViewById(R.id.volume_iv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {

            if (mTickers != null && mTickers.size() > 0) {
                final Ticker ticker = mTickers.get(position);
                String name = ticker.getName();
                String buy = ticker.getBuy();
                String sell = ticker.getSell();
                String last = ticker.getLast();
                String volume = ticker.getVolume();

                if (volume != null) {
                    double volume_d = Double.parseDouble(ticker.getVolume());
                    if (!mCaTickers.isEmpty()
                            && mCaTickers.containsKey(ticker.getName())) {
                        Ticker tickerCache = mCaTickers.get(ticker.getName());
                        if (tickerCache.getVolume() != null) {
                            if (Double.parseDouble(tickerCache.getVolume()) > volume_d) {
                                viewHolder.m_volume_iv
                                        .setVisibility(View.VISIBLE);
                                viewHolder.m_volume_iv
                                        .setImageResource(R.drawable.down);
                                viewHolder.m_volume.setTextColor(Color.GREEN);
                            } else if (Double.parseDouble(tickerCache
                                    .getVolume()) == volume_d) {
                                viewHolder.m_volume_iv
                                        .setVisibility(View.INVISIBLE);
                                viewHolder.m_volume.setTextColor(mContext
                                        .getResources().getColor(
                                                R.color.argb_999999));
                            } else {
                                viewHolder.m_volume_iv
                                        .setVisibility(View.VISIBLE);
                                viewHolder.m_volume_iv
                                        .setImageResource(R.drawable.up);
                                viewHolder.m_volume.setTextColor(Color.RED);
                            }
                        }
                    }
                    volume = DataUtil.changeToTwoPoint(ticker.getVolume());
                }
                if (buy != null) {
                    double buy_d = Double.parseDouble(ticker.getBuy());
                    if (!mCaTickers.isEmpty()
                            && mCaTickers.containsKey(ticker.getName())) {
                        Ticker tickerCache = mCaTickers.get(ticker.getName());
                        if (tickerCache.getBuy() != null) {
                            if (Double.parseDouble(tickerCache.getBuy()) > buy_d) {
                                viewHolder.m_buy_iv.setVisibility(View.VISIBLE);
                                viewHolder.m_buy_iv
                                        .setImageResource(R.drawable.down);
                                viewHolder.m_buy.setTextColor(Color.GREEN);
                            } else if (Double.parseDouble(tickerCache.getBuy()) == buy_d) {
                                viewHolder.m_buy_iv
                                        .setVisibility(View.INVISIBLE);
                                viewHolder.m_buy.setTextColor(mContext
                                        .getResources().getColor(
                                                R.color.argb_999999));
                            } else {
                                viewHolder.m_buy_iv.setVisibility(View.VISIBLE);
                                viewHolder.m_buy_iv
                                        .setImageResource(R.drawable.up);
                                viewHolder.m_buy.setTextColor(Color.RED);
                            }
                        }
                    }
                    buy = DataUtil.changeToTwoPoint(ticker.getBuy());
                }
                if (sell != null) {
                    double sell_d = Double.parseDouble(ticker.getSell());
                    if (!mCaTickers.isEmpty()
                            && mCaTickers.containsKey(ticker.getName())) {
                        Ticker tickerCache = mCaTickers.get(ticker.getName());
                        if (tickerCache.getSell() != null) {
                            if (Double.parseDouble(tickerCache.getSell()) > sell_d) {
                                viewHolder.m_sell_iv
                                        .setVisibility(View.VISIBLE);
                                viewHolder.m_sell_iv
                                        .setImageResource(R.drawable.down);
                                viewHolder.m_sell.setTextColor(Color.GREEN);
                            } else if (Double
                                    .parseDouble(tickerCache.getSell()) == sell_d) {
                                viewHolder.m_sell_iv
                                        .setVisibility(View.INVISIBLE);
                                viewHolder.m_sell.setTextColor(mContext
                                        .getResources().getColor(
                                                R.color.argb_999999));
                            } else {
                                viewHolder.m_sell_iv
                                        .setVisibility(View.VISIBLE);
                                viewHolder.m_sell_iv
                                        .setImageResource(R.drawable.up);
                                viewHolder.m_sell.setTextColor(Color.RED);
                            }
                        }
                    }
                    sell = DataUtil.changeToTwoPoint(ticker.getSell());
                }
                if (last != null) {
                    double last_d = Double.parseDouble(ticker.getLast());
                    if (!mCaTickers.isEmpty()
                            && mCaTickers.containsKey(ticker.getName())) {
                        Ticker tickerCache = mCaTickers.get(ticker.getName());
                        if (tickerCache.getLast() != null) {
                            if (Double.parseDouble(tickerCache.getLast()) > last_d) {
                                viewHolder.m_last_iv
                                        .setVisibility(View.VISIBLE);
                                viewHolder.m_last_iv
                                        .setImageResource(R.drawable.down);
                                viewHolder.m_last.setTextColor(Color.GREEN);
                            } else if (Double
                                    .parseDouble(tickerCache.getLast()) == last_d) {
                                viewHolder.m_last_iv
                                        .setVisibility(View.INVISIBLE);
                                viewHolder.m_last.setTextColor(mContext
                                        .getResources().getColor(
                                                R.color.argb_999999));
                            } else {
                                viewHolder.m_last_iv
                                        .setVisibility(View.VISIBLE);
                                viewHolder.m_last_iv
                                        .setImageResource(R.drawable.up);
                                viewHolder.m_last.setTextColor(Color.RED);
                            }
                        }
                    }
                    last = DataUtil.changeToTwoPoint(ticker.getLast());
                }
                if (platnames.get(0).equals(name)
                        || platnames.get(4).equals(name)
                        || platnames.get(5).equals(name)) {
                    if (last == null)
                        viewHolder.m_last.setText(R.string.no_data);
                    else {
                        viewHolder.m_last.setText("$" + last);
                    }
                    if (buy == null)
                        viewHolder.m_buy.setText(R.string.no_data);
                    else
                        viewHolder.m_buy.setText("$" + buy);
                    if (sell == null)
                        viewHolder.m_sell.setText(R.string.no_data);
                    else
                        viewHolder.m_sell.setText("$" + sell);
                } else {
                    if (last == null)

                        viewHolder.m_last.setText(R.string.no_data);
                    else {
                        viewHolder.m_last.setText("¥" + last);
                    }
                    if (buy == null)
                        viewHolder.m_buy.setText(R.string.no_data);
                    else
                        viewHolder.m_buy.setText("¥" + buy);
                    if (sell == null)
                        viewHolder.m_sell.setText(R.string.no_data);
                    else
                        viewHolder.m_sell.setText("¥" + sell);
                }
                viewHolder.m_name.setText(name);
                if (volume == null)
                    viewHolder.m_volume.setText(R.string.no_data);
                else
                    viewHolder.m_volume.setText(volume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        TextView m_name;
        TextView m_last;
        ImageView m_last_iv;
        TextView m_buy;
        ImageView m_buy_iv;
        TextView m_sell;
        ImageView m_sell_iv;
        TextView m_volume;
        ImageView m_volume_iv;
    }
}