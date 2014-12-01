package com.ruitu.btchelper.db;

/**
 * 委托信息表
 */
public interface OrderTable {
    public static final String TABLE_NAME = "ORDER";
    public static final String ID = "ORDER_ID";
    /**
     * 交易平台名称
     */
    public static final String PLAT_NAME = "ORDER_NAME";
    /**
     * 委托买入价
     */
    public static final String BUY_PRICE = "BUY_PRICE";
    /**
     * 委托卖出价
     */
    public static final String SELL_PRICE = "SELL_PRICE";
    /**
     * 委托买入量
     */
    public static final String BUY_VOLUME = "BUY_VOLUME";
    /**
     * 委托卖出量
     */
    public static final String SELL_VOLUME = "SELL_VOLUME";
    /**
     * 更新时间
     */
    public static final String UPDATE_TIME = "UPDATE_TIME";
    public static final String TABLE_CREATE = new StringBuilder(
            "CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(")
            .append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT ,")
            .append(PLAT_NAME).append(" TEXT UNIQUE,")
            .append(BUY_PRICE).append(" TEXT,")
            .append(SELL_PRICE).append(" TEXT,")
            .append(BUY_VOLUME).append(" TEXT,")
            .append(SELL_VOLUME).append(" TEXT,")
            .append(UPDATE_TIME).append(" TEXT)").toString();
    public static final String TABLE_DROP = new StringBuilder(
            "DROP TABLE IF EXISTS ").append(TABLE_NAME).toString();
}
