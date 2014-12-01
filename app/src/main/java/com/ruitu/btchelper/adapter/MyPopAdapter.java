package com.ruitu.btchelper.adapter;

import com.ruitu.btchelper.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyPopAdapter extends BaseAdapter {
    private Context mContext;
    private String[] arr;

    public MyPopAdapter(Context context, String[] arr) {
        this.arr = arr;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int position) {
        return arr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.fragment4_pop_item,
                    null);
            TextView tv = (TextView) convertView
                    .findViewById(R.id.time_fragment4);
            if (this.arr.length != 0) {
                String text = this.arr[position];
                tv.setText(text);
            }
        }
        return convertView;
    }

}
