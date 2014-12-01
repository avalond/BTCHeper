package com.ruitu.btchelper.adapter;

import java.util.List;

import com.ruitu.btchelper.fragment.TickerFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NoticeAdapter extends PagerAdapter {
    private Context mContext;
    private ImageView iv;
    private List<Bitmap> cacheImg2;
    private Fragment mFragment;

    public NoticeAdapter(Context context, List<Bitmap> cacheimg,
            Fragment fragment) {
        this.cacheImg2 = cacheimg;
        this.mContext = context;
        this.mFragment = fragment;
    }

    @Override
    public int getCount() {
        return cacheImg2.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        iv = new ImageView(mContext);
        iv.setTag(position); // Add tag
        try {
            Bitmap bm = cacheImg2.get(position);
            iv.setImageBitmap(bm);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        ((ViewPager) container).addView(iv);
        return iv;
    }

}
