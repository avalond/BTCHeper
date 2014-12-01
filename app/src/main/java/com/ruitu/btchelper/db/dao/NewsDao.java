package com.ruitu.btchelper.db.dao;

import java.util.Map;

import com.ruitu.btchelper.domain.News;

public interface NewsDao {
    /**
     * 保存一条新闻消息
     */
    public long save(News news);



    /**
     * 根据信息ID查询新闻
     */
    public boolean getNewsById(String newsId);

    /**
     * 删除新闻信息
     */
    public int deleteNews(News news);

    /**
     * 清空信息
     */
    public int clear();

    /**
     * 根据新闻类型查询记录
     */
    public Map<String, Object> getNewsByType(int type, int page, int infoNumber);
}
