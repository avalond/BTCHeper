package com.ruitu.btchelper.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "tb_news" )
public class NewsBean {
    @DatabaseField( generatedId = true )
    public int News_ID;
    /**
     * 新闻类型
     * 1  ---首页
     * 2   ---新闻
     * 3   ---交易
     * 4   ---百科
     * 5   ---分析
     * 6   ---挖矿
     * 7   –广告
     */
    @DatabaseField(canBeNull = true,  columnName = "NEWS_TYPE" )
    public Integer NEWS_TYPE;
    /**
     * 新闻标题
     */
    @DatabaseField(canBeNull = true,columnName = "NEWS_TITLE" )
    public String NEWS_TITLE;
    /**
     * 新闻内容
     */
    @DatabaseField(canBeNull = true, columnName = "NEWS_CONTENT" )
    public String NEWS_CONTENT;
    /**
     * 新闻图标
     */
    @DatabaseField(canBeNull = true, columnName = "NEWS_ICON" )
    public String NEWS_ICON;
    /**
     * 新闻发布者
     */
    @DatabaseField(canBeNull = true, columnName = "NEWS_AUTHOR" )
    public String NEWS_AUTHOR;
    /**
     * 新闻发布时间
     */
    @DatabaseField(canBeNull = true, columnName = "NEWS_TIME" )
    public String NEWS_TIME;
    /**
     * 网址
     */
    @DatabaseField(canBeNull = true, columnName = "NEWS_NET" )
    public String NEWS_NET;
    /**
     * 插入时间
     */
    @DatabaseField(canBeNull = true, columnName = "NEWS_UPDATE_TIME" )
    public Integer NEWS_UPDATE_TIME;

    public int getNews_ID(){
        return News_ID;
    }

    public void setNews_ID( int news_ID ){
        News_ID = news_ID;
    }

    public String getNEWS_TITLE(){
        return NEWS_TITLE;
    }

    public void setNEWS_TITLE( String NEWS_TITLE ){
        this.NEWS_TITLE = NEWS_TITLE;
    }

    public String getNEWS_CONTENT(){
        return NEWS_CONTENT;
    }

    public void setNEWS_CONTENT( String NEWS_CONTENT ){
        this.NEWS_CONTENT = NEWS_CONTENT;
    }

    public String getNEWS_ICON(){
        return NEWS_ICON;
    }

    public void setNEWS_ICON( String NEWS_ICON ){
        this.NEWS_ICON = NEWS_ICON;
    }

    public String getNEWS_AUTHOR(){
        return NEWS_AUTHOR;
    }

    public void setNEWS_AUTHOR( String NEWS_AUTHOR ){
        this.NEWS_AUTHOR = NEWS_AUTHOR;
    }

    public String getNEWS_TIME(){
        return NEWS_TIME;
    }

    public void setNEWS_TIME( String NEWS_TIME ){
        this.NEWS_TIME = NEWS_TIME;
    }

    public String getNEWS_NET(){
        return NEWS_NET;
    }

    public void setNEWS_NET( String NEWS_NET ){
        this.NEWS_NET = NEWS_NET;
    }

    public Integer getNEWS_UPDATE_TIME(){
        return NEWS_UPDATE_TIME;
    }

    public void setNEWS_UPDATE_TIME( Integer NEWS_UPDATE_TIME ){
        this.NEWS_UPDATE_TIME = NEWS_UPDATE_TIME;
    }

    public Integer getNEWS_TYPE(){
        return NEWS_TYPE;
    }

    public void setNEWS_TYPE( Integer NEWS_TYPE ){
        this.NEWS_TYPE = NEWS_TYPE;
    }
}
