package com.ruitu.btchelper.db.dao;

import java.util.Map;

import com.ruitu.btchelper.domain.Ticker;

public interface TickerDao {
    /**
     * 保存一条记录
     */
    public long save(Ticker ticker);

    /**
     * 根据交易平台名称查询出所有记录
     */
    public Map<String, Object> getTickers(int page, int infoNumber,String platName);

    /**
     * 根据信息ID查询记录
     */
    public boolean getTickersById(String tickerId);

    /**
     * 删除一条记录
     */
    public int deleteTicker(Ticker ticker);

    /**
     * 清空信息
     */
    public int clear();
}
