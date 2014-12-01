package com.ruitu.btchelper.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import com.ruitu.btchelper.adapter.MyPagerAdapter;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class NavigationActivity extends Activity implements
        OnPageChangeListener, OnClickListener {
    private ViewPager vw;
    private RadioButton btn1, btn2, btn3, btn4;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // 初始化控件
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vw.setCurrentItem(0);
        this.btn1.setChecked(true);
    }

    /**
     * 初始化控件
     */
    private void init() {
        this.vw = (ViewPager) this.findViewById(R.id.viewPager_navigation);
        this.btn1 = (RadioButton) this.findViewById(R.id.btn1_navigation);
        this.btn2 = (RadioButton) this.findViewById(R.id.btn2_navigation);
        this.btn3 = (RadioButton) this.findViewById(R.id.btn3_navigation);
        this.btn4 = (RadioButton) this.findViewById(R.id.btn4_navigation);
        // 第一个导航界面
        View view1 = View.inflate(this, R.layout.page_navigation, null);
        ImageView iv1 =(ImageView) view1.findViewById(R.id.iv_navigation);
        iv1.setBackgroundResource(R.drawable.ic_launcher);
        // 第二个导航界面
        View view2 = View.inflate(this, R.layout.page_navigation, null);
        ImageView iv2 =(ImageView) view1.findViewById(R.id.iv_navigation);
        iv2.setBackgroundResource(R.drawable.xtsz_jt1);
        // 第三个导航界面
        View view3 = View.inflate(this, R.layout.page_navigation, null);
        ImageView iv3 =(ImageView) view1.findViewById(R.id.iv_navigation);
        iv3.setBackgroundResource(R.drawable.news_pic);
        // 第四个导航界面
        View view4 = View.inflate(this, R.layout.page_navigation, null);
        ImageView iv4 =(ImageView) view1.findViewById(R.id.iv_navigation);
        iv4.setBackgroundResource(R.drawable.xtsz4);
        Button btn = (Button) view4.findViewById(R.id.btn_page_navigation);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(this);
        // 将导航界面添加到ViewPager中
        List<View> list = new ArrayList<View>();
        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);
        MyPagerAdapter adapter = new MyPagerAdapter(list);
        this.vw.setAdapter(adapter);
        this.vw.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        System.out.println(arg0);
        switch (arg0) {
        case 0:
            this.btn1.setChecked(true);
            break;
        case 1:
            this.btn2.setChecked(true);
            break;
        case 2:
            this.btn3.setChecked(true);
            break;
        case 3:
            this.btn4.setChecked(true);
            break;
        default:
            this.btn1.setChecked(true);
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_page_navigation:// 跳转至主界面
            // 将登录的第一次标志写入文件中
            this.sharedPreferences = this.getSharedPreferences(
                    DataHelper.SYSTEM_SETTING, MODE_PRIVATE);
            Editor editor = this.sharedPreferences.edit();
            editor.putBoolean(DataHelper.FIRST_LOGIN, false);
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.entry_anim, R.anim.exit_anim);
            this.finish();
            break;
        default:
            break;
        }
    }
}