package com.ruitu.btchelper.db;

/**
 * 实时详情表
 */
public interface TickerTable {
    public static final String TABLE_NAME = "TICKER";
    public static final String ID = "TICKER_ID";
    /**
     * 交易平台名称
     */
    public static final String TICKER_NAME = "TICKER_NAME";
    /**
     * 最近成交价
     */
    public static final String TICKER_LAST_PRICE = "TICKER_LAST_PRICE";
    /**
     * 买一价
     */
    public static final String TICKER_BUY_PRICE = "TICKER_BUY_PRICE";
    /**
     * 卖一价
     */
    public static final String TICKER_SELL_PRICE = "TICKER_SELL_PRICE";
    /**
     * 最高价
     */
    public static final String TICKER_HIGH_PRICE = "TICKER_HIGH_PRICE";
    /**
     * 最低价
     */
    public static final String TICKER_LOW_PRICE = "TICKER_LOW_PRICE";
    /**
     * 成交量
     */
    public static final String TICKER_VOLUME = "TICKER_VOLUME";
    /**
     * 更新时间
     */
    public static final String UPDATE_TIME = "UPDATE_TIME";
    /**
     * 网址
     */
    public static final String NET_ADDRESS = "NET_ADDRESS";
    /**
     * 成交均价
     */
    public static  final String TICKER_WAP ="TICKER_WAP";
    public static final String TABLE_CREATE = new StringBuilder(
            "CREATE TABLE IF NOT EXISTS ")
            .append(TABLE_NAME).append("(")
            .append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT ,")
            .append(TICKER_NAME).append(" TEXT UNIQUE,")
            .append(TICKER_LAST_PRICE).append(" TEXT,")
            .append(TICKER_BUY_PRICE).append(" TEXT,")
            .append(TICKER_SELL_PRICE).append(" TEXT,")
            .append(TICKER_HIGH_PRICE).append(" TEXT,")
            .append(TICKER_LOW_PRICE).append(" TEXT,")
            .append(TICKER_WAP).append(" TEXT,")
            .append(TICKER_VOLUME).append(" TEXT,")
            .append(NET_ADDRESS).append(" TEXT,")
            .append(UPDATE_TIME).append(" TEXT)").toString();
    public static final String TABLE_DROP = new StringBuilder(
            "DROP TABLE IF EXISTS ").append(TABLE_NAME).toString();
}
