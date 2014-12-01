package com.ruitu.btchelper.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.activity.BaseActivity;
import com.ruitu.btchelper.activity.TeuInfoAcitivity;
import com.ruitu.btchelper.domain.BtcEntity;
import com.ruitu.btchelper.domain.IToStringMap;
import com.ruitu.btchelper.domain.Mining;
import com.ruitu.btchelper.domain.Teu;
import com.ruitu.btchelper.network.BaseRequestPacket;
import com.ruitu.btchelper.network.ClientEngine;
import com.ruitu.btchelper.network.IRequestContentCallback;
import com.ruitu.btchelper.network.ParseJson;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.DialogUtils;
import com.ruitu.btchelper.util.NetworkUtils;
import com.ruitu.btchelper.util.TeuCalculate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeuFragment extends Fragment implements DataHelper,
        OnClickListener, IRequestContentCallback, IToStringMap {
    private static final String TAG = TeuFragment.class.getSimpleName();
    private EditText diffcuty_teu, diffcuty_add_teu, can_teu, power_teu,
            power_cost_teu, mining_poundage_teu, dev_cost, dev_others_cost,
            begin_date, china_teu, diffcuty_add_date_teu, dev_startdate,
            dev_stopdate;
    private Button js_btn_teu;
    private int timeFlag = 0;
    private int mYear, mMonth, mDay;
    private Timer mTimer;
    private MyTimerTask myTimerTask;
    private BtcEntity mBtcEntity;
    private TeuCalculate mCalculate;
    private TextView iv_diffcuty, iv_canteu, iv_diffcuty_add_teu,
            iv_power_cost_teu, iv_dev_others_cost, iv_power_teu,
            iv_mining_poundage_teu, iv_dev_cost, iv_begin_date, iv_china_teu,
            iv_diffcuty_add_date_teu;
    private TextView hint_tv, hint_tv1;
    private RelativeLayout hidden1_ll, hidden2_ll;
    private Button btn_hidden1_ll, btn_hidden2_ll;
    private LinearLayout hidden_ll;
    private ArrayList<Teu> list = new ArrayList<Teu>();
    private final static String mat = "^[0-9]+(\\.[0-9]+)?$";
    private double usd_to_cny = 0.0;
    private final static int TYPE = 1233;
    private View mView;
    public static TeuFragment getInstance() {
        return new TeuFragment();
    }
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
//        return super.onCreateView( inflater, container, savedInstanceState );
        Log.e( TAG, "onCreateView" );
        initViews( inflater, container );
        return mView;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.e(TAG, "oncreate执行了");
//        setContentView(R.layout.teu_layout);
//        initViews();
//    }

    private boolean matchText(String text) {
        Log.e(TAG, "text=====" + text);
        Pattern pattern = Pattern.compile(mat.trim());
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            Log.e(TAG, "match====true");
            return true;
        } else {
            Log.e(TAG, "match===false");
            return false;
        }
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume执行了");

        mTimer = new Timer();

        myTimerTask = new MyTimerTask(TYPE);
        if (mTimer != null && myTimerTask != null)
            mTimer.schedule(myTimerTask, 0, 60000);
        defaultCalculatorGet();
        super.onResume();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
            case TYPE:
                getUsdToCny();
                break;

            default:
                break;
            }
        }

    };

    private class MyTimerTask extends TimerTask {
        private int type;

        public MyTimerTask(int actionType) {
            this.type = actionType;
        }

        @Override
        public void run() {
            Log.e(TAG, "task的run方法执行了");
            Message message = new Message();
            message.what = type;
            mHandler.sendMessage(message);
        }
    }

    private void initViews(LayoutInflater inflater, @Nullable ViewGroup container) {

        mView = inflater.inflate( R.layout.teu_layout, container, false );
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        mMonth = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        mCalculate = new TeuCalculate();
        this.hidden1_ll = (RelativeLayout) mView.findViewById( R.id.hidden1_ll );
        this.hidden_ll = (LinearLayout) mView.findViewById( R.id.hidden_ll );
        this.btn_hidden1_ll = (Button) mView.findViewById( R.id.btn_hidden1_ll );
        this.btn_hidden1_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidden_ll.setVisibility(View.VISIBLE);
                hidden1_ll.setVisibility(View.GONE);
            }
        });
        this.hidden2_ll = (RelativeLayout) mView.findViewById( R.id.hidden2_ll );
        this.btn_hidden2_ll = (Button) mView.findViewById( R.id.btn_hidden2_ll );
        this.btn_hidden2_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidden1_ll.setVisibility(View.VISIBLE);
                hidden_ll.setVisibility(View.GONE);
            }
        });
        this.diffcuty_teu = (EditText) mView.findViewById( R.id.diffcuty_teu );
        this.diffcuty_add_teu = (EditText) mView
                .findViewById( R.id.diffcuty_add_teu );
        this.can_teu = (EditText) mView.findViewById( R.id.can_teu );
        this.power_teu = (EditText) mView.findViewById( R.id.power_teu );
        this.power_cost_teu = (EditText) mView.findViewById( R.id.power_cost_teu );
        this.mining_poundage_teu = (EditText) mView
                .findViewById( R.id.mining_poundage_teu );
        this.dev_cost = (EditText) mView.findViewById( R.id.dev_cost );
        this.dev_others_cost = (EditText) mView
                .findViewById( R.id.dev_others_cost );
        this.begin_date = (EditText) mView.findViewById( R.id.begin_date );
        this.china_teu = (EditText) mView.findViewById( R.id.china_teu );
        this.diffcuty_add_date_teu = (EditText) mView
                .findViewById( R.id.diffcuty_add_date_teu );
        this.js_btn_teu = (Button) mView.findViewById( R.id.js_btn_teu );
        this.iv_begin_date = (TextView) mView.findViewById( R.id.iv_begin_date );
        this.iv_canteu = (TextView) mView.findViewById( R.id.iv_canteu );
        this.iv_china_teu = (TextView) mView.findViewById( R.id.iv_china_teu );
        this.iv_dev_cost = (TextView) mView.findViewById( R.id.iv_dev_cost );
        this.iv_diffcuty = (TextView) mView.findViewById( R.id.iv_diffcuty );
        this.iv_diffcuty_add_teu = (TextView) mView
                .findViewById( R.id.iv_diffcuty_add_teu );
        this.iv_power_cost_teu = (TextView) mView
                .findViewById( R.id.iv_power_cost_teu );
        this.iv_dev_others_cost = (TextView) mView
                .findViewById( R.id.iv_dev_others_cost );
        this.iv_mining_poundage_teu = (TextView) mView
                .findViewById( R.id.iv_mining_poundage_teu );
        this.iv_power_teu = (TextView) mView.findViewById( R.id.iv_power_teu );
        this.hint_tv = (TextView) mView.findViewById( R.id.hint_tv );
        this.iv_diffcuty_add_date_teu = (TextView) mView
                .findViewById( R.id.iv_diffcuty_add_date_teu );
        this.hint_tv1 = (TextView) mView.findViewById( R.id.hint_tv2 );
        begin_date.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                timeFlag = 0;
                onCreateDialog(0).show();
//                getActivity().showDialog(0);
            }
        });
        begin_date.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    timeFlag = 0;
                    hideIM(v);
                    onCreateDialog(0).show();
//                    getActivity().showDialog(0);
                }
            }
        });
        this.dev_startdate = (EditText) mView.findViewById(R.id.dev_startdate);
        dev_startdate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                timeFlag = 1;
                onCreateDialog(1).show();
//                getActivity().showDialog(1);
            }
        });
        dev_startdate.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    timeFlag = 1;
                    hideIM(v);
                    onCreateDialog(1).show();
//                    getActivity().showDialog(1);
                }
            }
        });
        this.dev_stopdate = (EditText) mView.findViewById(R.id.dev_stopdate);
        dev_stopdate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                timeFlag = 2;
                onCreateDialog(2).show();
//                getActivity().showDialog(2);
            }
        });
        dev_stopdate.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    timeFlag = 2;
                    hideIM(v);
                    onCreateDialog(2).show();
//                    getActivity().showDialog(2);
                }
            }
        });
        this.js_btn_teu.setOnClickListener(this);
    }

    private void getUsdToCny() {
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            ClientEngine clientEngine = ClientEngine.getInstance(getActivity());
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = ACTION_GET_USD_ACTION;
            packet.url = URL_USD_TO_CNY;
            packet.extra = null;
            packet.context = getActivity();
            packet.object = null;
            clientEngine.httpGetRequest(packet, this);
        } else {
            DialogUtils.showNoNetDialogTeuActivity(getActivity());
        }
    }


    @Override
    public void onPause() {
        if (myTimerTask != null)
            myTimerTask.cancel();
        if (mTimer != null)
            mTimer.cancel();

        super.onPause();
    }

    private void defaultCalculatorGet() {
        if (toStringMap() == null) {
            return;
        }
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            ClientEngine clientEngine = ClientEngine.getInstance(getActivity());
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = ACTION_GET_CALCULATOR_DEFAULT;
            packet.url = BTC_GET_URL;
            packet.extra = toStringMap();
            packet.context = getActivity();
            packet.object = this;
            clientEngine.httpGetRequest(packet, this);
        } else {
            DialogUtils.showNoNetDialogTeuActivity(getActivity());
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            mYear = year;
            String mm;
            String dd;
            if (monthOfYear <= 8) {
                mMonth = monthOfYear + 1;
                mm = "0" + mMonth;
            } else {
                mMonth = monthOfYear + 1;
                mm = String.valueOf(mMonth);
            }
            if (dayOfMonth <= 9) {
                mDay = dayOfMonth;
                dd = "0" + mDay;
            } else {
                mDay = dayOfMonth;
                dd = String.valueOf(mDay);
            }
            mDay = dayOfMonth;
            switch (timeFlag) {
            case 0:
                begin_date.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
                break;
            case 1:
                dev_startdate.setText(String.valueOf(mYear) + "-" + mm + "-"
                        + dd);
                break;
            case 2:
                dev_stopdate.setText(String.valueOf(mYear) + "-" + mm + "-"
                        + dd);
                break;
            default:
                begin_date.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
                break;
            }
            // if (timeFlag == 0) {
            // begin_date.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
            // } else {
            // begin_date.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
            // }
        }

    };

    private Dialog onCreateDialog(int id) {
        switch (id) {
        case 0:
            return new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,
                    mDay);
        case 1:
            return new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,
                    mDay);
        case 2:
            return new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,
                    mDay);
        }
        return null;
    }

    // 隐藏手机键盘
    private void hideIM(View edt) {
        try {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService( Activity.INPUT_METHOD_SERVICE );
            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.js_btn_teu:
            can();
            break;

        default:
            break;
        }
    }

 

    private boolean isCan() {
        String diffcuty_d = this.diffcuty_teu.getText().toString().trim();
        String diffcuty_add_d = this.diffcuty_add_teu.getText().toString()
                .trim();
        String diffcuty_add_date_d = this.diffcuty_add_date_teu.getText()
                .toString().trim();
        String can_teu_str = this.can_teu.getText().toString().trim();
        String power_teu_str = this.power_teu.getText().toString().trim();
        String power_cost_teu_str = this.power_cost_teu.getText().toString()
                .trim();
        String mining_poundage_teu_str = this.mining_poundage_teu.getText()
                .toString().trim();
        String dev_cost_str = this.dev_cost.getText().toString().trim();
        String china_teu_str = this.china_teu.getText().toString().trim();
        String dev_others_cost_str = this.dev_others_cost.getText().toString()
                .trim();
        if (diffcuty_d == null || "".equals(diffcuty_d)
                || !matchText(diffcuty_d)) {
            return false;
        } 
        if (diffcuty_add_d == null || "".equals(diffcuty_add_d)
                || !matchText(diffcuty_add_d)) {
            this.iv_diffcuty_add_teu.setVisibility(View.VISIBLE);
            return false;
        } else {
            this.iv_diffcuty_add_teu.setVisibility(View.INVISIBLE);
        }
        if (diffcuty_add_date_d == null || "".equals(diffcuty_add_date_d)
                || !matchText(diffcuty_add_date_d)) {
            this.iv_diffcuty_add_date_teu.setVisibility(View.VISIBLE);
            return false;
        } else {
            this.iv_diffcuty_add_date_teu.setVisibility(View.INVISIBLE);
        }
        if (can_teu_str == null || "".equals(can_teu_str)
                || !matchText(can_teu_str)) {
            return false;
        } 
        if (power_teu_str == null || "".equals(power_teu_str)
                || !matchText(power_teu_str)) {
            this.iv_power_teu.setVisibility(View.VISIBLE);
            return false;
        } else {
            this.iv_power_teu.setVisibility(View.INVISIBLE);
        }
        if (power_cost_teu_str == null || "".equals(power_cost_teu_str)
                || !matchText(power_cost_teu_str)) {
            this.iv_power_cost_teu.setVisibility(View.VISIBLE);
            return false;
        } else {
            this.iv_power_cost_teu.setVisibility(View.INVISIBLE);
        }
        if (mining_poundage_teu_str == null
                || "".equals(mining_poundage_teu_str)
                || !matchText(mining_poundage_teu_str)) {
            return false;
        } 
        if (dev_cost_str == null || "".equals(dev_cost_str)
                || !matchText(dev_cost_str)) {
            return false;
        } 
        if (china_teu_str == null || "".equals(china_teu_str)
                || !matchText(china_teu_str)) {
            return false;
        } 
        if (dev_others_cost_str == null || "".equals(dev_others_cost_str)
                || !matchText(dev_others_cost_str)) {
            this.iv_dev_others_cost.setVisibility(View.VISIBLE);
            return false;
        } else {
            this.iv_dev_others_cost.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    private void can() {
        String diffcuty_d = this.diffcuty_teu.getText().toString().trim();
        String diffcuty_add_d = this.diffcuty_add_teu.getText().toString()
                .trim();
        String diffcuty_add_date_d = this.diffcuty_add_date_teu.getText()
                .toString().trim();
        String can_teu_str = this.can_teu.getText().toString().trim();
        String power_teu_str = this.power_teu.getText().toString().trim();
        String power_cost_teu_str = this.power_cost_teu.getText().toString()
                .trim();
        String mining_poundage_teu_str = this.mining_poundage_teu.getText()
                .toString().trim();
        String start_date = this.begin_date.getText().toString().trim();
        String dev_cost_str = this.dev_cost.getText().toString().trim();
        String china_teu_str = this.china_teu.getText().toString().trim();
        String dev_others_cost_str = this.dev_others_cost.getText().toString()
                .trim();
        try {
            if (isCan()) {
                this.hint_tv1.setVisibility(View.VISIBLE);
                this.hint_tv.setVisibility(View.INVISIBLE);
                Mining mining = new Mining();
                mining.setCost(Double.parseDouble(dev_cost_str));
                mining.setDifficulty(Double.parseDouble(diffcuty_d.replace(",",
                        "")));
                mining.setIncreate_date(Integer.parseInt(diffcuty_add_date_d));
                mining.setElectricity(Double.parseDouble(power_cost_teu_str));
                mining.setHashRate(Double.parseDouble(can_teu_str));
                mining.setHwRate(Double.parseDouble(china_teu_str));
                mining.setIncrease(Double.parseDouble(diffcuty_add_d));
                mining.setMisc(Double.parseDouble(dev_others_cost_str));
                mining.setPool(Double.parseDouble(mining_poundage_teu_str));
                mining.setPower(Double.parseDouble(power_teu_str));
                String year = start_date.substring(0, 4);
                String month = start_date.substring(5, 7);
                String day = start_date.substring(8, 10);
                Log.e(TAG, year + "-" + month + "-" + day);
                mining.setStartDay(Integer.parseInt(day));
                mining.setStartMonth(Integer.parseInt(month));
                mining.setStartYear(Integer.parseInt(year));
                Log.e("TeuActivity", mining.getCost() + "");
                Log.e("TeuActivity", mining.getDifficulty() + "");
                Log.e("TeuActivity", mining.getElectricity() + "");
                Log.e("TeuActivity", mining.getHashRate() + "");
                Log.e("TeuActivity", mining.getHwRate() + "");
                Log.e("TeuActivity", mining.getIncrease() + "");
                Log.e("TeuActivity", mining.getMisc() + "");
                Log.e("TeuActivity", mining.getPool() + "");
                Log.e("TeuActivity", mining.getPower() + "");
                Log.e("TeuActivity", mining.getStartDay() + "");
                Log.e("TeuActivity", mining.getStartMonth() + "");
                Log.e("TeuActivity", mining.getStartYear() + "");
                // list = mCalculate.calculate(mining);
                new MyTask().execute(mining);
            } else {
                this.hint_tv1.setVisibility(View.INVISIBLE);
                this.hint_tv.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<Object, Void, ArrayList<Teu>> {

        @Override
        protected ArrayList<Teu> doInBackground(Object... params) {
            Mining mining = (Mining) params[0];
            return mCalculate.calculate(mining);
        }

        @Override
        protected void onPostExecute(ArrayList<Teu> result) {
            if (result != null && !result.isEmpty()) {
                Intent intent = new Intent(getActivity(),
                        TeuInfoAcitivity.class);
                intent.putExtra("teu", result);

                getActivity().startActivity( intent );
            } else {
                // 弹出提示的pop窗口
                DialogUtils.showCantBackCost(getActivity());
            }
        }

    }

    @Override
    public void onResult(int requestAction, Boolean isSuccess, String content,
            Object extra) {
        switch (requestAction) {
        case ACTION_GET_CALCULATOR_DEFAULT:
            if (isSuccess) {
                if (content != null && !"".equals(content)) {
                    try {
                        mBtcEntity = ParseJson.parseBtcEnStr(content);
                        if (mBtcEntity == null) {
                            return;
                        }
                        setDefault();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        case ACTION_GET_USD_ACTION:
            if (isSuccess) {
                if (content != null && !"".equals(content)) {
                    try {
                        usd_to_cny = ParseJson.parseUsd_to_cny(content);
                        setDefault();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        default:
            break;
        }
    }

    private void setDefault() {
        if (mBtcEntity != null) {
            Log.e(TAG, usd_to_cny + "<<<usd_to_cny");
            this.diffcuty_teu.setText(DataUtil.changeDoubleToDigit(
                    mBtcEntity.getDifficulty(), 2));
            Log.e(TAG, mBtcEntity.getExchange_rate() + "");
            this.china_teu.setText(DataUtil.changeToTwoPoint(String
                    .valueOf(mBtcEntity.getExchange_rate() * usd_to_cny)));
        }
    }

    @Override
    public Map<String, String> toStringMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(HASHRATE, String.valueOf(1000000));
        return map;
    }
}
