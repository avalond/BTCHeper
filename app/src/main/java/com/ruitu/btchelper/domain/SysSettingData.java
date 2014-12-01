package com.ruitu.btchelper.domain;

import java.util.ArrayList;
import java.util.List;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.util.DateUtil;

import android.content.Context;

public class SysSettingData {
    public static List<SysSetting> getSysList(Context context){
        List<SysSetting> list = new ArrayList<SysSetting>();
        String[] arr = context.getResources().getStringArray(R.array.sys_text_arr);
        Long  time1 =LocalSharePreference.getMarketInterval(context);
        Long time2 = LocalSharePreference.getDetailInterval(context);
        
        SysSetting sys1 = new SysSetting(R.drawable.xtsz1, arr[0], null, R.drawable.xtsz_jt1);
        SysSetting sys2 = new SysSetting(R.drawable.xtsz2, arr[1], DateUtil.getMinuteStr(time1)+"分钟", 0);
        SysSetting sys3 = new SysSetting(R.drawable.xtsz3, arr[2], DateUtil.getMinuteStr(time2)+"分钟", 0);
        SysSetting sys4 = new SysSetting(R.drawable.xtsz4, arr[3], null, R.drawable.xtsz_jt1);
        SysSetting sys5 = new SysSetting(R.drawable.xtsz5, arr[4], null, R.drawable.xtsz_jt1);
        SysSetting sys6 = new SysSetting(R.drawable.xtsz6, arr[5], null, R.drawable.xtsz_jt1);
        list.add(sys1);
        list.add(sys2);
        list.add(sys3);
        list.add(sys4);
        list.add(sys5);
        list.add(sys6);
        return list;
    }
    public static List<SysSetting> getLeftMenuList(Context context){
        List<SysSetting> list = new ArrayList<SysSetting>();
        String[] arr = context.getResources().getStringArray(R.array.left_menu_arr);
        Long  time1 =LocalSharePreference.getMarketInterval(context);
        Long time2 = LocalSharePreference.getDetailInterval(context);

        SysSetting sys1 = new SysSetting(R.drawable.left_menu_t1, arr[0], null, R.drawable.left_menu_right);
        SysSetting sys2 = new SysSetting(R.drawable.left_menu_t2, arr[1], DateUtil.getMinuteStr(time1)+"分钟", 0);
        SysSetting sys3 = new SysSetting(R.drawable.left_menu_t3, arr[2], DateUtil.getMinuteStr(time2)+"分钟", 0);
        SysSetting sys4 = new SysSetting(R.drawable.left_menu_t4, arr[3], null, R.drawable.left_menu_right);
        SysSetting sys5 = new SysSetting(R.drawable.left_menu_t5, arr[4], null, R.drawable.left_menu_right);
        SysSetting sys6 = new SysSetting(R.drawable.left_menu_t6, arr[5], null, R.drawable.left_menu_right);
        SysSetting sys7 = new SysSetting(R.drawable.left_menu_t7, arr[6], null, R.drawable.left_menu_right);
        list.add(sys1);
        list.add(sys2);
        list.add(sys3);
        list.add(sys4);
        list.add(sys5);
        list.add(sys6);
        list.add( sys7 );
        return list;
    }
}
