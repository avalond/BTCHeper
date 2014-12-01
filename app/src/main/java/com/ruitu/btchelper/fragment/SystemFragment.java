package com.ruitu.btchelper.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.activity.AboutActivity;
import com.ruitu.btchelper.activity.HelperActivity;
import com.ruitu.btchelper.adapter.MyPopAdapter;
import com.ruitu.btchelper.adapter.SystemAdapter;
import com.ruitu.btchelper.domain.SysSetting;
import com.ruitu.btchelper.domain.SysSettingData;
import com.ruitu.btchelper.setting.LocalSharePreference;
import com.ruitu.btchelper.setting.RingtoneUtils;
import com.ruitu.btchelper.setting.UpdateManager;
import com.ruitu.btchelper.util.DateUtil;
import com.ruitu.btchelper.util.MediaPlayerUtils;

public class SystemFragment extends Fragment implements OnClickListener,
        OnItemClickListener {
    public static SystemFragment getInstance() {
        return new SystemFragment();
    }

    private static final String TAG = SystemFragment.class.getSimpleName();
    private View mView;
    private Button cancel_btn;
    private ListView mListView;
    private List<SysSetting> mList = new ArrayList<SysSetting>();
    private SystemAdapter mSystemAdapter;
    private String mRingtone;
    private RingtoneUtils mRingtoneUtils;
    private List<String> mStrings = new ArrayList<String>();
    private List<Uri> mUris = new ArrayList<Uri>();
    private Uri mRingtoneUri;
    private Map<String, Uri> map = new HashMap<String, Uri>();
    private UpdateManager mUpdateManager;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment4_main, container, false);
        init();
        return mView;
    }

    private void init() {
        mRingtoneUtils = new RingtoneUtils(getActivity());
        mUpdateManager = new UpdateManager(getActivity());
        this.cancel_btn = (Button) mView.findViewById(R.id.cancel_btn_top);
        this.mListView = (ListView) mView.findViewById(R.id.lv_fragment4);
        String username = LocalSharePreference.getUserName(getActivity());
        if (username == null || "".equals(username)) {
            this.cancel_btn.setVisibility(View.INVISIBLE);
        } else {
            this.cancel_btn.setVisibility(View.VISIBLE);
        }
        mList = SysSettingData.getSysList(getActivity());
        long depth_time = LocalSharePreference.getDetailInterval(getActivity());
        long ticker_time = LocalSharePreference
                .getMarketInterval(getActivity());
        String depth_time_str = DateUtil.getMinuteStr(depth_time);
        String ticker_time_str = DateUtil.getMinuteStr(ticker_time);
        mList.get(1).setSys_setting_text(ticker_time_str);
        mList.get(2).setSys_setting_text(depth_time_str);
        mSystemAdapter = new SystemAdapter(getActivity(), mList);
        if (mListView != null) {
            mListView.setAdapter(mSystemAdapter);
            mListView.setOnItemClickListener(this);
        }
        if (cancel_btn != null)
            this.cancel_btn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRingtoneUtils != null) {
            mStrings = mRingtoneUtils
                    .getRingtoneTitleList(RingtoneManager.TYPE_NOTIFICATION);
            mUris = mRingtoneUtils
                    .getRingtoneUriList(RingtoneManager.TYPE_NOTIFICATION);
        }
        if (mStrings != null && mUris != null & !mStrings.isEmpty()
                && !mUris.isEmpty()) {
            for (int i = 0; i < mStrings.size(); i++) {
                map.put(mStrings.get(i), mUris.get(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.cancel_btn_top:// 注销用户
            if (LocalSharePreference.clearUserName(getActivity())) {
                Toast.makeText(getActivity(), R.string.user_cancel_success,
                        Toast.LENGTH_LONG).show();
                this.cancel_btn.setVisibility(View.INVISIBLE);
            }
            break;

        default:
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Log.e(TAG, arg2 + ">><<<");
        Intent intent = null;
        switch (arg2) {
        case 0:// 设置铃声
            setAlarm();
            break;
        case 1:// 实时详情刷新间隔
            setTickerTime(arg1, arg2);
            break;
        case 2:// 市场深度刷新间隔
            setDepthTime(arg1, arg2);
            break;
        case 3:// 使用帮助
            intent = new Intent(getActivity(), HelperActivity.class);
            startActivity(intent);
            break;
        case 4:// 检查更新
            //checkUpdate();
            break;
        case 5:// 关于比特币助手
            intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        new UpdateAysncTask().execute();
    }

    private class UpdateAysncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return mUpdateManager.checkUpdate();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                mUpdateManager.checkUpdateInfo();
            } else {
               mUpdateManager.showLastIsNewDilog();
            }
        }

    }

    @SuppressWarnings("deprecation")
    private void setDepthTime(View v, final int pos) {
        LayoutInflater localinflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = localinflater.inflate(R.layout.fragment4_pop, null);
        ListView listview = (ListView) view.findViewById(R.id.lv_fragment4_pop);
        try {
            final String[] arr = getActivity().getResources().getStringArray(
                    R.array.time_arr);
            MyPopAdapter adapter = new MyPopAdapter(getActivity(), arr);
            listview.setAdapter(adapter);
            listview.setItemsCanFocus(false);
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final PopupWindow pop = new PopupWindow(view, 150,
                    LayoutParams.WRAP_CONTENT);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                        int arg2, long arg3) {
                    if (arr.length >= (arg2 + 1)) {
                        String timeSelected = arr[arg2];
                        if (mList != null && mList.size() >= pos)
                            mList.get(pos).setSys_setting_text(timeSelected);
                        if (mSystemAdapter != null)
                            mSystemAdapter.reflashData(mList);
                        LocalSharePreference.commintDetailInterval(
                                getActivity(),
                                DateUtil.getMillSecond(timeSelected));
                        pop.dismiss();
                    }
                }
            });
            pop.setContentView(view);
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.update();
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            pop.showAtLocation(v, Gravity.NO_GRAVITY,
                    location[0] + v.getWidth(), location[1] + v.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @SuppressWarnings("deprecation")
    private void setTickerTime(View v, final int pos) {
        View view = View.inflate(getActivity(), R.layout.fragment4_pop, null);
        ListView listview = (ListView) view.findViewById(R.id.lv_fragment4_pop);
        final String[] arr = getActivity().getResources().getStringArray(
                R.array.time_arr);
        Log.e(TAG, arr.length + ".......");
        MyPopAdapter adapter = new MyPopAdapter(getActivity(), arr);
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final PopupWindow pop = new PopupWindow(view, 150,
                LayoutParams.WRAP_CONTENT);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                String timeSelected = arr[arg2];
                mList.get(pos).setSys_setting_text(timeSelected);
                mSystemAdapter.reflashData(mList);
                Log.e(TAG, timeSelected);
                LocalSharePreference.commintMarketInterval(getActivity(),
                        DateUtil.getMillSecond(timeSelected));
                pop.dismiss();
            }
        });
        pop.setContentView(view);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.update();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        pop.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.getWidth(),
                location[1] + v.getHeight());
        // pop.showAsDropDown(v);

    }

    private void setAlarm() {
        mediaPlayer = new MediaPlayer();
        Uri uri = LocalSharePreference.getRingtoneUri(getActivity());
        int pos = mUris.indexOf(uri);
        mRingtoneUri = uri;
        // 被选择的铃声
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_singlechoice, mStrings);
        Builder builder = new Builder(getActivity());
        builder.setTitle("选择铃声");
        builder.setSingleChoiceItems(adapter, pos,
                new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRingtone = mStrings.get(which);
                        Log.e(TAG, mRingtone);
                        mRingtoneUri = map.get(mRingtone);
                        MediaPlayerUtils.resetMediaPlayer(mediaPlayer,
                                mRingtoneUri, getActivity());
                    }
                });
        builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 保存设置好的铃声
                LocalSharePreference.commitRingtoneUri(getActivity(),
                        mRingtoneUri);
                MediaPlayerUtils.cancelMediaplayer(mediaPlayer);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MediaPlayerUtils.cancelMediaplayer(mediaPlayer);
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();

    }

}
