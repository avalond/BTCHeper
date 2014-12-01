package com.ruitu.btchelper.domain;


import android.os.Parcel;
import android.os.Parcelable;

public class Ticker implements Parcelable {
	/**
	 * 刷新时间
	 */
	private String updateTime;
	/**
	 * 交易平台名称
	 */
	private String name;
	/**
	 * 交易平台网站
	 */
	private String net_name;
	/**
	 * 买一价
	 */
	private String buy;
	/**
	 * 卖一价
	 */
	private String sell;
	/**
	 * 最近成交价
	 */
	private String last;
	/**
	 * 成交量
	 */
	private String volume;
	/**
	 * 最高价
	 */
	private String high;
	/**
	 * 最低价
	 */
	private String low;
	/**
	 * 成交均价
	 */
	private String vwap;

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuy() {
		return buy;
	}

	public void setBuy(String buy) {
		this.buy = buy;
	}

	public String getSell() {
		return sell;
	}

	public void setSell(String sell) {
		this.sell = sell;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getVwap() {
		return vwap;
	}

	public void setVwap(String vwap) {
		this.vwap = vwap;
	}

	public String getNet_name() {
		return net_name;
	}

	public void setNet_name(String net_name) {
		this.net_name = net_name;
	}

	public Ticker(String updateTime, String name, String net_name, String buy,
			String sell, String last, String volume, String high, String low,
			String vwap) {
		super();
		this.updateTime = updateTime;
		this.name = name;
		this.net_name = net_name;
		this.buy = buy;
		this.sell = sell;
		this.last = last;
		this.volume = volume;
		this.high = high;
		this.low = low;
		this.vwap = vwap;
	}

	public Ticker() {
		super();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(updateTime);
		dest.writeString(name);
		dest.writeString(net_name);
		dest.writeString(buy);
		dest.writeString(sell);
		dest.writeString(last);
		dest.writeString(volume);
		dest.writeString(high);
		dest.writeString(low);
		dest.writeString(vwap);
	}

	public static final Creator<Ticker> CREATOR = new Creator<Ticker>() {

		@Override
		public Ticker createFromParcel(Parcel source) {
			return new Ticker(source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
		}

		@Override
		public Ticker[] newArray(int size) {
			return new Ticker[size];
		}
	};

}
