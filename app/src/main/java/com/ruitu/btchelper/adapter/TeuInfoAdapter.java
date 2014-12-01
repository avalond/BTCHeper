package com.ruitu.btchelper.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.adapter.TickerApdater.ViewHolder;
import com.ruitu.btchelper.domain.Teu;
import com.ruitu.btchelper.util.DataUtil;

public class TeuInfoAdapter extends BaseAdapter {
    private List<Teu> mTeus;
    private Context mContext;

    public TeuInfoAdapter(List<Teu> teus, Context context) {
        this.mContext = context;
        this.mTeus = teus;
        refreshData(teus);
    }

    private void refreshData(List<Teu> teus) {
        if (teus != null) {
            this.mTeus = teus;
        } else {
            this.mTeus.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTeus.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mTeus.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.teu_info_item, null);
            viewHolder = new ViewHolder();
            viewHolder.m_month = (TextView) convertView
                    .findViewById(R.id.month_item);
            viewHolder.m_diff = (TextView) convertView
                    .findViewById(R.id.diff_item);
            viewHolder.m_month_income = (TextView) convertView
                    .findViewById(R.id.month_income_item);
            viewHolder.m_month_count = (TextView) convertView
                    .findViewById(R.id.month_count_item);
            viewHolder.m_count_income = (TextView) convertView
                    .findViewById(R.id.count_income_item);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mTeus != null && mTeus.size() > 0) {
            try {
                Teu teu = mTeus.get(position);
                viewHolder.m_month.setText(teu.getDate_str());
                viewHolder.m_diff.setText(DataUtil.changeToNoPoint(String
                        .valueOf(teu.getDiff_d())));
                viewHolder.m_month_income.setText(DataUtil
                        .changeToTwoPoint(String.valueOf(teu.getDay_income())));
                viewHolder.m_month_count.setText(DataUtil
                        .changeToTwoPoint(String.valueOf(teu.getDay_gain())));
                viewHolder.m_count_income.setText(DataUtil
                        .changeToTwoPoint(String.valueOf(teu.getSum_gain())));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return convertView;
    }

    public class ViewHolder {
        TextView m_month;
        TextView m_diff;
        TextView m_month_income;
        TextView m_month_count;
        TextView m_count_income;
    }
}
