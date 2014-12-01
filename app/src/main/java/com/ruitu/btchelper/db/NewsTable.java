package com.ruitu.btchelper.db;

/**
 * 实时详情表
 */
public interface NewsTable {
    public static final String TABLE_NAME = "NEWS";
    public static final String ID = "NEWS_ID";
    /**
     * 新闻类型
     *  1  ---首页
        2   ---新闻
        3   ---交易
        4   ---百科
        5   ---分析
        6   ---挖矿
        7   –广告

     */
    public static final String NEWS_TYPE = "NEWS_TYPE";
    /**
     * 新闻标题
     */
    public static final String NEWS_TITLE = "NEWS_TITLE";
    /**
     * 新闻内容
     */
    public static final String NEWS_CONTENT = "NEWS_CONTENT";
    /**
     * 新闻图标
     */
    public static final String NEWS_ICON = "NEWS_ICON";
    /**
     * 新闻发布者
     */
    public static final String NEWS_AUTHOR = "NEWS_AUTHOR";
    /**
     * 新闻发布时间
     */
    public static final String NEWS_TIME = "NEWS_TIME";
    /**
     * 网址
     */
    public static final String NEWS_NET = "NEWS_NET";
    /**
     * 插入时间
     */
    public static final String NEWS_UPDATE_TIME ="NEWS_UPDATE_TIME";
    public static final String TABLE_CREATE = new StringBuilder(
            "CREATE TABLE IF NOT EXISTS ")
            .append(TABLE_NAME).append("(")
            .append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT ,")
            .append(NEWS_TYPE).append(" INTEGER,")
            .append(NEWS_TITLE).append(" TEXT,")
            .append(NEWS_CONTENT).append(" TEXT,")
            .append(NEWS_ICON).append(" TEXT,")
            .append(NEWS_AUTHOR).append(" TEXT,")
            .append(NEWS_UPDATE_TIME).append(" INTEGER,")
            .append(NEWS_NET).append(" TEXT)").toString();
    public static final String TABLE_DROP = new StringBuilder(
            "DROP TABLE IF EXISTS ").append(TABLE_NAME).toString();
}
