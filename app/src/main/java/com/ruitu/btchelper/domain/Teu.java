package com.ruitu.btchelper.domain;

import java.io.Serializable;

public class Teu implements Serializable {
    private static final long serialVersionUID = 1L;
    private String start_date;//开始挖矿日期
    private int back_days;// 回本时间,天数
    private double sum_cost;// 总成本
    private double day_elec;//每日电费
    private double max_income;// 最大收益
    private String max_income_date;//最大收益日期
    private String date_str;// 日期
    private double diff_d;// 当天难度
    private double block_time;// 每个block耗时（天）
    private double btc_ver_day;// 每天挖到的比特币数量
    private double day_income;// 每天收入
    private double day_gain;// 每天利润
    private double sum_gain;// 累计收益
    
    public double getDay_elec() {
        return day_elec;
    }
    public void setDay_elec(double day_elec) {
        this.day_elec = day_elec;
    }
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getMax_income_date() {
        return max_income_date;
    }
    public void setMax_income_date(String max_income_date) {
        this.max_income_date = max_income_date;
    }
    public int getBack_days() {
        return back_days;
    }
    public void setBack_days(int back_days) {
        this.back_days = back_days;
    }
    public double getSum_cost() {
        return sum_cost;
    }
    public void setSum_cost(double sum_cost) {
        this.sum_cost = sum_cost;
    }
    public double getMax_income() {
        return max_income;
    }
    public void setMax_income(double max_income) {
        this.max_income = max_income;
    }
    public String getDate_str() {
        return date_str;
    }
    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }
    public double getDiff_d() {
        return diff_d;
    }
    public void setDiff_d(double diff_d) {
        this.diff_d = diff_d;
    }
    public double getBlock_time() {
        return block_time;
    }
    public void setBlock_time(double block_time) {
        this.block_time = block_time;
    }
    public double getBtc_ver_day() {
        return btc_ver_day;
    }
    public void setBtc_ver_day(double btc_ver_day) {
        this.btc_ver_day = btc_ver_day;
    }
    public double getDay_income() {
        return day_income;
    }
    public void setDay_income(double day_income) {
        this.day_income = day_income;
    }
    public double getDay_gain() {
        return day_gain;
    }
    public void setDay_gain(double day_gain) {
        this.day_gain = day_gain;
    }
    public double getSum_gain() {
        return sum_gain;
    }
    public void setSum_gain(double sum_gain) {
        this.sum_gain = sum_gain;
    }

}
