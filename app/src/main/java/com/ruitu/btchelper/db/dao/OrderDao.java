package com.ruitu.btchelper.db.dao;

import java.util.Map;

import com.ruitu.btchelper.domain.Orders;

public interface OrderDao {
    /**
     * 保存一条记录
     */
    public long save(Orders orders);

    /**
     * 根据平台名称查询出所有记录
     */
    public Map<String, Object> getOrders(int page, int infoNumber,String platName);

    /**
     * 根据信息ID查询记录
     */
    public boolean getOrdersById(String ordersId);


    /**
     * 删除一条记录
     */
    public int deleteOrder(Orders orders);

    /**
     * 清空信息
     */
    public int clear();
}
