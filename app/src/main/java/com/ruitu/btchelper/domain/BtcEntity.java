package com.ruitu.btchelper.domain;

/**
 * { "bc_per_block": 25.0, "coins_before_retarget": 2.6131324803642494e-07,
 * "coins_per_hour": 5.387902021369586e-07, "coins_per_hour_after_retarget":
 * 5.235274402903417e-07, "difficulty": 35002482026.133,
 * "dollars_before_retarget": 9.708832417545332e-05, "dollars_per_hour":
 * 0.0002001821117019656, "dollars_per_hour_after_retarget":
 * 0.00019451138516547354, "exchange_rate": 371.54, "exchange_rate_source":
 * "preev", "hashrate": 900000000.0, "next_difficulty": 36022933880.40272,
 * "time_per_block": 167040899487.48236 }*
 */
public class BtcEntity {
    private static final String PREEV = "preev";
    private static final String USER = "user";
    private double bc_per_block;
    private double coins_before_retarget;
    private double coins_per_hour;
    private double coins_per_hour_after_retarget;
    private double difficulty;
    private double dollars_before_retarget;
    private double dollars_per_hour;
    private double dollars_per_hour_after_retarget;
    private double exchange_rate;
    private String exchange_rate_source;
    private double hashrate;
    private double next_difficulty;
    private double time_per_block;

    public double getBc_per_block() {
        return bc_per_block;
    }

    public void setBc_per_block(double bc_per_block) {
        this.bc_per_block = bc_per_block;
    }

    public double getCoins_before_retarget() {
        return coins_before_retarget;
    }

    public void setCoins_before_retarget(double coins_before_retarget) {
        this.coins_before_retarget = coins_before_retarget;
    }

    public double getCoins_per_hour() {
        return coins_per_hour;
    }

    public void setCoins_per_hour(double coins_per_hour) {
        this.coins_per_hour = coins_per_hour;
    }

    public double getCoins_per_hour_after_retarget() {
        return coins_per_hour_after_retarget;
    }

    public void setCoins_per_hour_after_retarget(
            double coins_per_hour_after_retarget) {
        this.coins_per_hour_after_retarget = coins_per_hour_after_retarget;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getDollars_before_retarget() {
        return dollars_before_retarget;
    }

    public void setDollars_before_retarget(double dollars_before_retarget) {
        this.dollars_before_retarget = dollars_before_retarget;
    }

    public double getDollars_per_hour() {
        return dollars_per_hour;
    }

    public void setDollars_per_hour(double dollars_per_hour) {
        this.dollars_per_hour = dollars_per_hour;
    }

    public double getDollars_per_hour_after_retarget() {
        return dollars_per_hour_after_retarget;
    }

    public void setDollars_per_hour_after_retarget(
            double dollars_per_hour_after_retarget) {
        this.dollars_per_hour_after_retarget = dollars_per_hour_after_retarget;
    }

    public double getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getExchange_rate_source() {
        return exchange_rate_source;
    }

    public void setExchange_rate_source(String exchange_rate_source) {
        this.exchange_rate_source = exchange_rate_source;
    }

    public double getHashrate() {
        return hashrate;
    }

    public void setHashrate(double hashrate) {
        this.hashrate = hashrate;
    }

    public double getNext_difficulty() {
        return next_difficulty;
    }

    public void setNext_difficulty(double next_difficulty) {
        this.next_difficulty = next_difficulty;
    }

    public double getTime_per_block() {
        return time_per_block;
    }

    public void setTime_per_block(double time_per_block) {
        this.time_per_block = time_per_block;
    }

    public BtcEntity(double bc_per_block, double coins_before_retarget,
            double coins_per_hour, double coins_per_hour_after_retarget,
            double difficulty, double dollars_before_retarget,
            double dollars_per_hour, double dollars_per_hour_after_retarget,
            double exchange_rate, String exchange_rate_source, double hashrate,
            double next_difficulty, double time_per_block) {
        super();
        this.bc_per_block = bc_per_block;
        this.coins_before_retarget = coins_before_retarget;
        this.coins_per_hour = coins_per_hour;
        this.coins_per_hour_after_retarget = coins_per_hour_after_retarget;
        this.difficulty = difficulty;
        this.dollars_before_retarget = dollars_before_retarget;
        this.dollars_per_hour = dollars_per_hour;
        this.dollars_per_hour_after_retarget = dollars_per_hour_after_retarget;
        this.exchange_rate = exchange_rate;
        this.exchange_rate_source = exchange_rate_source;
        this.hashrate = hashrate;
        this.next_difficulty = next_difficulty;
        this.time_per_block = time_per_block;
    }

    public BtcEntity() {
        super();
    }

}
