package com.ruitu.btchelper.db;

/**
 * 最近成交表
 */
public interface TransactionTable {
    public static final String TABLE_NAME = "TRANSACTION";
    public static final String ID = "TRANSACTION_ID";
    /**
     * 交易平台名称
     */
    public static final String TRANSACTION_NAME = "TRANSACTION_NAME";
    /**
     * 成交时间
     */
    public static final String TRANSACTION_TIME = "TRANSACTION_TIME";
    /**
     * 成交订单号
     */
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    /**
     * 成交价
     */
    public static final String TRANSACTION_PRICE = "TRANSACTION_PRICE";
    /**
     * 成交量
     */
    public static final String TRANSACTION_VOLUME = "TRANSACTION_VOLUME";
    public static final String TABLE_CREATE = new StringBuilder(
            "CREATE TABLE IF NOT EXISTS ")
            .append(TABLE_NAME).append("(")
            .append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT ,")
            .append(TRANSACTION_NAME).append(" TEXT UNIQUE,")
            .append(TRANSACTION_TIME).append(" TEXT,")
            .append(TRANSACTION_ID).append(" TEXT,")
            .append(TRANSACTION_PRICE).append(" TEXT,")
            .append(TRANSACTION_PRICE).append(" TEXT)").toString();
    public static final String TABLE_DROP = new StringBuilder(
            "DROP TABLE IF EXISTS ").append(TABLE_NAME).toString();
}
