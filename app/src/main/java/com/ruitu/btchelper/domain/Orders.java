package com.ruitu.btchelper.domain;
public class Orders {
    /**
     * 交易平台
     */
    private String plat_name;
    /**
     * 更新时间
     */
    private String update_time;
    /**
     * 买入价
     */
    private String buy_price;
    /**
     * 买入量
     */
    private String buy_vol;
    /**
     * 卖出价
     */
    private String sell_price;
    /**
     * 卖出量
     */
    private String sell_vol;
    /**
     * 最大交易量
     */
    private double max_vol;
    /**
     * 买入量所占比列
     */
    private double but_int;
    /**
     * 卖出量所占比列
     */
    private double sell_int;
    
    public double getBut_int() {
        return but_int;
    }
    public void setBut_int(double but_int) {
        this.but_int = but_int;
    }
    public double getSell_int() {
        return sell_int;
    }
    public void setSell_int(double sell_int) {
        this.sell_int = sell_int;
    }
    public double getMax_vol() {
        return max_vol;
    }
    public void setMax_vol(double max_vol) {
        this.max_vol = max_vol;
    }
    public String getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
    public String getBuy_price() {
        return buy_price;
    }
    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }
    public String getBuy_vol() {
        return buy_vol;
    }
    public void setBuy_vol(String buy_vol) {
        this.buy_vol = buy_vol;
    }
    public String getSell_price() {
        return sell_price;
    }
    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }
    public String getSell_vol() {
        return sell_vol;
    }
    public void setSell_vol(String sell_vol) {
        this.sell_vol = sell_vol;
    }
   
    public String getPlat_name() {
        return plat_name;
    }
    public void setPlat_name(String plat_name) {
        this.plat_name = plat_name;
    }
    public Orders() {
        super();
    }
    public Orders(String update_time, String buy_price, String buy_vol,
            String sell_price, String sell_vol, double max_vol, double but_int,
            double sell_int) {
        super();
        this.update_time = update_time;
        this.buy_price = buy_price;
        this.buy_vol = buy_vol;
        this.sell_price = sell_price;
        this.sell_vol = sell_vol;
        this.max_vol = max_vol;
        this.but_int = but_int;
        this.sell_int = sell_int;
    }
    

    public Orders(String plat_name, String update_time, String buy_price,
            String buy_vol, String sell_price, String sell_vol, double max_vol,
            double but_int, double sell_int) {
        super();
        this.plat_name = plat_name;
        this.update_time = update_time;
        this.buy_price = buy_price;
        this.buy_vol = buy_vol;
        this.sell_price = sell_price;
        this.sell_vol = sell_vol;
        this.max_vol = max_vol;
        this.but_int = but_int;
        this.sell_int = sell_int;
    }


    public static class ConinInst{
        public double mPrice = 0;
        public double mCount = 0;
        public double mRate = 0.01;
        public ConinInst(double val1, double val2){
            mPrice = val1;
            mCount = val2;
        }
    }
}
