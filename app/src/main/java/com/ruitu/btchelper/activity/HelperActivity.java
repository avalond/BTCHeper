package com.ruitu.btchelper.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.adapter.MyPagerAdapter;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.weight.MyAlertDialog;

@SuppressLint("CutPasteId")
public class HelperActivity extends BaseActivity implements OnPageChangeListener,
        OnClickListener {
    private ViewPager vw;
    private RadioButton btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
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
        this.findViewById(R.id.btn_help_top).setOnClickListener(this);
        this.findViewById(R.id.right_btn_help_top).setOnClickListener(this);
        this.vw = (ViewPager) this.findViewById(R.id.viewPager_help);
        this.btn1 = (RadioButton) this.findViewById(R.id.btn1_help);
        this.btn2 = (RadioButton) this.findViewById(R.id.btn2_help);
        this.btn3 = (RadioButton) this.findViewById(R.id.btn3_help);
        this.btn4 = (RadioButton) this.findViewById(R.id.btn4_help);

        View view1 = View.inflate(this, R.layout.page_help, null);
        ImageView iv1 = (ImageView) view1.findViewById(R.id.iv_page_help);
        iv1.setBackgroundResource(R.drawable.help1);
        View view2 = View.inflate(this, R.layout.page_help, null);
        ImageView iv2 = (ImageView) view2.findViewById(R.id.iv_page_help);
        iv2.setBackgroundResource(R.drawable.help2);
        View view3 = View.inflate(this, R.layout.page_help, null);
        ImageView iv3 = (ImageView) view3.findViewById(R.id.iv_page_help);
        iv3.setBackgroundResource(R.drawable.help3);
        View view4 = View.inflate(this, R.layout.page_help, null);
        ImageView iv4 = (ImageView) view4.findViewById(R.id.iv_page_help);
        iv4.setBackgroundResource(R.drawable.help4);
        Button btn = (Button) view4.findViewById(R.id.btn_page_help);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(this);
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
        case R.id.btn_page_help:// 跳转至主界面
            this.finish();
            break;
        case R.id.btn_help_top:
            this.finish();
            break;
        case R.id.right_btn_help_top:
            // 判断登录
            String username = LocalSharePreference.getUserName(this);
            if (username == null || "".equals(username)) {
                // final MyAlertDialog my = new MyAlertDialog(this);
                final MyAlertDialog my = MyAlertDialog.getInstance(this);
                my.setTitle(R.string.not_text);
                my.setMessage(R.string.not_login);
                my.setPositiveButton(
                        this.getResources().getString(R.string.ok_text),
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HelperActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                my.dismiss();
                            }
                        });
                my.setNegativeButton(
                        this.getResources().getString(R.string.cancel_text),
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                my.dismiss();
                            }
                        });

            } else {
                Intent intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
            }
            break;
        default:
            break;
        }
    }
}
