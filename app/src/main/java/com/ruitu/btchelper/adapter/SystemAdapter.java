package com.ruitu.btchelper.adapter;

import java.util.List;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.adapter.NewsListAdapter.ViewHolder;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.domain.SysSetting;
import com.ruitu.btchelper.util.AsyncImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemAdapter extends BaseAdapter {
    private Context mContext;
    private List<SysSetting> mSysSettings;

    public SystemAdapter(Context context, List<SysSetting> list) {
        this.mContext = context;
        reflashData(list);
    }

    public void reflashData(List<SysSetting> list) {
        if (list != null) {
            this.mSysSettings = list;
        } else {
            this.mSysSettings.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSysSettings.size();
    }

    @Override
    public Object getItem(int position) {
        return mSysSettings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(this.mContext, R.layout.fragment4_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.m_iv = (ImageView) convertView
                    .findViewById(R.id.iv_left_fragment4_item);
            viewHolder.m_text = (TextView) convertView
                    .findViewById(R.id.tv_fragment4_item);
            viewHolder.m_setting_text = (TextView) convertView
                    .findViewById(R.id.tv_right_fragment4_item);
            viewHolder.m_setting_iv = (ImageView) convertView
                    .findViewById(R.id.iv_right_fragment4_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mSysSettings != null && !mSysSettings.isEmpty()) {
            SysSetting sysSetting = mSysSettings.get(position);
            viewHolder.m_iv.setBackgroundResource(sysSetting.getSys_icon());
            viewHolder.m_text.setText(sysSetting.getSys_text());
            if (sysSetting.getSys_setting_icon() != 0) {
                viewHolder.m_setting_iv.setVisibility(View.VISIBLE);
                viewHolder.m_setting_text.setVisibility(View.INVISIBLE);
                viewHolder.m_setting_iv.setBackgroundResource(sysSetting
                        .getSys_setting_icon());
            }
            if (sysSetting.getSys_setting_text() != null
                    && !"".equals(sysSetting.getSys_setting_text())) {
                viewHolder.m_setting_iv.setVisibility(View.INVISIBLE);
                viewHolder.m_setting_text.setVisibility(View.VISIBLE);
                viewHolder.m_setting_text.setText(sysSetting
                        .getSys_setting_text());
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView m_iv;
        public TextView m_text;
        public TextView m_setting_text;
        public ImageView m_setting_iv;
    }

}
