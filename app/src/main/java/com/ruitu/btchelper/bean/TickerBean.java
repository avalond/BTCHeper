package com.ruitu.btchelper.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "tb_ticker" )
public class TickerBean {
    @DatabaseField( generatedId = true )
    public Integer Ticker_ID;
    /**
     * 交易平台名称
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_NAME" )
    public String TICKER_NAME;
    /**
     * 最近成交价
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_LAST_PRICE" )
    public String TICKER_LAST_PRICE;
    /**
     * 买一价
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_BUY_PRICE" )
    public String TICKER_BUY_PRICE;
    /**
     * 卖一价
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_SELL_PRICE" )
    public String TICKER_SELL_PRICE;
    /**
     * 最高价
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_HIGH_PRICE" )
    public String TICKER_HIGH_PRICE;
    /**
     * 最低价
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_LOW_PRICE" )
    public String TICKER_LOW_PRICE;
    /**
     * 成交量
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_VOLUME" )
    public String TICKER_VOLUME;
    /**
     * 更新时间
     */
    @DatabaseField(canBeNull = true, columnName = "UPDATE_TIME" )
    public String UPDATE_TIME;
    /**
     * 网址
     */
    @DatabaseField(canBeNull = true, columnName = "NET_ADDRESS" )
    public String NET_ADDRESS;
    /**
     * 成交均价
     */
    @DatabaseField(canBeNull = true, columnName = "TICKER_WAP" )
    public String TICKER_WAP;

    public Integer getTicker_ID(){
        return Ticker_ID;
    }

    public void setTicker_ID( Integer ticker_ID ){
        Ticker_ID = ticker_ID;
    }

    public String getTICKER_NAME(){
        return TICKER_NAME;
    }

    public void setTICKER_NAME( String TICKER_NAME ){
        this.TICKER_NAME = TICKER_NAME;
    }

    public String getTICKER_LAST_PRICE(){
        return TICKER_LAST_PRICE;
    }

    public void setTICKER_LAST_PRICE( String TICKER_LAST_PRICE ){
        this.TICKER_LAST_PRICE = TICKER_LAST_PRICE;
    }

    public String getTICKER_BUY_PRICE(){
        return TICKER_BUY_PRICE;
    }

    public void setTICKER_BUY_PRICE( String TICKER_BUY_PRICE ){
        this.TICKER_BUY_PRICE = TICKER_BUY_PRICE;
    }

    public String getTICKER_SELL_PRICE(){
        return TICKER_SELL_PRICE;
    }

    public void setTICKER_SELL_PRICE( String TICKER_SELL_PRICE ){
        this.TICKER_SELL_PRICE = TICKER_SELL_PRICE;
    }

    public String getTICKER_HIGH_PRICE(){
        return TICKER_HIGH_PRICE;
    }

    public void setTICKER_HIGH_PRICE( String TICKER_HIGH_PRICE ){
        this.TICKER_HIGH_PRICE = TICKER_HIGH_PRICE;
    }

    public String getTICKER_LOW_PRICE(){
        return TICKER_LOW_PRICE;
    }

    public void setTICKER_LOW_PRICE( String TICKER_LOW_PRICE ){
        this.TICKER_LOW_PRICE = TICKER_LOW_PRICE;
    }

    public String getUPDATE_TIME(){
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME( String UPDATE_TIME ){
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public String getTICKER_VOLUME(){
        return TICKER_VOLUME;
    }

    public void setTICKER_VOLUME( String TICKER_VOLUME ){
        this.TICKER_VOLUME = TICKER_VOLUME;
    }

    public String getNET_ADDRESS(){
        return NET_ADDRESS;
    }

    public void setNET_ADDRESS( String NET_ADDRESS ){
        this.NET_ADDRESS = NET_ADDRESS;
    }

    public String getTICKER_WAP(){
        return TICKER_WAP;
    }

    public void setTICKER_WAP( String TICKER_WAP ){
        this.TICKER_WAP = TICKER_WAP;
    }
}
