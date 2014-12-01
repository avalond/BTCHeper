package com.ruitu.btchelper.db.dao;

import java.util.Map;

import com.ruitu.btchelper.domain.Transaction;

public interface TransactionDao {
    /**
     * 保存一条记录
     */
    public long save(Transaction ticker);

    /**
     * 根据交易平台名称查询记录
     */
    public Map<String, Object> getTransactions(int page, int infoNumber,String platName);

    /**
     * 根据信息ID查询记录
     */
    public boolean getTransactionById(String transactionId);

    /**
     * 删除一条记录
     */
    public int deleteTransaction(Transaction ticker);

    /**
     * 清空信息
     */
    public int clear();
}
