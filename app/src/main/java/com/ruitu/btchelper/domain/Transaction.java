package com.ruitu.btchelper.domain;

public class Transaction {
    /**
     * 交易平台名称
     */
    private String tra_name;
    /**
     * 成交时间
     */
    private String tra_time;
    /**
     * 订单号
     */
    private String tra_id;
    /**
     * 成交价
     */
    private String tra_price;
    /**
     * 成交量
     */
    private String tra_volume;
    /**
     * 最大成交量
     */
    private double max_volume;

    public double getMax_volume(){
        return max_volume;
    }

    public void setMax_volume( double max_volume ){
        this.max_volume = max_volume;
    }

    public Transaction(){
        super();
    }

    public String getTra_time(){
        return tra_time;
    }

    public void setTra_time( String tra_time ){
        this.tra_time = tra_time;
    }

    public String getTra_price(){
        return tra_price;
    }

    public void setTra_price( String tra_price ){
        this.tra_price = tra_price;
    }

    public String getTra_volume(){
        return tra_volume;
    }

    public void setTra_volume( String tra_volume ){
        this.tra_volume = tra_volume;
    }

    public String getTra_id(){
        return tra_id;
    }

    public void setTra_id( String tra_id ){
        this.tra_id = tra_id;
    }

    public Transaction( String tra_time, String tra_id, String tra_price, String tra_volume, double max_volume ){
        super();
        this.tra_time = tra_time;
        this.tra_id = tra_id;
        this.tra_price = tra_price;
        this.tra_volume = tra_volume;
        this.max_volume = max_volume;
    }


    public String getTra_name(){
        return tra_name;
    }

    public Transaction( String tra_time, String tra_name, String tra_id, String tra_price, String tra_volume, double max_volume ){
        this.tra_time = tra_time;
        this.tra_name = tra_name;
        this.tra_id = tra_id;
        this.tra_price = tra_price;
        this.tra_volume = tra_volume;
        this.max_volume = max_volume;
    }

    public void setTra_name( String tra_name ){
        this.tra_name = tra_name;
    }
}
