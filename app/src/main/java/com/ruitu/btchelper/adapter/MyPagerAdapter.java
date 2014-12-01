package com.ruitu.btchelper.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {
    private List<View> list;

    public MyPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (null != list)
            return list.size();
        else
            return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (list != null && !list.isEmpty()) {
            View pager = list.get(position);
            container.addView(pager);
            return pager;
        } else {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (list == null || list.isEmpty())
            return;
        View view = list.get(position);
        container.removeView(view);
    }

}
