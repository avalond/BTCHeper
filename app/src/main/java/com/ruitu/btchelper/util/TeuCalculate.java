package com.ruitu.btchelper.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.Log;

import com.ruitu.btchelper.domain.Mining;
import com.ruitu.btchelper.domain.Teu;

public class TeuCalculate {
    private static final String TAG = TeuCalculate.class.getSimpleName();
    private int revPerBlock = DataHelper.VER_BLOCK;

    public ArrayList<Teu> calculate(Mining mMining) {
        if (mMining == null) {
            return null;
        }
        ArrayList<Teu> list = new ArrayList<Teu>();
        double powerCost_btc = mMining.getPower() * 24 / 1000
                * mMining.getElectricity() / mMining.getHwRate();
        double powerCost_usd = powerCost_btc * mMining.getHwRate();// 每天电费
        double sum_count = mMining.getCost() + mMining.getMisc();// 设备成本+其他成本
        double btc_ver_day = 0;// 每天挖出多少btc
        int inc_day = 0;// 挖矿天数
        double curr_dif = 0;// 当前难度
        double day_income = 0;// 当日收入
        double sum_day_income = 0;// 累计日收入
        double day_gain = 0;// 当日利润
        double max_gain = 0;// 累计收益
        double max_gain_temp = 0;// 累计收益缓存
        double max_income = 0;// 最大收益
        String max_income_date = null;// 最大收益日期
        String startdate = mMining.getStartYear() + "-"
                + mMining.getStartMonth() + "-" + mMining.getStartDay();
        int back_day = 0;// 回本时间

        do {
            inc_day++;// 挖的天数
            // sum_cost = powerCost_usd * inc_day + sum_count;//
            // 总成本=设备成本+其他成本+电费成本(每天电费*天数)
            // Log.e(TAG, "挖矿" + inc_day + "天后的总成本:" + sum_cost);
            curr_dif = getCurrDiffcuclt(mMining, inc_day);// 当前难度
            Log.e(TAG, "挖矿第" + inc_day + "天的当前难度:" + curr_dif);
            btc_ver_day = getBtcVerDay(inc_day, mMining);// 当日挖到的比特币数量
            Log.e(TAG, "挖矿第" + inc_day + "天一天挖到btc:" + btc_ver_day);
            day_income = btc_ver_day * mMining.getHwRate();// 当日挖到比特币的价值，人民币,当日收入
            Log.e(TAG, "挖矿第" + inc_day + "的btc收入:" + day_income);
            sum_day_income += day_income;// 累计收入
            Log.e(TAG, "挖矿第" + inc_day + "后btc累计收入:" + day_income);
            day_gain = day_income - powerCost_usd;// 当日收益
            Log.e(TAG, "挖矿第" + inc_day + "天的收益：" + day_gain);
            // 将当日的收益与最大收益进行比较，获得最大收益
            if (max_gain >= max_income) {
                max_income = max_gain;
                Log.e(TAG, "当前最大收益：" + max_income);

            }
            // 累计收益
            max_gain = sum_day_income - sum_count - powerCost_usd * (inc_day);
            if (max_gain >= 0 && max_gain_temp < 0) {
                back_day = inc_day;
            }
            max_gain_temp = max_gain;
            Log.e(TAG, "挖到第" + inc_day + "天的累计收益:" + max_gain);
            String date_str = getDateStr(inc_day, mMining.getStartYear(),
                    mMining.getStartMonth(), mMining.getStartDay());
            Log.e(TAG, "当前日期:" + date_str);
            if (day_gain <= 0) {
                break;
            }
            max_income_date = getDateStr(inc_day, mMining.getStartYear(),
                    mMining.getStartMonth(), mMining.getStartDay());
            Log.e(TAG, "挖到第" + inc_day + "天获得最大收益");
            Teu teu = new Teu();
            teu.setBtc_ver_day(btc_ver_day);
            teu.setDay_elec(powerCost_usd);
            teu.setDiff_d(curr_dif);
            teu.setDay_income(day_income);
            teu.setSum_gain(max_gain);
            teu.setDay_gain(day_gain);
            teu.setDate_str(date_str);
            list.add(teu);
        } while (true);
        for (Teu teu : list) {
            teu.setMax_income(max_income);
            teu.setMax_income_date(max_income_date);
            teu.setBack_days(back_day);
            teu.setSum_cost(sum_count);
            teu.setStart_date(startdate);
        }
        return list;
    }

    private String getDateStr(int inc_day, int startYear, int startMonth,
            int startDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth - 1, startDay);
        calendar.add(Calendar.DATE, inc_day--);
        String month_str = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DATE);
        return month_str;
    }

    private double getBtcVerDay(int day, Mining mMining) {
        // 获得当日难度
        double disp_diff = getCurrDiffcuclt(mMining, day);
        Log.e(TAG, disp_diff + "");
        // 算力
        double hashRate = mMining.getHashRate() * 1000;
        double rev_btc = 24
                / (disp_diff * Math.pow(2, 32) / (hashRate * Math.pow(10, 6))
                        / 60 / 60) * revPerBlock;
        // double block_time = Math.pow(2, 256)
        // / ( mMining.getHashRate() * 1000000000)
        // / (60 * 60 * 24);
        //
        // double rev_btc = revPerBlock /
        //
        // block_time
        //
        //
        // * (1 - mMining.getPool() * 0.01);
        Log.e(TAG, rev_btc + "");
        return rev_btc;
    }

    private double getCurrDiffcuclt(Mining mining, int day) {
        // 当前难度
        double dif = mining.getDifficulty();
        // 难度涨幅，默认20%
        double inc_dif = mining.getIncrease() * 0.01;
        // 难度调整周期,默认11天
        int inc_date = mining.getIncreate_date();
        // 难度调整次数
        int retu = day / inc_date;
        for (int i = 0; i < retu; i++) {
            dif = dif * (1 + inc_dif);
        }
        return dif;
    }
}
