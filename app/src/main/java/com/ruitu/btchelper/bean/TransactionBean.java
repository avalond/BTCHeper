package com.ruitu.btchelper.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "tb_transaction" )
public class TransactionBean {
    @DatabaseField( generatedId = true )
    private Integer TRANSACTION_ID_mk;
    /**
     * 交易平台名称
     */
    @DatabaseField(canBeNull = true, columnName = "TRANSACTION_NAME" )
    public String TRANSACTION_NAME;
    /**
     * 成交时间
     */
    @DatabaseField(canBeNull = true, columnName = "TRANSACTION_TIME" )
    public String TRANSACTION_TIME;
    /**
     * 成交订单号
     */
    @DatabaseField(canBeNull = true, columnName = "TRANSACTION_ID" )
    public String TRANSACTION_ID;
    /**
     * 成交价
     */
    @DatabaseField(canBeNull = true, columnName = "TRANSACTION_PRICE" )
    public String TRANSACTION_PRICE;
    /**
     * 成交量
     */
    @DatabaseField(canBeNull = true, columnName = "TRANSACTION_VOLUME" )
    public String TRANSACTION_VOLUME;

    public Integer getTRANSACTION_ID_mk(){
        return TRANSACTION_ID_mk;
    }

    public void setTRANSACTION_ID_mk( Integer TRANSACTION_ID_mk ){
        this.TRANSACTION_ID_mk = TRANSACTION_ID_mk;
    }

    public String getTRANSACTION_TIME(){
        return TRANSACTION_TIME;
    }

    public void setTRANSACTION_TIME( String TRANSACTION_TIME ){
        this.TRANSACTION_TIME = TRANSACTION_TIME;
    }

    public String getTRANSACTION_NAME(){
        return TRANSACTION_NAME;
    }

    public void setTRANSACTION_NAME( String TRANSACTION_NAME ){
        this.TRANSACTION_NAME = TRANSACTION_NAME;
    }

    public String getTRANSACTION_ID(){
        return TRANSACTION_ID;
    }

    public void setTRANSACTION_ID( String TRANSACTION_ID ){
        this.TRANSACTION_ID = TRANSACTION_ID;
    }

    public String getTRANSACTION_PRICE(){
        return TRANSACTION_PRICE;
    }

    public void setTRANSACTION_PRICE( String TRANSACTION_PRICE ){
        this.TRANSACTION_PRICE = TRANSACTION_PRICE;
    }

    public String getTRANSACTION_VOLUME(){
        return TRANSACTION_VOLUME;
    }

    public void setTRANSACTION_VOLUME( String TRANSACTION_VOLUME ){
        this.TRANSACTION_VOLUME = TRANSACTION_VOLUME;
    }
}
