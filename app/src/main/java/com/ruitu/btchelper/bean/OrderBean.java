package com.ruitu.btchelper.bean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "tb_orders" )
public class OrderBean {
    @DatabaseField(generatedId = true)
    public Integer Order_ID;
    /**
     * 交易平台名称
     */
    @DatabaseField(canBeNull = true, columnName = "PLAT_NAME")
    public  String PLAT_NAME = "ORDER_NAME";
    /**
     * 委托买入价
     */
    @DatabaseField(canBeNull = true, columnName = "BUY_PRICE")
    public  String BUY_PRICE;
    /**
     * 委托卖出价
     */
    @DatabaseField(canBeNull = true, columnName = "SELL_PRICE")
    public  String SELL_PRICE;
    /**
     * 委托买入量
     */
    @DatabaseField(canBeNull = true, columnName = "BUY_VOLUME")
    public  String BUY_VOLUME = "BUY_VOLUME";
    /**
     * 委托卖出量
     */
    @DatabaseField(canBeNull = true, columnName = "SELL_VOLUME")
    public  String SELL_VOLUME = "SELL_VOLUME";
    /**
     * 更新时间
     */
    @DatabaseField(canBeNull = true, columnName = "UPDATE_TIME")
    public  String UPDATE_TIME = "UPDATE_TIME";

    public String getSELL_PRICE(){
        return SELL_PRICE;
    }

    public void setSELL_PRICE( String SELL_PRICE ){
        this.SELL_PRICE = SELL_PRICE;
    }

    public Integer getOrder_ID(){
        return Order_ID;
    }

    public void setOrder_ID( Integer order_ID ){
        Order_ID = order_ID;
    }

    public String getPLAT_NAME(){
        return PLAT_NAME;
    }

    public void setPLAT_NAME( String PLAT_NAME ){
        this.PLAT_NAME = PLAT_NAME;
    }

    public String getBUY_PRICE(){
        return BUY_PRICE;
    }

    public void setBUY_PRICE( String BUY_PRICE ){
        this.BUY_PRICE = BUY_PRICE;
    }

    public String getBUY_VOLUME(){
        return BUY_VOLUME;
    }

    public void setBUY_VOLUME( String BUY_VOLUME ){
        this.BUY_VOLUME = BUY_VOLUME;
    }

    public String getSELL_VOLUME(){
        return SELL_VOLUME;
    }

    public void setSELL_VOLUME( String SELL_VOLUME ){
        this.SELL_VOLUME = SELL_VOLUME;
    }

    public String getUPDATE_TIME(){
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME( String UPDATE_TIME ){
        this.UPDATE_TIME = UPDATE_TIME;
    }

}
