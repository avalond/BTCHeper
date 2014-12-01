package com.ruitu.btchelper.domain;

public class Mining {
    private double power;// 矿机功耗
    private double hashRate;// 矿机算力 GH/s
    private double cost;// 矿机价格
    private double hwRate;// 人民币汇率 换算单位 ￥/BTC
    private double misc;// 其他成本
    private double difficulty;// 难度
    private double increase = 20.0;// 难度涨幅
    private int increate_date = 11;// 难度调整周期
    private int startYear;// 开始挖矿的年份
    private int startMonth;// 开始挖矿的月份
    private int startDay;// 开始挖矿的日期
    private double pool;// 矿池手续费
    private double electricity = 0.6;// 电费
    public int getIncreate_date() {
        return increate_date;
    }

    public void setIncreate_date(int increate_date) {
        this.increate_date = increate_date;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getHashRate() {
        return hashRate;
    }

    public void setHashRate(double hashRate) {
        this.hashRate = hashRate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getHwRate() {
        return hwRate;
    }

    public void setHwRate(double hwRate) {
        this.hwRate = hwRate;
    }

    public double getMisc() {
        return misc;
    }

    public void setMisc(double misc) {
        this.misc = misc;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getIncrease() {
        return increase;
    }

    public void setIncrease(double increase) {
        this.increase = increase;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public double getPool() {
        return pool;
    }

    public void setPool(double pool) {
        this.pool = pool;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }

}
